<template>
  <div class="chat-container">
    <div class="chat-page">
      <header class="chat-header">
        <router-link to="/" class="back-btn">← 返回</router-link>
        <h2>AI 超级智能体</h2>
        <div class="header-right">
          <span class="chat-id">ID: {{ chatId }}</span>
          <button @click="handleLogout" class="logout-btn">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
              <path d="M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z"/>
            </svg>
            <span>退出</span>
          </button>
        </div>
      </header>

      <main class="chat-body card" ref="chatBodyRef">
        <div v-for="(m, idx) in messages" :key="idx" class="msg" :class="m.role">
          <img class="avatar" :class="m.role" :src="m.role==='ai'? aiAvatar : userAvatar" @error="onImgError($event, m.role)" alt="avatar" />
          <div class="bubble">{{ m.content }}</div>
        </div>
      </main>

      <footer class="chat-input">
        <div class="input-box">
          <textarea
            ref="taRef"
            v-model="input"
            :disabled="loading"
            @input="onInput"
            @keydown.enter.exact.prevent="send"
            maxlength="150"
            rows="1"
            placeholder="输入你的任务需求...（最多150字符）"
          />
          <div class="tools">
            <span class="hint">{{ input.length }}/150 · Enter 发送 · Shift+Enter 换行</span>
            <button :disabled="loading || !input.trim()" @click="send">发送</button>
          </div>
        </div>
      </footer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { openSse } from '@/utils/sse'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

type ChatMsg = { role: 'user' | 'ai'; content: string }

const messages = ref<ChatMsg[]>([])
const input = ref('')
const loading = ref(false)
const chatId = ref('')
const chatBodyRef = ref<HTMLElement>()
const taRef = ref<HTMLTextAreaElement>()

// 头像
const aiAvatar = 'https://api.dicebear.com/7.x/avataaars/svg?seed=ai-manus'
const userAvatar = 'https://api.dicebear.com/7.x/avataaars/svg?seed=user'
const fallbackAi =
  'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><rect width="64" height="64" rx="32" fill="%23e0f2fe"/><path d="M32 12c11.046 0 20 8.954 20 20s-8.954 20-20 20-20-8.954-20-20 8.954-20 20-20zm0 4c-8.837 0-16 7.163-16 16s7.163 16 16 16 16-7.163 16-16-7.163-16-16-16zm0 8c4.418 0 8 3.582 8 8s-3.582 8-8 8-8-3.582-8-8 3.582-8 8-8z" fill="%2360a5fa"/></svg>'
const fallbackUser =
  'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><rect width="64" height="64" rx="32" fill="%23e5e7eb"/><circle cx="32" cy="26" r="10" fill="%2399a3ad"/><rect x="14" y="38" width="36" height="14" rx="7" fill="%2399a3ad"/></svg>'

// 用户状态
const userStore = useUserStore()
const router = useRouter()

function onImgError(e: Event, role: 'user' | 'ai') {
  const img = e.target as HTMLImageElement
  img.onerror = null
  img.src = role === 'ai' ? fallbackAi : fallbackUser
}

function generateChatId() {
  return 'manus-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9)
}

function scrollToBottom() {
  nextTick(() => {
    if (chatBodyRef.value) {
      chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
    }
  })
}

function onInput() {
  const el = taRef.value
  if (!el) return
  el.style.height = 'auto'
  el.style.height = Math.min(el.scrollHeight, 180) + 'px'
}

function truncate200(text: string) {
  return text.length > 200 ? text.substring(0, 200) + '...' : text
}

function send() {
  if (loading.value || !input.value.trim()) return
  
  const userMsg = input.value.trim()
  input.value = ''
  onInput()
  
  messages.value.push({ role: 'user', content: userMsg })
  scrollToBottom()
  
  loading.value = true
  startStream(userMsg)
}

