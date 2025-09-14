import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import { createPinia } from 'pinia'
import App from './App.vue'
import Home from './pages/Home.vue'
import Login from './pages/Login.vue'
import Register from './pages/Register.vue'
import LoveChat from './pages/LoveChat.vue'
import ManusChat from './pages/ManusChat.vue'
import LoveRagChat from './pages/LoveRagChat.vue'
import './styles.css'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: Home, meta: { title: 'Lantz AI | 首页' } },
    { path: '/login', name: 'login', component: Login, meta: { title: 'Lantz AI | 登录' } },
    { path: '/register', name: 'register', component: Register, meta: { title: 'Lantz AI | 注册' } },
    { path: '/love', name: 'love', component: LoveChat, meta: { title: 'Lantz AI | 恋爱大师', requiresAuth: true } },
    { path: '/manus', name: 'manus', component: ManusChat, meta: { title: 'Lantz AI | 超级智能体', requiresAuth: true, requiresAdmin: true } },
    { path: '/love-rag', name: 'love-rag', component: LoveRagChat, meta: { title: 'Lantz AI | 恋爱大师 RAG', requiresAuth: true } },
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 需要登录的页面
  if (to.meta.requiresAuth) {
    // 检查 localStorage 中的用户信息
    const savedUser = localStorage.getItem('user')
    const savedToken = localStorage.getItem('token')
    
    if (!savedUser || !savedToken) {
      next('/login')
      return
    }
    
    // 需要管理员权限的页面
    if (to.meta.requiresAdmin) {
      try {
        const user = JSON.parse(savedUser)
        if (user.userRole !== 'admin') {
          alert('此功能仅限管理员使用')
          next('/')
          return
        }
      } catch (error) {
        next('/login')
        return
      }
    }
  }
  
  next()
})

router.afterEach((to) => {
  const t = (to.meta?.title as string) || 'Lantz AI'
  if (t) document.title = t
})

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.mount('#app')


