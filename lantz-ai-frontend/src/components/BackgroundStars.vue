<template>
  <div class="stars" aria-hidden="true"></div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.stars { position: fixed; inset: 0; pointer-events: none; z-index: 0; background-image:
  radial-gradient(2px 2px at 20% 30%, rgba(255,255,255,.5), transparent 60%),
  radial-gradient(1.5px 1.5px at 80% 20%, rgba(255,255,255,.35), transparent 60%),
  radial-gradient(1.5px 1.5px at 60% 70%, rgba(255,255,255,.35), transparent 60%),
  radial-gradient(2px 2px at 40% 80%, rgba(255,255,255,.5), transparent 60%),
  radial-gradient(1px 1px at 10% 60%, rgba(255,255,255,.25), transparent 60%);
  animation: drift 40s linear infinite;
  opacity: .7;
}
@keyframes drift { to { background-position: 1000px 0, -800px 0, 600px 0, -400px 0, 300px 0; } }
</style>


