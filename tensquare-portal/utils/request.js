import axios from 'axios'
import {getUser} from '@/utils/auth'
// 创建axios实例
const service = axios.create({
    baseURL: 'http://192.168.184.133:7300/mock/5af314a4c612520d0d7650c7', // api的base_url
    timeout: 30000, // 请求超时时间
    headers: { 'Authorization':  "Bearer "+getUser().token }
  })
export default service