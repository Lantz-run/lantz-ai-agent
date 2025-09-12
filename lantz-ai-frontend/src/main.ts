import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import Home from './pages/Home.vue'
import LoveChat from './pages/LoveChat.vue'
import ManusChat from './pages/ManusChat.vue'
import './styles.css'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: Home, meta: { title: 'Lantz AI | 首页' } },
    { path: '/love', name: 'love', component: LoveChat, meta: { title: 'Lantz AI | 恋爱大师' } },
    { path: '/manus', name: 'manus', component: ManusChat, meta: { title: 'Lantz AI | 超级智能体' } }
  ]
})

router.afterEach((to) => {
  const t = (to.meta?.title as string) || 'Lantz AI'
  if (t) document.title = t
})

createApp(App).use(router).mount('#app')


