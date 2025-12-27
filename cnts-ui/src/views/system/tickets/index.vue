<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="故障标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入故障标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类别" prop="categoryId">
        <el-select
          v-model="queryParams.categoryId"
          filterable
          clearable
          remote
          reserve-keyword
          placeholder="搜索类别名称"
          :remote-method="remoteSearchCategories"
          :loading="categoriesLoading"
          style="width: 180px"
        >
          <el-option
            v-for="item in categoriesOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="故障地点" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入故障地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.tickets"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item v-if="!isUserRole" label="上报用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入上报用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="处理管理员ID" prop="adminId">
        <el-input
          v-model="queryParams.adminId"
          placeholder="请输入处理管理员ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:tickets:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:tickets:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:tickets:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:tickets:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ticketsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单ID" align="center" prop="id" />
      <el-table-column label="故障标题" align="center" prop="title" />
      <el-table-column label="故障描述" align="center" width="110">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="openDescription(scope.row)">查看描述</el-button>
        </template>
      </el-table-column>
      <el-table-column label="类别ID" align="center" prop="categoryId" />
      <el-table-column label="故障地点" align="center" prop="location" />
      <el-table-column label="图片JSON，支持多图" align="center" prop="imgs" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tickets" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column v-if="!isUserRole" label="上报用户" align="center">
        <template slot-scope="scope">
          <el-link
            v-if="scope.row.userId"
            type="primary"
            :underline="false"
            :title="`用户ID：${scope.row.userId}`"
            @click="openUserDetail(scope.row.userId)"
          >点击查看上报用户</el-link>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="指派工程师" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.engineerName || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处理管理员ID" align="center" prop="adminId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="text"
            @click="handleAccept(scope.row)"
            v-hasPermi="['system:tickets:edit']"
          >受理</el-button>
          <el-button
            v-if="scope.row.status === 1"
            size="mini"
            type="text"
            @click="openAssign(scope.row)"
            v-hasPermi="['system:tickets:edit']"
          >指派</el-button>
          <!-- <el-button
            v-if="scope.row.status === 1 || scope.row.status === 2"
            size="mini"
            type="text"
            @click="handleProcessing(scope.row)"
            v-hasPermi="['system:tickets:edit']"
          >处理中</el-button> -->
          <el-button
            v-if="scope.row.status === 2"
            size="mini"
            type="text"
            @click="handleFinish(scope.row)"
            v-hasPermi="['system:tickets:edit']"
          >完成</el-button>
          <el-button
            size="mini"
            type="text"
            @click="openTimeline(scope.row)"
          >进度</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tickets:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tickets:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 指派工单 -->
    <el-dialog title="指派工单" :visible.sync="assignOpen" width="400px" append-to-body>
      <el-form ref="assignForm" :model="assignForm" :rules="assignRules" label-width="100px">
        <el-form-item label="工单ID">
          <el-input v-model="assignForm.ticketId" disabled />
        </el-form-item>
        <el-form-item label="指派工程师" prop="adminId">
          <el-select
            v-model="assignForm.adminId"
            filterable
            remote
            clearable
            reserve-keyword
            placeholder="搜索工程师用户名"
            :remote-method="remoteSearchEngineer"
            :loading="engineerLoading"
            @change="onAssignEngineerChange"
            style="width: 100%;"
          >
            <el-option
              v-for="item in engineerOptions"
              :key="item.userId"
              :label="`${item.userName || item.nickName || '用户'}（ID: ${item.userId}）`"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="工程师姓名">
          <el-input v-model="assignForm.engineerName" placeholder="将自动带出，也可手填" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAssign">确 定</el-button>
        <el-button @click="assignOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 工单进度时间线 -->
    <el-dialog title="工单进度" :visible.sync="timelineOpen" width="600px" append-to-body>
      <div v-loading="timelineLoading" class="timeline-container">
        <div class="timeline-meta" v-if="timelineTicket.id">
          <p>工单ID：{{ timelineTicket.id }}</p>
          <p>标题：{{ timelineTicket.title }}</p>
          <p>状态：{{ formatStatus(timelineTicket.status) }}</p>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="item in timelineLogs"
            :key="item.id"
            :timestamp="item.createTime"
          >
            <p>{{ item.action }}</p>
            <p v-if="item.remark" class="timeline-remark">{{ item.remark }}</p>
            <p v-if="item.operatorId && !isUserRole" class="timeline-operator">操作人：{{ formatOperator(item) }}</p>
          </el-timeline-item>
          <el-timeline-item v-if="!timelineLogs.length">暂无流转记录</el-timeline-item>
        </el-timeline>
      </div>
    </el-dialog>

    <!-- 上报用户信息 -->
    <el-dialog title="上报用户" :visible.sync="userDetailOpen" width="480px" append-to-body>
      <div v-loading="userDetailLoading">
        <el-descriptions v-if="Object.keys(userDetail).length" :column="1" border>
          <el-descriptions-item label="用户ID">{{ userDetail.userId || "-" }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ userDetail.userName || "-" }}</el-descriptions-item>
          <el-descriptions-item label="昵称">{{ userDetail.nickName || "-" }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ userDetail.phonenumber || "-" }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ userDetail.email || "-" }}</el-descriptions-item>
          <el-descriptions-item label="部门">{{ (userDetail.dept && userDetail.dept.deptName) || "-" }}</el-descriptions-item>
        </el-descriptions>
        <div v-else class="empty-user">暂无用户信息</div>
      </div>
    </el-dialog>

    <!-- 故障描述 -->
    <el-dialog title="故障描述" :visible.sync="descOpen" width="760px" append-to-body>
      <div class="desc-body" v-html="descHtml"></div>
      <div v-if="descImages.length" class="desc-images">
        <el-image
          v-for="(img, idx) in descImages"
          :key="idx"
          :src="img"
          :preview-src-list="descImages"
          fit="contain"
          style="width: 100px; height: 90px; margin-right: 8px; margin-bottom: 8px;"
        />
      </div>
      <div v-if="!descHtml && !descImages.length" class="empty-user">暂无描述</div>
    </el-dialog>

    <!-- 添加或修改故障工单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="故障标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入故障标题" />
        </el-form-item>
        <el-form-item label="故障描述">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="类别" prop="categoryId">
          <el-select
            v-model="form.categoryId"
            filterable
            clearable
            remote
            reserve-keyword
            placeholder="搜索类别名称"
            :remote-method="remoteSearchCategories"
            :loading="categoriesLoading"
            style="width: 100%;"
          >
            <el-option
              v-for="item in categoriesOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="故障地点" prop="location">
          <el-input v-model="form.location" placeholder="请输入故障地点" />
        </el-form-item>
        <!-- <el-form-item label="图片JSON，支持多图" prop="imgs">
          <el-input v-model="form.imgs" type="textarea" placeholder="请输入内容" />
        </el-form-item> -->
        <el-form-item v-if="isEdit" label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.tickets"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上报用户">
          <div v-loading="reporterDetailLoading">
            <el-descriptions v-if="form.userId" :column="1" border size="small" class="reporter-info">
              <el-descriptions-item label="用户ID">{{ form.userId }}</el-descriptions-item>
              <el-descriptions-item label="用户名">{{ reporterDetail.userName || "-" }}</el-descriptions-item>
              <el-descriptions-item label="昵称">{{ reporterDetail.nickName || "-" }}</el-descriptions-item>
              <el-descriptions-item label="手机号">{{ reporterDetail.phonenumber || "-" }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ reporterDetail.email || "-" }}</el-descriptions-item>
            </el-descriptions>
            <div v-else class="empty-user">暂无上报用户</div>
          </div>
        </el-form-item>
        <el-form-item v-if="isEdit" label="处理工程师" prop="adminId">
          <el-select
            v-model="form.adminId"
            filterable
            remote
            clearable
            reserve-keyword
            placeholder="搜索工程师用户名"
            :remote-method="remoteSearchEngineer"
            :loading="engineerLoading"
            @change="onFormEngineerChange"
            style="width: 100%;"
          >
            <el-option
              v-for="item in engineerOptions"
              :key="item.userId"
              :label="`${item.userName || item.nickName || '用户'}（ID: ${item.userId}）`"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listTickets,
  getTickets,
  delTickets,
  addTickets,
  updateTickets,
  acceptTicket,
  assignTicket,
  processingTicket,
  finishTicket
} from "@/api/system/tickets"
import { listTicketLogsByTicket } from "@/api/system/ticket_Logs"
import { getUser } from "@/api/system/user"
import { allocatedUserList } from "@/api/system/role"
import { listCategories } from "@/api/system/categories"

