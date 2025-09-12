<template>
  <footer class="footer card">
    <div class="inner container">
      <div class="brand">Lantz AI</div>
      <div class="copy">© 2025 Lantz AI Agent Platform <a href="https://github.com/Lantz-run" target="_blank" rel="noopener">Lantz_Github</a></div>
    </div>
  </footer>
</template>

<script setup lang="ts">
</script>

<style scoped>
.footer { margin-top: 24px; border-radius: 0; border-top-left-radius: 12px; border-top-right-radius: 12px; }
.inner { display: flex; align-items: center; gap: 12px; padding: 16px 24px; justify-content: space-between; }
.brand { font-weight: 700; letter-spacing: .5px; background: linear-gradient(90deg, var(--primary), var(--primary-2)); -webkit-background-clip: text; background-clip: text; color: transparent; }
.copy { color: var(--muted); font-size: 12px; }
.copy a { color: var(--text); text-decoration: underline; opacity: .8; }
@media (max-width: 640px) { .inner { flex-direction: column; gap: 8px; } }
</style>


