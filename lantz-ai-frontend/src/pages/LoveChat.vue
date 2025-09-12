<template>
  <div class="chat-page">
    <header class="chat-header">
      <router-link to="/">← 返回</router-link>
      <h2>AI 恋爱大师</h2>
      <span class="chat-id">ID: {{ chatId }}</span>
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
          placeholder="输入你的恋爱问题...（最多150字符）"
        />
        <div class="tools">
          <span class="hint">{{ input.length }}/150 · Enter 发送 · Shift+Enter 换行</span>
          <button :disabled="loading || !input.trim()" @click="send">发送</button>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { openSse } from '@/utils/sse'

type ChatMsg = { role: 'user' | 'ai'; content: string }

const messages = ref<ChatMsg[]>([])
const input = ref('')
const chatId = ref('')
const loading = ref(false)
const chatBodyRef = ref<HTMLElement | null>(null)
const taRef = ref<HTMLTextAreaElement | null>(null)
let es: EventSource | null = null
let hasReceived = false
const aiAvatar = '/ai-love.png'
const userAvatar = '/user.png'
const fallbackAi =
  'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><rect width="64" height="64" rx="32" fill="%23ffe4ef"/><path d="M32 50s-12-8-16.4-12.4A10 10 0 0 1 31 17l1 1 1-1a10 10 0 0 1 15.4 13.6C44 42 32 50 32 50z" fill="%23f472b6"/></svg>'
const fallbackUser =
  'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 64 64"><rect width="64" height="64" rx="32" fill="%23e5e7eb"/><circle cx="32" cy="26" r="10" fill="%2399a3ad"/><rect x="14" y="38" width="36" height="14" rx="7" fill="%2399a3ad"/></svg>'

function onImgError(e: Event, role: 'user' | 'ai') {
  const img = e.target as HTMLImageElement
  img.onerror = null
  img.src = role === 'ai' ? fallbackAi : fallbackUser
}

function genId() {
  return 'chat_' + Math.random().toString(36).slice(2, 10)
}

function scrollToBottom() {
  nextTick(() => {
    const el = chatBodyRef.value
    if (el) el.scrollTop = el.scrollHeight
  })
}

function startStream(message: string) {
  if (es) {
    es.close()
    es = null
  }
  loading.value = true
  const url = new URL('http://localhost:8123/api/ai/love_app/ai_chat/sse')
  url.searchParams.set('message', message)
  url.searchParams.set('chatId', chatId.value)
  let aiBuffer = ''
  hasReceived = false
  es = openSse(url.toString(), {
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
    onError: () => {
      loading.value = false
      // 如果已经收到过流数据，则视为正常结束，不提示错误
      if (!hasReceived) {
        const hint = '请求异常，请稍后重试或检查输入内容。'
        messages.value.push({ role: 'ai', content: hint })
        scrollToBottom()
      }
    },
    onOpen: () => {}
  })
}

function send() {
  const text = input.value.trim()
  if (!text) return
  input.value = text.slice(0, 150)
  messages.value.push({ role: 'user', content: text })
  input.value = ''
  scrollToBottom()
  startStream(text)
}

onMounted(() => {
  chatId.value = genId()
})

onBeforeUnmount(() => {
  es?.close()
})

function onInput() {
  // 限制长度
  if (input.value.length > 150) input.value = input.value.slice(0, 150)
  // 自适应高度
  const el = taRef.value
  if (!el) return
  el.style.height = 'auto'
  el.style.height = Math.min(el.scrollHeight, 180) + 'px'
}
</script>

<style scoped>
.chat-page { display: grid; grid-template-rows: auto 1fr auto; height: 100vh; }
.chat-header { display: flex; align-items: center; gap: 12px; padding: 12px 16px; border-bottom: 1px solid rgba(148,163,184,.15); }
.chat-header h2 { margin: 0; font-size: 18px; }
.chat-id { margin-left: auto; color: var(--muted); font-size: 12px; }
.chat-body { overflow-y: auto; padding: 16px; }
.msg { display: flex; gap: 8px; margin: 10px 0; align-items: flex-start; }
.msg.user { flex-direction: row-reverse; }
.avatar { width: 28px; height: 28px; border-radius: 50%; border: 1px solid rgba(148,163,184,.25); background: #fff; }
.bubble { max-width: 70%; padding: 10px 12px; border-radius: 12px; white-space: pre-wrap; word-break: break-word; background: linear-gradient(180deg, var(--panel), var(--panel-2)); border: 1px solid rgba(148,163,184,.2); }
.msg.user .bubble { background: linear-gradient(180deg, rgba(34,211,238,.2), rgba(96,165,250,.2)); border-color: rgba(96,165,250,.35); }
.chat-input { padding: 12px; border-top: 1px solid rgba(148,163,184,.15); }
.input-box { width: 100%; background: linear-gradient(180deg, var(--panel), var(--panel-2)); border: 1px solid rgba(148,163,184,.25); border-radius: 16px; box-shadow: var(--shadow); padding: 8px; display: flex; flex-direction: column; gap: 6px; }
.input-box:focus-within { border-color: var(--primary-2); box-shadow: 0 0 0 2px rgba(96,165,250,.25); }
.chat-input textarea { width: 100%; padding: 10px 12px; border: none; outline: none; background: transparent; color: var(--text); resize: none; overflow: hidden; max-height: 180px; }
.tools { display: flex; align-items: center; justify-content: space-between; gap: 8px; }
.hint { color: var(--muted); font-size: 12px; }
.chat-input button { padding: 8px 14px; border: none; background: linear-gradient(90deg, var(--primary), var(--primary-2)); color: #0b0f14; font-weight: 600; border-radius: 10px; cursor: pointer; }
.chat-input button[disabled] { opacity: .6; cursor: not-allowed; }
</style>


