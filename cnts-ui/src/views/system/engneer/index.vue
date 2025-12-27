<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="用户账号" prop="userName">
                <el-input v-model="queryParams.userName" placeholder="请输入用户账号" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="用户昵称" prop="nickName">
                <el-input v-model="queryParams.nickName" placeholder="请输入用户昵称" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="手机号码" prop="phonenumber">
                <el-input v-model="queryParams.phonenumber" placeholder="请输入手机号码" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="忙闲状态" prop="busyStatus">
                <el-select v-model="queryParams.busyStatus" placeholder="请选择忙闲状态" clearable>
                    <el-option label="空闲" value="0" />
                    <el-option label="忙碌" value="1" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                    v-hasPermi="['system:user:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="userList">
            <el-table-column label="用户ID" align="center" prop="userId" />
            <el-table-column label="用户账号" align="center" prop="userName" />
            <el-table-column label="用户昵称" align="center" prop="nickName" />
            <el-table-column label="用户邮箱" align="center" prop="email" />
            <el-table-column label="手机号码" align="center" prop="phonenumber" />
            <el-table-column label="账号状态" align="center" prop="status">
                <template slot-scope="scope">
                    <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
                </template>
            </el-table-column>
            <el-table-column label="忙闲状态" align="center" prop="busyStatus">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.busyStatus === '1'" type="danger">忙碌</el-tag>
                    <el-tag v-else type="success">空闲</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-warning-outline"
                        :disabled="scope.row.busyStatus === '1'"
                        @click="handleBusyStatus(scope.row, '1')" v-hasPermi="['system:user:edit']">设为忙碌</el-button>
                    <el-button size="mini" type="text" icon="el-icon-check"
                        :disabled="scope.row.busyStatus === '0'"
                        @click="handleBusyStatus(scope.row, '0')" v-hasPermi="['system:user:edit']">设为空闲</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

    </div>
</template>

<script>
import { listUser, changeUserBusyStatus } from "@/api/system/user"

export default {
    name: "Engineer",
    dicts: ['sys_normal_disable'],
    data() {
        return {
            // 遮罩层
            loading: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 用户信息表格数据
            userList: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                roleId: 3,
                userName: null,
                nickName: null,
                phonenumber: null,
                busyStatus: null
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        /** 查询用户信息列表 */
        getList() {
            this.loading = true
            listUser(this.queryParams).then(response => {
                this.userList = response.rows
                this.total = response.total
                this.loading = false
            })
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1
            this.getList()
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm")
            this.handleQuery()
        },
        handleBusyStatus(row, busyStatus) {
            const label = busyStatus === "1" ? "忙碌" : "空闲"
            this.$modal.confirm('确认将工程师 "' + row.userName + '" 设为' + label + '吗？').then(() => {
                return changeUserBusyStatus(row.userId, busyStatus)
            }).then(() => {
                this.$modal.msgSuccess("状态已更新")
                this.getList()
            }).catch(() => { })
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('system/user/export', {
                ...this.queryParams
            }, `engineer_${new Date().getTime()}.xlsx`)
        }
    }
}
</script>
