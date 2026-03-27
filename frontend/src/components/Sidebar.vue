<script setup>
import { useRouter, useRoute } from 'vue-router'
import { useAuth } from '../stores/auth.js'

const router = useRouter()
const route  = useRoute()
const { state, logout } = useAuth()

const navItems = [
  { to: '/app/home',      icon: '⊞', label: 'Übersicht' },
  { to: '/app/artists',   icon: '🎤', label: 'Artists' },
  { to: '/app/albums',    icon: '💿', label: 'Alben' },
  { to: '/app/playlists', icon: '▤',  label: 'Playlists' },
]

function isActive(path) {
  return route.path === path
}

function handleLogout() {
  logout()
  router.push('/login')
}
</script>

<template>
  <nav class="sidebar">
    <div class="sidebar-top">
      <div class="brand">
        <span class="brand-icon">♪</span>
        <span class="brand-name">MusicDB</span>
      </div>

      <ul class="nav-list">
        <li v-for="item in navItems" :key="item.to">
          <RouterLink :to="item.to" class="nav-item" :class="{ active: isActive(item.to) }">
            <span class="nav-icon">{{ item.icon }}</span>
            <span class="nav-label">{{ item.label }}</span>
          </RouterLink>
        </li>

        <li v-if="state.artist">
          <RouterLink to="/app/studio" class="nav-item studio-link" :class="{ active: isActive('/app/studio') }">
            <span class="nav-icon">🎛️</span>
            <span class="nav-label">Mein Studio</span>
          </RouterLink>
        </li>
      </ul>
    </div>

    <div class="sidebar-bottom">
      <RouterLink to="/app/settings" class="nav-item" :class="{ active: isActive('/app/settings') }">
        <span class="nav-icon">⚙</span>
        <span class="nav-label">Einstellungen</span>
      </RouterLink>

      <div class="user-chip" v-if="state.user">
        <img :src="state.user.profilePicture" :alt="state.user.username" class="avatar" />
        <div class="user-info">
          <span class="user-name">{{ state.user.username }}</span>
          <span class="user-role">{{ state.artist ? 'Artist' : 'Nutzer' }}</span>
        </div>
        <button class="logout-btn" title="Abmelden" @click="handleLogout">↪</button>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.sidebar {
  width: 220px;
  min-width: 220px;
  height: 100vh;
  background: var(--sidebar);
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  padding: 16px 12px;
  position: sticky;
  top: 0;
}

.sidebar-top { flex: 1; }
.sidebar-bottom { display: flex; flex-direction: column; gap: 6px; }

.brand {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 10px 20px;
}
.brand-icon { font-size: 22px; color: var(--accent); }
.brand-name { font-size: 16px; font-weight: 700; color: var(--text); letter-spacing: -0.3px; }

.nav-list {
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 10px;
  border-radius: var(--radius-sm);
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 500;
  transition: all 0.15s;
  cursor: pointer;
}
.nav-item:hover { background: rgba(255,255,255,0.05); color: var(--text); }
.nav-item.active { background: var(--accent-dim); color: #a78bfa; }

.nav-icon { font-size: 16px; width: 20px; text-align: center; }

.studio-link { margin-top: 8px; }

.user-chip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 10px;
  border-radius: var(--radius-sm);
  background: rgba(255,255,255,0.04);
  border: 1px solid var(--border);
  margin-top: 4px;
}

.avatar {
  width: 28px; height: 28px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.user-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}
.user-name {
  font-size: 12px;
  font-weight: 600;
  color: var(--text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.user-role { font-size: 10px; color: var(--text-dim); }

.logout-btn {
  background: none;
  border: none;
  color: var(--text-dim);
  cursor: pointer;
  font-size: 15px;
  padding: 2px;
  flex-shrink: 0;
}
.logout-btn:hover { color: var(--text-muted); }
</style>
