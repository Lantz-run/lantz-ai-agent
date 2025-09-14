<template>
  <div class="chat-container">
    <div class="chat-page">
      <header class="chat-header">
        <router-link to="/" class="back-btn">← 返回</router-link>
        <h2>AI 恋爱大师 RAG</h2>
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
          <div class="state-selector">
            <label>情感状态：</label>
            <select v-model="selectedState" :disabled="loading">
              <option value="单身">单身 - 寻找真爱</option>
              <option value="恋爱">恋爱中 - 维护关系</option>
              <option value="已婚">已婚 - 经营婚姻</option>
            </select>
            <span class="state-hint">选择你的情感状态，AI将提供更精准的建议</span>
          </div>
          <textarea
            ref="taRef"
            v-model="input"
            :disabled="loading"
            @input="onInput"
            @keydown.enter.exact.prevent="send"
            maxlength="150"
            rows="1"
            placeholder="告诉我你的情感状态和困扰，获得个性化建议...（最多150字符）"
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
const selectedState = ref('单身')
const chatBodyRef = ref<HTMLElement>()
const taRef = ref<HTMLTextAreaElement>()

// SSE相关变量
let es: EventSource | null = null
let hasReceived = false

// 头像
const aiAvatar = 'https://api.dicebear.com/7.x/avataaars/svg?seed=ai-love'
const userAvatar = 'https://api.dicebear.com/7.x/avataaars/svg?seed=user'
const fallbackAi =
  'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><rect width="64" height="64" rx="32" fill="%23ffe4ef"/><path d="M32 50s-12-8-16.4-12.4A10 10 0 0 1 31 17l1 1 1-1a10 10 0 0 1 15.4 13.6C44 42 32 50 32 50z" fill="%23f472b6"/></svg>'
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
  return 'rag-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9)
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

function send() {
  if (loading.value || !input.value.trim()) return
  
  // 检查用户登录状态
  if (!userStore.isLoggedIn || !userStore.currentUser) {
    messages.value.push({ 
      role: 'ai', 
      content: '请先登录后再使用AI恋爱大师RAG功能。' 
    })
    scrollToBottom()
    return
  }
  
  const userMsg = input.value.trim()
  input.value = ''
  onInput()
  
  messages.value.push({ role: 'user', content: userMsg })
  scrollToBottom()
  
  loading.value = true
  startStream(userMsg)
}

function startStream(userInput: string) {
  if (es) {
    es.close()
    es = null
  }
  
  const url = `/api/ai/love_app/ai_chat_rag/sse?message=${encodeURIComponent(userInput)}&state=${encodeURIComponent(selectedState.value)}&chatId=${chatId.value}`
  
  let aiBuffer = ''
  hasReceived = false
  
  es = openSse(url, {
    onMessage: (data) => {
      hasReceived = true
      aiBuffer += data
      const last = messages.value[messages.value.length - 1]
      if (last && last.role === 'ai') {
        last.content = aiBuffer
      } else {
        messages.value.push({ role: 'ai', content: aiBuffer })
      }
      scrollToBottom()
    },
    onError: (error) => {
      console.error('SSE Error:', error)
      loading.value = false
      es?.close()
      // 如果已经收到过流数据，则视为正常结束，不提示错误
      if (!hasReceived) {
        const hint = '请求异常，请检查登录状态或稍后重试。'
        messages.value.push({ role: 'ai', content: hint })
        scrollToBottom()
      }
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

onBeforeUnmount(() => {
  if (es) {
    es.close()
    es = null
  }
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
.state-selector { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
.state-selector label { font-size: 14px; color: var(--text); }
.state-selector select { padding: 6px 12px; border: 1px solid rgba(148,163,184,.3); border-radius: 6px; background: var(--panel); color: var(--text); font-size: 14px; }
.state-selector select:disabled { opacity: 0.5; cursor: not-allowed; }
.state-hint { font-size: 12px; color: var(--muted); margin-left: 8px; }
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