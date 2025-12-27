<template>
  <div class="dashboard-editor-container">
    <el-row :gutter="24">
      <el-col :xs="24" :lg="12">
        <div class="chart-wrapper">
          <div class="chart-header">
            <div class="chart-title">工单统计</div>
            <div class="chart-subtitle">按状态分布</div>
          </div>
          <ticket-pie-chart :status-options="dict.type.tickets" height="360px" />
        </div>
      </el-col>
      <el-col :xs="24" :lg="12">
        <div class="chart-wrapper flow-wrapper">
          <div class="chart-header">
            <div class="chart-title">工单流转流程</div>
            <div class="chart-subtitle">固定流程展示</div>
          </div>
          <div class="ticket-flow">
            <div class="flow-step status-pending">
              <span class="flow-label">收到工单</span>
              <span class="flow-status">待受理</span>
            </div>
            <i class="el-icon-right flow-arrow"></i>
            <div class="flow-step status-accepted">
              <span class="flow-label">受理工单</span>
              <span class="flow-status">已受理</span>
            </div>
            <i class="el-icon-right flow-arrow"></i>
            <div class="flow-step status-processing">
              <span class="flow-label">指派工程师</span>
              <span class="flow-status">处理中</span>
            </div>
            <i class="el-icon-right flow-arrow"></i>
            <div class="flow-step status-finished">
              <span class="flow-label">完成工单</span>
              <span class="flow-status">已完成</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import TicketPieChart from './dashboard/TicketPieChart'

export default {
  name: 'Index',
  dicts: ['tickets'],
  components: {
    TicketPieChart
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px;
    margin-bottom: 32px;
    border-radius: 8px;
    box-shadow: 0 10px 24px rgba(15, 23, 42, 0.06);
  }

  .chart-header {
    display: flex;
    align-items: baseline;
    justify-content: space-between;
    margin-bottom: 8px;
  }

  .chart-title {
    font-size: 16px;
    font-weight: 600;
    color: #1f2d3d;
  }

  .chart-subtitle {
    font-size: 12px;
    color: #8091a5;
  }

  .flow-wrapper {
    min-height: 420px;
  }

  .ticket-flow {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
    margin-top: 24px;
    padding: 20px;
    border-radius: 12px;
    background: #f7f9fc;
    border: 1px dashed #d6dde6;
  }

  .flow-step {
    flex: 1;
    min-width: 0;
    padding: 16px 12px;
    border-radius: 10px;
    background: #fff;
    text-align: center;
    box-shadow: 0 6px 16px rgba(15, 23, 42, 0.08);
  }

  .flow-label {
    display: block;
    font-size: 14px;
    font-weight: 600;
    color: #1f2d3d;
  }

  .flow-status {
    display: inline-block;
    margin-top: 6px;
    padding: 2px 10px;
    border-radius: 999px;
    font-size: 12px;
    color: #4a6078;
    background: #e9eff6;
  }

  .status-pending {
    border: 1px solid #ffc2c2;
    background: #fff2f2;
  }

  .status-pending .flow-status {
    color: #ff6b6b;
    background: #ffe5e5;
  }

  .status-accepted {
    border: 1px solid #ffe0a3;
    background: #fff7e1;
  }

  .status-accepted .flow-status {
    color: #f2a300;
    background: #ffeec1;
  }

  .status-processing {
    border: 1px solid #d6dde6;
    background: #f3f5f8;
  }

  .status-processing .flow-status {
    color: #7b8794;
    background: #e8ecf2;
  }

  .status-finished {
    border: 1px solid #b7e9d0;
    background: #eefaf4;
  }

  .status-finished .flow-status {
    color: #2fbf71;
    background: #dbf4e7;
  }

  .flow-arrow {
    font-size: 18px;
    color: #9aa9bd;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }

  .ticket-flow {
    flex-direction: column;
    gap: 12px;
  }

  .flow-arrow {
    transform: rotate(90deg);
  }
}
</style>
