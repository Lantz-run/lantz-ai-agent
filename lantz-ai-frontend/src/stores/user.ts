import { defineStore } from 'pinia'
import { ref } from 'vue'
import { http } from '@/utils/http'

export interface User {
  id: number
  userAccount: string
  userName: string
  userRole: string
  userProfile: string
  createTime: string
  updateTime: string
}

export interface LoginUserVO {
  user: User
}

export interface UserLoginRequest {
  userAccount: string
  userPassword: string
}

export interface UserRegisterRequest {
  userAccount: string
  userName: string
  userPassword: string
  checkPassword: string
}

export const useUserStore = defineStore('user', () => {
  const currentUser = ref<User | null>(null)
  const token = ref<string>('')
  const isLoggedIn = ref(false)

  // 登录
  async function login(data: UserLoginRequest) {
    const response = await http.post<any>('/user/login', data)
    
    console.log('登录响应:', response.data)
    
    // 处理后端 BaseResponse 格式
    if (response.data.code === 0) {
      // 检查返回的数据结构
      let user
      if (response.data.data && response.data.data.user) {
        // 如果返回的是 { user: {...} } 格式
        user = response.data.data.user
      } else if (response.data.data && response.data.data.id) {
        // 如果直接返回用户对象
        user = response.data.data
      } else {
        // 如果data为空对象，尝试从后端获取用户信息
        console.log('登录响应data为空，尝试获取用户信息')
        try {
          user = await getCurrentUser()
        } catch (error) {
          console.error('获取用户信息失败:', error)
          throw new Error('登录成功但无法获取用户信息')
        }
      }
      
      console.log('登录成功，用户信息:', user)
      
      if (user) {
        currentUser.value = user
        token.value = 'logged_in' // 使用固定值表示已登录状态
        isLoggedIn.value = true
        
        // 存储到 localStorage
        localStorage.setItem('user', JSON.stringify(user))
        localStorage.setItem('token', 'logged_in')
        
        console.log('登录状态已设置:', { isLoggedIn: isLoggedIn.value, currentUser: currentUser.value })
      } else {
        throw new Error('无法获取用户信息')
      }
    } else {
      console.error('登录失败，后端返回错误:', response.data)
      throw new Error(response.data.message || '登录失败')
    }
  }

  // 注册
  async function register(data: UserRegisterRequest) {
    await http.post<number>('/user/register', data)
  }

  // 登出
  async function logout() {
    try {
      // 调用后端注销接口
      await http.post('/user/logout')
    } catch (error) {
      // 即使后端注销失败，也要清除本地状态
      console.warn('后端注销失败，但已清除本地状态:', error)
    } finally {
      // 清除本地状态
      currentUser.value = null
      token.value = ''
      isLoggedIn.value = false
      
      localStorage.removeItem('user')
      localStorage.removeItem('token')
    }
  }

  // 获取当前用户
  async function getCurrentUser() {
    try {
      const response = await http.get<any>('/user/get/login')
      
      console.log('获取用户信息响应:', response.data)
      
      // 处理后端 BaseResponse 格式
      if (response.data.code === 0) {
        // 检查返回的数据结构
        let user
        if (response.data.data && response.data.data.user) {
          // 如果返回的是 { user: {...} } 格式
          user = response.data.data.user
        } else if (response.data.data && response.data.data.id) {
          // 如果直接返回用户对象
          user = response.data.data
        } else {
          console.error('后端返回的用户数据格式无效:', response.data.data)
          throw new Error('用户数据格式错误')
        }
        
        console.log('解析到的用户信息:', user)
        
        if (user) {
          currentUser.value = user
          token.value = 'logged_in' // 使用固定值表示已登录状态
          isLoggedIn.value = true
          
          // 更新本地存储
          localStorage.setItem('user', JSON.stringify(user))
          localStorage.setItem('token', 'logged_in')
          
          return user
        } else {
          throw new Error('用户信息为空')
        }
      } else {
        console.error('后端返回错误:', response.data)
        throw new Error(response.data.message || '获取用户信息失败')
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
      // 不要在这里调用 logout()，让调用方决定如何处理
      throw error
    }
  }

  // 检查是否为管理员
  function isAdmin() {
    return currentUser.value?.userRole === 'admin'
  }

  // 更新用户信息
  async function updateUserProfile(updates: Partial<User>) {
    try {
      // 这里可以调用后端API更新用户信息
      // const response = await http.put('/user/update', updates)
      
      // 暂时只更新本地状态
      if (currentUser.value) {
        const updatedUser = { ...currentUser.value, ...updates }
        currentUser.value = updatedUser
        localStorage.setItem('user', JSON.stringify(updatedUser))
      }
      
      return currentUser.value
    } catch (error) {
      console.error('更新用户信息失败:', error)
      throw error
    }
  }

  // 清理无效的本地存储
  function clearInvalidStorage() {
    console.log('清理无效的本地存储数据')
    currentUser.value = null
    isLoggedIn.value = false
    token.value = ''
    localStorage.removeItem('user')
    localStorage.removeItem('token')
  }

  // 初始化用户状态
  async function initUser() {
    const savedToken = localStorage.getItem('token')
    const savedUser = localStorage.getItem('user')
    
    console.log('初始化用户状态 - 本地存储:', { savedToken, savedUser })
    
    if (savedToken && savedUser && savedUser !== 'null' && savedUser !== 'undefined') {
      try {
        // 先设置本地用户信息，确保UI能立即显示
        const localUser = JSON.parse(savedUser)
        if (localUser && typeof localUser === 'object') {
          currentUser.value = localUser
          isLoggedIn.value = true
          token.value = savedToken
          
          console.log('已设置本地用户信息:', localUser)
        } else {
          throw new Error('本地用户信息格式无效')
        }
        
        // 然后尝试从后端获取最新的用户信息
        try {
          const user = await getCurrentUser()
          console.log('从后端获取到最新用户信息:', user)
          return user
        } catch (backendError) {
          console.warn('从后端获取用户信息失败，使用本地存储:', backendError)
          // 后端获取失败，但本地信息有效，继续使用本地信息
          return localUser
        }
      } catch (parseError) {
        console.error('本地用户信息无效，清除所有状态:', parseError)
        clearInvalidStorage()
        return null
      }
    } else {
      console.log('没有本地用户信息，清除状态')
      clearInvalidStorage()
      return null
    }
  }

  return {
    currentUser,
    token,
    isLoggedIn,
    login,
    register,
    logout,
    getCurrentUser,
    isAdmin,
    initUser,
    updateUserProfile
  }
})
