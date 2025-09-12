# Lantz AI Frontend

这是基于 Vue3 + Vite 的前端，包含两个基于 SSE 的实时聊天页面。

## 功能
- AI 恋爱大师（`/ai/love_app/ai_chat/sse`）
- AI 超级智能体（`/ai/manus/ai_chat`）

## 开发
1. 安装依赖
```bash
npm i
```
2. 启动
```bash
npm run dev
```
访问：`http://localhost:5173`

## 目录
- `src/pages/Home.vue`
- `src/pages/LoveChat.vue`
- `src/pages/ManusChat.vue`
- `src/utils/sse.ts`
- `src/utils/http.ts`

## 后端
后端基础地址：`http://localhost:8123/api`
- 恋爱大师：`GET /ai/love_app/ai_chat/sse?message=...&chatId=...` 返回 `text/event-stream`
- 超级智能体：`GET /ai/manus/ai_chat?message=...` 返回 `SseEmitter`

## 注意
- 若有跨域，请在后端放通 `http://localhost:5173` 或配置代理。
- SSE 需后端持续以 `text/event-stream` 推送；前端使用原生 `EventSource`。
