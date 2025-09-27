import { http } from '@/utils/http'

export type AppKey = 'love_app' | 'love_app_rag' | 'manus'

export interface ConversationItem {
  conversationId: string
  updateTime?: string
}

async function unwrap<T>(p: Promise<any>): Promise<T> {
  const res = await p
  const data = res.data
  if (data && typeof data === 'object') {
    if (data.code === 0) return data.data as T
    if (data.data !== undefined && data.code === undefined) return data as T
  }
  return data as T
}

export async function listConversations(app: AppKey): Promise<ConversationItem[]> {
  return unwrap<ConversationItem[]>(http.get(`/ai/conversations/list`, { params: { app } }))
}

export async function createConversation(app: AppKey): Promise<{ conversationId: string; updateTime?: string }> {
  return unwrap<{ conversationId: string; updateTime?: string }>(http.post(`/ai/conversations/create`, { app }))
}
