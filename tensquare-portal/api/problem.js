import request from '@/utils/request'
const group_name = 'qa'
const api_name = 'problem'
export default {
    list(type,label,page,size){
        return request({
            url:`/${group_name}/${api_name}/${type}/${label}/${page}/${size}`,
            method: 'get'
        })
    }

}