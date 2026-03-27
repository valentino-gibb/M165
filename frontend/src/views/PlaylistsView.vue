<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { api } from '../api.js'
import { useAuth } from '../stores/auth.js'
import CoverImage from '../components/CoverImage.vue'

const { state, setUser } = useAuth()

const playlists = ref([])
const search    = ref('')
const loading   = ref(true)
const showModal = ref(false)
const saving    = ref(false)
const saveError = ref('')

onMounted(async () => {
  playlists.value = await api.getPlaylists()
  loading.value   = false
})

const filtered = computed(() =>
  playlists.value.filter(p =>
    p.name.toLowerCase().includes(search.value.toLowerCase()) ||
    p.tags?.some(t => t.toLowerCase().includes(search.value.toLowerCase()))
  )
)

function formatDate(d) {
  return d ? new Date(d).toLocaleDateString('de-CH', { year: 'numeric', month: 'short' }) : '—'
}

// ── Create playlist ────────────────────────────────────────────
const emptyForm = () => ({ name: '', description: '', tagsInput: '' })
const form = reactive(emptyForm())

function openModal() {
  Object.assign(form, emptyForm())
  saveError.value = ''
  showModal.value = true
}

async function savePlaylist() {
  if (!form.name.trim()) { saveError.value = 'Name ist ein Pflichtfeld.'; return }
  saving.value    = true
  saveError.value = ''
  try {
    const tags = form.tagsInput
      .split(',')
      .map(t => t.trim())
      .filter(Boolean)

    const playlist = await api.createPlaylist({
      name:        form.name.trim(),
      description: form.description.trim(),
      tags,
      coverImage:  '',
      userId:      state.user.id,
      albumIds:    [],
      songIds:     [],
      createdAt:   new Date().toISOString(),
    })

    // Link playlist to user
    const updatedUser = await api.updateUser(state.user.id, {
      ...state.user,
      playlistIds: [...(state.user.playlistIds || []), playlist.id],
    })
    setUser(updatedUser)

    playlists.value.unshift(playlist)
    showModal.value = false
  } catch {
    saveError.value = 'Fehler beim Erstellen der Playlist.'
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <div>
    <div class="page-header">
      <h1>Playlists</h1>
      <p>{{ playlists.length }} Playlists in der Datenbank</p>
    </div>

    <div class="toolbar">
      <input v-model="search" class="input search" placeholder="Playlist oder Tag suchen…" />
      <button class="btn btn-primary" @click="openModal">+ Neue Playlist</button>
    </div>

    <div v-if="loading" class="empty">
      <svg class="spinner" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round">
        <path d="M12 2a10 10 0 1 0 10 10" />
      </svg>
      <p>Laden…</p>
    </div>

    <div v-else-if="!filtered.length" class="empty">
      <div class="empty-icon">▤</div><p>Keine Playlists gefunden</p>
    </div>

    <div v-else class="grid">
      <div v-for="pl in filtered" :key="pl.id" class="pl-card">
        <div class="pl-cover">
          <CoverImage :src="pl.coverImage" :title="pl.name" />
        </div>
        <div class="pl-body">
          <div class="pl-name">{{ pl.name }}</div>
          <div class="pl-desc">{{ pl.description }}</div>
          <div class="pl-tags">
            <span v-for="tag in pl.tags?.slice(0, 3)" :key="tag" class="tag">{{ tag }}</span>
          </div>
          <div class="pl-date">{{ formatDate(pl.createdAt) }}</div>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <div class="modal-header">
          <h2>Neue Playlist erstellen</h2>
          <button class="modal-close" @click="showModal = false">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
          </button>
        </div>

        <div class="form-group">
          <label class="label">Name *</label>
          <input v-model="form.name" class="input" placeholder="Meine Playlist" autofocus />
        </div>
        <div class="form-group">
          <label class="label">Beschreibung</label>
          <textarea v-model="form.description" class="input" placeholder="Worum geht es in dieser Playlist?" rows="3"></textarea>
        </div>
        <div class="form-group">
          <label class="label">Tags <span class="hint">(kommagetrennt)</span></label>
          <input v-model="form.tagsInput" class="input" placeholder="z.B. rap, chill, workout" />
        </div>

        <p v-if="saveError" class="error-msg">{{ saveError }}</p>

        <div class="modal-actions">
          <button class="btn btn-ghost" @click="showModal = false">Abbrechen</button>
          <button class="btn btn-primary" @click="savePlaylist" :disabled="saving || !form.name.trim()">
            {{ saving ? 'Erstellen…' : '+ Playlist erstellen' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.toolbar {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 24px;
}
.search { flex: 1; max-width: 360px; }

.pl-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow: hidden;
  transition: transform 0.15s, border-color 0.15s;
}
.pl-card:hover {
  transform: translateY(-2px);
  border-color: rgba(255,255,255,0.12);
}

.pl-cover {
  width: 100%;
  aspect-ratio: 1;
  overflow: hidden;
}

.pl-body { padding: 12px; display: flex; flex-direction: column; gap: 6px; }
.pl-name { font-size: 13px; font-weight: 600; }
.pl-desc {
  font-size: 11px;
  color: var(--text-muted);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.pl-tags { display: flex; gap: 4px; flex-wrap: wrap; }
.pl-date { font-size: 10px; color: var(--text-dim); margin-top: 2px; }

.hint { color: var(--text-dim); font-size: 10px; font-weight: 400; text-transform: none; letter-spacing: 0; }

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>
