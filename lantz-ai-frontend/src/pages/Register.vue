<template>
  <div class="register-page">
    <div class="geek-glow" :style="glowStyle"></div>
    <div class="register-container card">
      <div class="header">
        <h1 class="neon">Lantz AI</h1>
        <p class="subtitle">创建你的智能助手账户</p>
      </div>
      
      <form @submit.prevent="handleRegister" class="form">
        <div class="input-group">
          <label>账户名</label>
          <input 
            v-model="form.userAccount" 
            type="text" 
            placeholder="请输入账户名"
            required
            :disabled="loading"
          />
        </div>
        
        <div class="input-group">
          <label>用户名</label>
          <input 
            v-model="form.userName" 
            type="text" 
            placeholder="请输入用户名"
            required
            :disabled="loading"
          />
        </div>
        
        <div class="input-group">
          <label>密码</label>
          <input 
            v-model="form.userPassword" 
            type="password" 
            placeholder="请输入密码"
            required
            :disabled="loading"
          />
        </div>
        
        <div class="input-group">
          <label>确认密码</label>
          <input 
            v-model="form.checkPassword" 
            type="password" 
            placeholder="请再次输入密码"
            required
            :disabled="loading"
          />
        </div>
        
        <button type="submit" :disabled="loading" class="submit-btn">
          <span v-if="loading">注册中...</span>
          <span v-else>注册</span>
        </button>
      </form>
      
      <div class="footer">
        <p>已有账户？ <router-link to="/login" class="link">立即登录</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const form = reactive({
  userAccount: '',
  userName: '',
  userPassword: '',
  checkPassword: ''
})

const glowStyle = ref<Record<string, string>>({})

function onMouseMove(e: MouseEvent) {
  const el = e.currentTarget as HTMLElement
  const r = el.getBoundingClientRect()
  const x = ((e.clientX - r.left) / r.width) * 100
  const y = ((e.clientY - r.top) / r.height) * 100
  glowStyle.value = {
    background: `radial-gradient(400px 200px at ${x}% ${y}%, rgba(34,211,238,.12), transparent 60%), radial-gradient(320px 160px at ${100 - x}% ${y}%, rgba(96,165,250,.10), transparent 65%)`
  }
}

function onMouseLeave() {
  glowStyle.value = { background: 'none' }
}

async function handleRegister() {
  if (!form.userAccount || !form.userName || !form.userPassword || !form.checkPassword) return
  
  if (form.userPassword !== form.checkPassword) {
    alert('两次输入的密码不一致')
    return
  }
  
  loading.value = true
  try {
    await userStore.register(form)
    alert('注册成功！请登录')
    router.push('/login')
  } catch (error) {
    console.error('注册失败:', error)
    alert('注册失败，请检查输入信息')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  position: relative;
}

.geek-glow {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;
  transition: background .15s ease;
}

.register-container {
  width: 100%;
  max-width: 400px;
  padding: 40px;
  position: relative;
  z-index: 1;
}

.header {
  text-align: center;
  margin-bottom: 32px;
}

.neon {
  margin: 0 0 8px;
  font-size: 32px;
  letter-spacing: .6px;
  background: linear-gradient(90deg, #22d3ee 0%, #60a5fa 50%, #a78bfa 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-shadow: 0 0 18px rgba(34,211,238,.35), 0 0 24px rgba(96,165,250,.25);
}

.subtitle {
  margin: 0;
  color: var(--muted);
  font-size: 14px;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-group label {
  color: var(--text);
  font-size: 14px;
  font-weight: 500;
}

.input-group input {
  padding: 12px 16px;
  border: 1px solid rgba(148,163,184,.25);
  border-radius: 8px;
  background: transparent;
  color: var(--text);
  font-size: 14px;
  transition: border-color .2s ease;
}

.input-group input:focus {
  outline: none;
  border-color: var(--primary-2);
  box-shadow: 0 0 0 2px rgba(96,165,250,.25);
}

.input-group input:disabled {
  opacity: .6;
  cursor: not-allowed;
}

.submit-btn {
  padding: 12px 24px;
  border: none;
  background: linear-gradient(90deg, var(--primary), var(--primary-2));
  color: #0b0f14;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: opacity .2s ease;
  font-size: 16px;
}

.submit-btn:hover:not(:disabled) {
  opacity: .9;
}

.submit-btn:disabled {
  opacity: .6;
  cursor: not-allowed;
}

.footer {
  text-align: center;
  margin-top: 24px;
}

.footer p {
  margin: 0;
  color: var(--muted);
  font-size: 14px;
}

.link {
  color: var(--primary-2);
  text-decoration: none;
  font-weight: 500;
}

.link:hover {
  text-decoration: underline;
}
</style>
