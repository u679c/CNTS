import request from '@/utils/request'

// 查询故障类别列表
export function listCategories(query) {
  return request({
    url: '/system/categories/list',
    method: 'get',
    params: query
  })
}

// 查询故障类别详细
export function getCategories(id) {
  return request({
    url: '/system/categories/' + id,
    method: 'get'
  })
}

// 新增故障类别
export function addCategories(data) {
  return request({
    url: '/system/categories',
    method: 'post',
    data: data
  })
}

// 修改故障类别
export function updateCategories(data) {
  return request({
    url: '/system/categories',
    method: 'put',
    data: data
  })
}

// 删除故障类别
export function delCategories(id) {
  return request({
    url: '/system/categories/' + id,
    method: 'delete'
  })
}