export default {
  name: "Tickets",
  dicts: ['tickets'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 故障工单表格数据
      ticketsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否编辑模式
      isEdit: false,
      // 指派弹窗
      assignOpen: false,
      assignForm: {
        ticketId: null,
        adminId: null,
        engineerName: ""
      },
      assignRules: {
        adminId: [{ required: true, message: "请选择工程师", trigger: "change" }]
      },
      engineerOptions: [],
      engineerLoading: false,
      categoriesOptions: [],
      categoriesLoading: false,
      // 时间线
      timelineOpen: false,
      timelineLoading: false,
      timelineLogs: [],
      timelineTicket: {},
      operatorUsers: {},
      // 上报用户详情
      userDetailOpen: false,
      userDetailLoading: false,
      userDetail: {},
      reporterDetail: {},
      reporterDetailLoading: false,
      descOpen: false,
      descHtml: "",
      descImages: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        content: null,
        categoryId: null,
        location: null,
        imgs: null,
        status: null,
        userId: null,
        adminId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "故障标题不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "故障描述不能为空", trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: "类别ID不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        userId: [
          { required: true, message: "上报用户ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.fetchCategories('')
  },
  computed: {
    isUserRole() {
      const roles = (this.$store && this.$store.getters && this.$store.getters.roles) || []
      return roles.some(r => r === 'user' || r === '2' || r === 2)
    },
    currentUserId() {
      return (this.$store && this.$store.getters && this.$store.getters.id) || null
    }
  },
  methods: {
    /** 查询故障工单列表 */
    getList() {
      this.loading = true
      if (this.isUserRole) {
        this.queryParams.userId = this.currentUserId
      }
      listTickets(this.queryParams).then(response => {
        this.ticketsList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        content: null,
        categoryId: null,
        location: null,
        imgs: null,
        status: 0,
        userId: this.isUserRole ? this.currentUserId : null,
        adminId: null,
        engineerName: "",
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      }
      this.reporterDetail = {}
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      if (this.isUserRole) {
        this.queryParams.userId = this.currentUserId
      }
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 受理工单 */
    handleAccept(row) {
      const id = row.id
      this.$modal.confirm('确认受理该工单吗？').then(() => acceptTicket(id)).then(() => {
        this.$modal.msgSuccess("受理成功")
        this.getList()
      }).catch(() => {})
    },
    /** 指派弹窗 */
    openAssign(row) {
      this.assignForm = {
        ticketId: row.id,
        adminId: row.adminId,
        engineerName: row.engineerName || ""
      }
      this.assignOpen = true
      this.fetchEngineers('')
      this.$nextTick(() => {
        if (this.$refs.assignForm) {
          this.$refs.assignForm.clearValidate()
        }
      })
    },
    /** 提交指派 */
    submitAssign() {
      this.$refs["assignForm"].validate(valid => {
        if (!valid) return
        if (!this.assignForm.engineerName && this.assignForm.adminId) {
          this.assignForm.engineerName = this.resolveEngineerNameById(this.assignForm.adminId)
        }
        assignTicket(this.assignForm).then(() => {
          this.$modal.msgSuccess("指派成功")
          this.assignOpen = false
          this.assignForm = { ticketId: null, adminId: null, engineerName: "" }
          this.getList()
        })
      })
    },
    /** 标记处理中 */
    handleProcessing(row) {
      const id = row.id
      this.$modal.confirm('确认将工单标记为处理中吗？').then(() => processingTicket(id)).then(() => {
        this.$modal.msgSuccess("状态已更新为处理中")
        this.getList()
      }).catch(() => {})
    },
    /** 完成工单 */
    handleFinish(row) {
      const id = row.id
      this.$modal.confirm('确认完成该工单吗？').then(() => finishTicket(id)).then(() => {
        this.$modal.msgSuccess("工单已完成")
        this.getList()
      }).catch(() => {})
    },
    /** 打开时间线 */
    openTimeline(row) {
      this.timelineOpen = true
      this.timelineLoading = true
      this.timelineTicket = {}
      this.timelineLogs = []
      this.operatorUsers = {}
      Promise.all([getTickets(row.id), listTicketLogsByTicket(row.id)])
        .then(([ticketRes, logRes]) => {
          this.timelineTicket = ticketRes.data || row
          this.timelineLogs = logRes.rows || logRes.data || []
          this.populateOperatorUsers(this.timelineLogs)
        })
        .catch(() => {
          this.timelineTicket = row
          this.timelineLogs = []
        })
        .finally(() => {
          this.timelineLoading = false
        })
    },
    /** 查看上报用户 */
    openUserDetail(userId) {
      if (!userId) {
        this.$modal.msgWarning("暂无上报用户信息")
        return
      }
      this.userDetailOpen = true
      this.userDetailLoading = true
      this.userDetail = {}
      getUser(userId)
        .then(res => {
          const data = res.data || {}
          this.userDetail = data.user || data || {}
          // 如果接口返回为空，则至少保留用户ID
          if (!Object.keys(this.userDetail).length) {
            this.userDetail = { userId }
          }
        })
        .catch(() => {
          this.userDetail = { userId }
        })
        .finally(() => {
          this.userDetailLoading = false
        })
    },
    /** 远程搜索工程师（角色 key = engineer） */
    remoteSearchEngineer(query) {
      this.fetchEngineers(query)
    },
    fetchEngineers(query) {
      this.engineerLoading = true
      const params = {
        roleKey: "engineer",
        roleId: 3,
        busyStatus: "0",
        userName: query || undefined,
        pageNum: 1,
        pageSize: 20
      }
      allocatedUserList(params)
        .then(res => {
          this.engineerOptions = res.rows || []
          if (!this.assignForm.engineerName && this.assignForm.adminId) {
            this.assignForm.engineerName = this.resolveEngineerNameById(this.assignForm.adminId)
          }
          if (!this.form.engineerName && this.form.adminId) {
            this.form.engineerName = this.resolveEngineerNameById(this.form.adminId)
          }
        })
        .catch(() => {
          this.engineerOptions = []
        })
        .finally(() => {
          this.engineerLoading = false
        })
    },
    resolveEngineerNameById(id) {
      const target = (this.engineerOptions || []).find(item => item.userId == id)
      if (!target) return ""
      return target.nickName || target.userName || ""
    },
    onAssignEngineerChange(val) {
      this.assignForm.engineerName = this.resolveEngineerNameById(val)
    },
    onFormEngineerChange(val) {
      this.form.engineerName = this.resolveEngineerNameById(val)
    },
    /** 远程搜索类别 */
    remoteSearchCategories(query) {
      this.fetchCategories(query)
    },
    fetchCategories(query) {
      this.categoriesLoading = true
      const params = {
        name: query || undefined,
        pageNum: 1,
        pageSize: 20
      }
      listCategories(params)
        .then(res => {
          this.categoriesOptions = res.rows || []
        })
        .catch(() => {
          this.categoriesOptions = []
        })
        .finally(() => {
          this.categoriesLoading = false
        })
    },
    loadReporterDetail(userId) {
      if (!userId) {
        this.reporterDetail = {}
        return
      }
      this.reporterDetailLoading = true
      getUser(userId)
        .then(res => {
          const data = res.data || {}
          this.reporterDetail = data.user || data || {}
        })
        .catch(() => {
          this.reporterDetail = {}
        })
        .finally(() => {
          this.reporterDetailLoading = false
        })
    },
    openDescription(row) {
      this.descHtml = row.content || ""
      this.descImages = this.parseImages(row.imgs)
      this.descOpen = true
    },
    parseImages(imgs) {
      if (!imgs) return []
      let arr = []
      if (Array.isArray(imgs)) {
        arr = imgs
      } else if (typeof imgs === 'string') {
        try {
          const parsed = JSON.parse(imgs)
          if (Array.isArray(parsed)) {
            arr = parsed
          }
        } catch (e) {
          arr = imgs.split(',').map(i => i.trim()).filter(Boolean)
        }
      }
      return arr.filter(i => typeof i === 'string' && i.length > 0)
    },
    formatStatus(status) {
      return this.selectDictLabel(this.dict.type.tickets, status) || "-"
    },
    formatOperator(log) {
      const cached = this.operatorUsers[log.operatorId]
      if (cached && cached.userName) {
        return `【账号：${cached.userName}；id：${log.operatorId}】`
      }
      return `${log.operatorId}`
    },
    populateOperatorUsers(logs = []) {
      const ids = Array.from(new Set(logs.map(item => item.operatorId).filter(Boolean)))
      ids.forEach(id => {
        if (this.operatorUsers[id]) return
        this.$set(this.operatorUsers, id, {})
        getUser(id)
          .then(res => {
            const data = res.data || {}
            const user = data.user || data || {}
            this.$set(this.operatorUsers, id, user)
          })
          .catch(() => {
            // 保留占位，避免重复请求
            this.$set(this.operatorUsers, id, {})
          })
      })
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.form.userId = this.currentUserId
      this.isEdit = false
      this.open = true
      this.title = "添加故障工单"
      this.loadReporterDetail(this.form.userId)
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getTickets(id).then(response => {
        this.form = response.data
        this.form.engineerName = this.form.engineerName || ""
        this.isEdit = true
        this.open = true
        this.title = "修改故障工单"
        this.fetchEngineers('')
        this.loadReporterDetail(this.form.userId)
      })
    },
    /** 提交按钮 */
    submitForm() {
      if (!this.isEdit) {
        this.form.status = 0
        this.form.adminId = null
        this.form.engineerName = ""
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.form.userId) {
            this.form.userId = this.currentUserId
          }
          if (!this.form.engineerName && this.form.adminId) {
            this.form.engineerName = this.resolveEngineerNameById(this.form.adminId)
          }
          if (this.form.id != null) {
            updateTickets(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTickets(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除故障工单编号为"' + ids + '"的数据项？').then(function() {
        return delTickets(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/tickets/export', {
        ...this.queryParams
      }, `tickets_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.timeline-container {
  min-height: 180px;
}
.timeline-meta {
  margin-bottom: 12px;
  color: #606266;
}
.timeline-meta p {
  margin: 0 0 4px 0;
}
.timeline-remark {
  color: #909399;
  margin: 4px 0 0 0;
}
.timeline-operator {
  color: #606266;
  font-size: 13px;
  margin: 4px 0 0 0;
}
.empty-user {
  color: #909399;
  text-align: center;
  padding: 12px 0;
}
.reporter-info {
  width: 100%;
}
.desc-body {
  max-height: 360px;
  overflow: auto;
  margin-bottom: 12px;
}
.desc-body img {
  width: 140px;
  height: 90px;
  object-fit: contain;
  margin: 4px 8px 8px 0;
  display: inline-block;
  vertical-align: middle;
}
.desc-images {
  display: flex;
  flex-wrap: wrap;
}
</style>
