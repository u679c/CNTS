import request from '@/utils/request'

// 查询工单流转记录列表
export function listTicket_Logs(query) {
  return request({
    url: '/system/ticket_Logs/list',
    method: 'get',
    params: query
  })
}

// 按工单查询流转记录（时间线使用）
export function listTicketLogsByTicket(ticketId) {
  return listTicket_Logs({
    ticketId,
    pageNum: 1,
    pageSize: 50
  })
}

// 查询工单流转记录详细
export function getTicket_Logs(id) {
  return request({
    url: '/system/ticket_Logs/' + id,
    method: 'get'
  })
}

// 新增工单流转记录
export function addTicket_Logs(data) {
  return request({
    url: '/system/ticket_Logs',
    method: 'post',
    data: data
  })
}

// 修改工单流转记录
export function updateTicket_Logs(data) {
  return request({
    url: '/system/ticket_Logs',
    method: 'put',
    data: data
  })
}

// 删除工单流转记录
export function delTicket_Logs(id) {
  return request({
    url: '/system/ticket_Logs/' + id,
    method: 'delete'
  })
}
