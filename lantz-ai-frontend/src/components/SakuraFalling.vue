<template>
  <div class="sakura" aria-hidden="true">
    <span v-for="n in 14" :key="n" class="petal" :style="styleFor(n)"></span>
  </div>
</template>

<script setup lang="ts">
function styleFor(n: number) {
  const delay = (n * 0.6) % 8
  const duration = 8 + (n % 5)
  const left = (n * 7) % 100
  const size = 8 + (n % 6) * 2
  const rotate = (n % 360)
  return {
    left: `${left}%`,
    animationDelay: `${delay}s`,
    animationDuration: `${duration}s`,
    width: `${size}px`,
    height: `${size * 0.8}px`,
    transform: `rotate(${rotate}deg)`
  }
}
</script>

<style scoped>
.sakura { position: fixed; inset: 0; pointer-events: none; z-index: 0; overflow: hidden; }
.petal { position: absolute; top: -40px; background: radial-gradient(circle at 30% 30%, #ffd1dc 0%, #ff9db6 60%, #ff7aa1 100%); border-radius: 60% 40% 60% 40%; opacity: .8; filter: drop-shadow(0 2px 6px rgba(255,122,161,.25)); animation-name: fall, sway; animation-timing-function: linear, ease-in-out; animation-iteration-count: infinite, infinite; }
@keyframes fall { to { transform: translateY(110vh) rotate(720deg); } }
@keyframes sway { 0% { margin-left: -10px; } 50% { margin-left: 10px; } 100% { margin-left: -10px; } }
</style>


