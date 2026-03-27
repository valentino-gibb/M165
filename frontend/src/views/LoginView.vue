<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../stores/auth.js'

const router   = useRouter()
const { login } = useAuth()

const username = ref('')
const error    = ref('')
const loading  = ref(false)

async function handleLogin() {
  if (!username.value.trim()) return
  error.value   = ''
  loading.value = true
  try {
    await login(username.value.trim())
    router.push('/app/home')
  } catch {
    error.value = 'Benutzername nicht gefunden. Bitte versuche es erneut.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-bg">
      <div class="bg-blob b1"></div>
      <div class="bg-blob b2"></div>
    </div>

    <div class="login-card">
      <div class="login-logo">
        <span class="logo-icon">♪</span>
        <span class="logo-name">MusicDB</span>
      </div>

      <h1 class="login-title">Willkommen zurück</h1>
      <p class="login-sub">Melde dich mit deinem Benutzernamen an</p>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label class="label">Benutzername</label>
          <input
            v-model="username"
            class="input"
            placeholder="z.B. kanye_west"
            autocomplete="username"
            autofocus
          />
        </div>

        <p v-if="error" class="error-msg">{{ error }}</p>

        <button type="submit" class="btn btn-primary login-btn" :disabled="loading || !username.trim()">
          <span v-if="loading">Laden…</span>
          <span v-else>Anmelden →</span>
        </button>
      </form>

      <p class="login-hint">Nutze einen der vorhandenen Benutzernamen aus der Datenbank.</p>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg);
  position: relative;
  overflow: hidden;
}

.login-bg { position: absolute; inset: 0; pointer-events: none; }
.bg-blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.15;
}
.b1 {
  width: 400px; height: 400px;
  background: var(--accent);
  top: -100px; left: -100px;
}
.b2 {
  width: 300px; height: 300px;
  background: #2563eb;
  bottom: -80px; right: -60px;
}

.login-card {
  position: relative;
  background: #161616;
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 40px 36px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 24px 60px rgba(0,0,0,0.5);
}

.login-logo {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 28px;
}
.logo-icon { font-size: 24px; color: var(--accent); }
.logo-name { font-size: 18px; font-weight: 700; }

.login-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 6px;
}
.login-sub {
  color: var(--text-muted);
  font-size: 14px;
  margin-bottom: 28px;
}

.login-form { display: flex; flex-direction: column; }

.login-btn {
  width: 100%;
  justify-content: center;
  padding: 11px;
  font-size: 14px;
  margin-top: 4px;
}

.login-hint {
  margin-top: 20px;
  font-size: 12px;
  color: var(--text-dim);
  text-align: center;
}
</style>
