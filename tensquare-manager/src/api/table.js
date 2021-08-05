import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/base/city/list',
    method: 'get',
    params
  })
}
