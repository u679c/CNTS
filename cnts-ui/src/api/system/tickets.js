import request from '@/utils/request'

// 查询故障工单列表
export function listTickets(query) {
  return request({
    url: '/system/tickets/list',
    method: 'get',
    params: query
  })
}

// 查询故障工单详细
export function getTickets(id) {
  return request({
    url: '/system/tickets/' + id,
    method: 'get'
  })
}

// 新增故障工单
export function addTickets(data) {
  return request({
    url: '/system/tickets',
    method: 'post',
    data: data
  })
}

// 修改故障工单
export function updateTickets(data) {
  return request({
    url: '/system/tickets',
    method: 'put',
    data: data
  })
}

// 删除故障工单
export function delTickets(id) {
  return request({
    url: '/system/tickets/' + id,
    method: 'delete'
  })
}
