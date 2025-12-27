<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons')
import resize from './mixins/resize'
import { listTickets } from '@/api/system/tickets'

const fallbackStatusOptions = [
  { label: '待受理', value: 0 },
  { label: '已受理', value: 1 },
  { label: '处理中', value: 2 },
  { label: '已完成', value: 3 }
]



export default {
  name: 'TicketPieChart',
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    },
    statusOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      chart: null,
      rawTickets: [],
      seriesData: []
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
      this.fetchTickets()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  watch: {
    statusOptions: {
      handler() {
        this.updateSeries()
      },
      deep: true
    }
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions()
    },
    fetchTickets() {
      listTickets({ pageNum: 1, pageSize: 10000 }).then(res => {
        this.rawTickets = Array.isArray(res.rows) ? res.rows : []
        this.updateSeries()
      }).catch(() => {
        this.rawTickets = []
        this.updateSeries()
      })
    },
    updateSeries() {
      const options = this.statusOptions.length ? this.statusOptions : fallbackStatusOptions
      const countMap = this.rawTickets.reduce((acc, item) => {
        const key = item && item.status !== undefined && item.status !== null ? String(item.status) : 'unknown'
        acc[key] = (acc[key] || 0) + 1
        return acc
      }, {})
      const knownKeys = new Set(options.map(option => String(option.value)))
      const seriesData = options.map(option => ({
        name: option.label,
        value: countMap[String(option.value)] || 0
      }))
      const extraCount = Object.keys(countMap).reduce((sum, key) => {
        if (knownKeys.has(key)) {
          return sum
        }
        return sum + countMap[key]
      }, 0)
      if (extraCount) {
        seriesData.push({ name: '其他', value: extraCount })
      }
      this.seriesData = seriesData
      this.setOptions()
    },
    setOptions() {
      if (!this.chart) {
        return
      }
      const hasData = this.seriesData.some(item => item.value > 0)
      const displayData = hasData
        ? this.seriesData
        : [{ value: 1, name: '暂无数据', itemStyle: { color: '#e3e8ef' }, label: { show: false } }]

      this.chart.setOption({
        title: {
          text: '故障工单统计',
          left: 'center',
          top: 8,
          textStyle: {
            fontSize: 14,
            color: '#1f2d3d'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: 10,
          data: displayData.map(item => item.name)
        },
        series: [
          {
            name: '工单状态',
            type: 'pie',
            radius: ['30%', '70%'],
            center: ['50%', '52%'],
            data: displayData,
            label: {
              formatter: '{b}\n{c}'
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.2)'
              }
            },
            animationEasing: 'cubicInOut',
            animationDuration: 1200
          }
        ]
      })
    }
  }
}
</script>
