<script setup>
import { onMounted } from 'vue'
import Sidebar from '../components/Sidebar.vue'
import { useAuth } from '../stores/auth.js'

const { refreshUser } = useAuth()
onMounted(() => refreshUser())
</script>

<template>
  <div class="app-layout">
    <Sidebar />
    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.app-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}
.main-content {
  flex: 1;
  overflow-y: auto;
  padding: 32px 36px;
  background: var(--bg);
}
</style>
