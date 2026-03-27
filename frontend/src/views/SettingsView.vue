<script setup>
import { ref, reactive, onMounted } from 'vue'
import { api } from '../api.js'
import { useAuth } from '../stores/auth.js'

const { state, setUser, setArtist } = useAuth()

// ── Profile edit ──────────────────────────────────────────────
const profile = reactive({
  username:       state.user?.username       || '',
  email:          state.user?.email          || '',
  country:        state.user?.country        || '',
  profilePicture: state.user?.profilePicture || '',
})
const profileMsg   = ref('')
const profileError = ref('')
const savingProfile = ref(false)

async function saveProfile() {
  savingProfile.value = true
  profileMsg.value    = ''
  profileError.value  = ''
  try {
    const updated = await api.updateUser(state.user.id, { ...state.user, ...profile })
    setUser(updated)
    profileMsg.value = 'Profil gespeichert.'
  } catch {
    profileError.value = 'Fehler beim Speichern.'
  } finally {
    savingProfile.value = false
  }
}

// ── Artist upgrade ────────────────────────────────────────────
const artistForm = reactive({
  name:      state.user?.username || '',
  label:     '',
  debutYear: new Date().getFullYear(),
  biography: '',
  country:   state.user?.country || '',
})
const upgradeMsg   = ref('')
const upgradeError = ref('')
const upgrading    = ref(false)

async function becomeArtist() {
  if (!artistForm.name.trim() || !artistForm.biography.trim()) {
    upgradeError.value = 'Name und Biographie sind pflichtfelder.'
    return
  }
  upgrading.value    = true
  upgradeMsg.value   = ''
  upgradeError.value = ''
  try {
    const artist  = await api.createArtist({ ...artistForm, albumIds: [], userId: state.user.id })
    const updated = await api.updateUser(state.user.id, { ...state.user, artistId: artist.id })
    setArtist(artist)
    setUser(updated)
    upgradeMsg.value = 'Dein Account wurde zum Artist-Account aufgewertet! 🎤'
  } catch {
    upgradeError.value = 'Fehler beim Aufwerten.'
  } finally {
    upgrading.value = false
  }
}
</script>

