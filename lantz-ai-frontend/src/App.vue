<template>
  <div class="app-shell">
    <BackgroundStars />
    <SakuraFalling />
    <router-view />
    <FooterBar />
  </div>
  
</template>

<script setup lang="ts">
import FooterBar from './components/FooterBar.vue'
import BackgroundStars from './components/BackgroundStars.vue'
import SakuraFalling from './components/SakuraFalling.vue'
</script>

<style scoped>
.app-shell { min-height: 100vh; display: grid; grid-template-rows: 1fr auto; }
</style>


