<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../stores/auth.js'
import { api } from '../api.js'

const router     = useRouter()
const { login, setUser } = useAuth()

const mode    = ref('login') // 'login' | 'signup'
const error   = ref('')
const loading = ref(false)

// ── Login ──────────────────────────────────────────────────────
const loginForm = reactive({ username: '' })

async function handleLogin() {
  if (!loginForm.username.trim()) return
  error.value   = ''
  loading.value = true
  try {
    await login(loginForm.username.trim())
    router.push('/app/home')
  } catch {
    error.value = 'Benutzername nicht gefunden.'
  } finally {
    loading.value = false
  }
}

// ── Signup ─────────────────────────────────────────────────────
const signupForm = reactive({
  username:       '',
  email:          '',
  country:        '',
  profilePicture: '',
  passwordHash:   '',
})

async function handleSignup() {
  if (!signupForm.username.trim() || !signupForm.email.trim()) {
    error.value = 'Benutzername und E-Mail sind Pflichtfelder.'
    return
  }
  error.value   = ''
  loading.value = true
  try {
    const newUser = await api.createUser({
      username:       signupForm.username.trim(),
      email:          signupForm.email.trim(),
      country:        signupForm.country.trim() || 'Unknown',
      profilePicture: signupForm.profilePicture.trim() || `https://i.pravatar.cc/150?u=${signupForm.username}`,
      passwordHash:   signupForm.passwordHash || 'hashed_pw',
      playlistIds:    [],
      artistId:       null,
    })
    setUser(newUser)
    router.push('/app/home')
  } catch {
    error.value = 'Fehler beim Erstellen des Accounts. Versuche es erneut.'
  } finally {
    loading.value = false
  }
}

function switchMode(m) {
  mode.value  = m
  error.value = ''
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

      <!-- Tab switcher -->
      <div class="tabs">
        <button class="tab" :class="{ active: mode === 'login' }"  @click="switchMode('login')">Anmelden</button>
        <button class="tab" :class="{ active: mode === 'signup' }" @click="switchMode('signup')">Registrieren</button>
      </div>

      <!-- Login -->
      <template v-if="mode === 'login'">
        <p class="form-sub">Melde dich mit deinem Benutzernamen an</p>
        <form @submit.prevent="handleLogin" class="auth-form">
          <div class="form-group">
            <label class="label">Benutzername</label>
            <input v-model="loginForm.username" class="input" placeholder="z.B. kanye_west" autofocus />
          </div>
          <p v-if="error" class="error-msg">{{ error }}</p>
          <button type="submit" class="btn btn-primary full-btn" :disabled="loading || !loginForm.username.trim()">
            {{ loading ? 'Laden…' : 'Anmelden →' }}
          </button>
        </form>
      </template>

      <!-- Signup -->
      <template v-else>
        <p class="form-sub">Erstelle deinen neuen Account</p>
        <form @submit.prevent="handleSignup" class="auth-form">
          <div class="form-row">
            <div class="form-group">
              <label class="label">Benutzername *</label>
              <input v-model="signupForm.username" class="input" placeholder="dein_username" />
            </div>
            <div class="form-group">
              <label class="label">E-Mail *</label>
              <input v-model="signupForm.email" class="input" type="email" placeholder="mail@example.com" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="label">Land</label>
              <input v-model="signupForm.country" class="input" placeholder="z.B. Switzerland" />
            </div>
            <div class="form-group">
              <label class="label">Passwort</label>
              <input v-model="signupForm.passwordHash" class="input" type="password" placeholder="••••••••" />
            </div>
          </div>
          <div class="form-group">
            <label class="label">Profilbild URL <span class="optional">(optional)</span></label>
            <input v-model="signupForm.profilePicture" class="input" placeholder="https://…" />
          </div>
          <p v-if="error" class="error-msg">{{ error }}</p>
          <button type="submit" class="btn btn-primary full-btn" :disabled="loading || !signupForm.username.trim() || !signupForm.email.trim()">
            {{ loading ? 'Erstellen…' : 'Account erstellen →' }}
          </button>
        </form>
      </template>
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
.bg-blob { position: absolute; border-radius: 50%; filter: blur(80px); opacity: 0.15; }
.b1 { width: 400px; height: 400px; background: var(--accent); top: -100px; left: -100px; }
.b2 { width: 300px; height: 300px; background: #2563eb; bottom: -80px; right: -60px; }

.login-card {
  position: relative;
  background: #161616;
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 36px;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 24px 60px rgba(0,0,0,0.5);
}

.login-logo {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 24px;
}
.logo-icon { font-size: 24px; color: var(--accent); }
.logo-name  { font-size: 18px; font-weight: 700; }

.tabs {
  display: flex;
  background: rgba(255,255,255,0.04);
  border-radius: var(--radius-sm);
  padding: 3px;
  margin-bottom: 22px;
}
.tab {
  flex: 1;
  padding: 8px;
  border: none;
  border-radius: 6px;
  background: transparent;
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s;
}
.tab.active {
  background: var(--accent);
  color: #fff;
}

.form-sub {
  color: var(--text-muted);
  font-size: 13px;
  margin-bottom: 20px;
}

.auth-form { display: flex; flex-direction: column; }

.full-btn {
  width: 100%;
  justify-content: center;
  padding: 11px;
  font-size: 14px;
  margin-top: 4px;
}

.optional { color: var(--text-dim); font-size: 10px; font-weight: 400; text-transform: none; }
</style>
