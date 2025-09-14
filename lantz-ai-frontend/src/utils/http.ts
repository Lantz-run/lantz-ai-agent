import axios from 'axios'

// 根据环境变量设置 API 基础 URL
const API_BASE_URL = import.meta.env.PROD 
  ? '/api' // 生产环境使用相对路径，适用于前后端部署在同一域名下
  : 'http://localhost:8123/api' // 开发环境指向本地后端服务

export const http = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  withCredentials: true // 启用cookies，用于session认证
})

// 请求拦截器 - 添加认证信息
http.interceptors.request.use(
  (config) => {
    // 后端使用session认证，不需要添加token
    // 但我们可以添加一些通用的请求头
    config.headers['Content-Type'] = 'application/json'
    
    // 添加调试日志
    console.log('发送请求:', {
      url: config.url,
      method: config.method,
      withCredentials: config.withCredentials,
      headers: config.headers
    })
    
    return config
  },
  (error) => {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
http.interceptors.response.use(
  (res) => {
    // 添加成功响应的日志
    console.log('请求成功:', {
      url: res.config.url,
      status: res.status,
      data: res.data
    })
    return res
  },
  (err) => {
    // 添加错误响应的日志
    console.error('请求失败:', {
      url: err.config?.url,
      status: err.response?.status,
      data: err.response?.data,
      message: err.message
    })
    
    // 检查业务状态码，如果是未登录状态，清除本地存储并跳转到登录页
    if (err.response?.data?.code === 40100) {
      console.log('检测到未登录状态，清除本地存储并跳转到登录页')
      localStorage.removeItem('user')
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

export default http