<template>
  <div>
    <div class="page-header">
      <h1>Einstellungen</h1>
      <p>Profil verwalten und Account anpassen</p>
    </div>

    <!-- Profile section -->
    <div class="settings-section card">
      <div class="section-head">
        <div class="section-icon">👤</div>
        <div>
          <h2>Mein Profil</h2>
          <p class="section-sub">Persönliche Daten bearbeiten</p>
        </div>
      </div>

      <div class="divider"></div>

      <div class="avatar-row">
        <img :src="profile.profilePicture || 'https://i.pravatar.cc/80'" class="big-avatar" alt="Avatar" />
        <div>
          <div class="avatar-name">{{ state.user?.username }}</div>
          <div class="avatar-role">
            <span v-if="state.artist" class="artist-badge">🎤 Artist</span>
            <span v-else class="user-badge">Nutzer</span>
          </div>
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label class="label">Benutzername</label>
          <input v-model="profile.username" class="input" />
        </div>
        <div class="form-group">
          <label class="label">E-Mail</label>
          <input v-model="profile.email" class="input" type="email" />
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label class="label">Land</label>
          <input v-model="profile.country" class="input" placeholder="z.B. Switzerland" />
        </div>
        <div class="form-group">
          <label class="label">Profilbild URL</label>
          <input v-model="profile.profilePicture" class="input" placeholder="https://…" />
        </div>
      </div>

      <p v-if="profileMsg"   class="success-msg">{{ profileMsg }}</p>
      <p v-if="profileError" class="error-msg">{{ profileError }}</p>

      <button class="btn btn-primary" @click="saveProfile" :disabled="savingProfile">
        {{ savingProfile ? 'Speichern…' : 'Profil speichern' }}
      </button>
    </div>

    <!-- Already an artist -->
    <div v-if="state.artist" class="settings-section card artist-info-card">
      <div class="section-head">
        <div class="section-icon">🎤</div>
        <div>
          <h2>Artist-Profil</h2>
          <p class="section-sub">Du bist bereits als Artist registriert</p>
        </div>
      </div>
      <div class="divider"></div>
      <div class="artist-overview">
        <div class="ao-row">
          <span class="ao-key">Name</span>
          <span class="ao-val">{{ state.artist.name }}</span>
        </div>
        <div class="ao-row">
          <span class="ao-key">Label</span>
          <span class="ao-val">{{ state.artist.label || '—' }}</span>
        </div>
        <div class="ao-row">
          <span class="ao-key">Debüt</span>
          <span class="ao-val">{{ state.artist.debutYear }}</span>
        </div>
        <div class="ao-row">
          <span class="ao-key">Alben</span>
          <span class="ao-val">{{ state.artist.albumIds?.length || 0 }}</span>
        </div>
      </div>
      <RouterLink to="/app/studio" class="btn btn-primary" style="margin-top: 16px; display: inline-flex">
        🎛️ Zum Studio →
      </RouterLink>
    </div>

    <!-- Upgrade to artist -->
    <div v-else class="settings-section card upgrade-card">
      <div class="section-head">
        <div class="section-icon">⭐</div>
        <div>
          <h2>Zum Artist werden</h2>
          <p class="section-sub">Erstelle ein Artist-Profil und veröffentliche eigene Musik</p>
        </div>
      </div>
      <div class="divider"></div>

      <div class="form-row">
        <div class="form-group">
          <label class="label">Künstlername *</label>
          <input v-model="artistForm.name" class="input" placeholder="Dein Künstlername" />
        </div>
        <div class="form-group">
          <label class="label">Land</label>
          <input v-model="artistForm.country" class="input" placeholder="z.B. Switzerland" />
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label class="label">Label</label>
          <input v-model="artistForm.label" class="input" placeholder="z.B. Indie / Universal" />
        </div>
        <div class="form-group">
          <label class="label">Debüt Jahr</label>
          <input v-model.number="artistForm.debutYear" class="input" type="number" min="1900" :max="new Date().getFullYear()" />
        </div>
      </div>
      <div class="form-group">
        <label class="label">Biographie *</label>
        <textarea v-model="artistForm.biography" class="input" placeholder="Erzähle etwas über dich als Artist…" rows="4"></textarea>
      </div>

      <p v-if="upgradeMsg"   class="success-msg">{{ upgradeMsg }}</p>
      <p v-if="upgradeError" class="error-msg">{{ upgradeError }}</p>

      <button class="btn btn-primary upgrade-btn" @click="becomeArtist" :disabled="upgrading">
        {{ upgrading ? 'Wird verarbeitet…' : '⭐ Jetzt Artist werden' }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.settings-section { margin-bottom: 20px; }

.section-head {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 0;
}
.section-icon { font-size: 22px; }
.section-head h2 { font-size: 16px; font-weight: 600; }
.section-sub { font-size: 12px; color: var(--text-muted); margin-top: 2px; }

.avatar-row {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}
.big-avatar {
  width: 60px; height: 60px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--border);
}
.avatar-name { font-size: 16px; font-weight: 600; }
.avatar-role { margin-top: 4px; }
.artist-badge {
  background: var(--accent-dim);
  color: #a78bfa;
  border-radius: 20px;
  padding: 2px 10px;
  font-size: 12px;
  font-weight: 600;
}
.user-badge { font-size: 12px; color: var(--text-muted); }

.artist-overview { display: flex; flex-direction: column; gap: 10px; }
.ao-row { display: flex; gap: 16px; align-items: center; }
.ao-key { width: 70px; font-size: 12px; color: var(--text-dim); text-transform: uppercase; letter-spacing: 0.05em; flex-shrink: 0; }
.ao-val { font-size: 14px; font-weight: 500; }

.upgrade-card { border-color: rgba(124,58,237,0.2); }
.upgrade-btn { margin-top: 4px; }
</style>
