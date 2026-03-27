<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { api } from '../api.js'
import { useAuth } from '../stores/auth.js'

const { state, setUser, setArtist } = useAuth()

const allAlbums = ref([])
onMounted(async () => {
  allAlbums.value = await api.getAlbums()
})
const artistAlbumCount = computed(() =>
  state.artist
    ? allAlbums.value.filter(a => a.artistIds?.includes(state.artist.id)).length
    : 0
)

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
    upgradeMsg.value = 'Dein Account wurde zum Artist-Account aufgewertet!'
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
        <div class="section-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/>
          </svg>
        </div>
        <div>
          <h2>Mein Profil</h2>
          <p class="section-sub">Persönliche Daten bearbeiten</p>
        </div>
      </div>

      <div class="divider"></div>

      <div class="avatar-row">
        <img v-if="profile.profilePicture" :src="profile.profilePicture" class="big-avatar" alt="Avatar" />
        <div v-else class="big-avatar avatar-letter">{{ state.user?.username?.[0]?.toUpperCase() }}</div>
        <div>
          <div class="avatar-name">{{ state.user?.username }}</div>
          <div class="avatar-role">
            <span v-if="state.artist" class="artist-badge">Artist</span>
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
        <div class="section-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
            <rect x="9" y="2" width="6" height="12" rx="3"/><path d="M5 10a7 7 0 0 0 14 0"/><line x1="12" y1="17" x2="12" y2="21"/><line x1="8" y1="21" x2="16" y2="21"/>
          </svg>
        </div>
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
          <span class="ao-val">{{ artistAlbumCount }}</span>
        </div>
      </div>
      <RouterLink to="/app/studio" class="btn btn-primary" style="margin-top: 16px; display: inline-flex">
        Zum Studio →
      </RouterLink>
    </div>

    <!-- Upgrade to artist -->
    <div v-else class="settings-section card upgrade-card">
      <div class="section-head">
        <div class="section-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
          </svg>
        </div>
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
        {{ upgrading ? 'Wird verarbeitet…' : 'Jetzt Artist werden' }}
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
.section-icon { width: 22px; height: 22px; color: var(--accent); flex-shrink: 0; }
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
  flex-shrink: 0;
}
.avatar-letter {
  background: var(--accent-dim);
  color: #a78bfa;
  font-size: 22px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
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
