<script setup>
import { useRouter, useRoute } from 'vue-router'
import { useAuth } from '../stores/auth.js'

const router = useRouter()
const route  = useRoute()
const { state, logout } = useAuth()

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
      <!-- Brand -->
      <div class="brand">
        <svg class="brand-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
          <path d="M9 18V5l12-2v13"/>
          <circle cx="6" cy="18" r="3"/>
          <circle cx="18" cy="16" r="3"/>
        </svg>
        <span class="brand-name">MusicDB</span>
      </div>

      <!-- Navigation -->
      <ul class="nav-list">
        <li>
          <RouterLink to="/app/home" class="nav-item" :class="{ active: isActive('/app/home') }">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="3" width="7" height="7" rx="1.5"/>
              <rect x="14" y="3" width="7" height="7" rx="1.5"/>
              <rect x="3" y="14" width="7" height="7" rx="1.5"/>
              <rect x="14" y="14" width="7" height="7" rx="1.5"/>
            </svg>
            <span>Übersicht</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/app/artists" class="nav-item" :class="{ active: isActive('/app/artists') }">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
              <rect x="9" y="2" width="6" height="11" rx="3"/>
              <path d="M5 10a7 7 0 0 0 14 0"/>
              <line x1="12" y1="19" x2="12" y2="22"/>
              <line x1="8" y1="22" x2="16" y2="22"/>
            </svg>
            <span>Artists</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/app/albums" class="nav-item" :class="{ active: isActive('/app/albums') }">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="10"/>
              <circle cx="12" cy="12" r="3"/>
              <line x1="12" y1="2" x2="12" y2="9"/>
            </svg>
            <span>Alben</span>
          </RouterLink>
        </li>
        <li>
          <RouterLink to="/app/playlists" class="nav-item" :class="{ active: isActive('/app/playlists') }">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
              <line x1="8" y1="6" x2="21" y2="6"/>
              <line x1="8" y1="12" x2="21" y2="12"/>
              <line x1="8" y1="18" x2="16" y2="18"/>
              <circle cx="4" cy="6"  r="1" fill="currentColor" stroke="none"/>
              <circle cx="4" cy="12" r="1" fill="currentColor" stroke="none"/>
              <circle cx="4" cy="18" r="1" fill="currentColor" stroke="none"/>
            </svg>
            <span>Playlists</span>
          </RouterLink>
        </li>

        <!-- Studio (nur für Artists) -->
        <li v-if="state.artist" class="studio-item">
          <RouterLink to="/app/studio" class="nav-item studio-link" :class="{ active: isActive('/app/studio') }">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
              <line x1="4"  y1="21" x2="4"  y2="14"/>
              <line x1="4"  y1="10" x2="4"  y2="3"/>
              <line x1="12" y1="21" x2="12" y2="12"/>
              <line x1="12" y1="8"  x2="12" y2="3"/>
              <line x1="20" y1="21" x2="20" y2="16"/>
              <line x1="20" y1="12" x2="20" y2="3"/>
              <line x1="1"  y1="14" x2="7"  y2="14"/>
              <line x1="9"  y1="8"  x2="15" y2="8"/>
              <line x1="17" y1="16" x2="23" y2="16"/>
            </svg>
            <span>Mein Studio</span>
          </RouterLink>
        </li>
      </ul>
    </div>

    <!-- Bottom: Settings + User -->
    <div class="sidebar-bottom">
      <RouterLink to="/app/settings" class="nav-item" :class="{ active: isActive('/app/settings') }">
        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
          <circle cx="12" cy="12" r="3"/>
          <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83-2.83l.06-.06A1.65 1.65 0 0 0 4.68 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 2.83-2.83l.06.06A1.65 1.65 0 0 0 9 4.68a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/>
        </svg>
        <span>Einstellungen</span>
      </RouterLink>

      <div class="user-chip" v-if="state.user">
        <img v-if="state.user.profilePicture" :src="state.user.profilePicture" :alt="state.user.username" class="avatar" />
        <div v-else class="avatar avatar-letter">{{ state.user.username?.[0]?.toUpperCase() }}</div>
        <div class="user-info">
          <span class="user-name">{{ state.user.username }}</span>
          <span class="user-role">{{ state.artist ? 'Artist' : 'Nutzer' }}</span>
        </div>
        <button class="logout-btn" title="Abmelden" @click="handleLogout">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
            <polyline points="16 17 21 12 16 7"/>
            <line x1="21" y1="12" x2="9" y2="12"/>
          </svg>
        </button>
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
  gap: 9px;
  padding: 8px 10px 20px;
}
.brand-icon {
  width: 22px; height: 22px;
  color: var(--accent);
  flex-shrink: 0;
}
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
  text-decoration: none;
}
.nav-item:hover { background: rgba(255,255,255,0.05); color: var(--text); }
.nav-item.active { background: var(--accent-dim); color: #a78bfa; }

.nav-icon {
  width: 17px; height: 17px;
  flex-shrink: 0;
}

.studio-item { margin-top: 8px; padding-top: 8px; border-top: 1px solid var(--border); }

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
.avatar-letter {
  background: var(--accent-dim);
  color: #a78bfa;
  font-size: 12px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
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
  padding: 2px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
}
.logout-btn svg { width: 15px; height: 15px; }
.logout-btn:hover { color: var(--text-muted); }
</style>
