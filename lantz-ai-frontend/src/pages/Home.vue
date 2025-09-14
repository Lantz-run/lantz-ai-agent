<template>
  <div class="home-container">
    <div class="container home" @mousemove="onHomeMove" @mouseleave="onHomeLeave">
      <div class="geek-glow" :style="glowStyle"></div>
      <header class="hero card">
        <h1 class="neon">AI 智能助手应用平台</h1>
        <p class="subtitle">探索AI · 启动你的次世代对话体验</p>
        <div class="terminal" @mousemove="onTermMove" @mouseleave="onTermLeave" :style="termStyle">
          <div class="dots">
            <span></span><span></span><span></span>
          </div>
          <pre>{{ terminalText }}</pre>
        </div>
      </header>
      <div class="grid">
        <router-link class="app-card card" to="/love" @mousemove="onCardMove($event, 'love')" @mouseleave="onCardLeave('love')" :style="cardStyle.love">
          <div class="badge love">LOVE</div>
          <div class="icon">
            <svg viewBox="0 0 24 24" width="48" height="48" fill="#f472b6"><path d="M12 21s-6.716-4.623-9.192-7.1A5.6 5.6 0 0 1 11.2 5.508L12 6.3l.8-.792a5.6 5.6 0 0 1 8.392 7.792C18.716 16.377 12 21 12 21z"/></svg>
          </div>
          <div class="content">
            <h2>AI 恋爱大师</h2>
            <p>智能情感顾问，为你提供专业的恋爱建议、情感分析和心理支持，让爱情之路更加顺畅。</p>
          </div>
        </router-link>
        <router-link class="app-card card" to="/love-rag" @mousemove="onCardMove($event, 'love-rag')" @mouseleave="onCardLeave('love-rag')" :style="cardStyle['love-rag']">
          <div class="badge love-rag">RAG</div>
          <div class="icon">
            <svg viewBox="0 0 24 24" width="48" height="48" fill="#f472b6"><path d="M12 21s-6.716-4.623-9.192-7.1A5.6 5.6 0 0 1 11.2 5.508L12 6.3l.8-.792a5.6 5.6 0 0 1 8.392 7.792C18.716 16.377 12 21 12 21z"/></svg>
          </div>
          <div class="content">
            <h2>AI 恋爱大师 RAG</h2>
            <p>基于海量情感知识库的智能恋爱顾问，结合你的情感状态提供个性化建议，让每一次对话都更有价值。</p>
          </div>
        </router-link>
        <router-link 
          v-if="isAdmin" 
          class="app-card card" 
          to="/manus" 
          @mousemove="onCardMove($event, 'manus')" 
          @mouseleave="onCardLeave('manus')" 
          :style="cardStyle.manus"
        >
          <div class="badge manus">AGENT</div>
          <div class="icon">
            <svg viewBox="0 0 24 24" width="48" height="48" fill="#60a5fa"><path d="M12 3c4.418 0 8 3.134 8 7 0 2.577-1.61 4.81-4 6v2a2 2 0 0 1-2 2h-1v-3h-2v3H10a2 2 0 0 1-2-2v-1.268C5.043 15.79 4 13.557 4 10c0-3.866 3.582-7 8-7z"/></svg>
          </div>
          <div class="content">
            <h2>AI 超级智能体</h2>
            <p>多工具协作、分步骤执行并回传进度，效率与创造力加速。</p>
          </div>
        </router-link>
      </div>
    </div>
    
    <!-- 左下角退出登录按钮 -->
    <div class="logout-container">
      <button @click="handleLogout" class="logout-btn">
        <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
          <path d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z"/>
        </svg>
        <span>退出登录</span>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

// 使用计算属性来避免响应式问题
const isLoggedIn = computed(() => userStore.isLoggedIn)
const currentUser = computed(() => userStore.currentUser)
const isAdmin = computed(() => userStore.isAdmin())

const fullText = `$ ./launch-ai-platform.sh\nInitializing AI engine...\nLoading agent models...\nBinding services...\n\nAI platform ready ✔  选择下方应用开始体验`
const terminalText = ref('')
let timer: number | null = null

onMounted(async () => {
  try {
    // 初始化用户状态（从后端获取）
    await userStore.initUser()
    console.log('用户状态初始化完成:', userStore.currentUser)
  } catch (error) {
    console.error('用户状态初始化失败:', error)
  }
  
  // 启动终端打字效果
  let i = 0
  timer = window.setInterval(() => {
    terminalText.value = fullText.slice(0, i++)
    if (i > fullText.length && timer) window.clearInterval(timer)
  }, 18)
})

const termStyle = ref<Record<string, string>>({})
function onTermMove(e: MouseEvent) {
  const el = e.currentTarget as HTMLElement
  const r = el.getBoundingClientRect()
  const x = (e.clientX - r.left) / r.width - 0.5
  const y = (e.clientY - r.top) / r.height - 0.5
  termStyle.value = { transform: `perspective(800px) rotateX(${(-y * 3).toFixed(2)}deg) rotateY(${(x * 3).toFixed(2)}deg)` }
}
function onTermLeave() { termStyle.value = { transform: 'none' } }

const cardStyle = ref<{ love?: Record<string, string>, 'love-rag'?: Record<string, string>, manus?: Record<string, string> }>({})
function onCardMove(e: MouseEvent, key: 'love' | 'love-rag' | 'manus') {
  const el = e.currentTarget as HTMLElement
  const r = el.getBoundingClientRect()
  const x = (e.clientX - r.left) / r.width - 0.5
  const y = (e.clientY - r.top) / r.height - 0.5
  cardStyle.value[key] = { transform: `translateY(-2px) rotateX(${(-y * 4).toFixed(2)}deg) rotateY(${(x * 6).toFixed(2)}deg)` }
}
function onCardLeave(key: 'love' | 'love-rag' | 'manus') { cardStyle.value[key] = { transform: 'none' } }

