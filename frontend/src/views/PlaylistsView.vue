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
const editingId = ref(null)

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

// ── Create / Edit playlist ─────────────────────────────────────
const emptyForm = () => ({ name: '', description: '', tagsInput: '', coverImage: '' })
const form = reactive(emptyForm())

function openModal(playlist = null) {
  editingId.value = playlist?.id ?? null
  Object.assign(form, playlist
    ? { name: playlist.name, description: playlist.description || '', tagsInput: (playlist.tags || []).join(', '), coverImage: playlist.coverImage || '' }
    : emptyForm()
  )
  saveError.value = ''
  showModal.value = true
}

async function deletePlaylist(pl) {
  if (!confirm(`Playlist "${pl.name}" wirklich löschen?`)) return
  await api.deletePlaylist(pl.id)
  playlists.value = playlists.value.filter(p => p.id !== pl.id)
  const updatedUser = await api.updateUser(state.user.id, {
    ...state.user, playlistIds: (state.user.playlistIds || []).filter(id => id !== pl.id),
  })
  setUser(updatedUser)
}

async function savePlaylist() {
  if (!form.name.trim()) { saveError.value = 'Name ist ein Pflichtfeld.'; return }
  saving.value    = true
  saveError.value = ''
  try {
    const tags = form.tagsInput.split(',').map(t => t.trim()).filter(Boolean)

    if (editingId.value) {
      const existing = playlists.value.find(p => p.id === editingId.value)
      const updated  = await api.updatePlaylist(editingId.value, {
        ...existing, name: form.name.trim(), description: form.description.trim(), tags, coverImage: form.coverImage.trim(),
      })
      const idx = playlists.value.findIndex(p => p.id === editingId.value)
      playlists.value.splice(idx, 1, updated)
    } else {
      const playlist = await api.createPlaylist({
        name: form.name.trim(), description: form.description.trim(), tags,
        coverImage: form.coverImage.trim(), userId: state.user.id, albumIds: [], songIds: [],
        createdAt: new Date().toISOString(),
      })
      const updatedUser = await api.updateUser(state.user.id, {
        ...state.user, playlistIds: [...(state.user.playlistIds || []), playlist.id],
      })
      setUser(updatedUser)
      playlists.value.unshift(playlist)
    }
    showModal.value = false
  } catch {
    saveError.value = editingId.value ? 'Fehler beim Speichern.' : 'Fehler beim Erstellen der Playlist.'
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <div>
    <div class="page-header">
      <h1>Playlists</h1>
      <p>{{ playlists.length }} erstellte Playlists</p>
    </div>

    <div class="toolbar">
      <input v-model="search" class="input search" placeholder="Playlist oder Tag suchen…" />
      <button class="btn btn-primary" @click="openModal()">+ Neue Playlist</button>
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
          <template v-if="pl.userId === state.user?.id">
            <button class="edit-btn" @click.stop="openModal(pl)">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
              </svg>
            </button>
            <button class="del-btn" @click.stop="deletePlaylist(pl)">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14H6L5 6"/><path d="M10 11v6"/><path d="M14 11v6"/><path d="M9 6V4h6v2"/>
              </svg>
            </button>
          </template>
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
          <h2>{{ editingId ? 'Playlist bearbeiten' : 'Neue Playlist erstellen' }}</h2>
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
        <div class="form-group">
          <label class="label">Cover Bild URL <span class="hint">(optional)</span></label>
          <input v-model="form.coverImage" class="input" placeholder="https://…" />
        </div>

        <p v-if="saveError" class="error-msg">{{ saveError }}</p>

        <div class="modal-actions">
          <button class="btn btn-ghost" @click="showModal = false">Abbrechen</button>
          <button class="btn btn-primary" @click="savePlaylist" :disabled="saving || !form.name.trim()">
            {{ saving ? (editingId ? 'Speichern…' : 'Erstellen…') : (editingId ? 'Speichern' : '+ Playlist erstellen') }}
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
  position: relative;
}
.edit-btn {
  position: absolute;
  top: 8px; right: 8px;
  width: 30px; height: 30px;
  border-radius: 6px;
  border: none;
  background: rgba(0,0,0,0.6);
  color: #fff;
  cursor: pointer;
  display: flex; align-items: center; justify-content: center;
  opacity: 0;
  transition: opacity 0.15s;
}
.edit-btn svg { width: 14px; height: 14px; }
.pl-card:hover .edit-btn { opacity: 1; }
.edit-btn:hover { background: var(--accent); }
.del-btn {
  position: absolute;
  top: 8px; right: 44px;
  width: 30px; height: 30px;
  border-radius: 6px;
  border: none;
  background: rgba(0,0,0,0.6);
  color: #fff;
  cursor: pointer;
  display: flex; align-items: center; justify-content: center;
  opacity: 0;
  transition: opacity 0.15s;
}
.del-btn svg { width: 14px; height: 14px; }
.pl-card:hover .del-btn { opacity: 1; }
.del-btn:hover { background: var(--danger); }

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