function startStream(userInput: string) {
  const url = `/api/ai/manus/ai_chat/sse?message=${encodeURIComponent(userInput)}&chatId=${chatId.value}`
  
  openSse(url, {
    onMessage: (data) => {
      if (data && data.trim()) {
        // 每个步骤单独显示为一个气泡
        const truncatedData = truncate200(data)
        messages.value.push({ role: 'ai', content: truncatedData })
        scrollToBottom()
      }
    },
    onError: (error) => {
      console.error('SSE Error:', error)
      messages.value.push({ 
        role: 'ai', 
        content: '请求异常，请稍后重试或检查输入内容。' 
      })
      scrollToBottom()
    },
    onClose: () => {
      loading.value = false
    }
  })
}

// 退出登录
async function handleLogout() {
  try {
    await userStore.logout()
    router.push('/login')
  } catch (error) {
    console.error('退出登录失败:', error)
  }
}

onMounted(async () => {
  chatId.value = generateChatId()
  await userStore.initUser()
})
</script>

<style scoped>
.chat-container {
  min-height: 100vh;
  background: var(--bg);
  position: relative;
}

.chat-page { display: grid; grid-template-rows: auto 1fr auto; height: 100vh; }
.chat-header { display: flex; align-items: center; gap: 12px; padding: 12px 16px; border-bottom: 1px solid rgba(148,163,184,.15); }
.chat-header h2 { margin: 0; font-size: 18px; }
.chat-id { margin-left: auto; color: var(--muted); font-size: 12px; }

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: auto;
}

.back-btn {
  color: var(--primary);
  text-decoration: none;
  font-size: 14px;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

.back-btn:hover {
  background-color: rgba(96, 165, 250, 0.1);
}

.chat-body { overflow-y: auto; padding: 16px; }
.msg { display: flex; gap: 8px; margin: 10px 0; align-items: flex-start; }
.msg.user { flex-direction: row-reverse; }
.avatar { width: 28px; height: 28px; border-radius: 50%; border: 1px solid rgba(148,163,184,.25); background: #fff; }
.bubble { max-width: 70%; padding: 10px 12px; border-radius: 12px; white-space: pre-wrap; word-break: break-word; background: linear-gradient(180deg, var(--panel), var(--panel-2)); border: 1px solid rgba(148,163,184,.2); }
.msg.user .bubble { background: linear-gradient(180deg, var(--primary), #3b82f6); color: white; border-color: rgba(59,130,246,.3); }
.chat-input { border-top: 1px solid rgba(148,163,184,.15); }
.input-box { padding: 16px; display: flex; flex-direction: column; gap: 12px; }
textarea { width: 100%; min-height: 40px; max-height: 180px; padding: 12px; border: 1px solid rgba(148,163,184,.3); border-radius: 8px; background: var(--panel); color: var(--text); font-size: 14px; resize: none; outline: none; transition: border-color 0.2s ease; }
textarea:focus { border-color: var(--primary); }
textarea:disabled { opacity: 0.5; cursor: not-allowed; }
.tools { display: flex; justify-content: space-between; align-items: center; }
.hint { font-size: 12px; color: var(--muted); }
button { padding: 8px 16px; background: var(--primary); color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; transition: background-color 0.2s ease; }
button:hover:not(:disabled) { background: #3b82f6; }
button:disabled { opacity: 0.5; cursor: not-allowed; }

/* 右上角退出登录按钮样式 */
.logout-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  border-radius: 6px;
  color: #ef4444;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 12px;
  font-weight: 500;
  backdrop-filter: blur(10px);
}

.logout-btn:hover {
  background: rgba(239, 68, 68, 0.2);
  border-color: rgba(239, 68, 68, 0.5);
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.2);
}

.logout-btn svg {
  flex-shrink: 0;
}

@media (max-width: 768px) { 
  .input-box { padding: 12px; }
  .tools { flex-direction: column; gap: 8px; align-items: stretch; }
  .tools button { width: 100%; }
  
  .header-right {
    gap: 8px;
  }
  
  .logout-btn {
    padding: 6px 10px;
    font-size: 11px;
  }
  
  .chat-id {
    font-size: 10px;
  }
}
</style>