// 背景极客星光跟随
const glowStyle = ref<Record<string, string>>({})
function onHomeMove(e: MouseEvent) {
  const el = e.currentTarget as HTMLElement
  const r = el.getBoundingClientRect()
  const x = ((e.clientX - r.left) / r.width) * 100
  const y = ((e.clientY - r.top) / r.height) * 100
  glowStyle.value = {
    background: `radial-gradient(400px 200px at ${x}% ${y}%, rgba(34,211,238,.12), transparent 60%), radial-gradient(320px 160px at ${100 - x}% ${y}%, rgba(96,165,250,.10), transparent 65%)`
  }
}
function onHomeLeave() { glowStyle.value = { background: 'none' } }

async function handleLogout() {
  try {
    await userStore.logout()
    router.push('/login')
  } catch (error) {
    console.error('注销失败:', error)
    // 即使注销失败，也跳转到登录页面
    router.push('/login')
  }
}

function toggleRole() {
  if (userStore.currentUser) {
    const newRole = userStore.currentUser.userRole === 'admin' ? 'user' : 'admin'
    const newUser = {
      ...userStore.currentUser,
      userRole: newRole === 'admin' ? '管理员' : '普通用户',
      userName: userStore.currentUser.userName
    }
    userStore.currentUser = newUser
    localStorage.setItem('user', JSON.stringify(newUser))
  }
}


</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: var(--bg);
  position: relative;
}

.home { 
  padding-top: 40px; 
  padding-bottom: 28px; 
  position: relative; 
}
.geek-glow { position: absolute; inset: 0; z-index: 0; pointer-events: none; transition: background .15s ease; }
.hero { padding: 36px; text-align: center; margin-bottom: 20px; position: relative; z-index: 1; }
.hero.card { background: transparent; border: none; box-shadow: none; }
.neon { margin: 0 0 10px; font-size: 40px; letter-spacing: .6px; background: linear-gradient(90deg, #22d3ee 0%, #60a5fa 50%, #a78bfa 100%); -webkit-background-clip: text; background-clip: text; color: transparent; text-shadow: 0 0 18px rgba(34,211,238,.35), 0 0 24px rgba(96,165,250,.25); }
.subtitle { margin: 0 0 14px; color: var(--muted); }
.terminal { text-align: left; margin: 0 auto; max-width: 900px; border: 1px solid rgba(148,163,184,.18); border-radius: 12px; overflow: hidden; background: linear-gradient(180deg, rgba(2,6,23,.6), rgba(2,6,23,.9)); box-shadow: var(--shadow); }
.terminal .dots { display: flex; gap: 6px; padding: 10px 12px; border-bottom: 1px solid rgba(148,163,184,.12); background: rgba(148,163,184,.05); }
.terminal .dots span { width: 10px; height: 10px; border-radius: 50%; background: #f87171; }
.terminal .dots span:nth-child(2) { background: #fbbf24; }
.terminal .dots span:nth-child(3) { background: #34d399; }
.terminal pre { margin: 0; padding: 18px; font-size: 15px; color: #c7d2fe; font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace; white-space: pre-wrap; }
.grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 20px; max-width: 1200px; margin: 0 auto; }
.app-card { display: grid; grid-template-rows: auto 1fr; place-items: center; gap: 8px; padding: 26px; min-height: 220px; position: relative; text-decoration: none; color: inherit; border: 1px solid rgba(148,163,184,.18); transition: transform .15s ease, box-shadow .2s ease; will-change: transform; overflow: hidden; }
.app-card:hover { transform: translateY(-2px); box-shadow: var(--shadow); }
.content { text-align: center; }
.app-card h2 { margin: 6px 0 6px; font-size: 22px; }
.app-card p { margin: 0; color: var(--muted); max-width: 90%; }
.icon { opacity: .95; filter: drop-shadow(0 8px 24px rgba(96,165,250,.25)); }
.icon svg { width: 56px; height: 56px; }
.badge { position: absolute; top: 12px; right: 12px; font-size: 11px; padding: 4px 8px; border-radius: 999px; letter-spacing: .6px; background: rgba(255,255,255,.06); border: 1px solid rgba(148,163,184,.25); }
.badge.love { color: #f472b6; }
.badge.love-rag { color: #f472b6; }
.badge.manus { color: #60a5fa; }
/* 响应式设计 */
@media (max-width: 1024px) { 
  .grid { grid-template-columns: repeat(2, minmax(0, 1fr)); } 
}

/* 退出登录按钮样式 */
.logout-container {
  position: fixed;
  bottom: 20px;
  left: 20px;
  z-index: 100;
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  border-radius: 8px;
  color: #ef4444;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
  backdrop-filter: blur(10px);
}

.logout-btn:hover {
  background: rgba(239, 68, 68, 0.2);
  border-color: rgba(239, 68, 68, 0.5);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.2);
}

.logout-btn svg {
  flex-shrink: 0;
}

@media (max-width: 768px) { 
  .grid { grid-template-columns: 1fr; } 
  
  .logout-container {
    bottom: 16px;
    left: 16px;
  }
  
  .logout-btn {
    padding: 10px 14px;
    font-size: 13px;
  }
}
</style>


