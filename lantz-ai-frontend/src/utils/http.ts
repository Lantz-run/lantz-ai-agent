import axios from 'axios'

export const http = axios.create({
  baseURL: 'http://localhost:8123/api',
  timeout: 30000
})

http.interceptors.response.use(
  (res) => res,
  (err) => {
    return Promise.reject(err)
  }
)

export default http


