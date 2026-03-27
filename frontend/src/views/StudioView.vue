<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../api.js'
import { useAuth } from '../stores/auth.js'
import CoverImage from '../components/CoverImage.vue'

const router = useRouter()
const { state, setArtist } = useAuth()

// Redirect if not artist
if (!state.artist) {
  router.push('/app/settings')
}

const allAlbums  = ref([])
const showModal  = ref(false)
const saving     = ref(false)
const saveMsg    = ref('')
const saveError  = ref('')
const editingId  = ref(null)

const myAlbums = computed(() =>
  allAlbums.value.filter(a => a.artistIds?.includes(state.artist?.id))
)

onMounted(async () => {
  allAlbums.value = await api.getAlbums()
})

// ── New album form ──────────────────────────────────────────
const emptyAlbum = () => ({
  title:         '',
  description:   '',
  releaseDate:   new Date().toISOString().slice(0, 10),
  coverImage:    '',
  totalDuration: 0,
  artistIds:     [state.artist?.id],
  songs:         [emptySong()],
})

function emptySong() {
  return {
    title:       '',
    duration:    '',
    genre:       '',
    releaseDate: new Date().toISOString().slice(0, 10),
    explicit:    false,
    streamCount: 0,
    lyrics:      '',
    language:    'English',
  }
}

const form = ref(emptyAlbum())

function addSong() {
  form.value.songs.push(emptySong())
}

function removeSong(i) {
  if (form.value.songs.length > 1) form.value.songs.splice(i, 1)
}

function parseDuration(val) {
  if (!val) return 0
  if (String(val).includes(':')) {
    const [m, s] = String(val).split(':').map(Number)
    return (m || 0) * 60 + (s || 0)
  }
  return parseInt(val) || 0
}

function secsToMmSs(secs) {
  if (!secs) return ''
  const m = Math.floor(secs / 60)
  const s = secs % 60
  return `${m}:${String(s).padStart(2, '0')}`
}

async function saveAlbum() {
  if (!form.value.title.trim()) { saveError.value = 'Titel ist Pflichtfeld.'; return }
  saveMsg.value   = ''
  saveError.value = ''
  saving.value    = true

  const payload = {
    ...form.value,
    totalDuration: form.value.songs.reduce((s, song) => s + parseDuration(song.duration), 0),
    songs: form.value.songs.map(s => ({ ...s, duration: parseDuration(s.duration) })),
  }

  try {
    if (editingId.value) {
      const updated = await api.updateAlbum(editingId.value, { ...payload, id: editingId.value })
      const idx = allAlbums.value.findIndex(a => a.id === editingId.value)
      allAlbums.value.splice(idx, 1, updated)
      saveMsg.value = 'Album gespeichert!'
    } else {
      const album = await api.createAlbum(payload)
      const updatedArtist = await api.updateArtist(state.artist.id, {
        ...state.artist,
        albumIds: [...(state.artist.albumIds || []), album.id],
      })
      setArtist(updatedArtist)
      allAlbums.value.push(album)
      saveMsg.value = 'Album veröffentlicht!'
    }
    setTimeout(() => {
      showModal.value = false
      form.value      = emptyAlbum()
      saveMsg.value   = ''
    }, 1500)
  } catch {
    saveError.value = 'Fehler beim Speichern des Albums.'
  } finally {
    saving.value = false
  }
}

async function deleteAlbum(album) {
  if (!confirm(`Album "${album.title}" wirklich löschen?`)) return
  await api.deleteAlbum(album.id)
  allAlbums.value = allAlbums.value.filter(a => a.id !== album.id)
  const updatedArtist = await api.updateArtist(state.artist.id, {
    ...state.artist, albumIds: (state.artist.albumIds || []).filter(id => id !== album.id),
  })
  setArtist(updatedArtist)
}

function openModal(album = null) {
  editingId.value = album?.id ?? null
  form.value = album
    ? {
        ...album,
        releaseDate: album.releaseDate?.slice(0, 10) ?? new Date().toISOString().slice(0, 10),
        songs: (album.songs || []).map(s => ({ ...s, duration: secsToMmSs(s.duration) })),
      }
    : emptyAlbum()
  saveMsg.value   = ''
  saveError.value = ''
  showModal.value = true
}

function formatDate(d) {
  return d ? new Date(d).getFullYear() : '—'
}
</script>

<template>
  <div>
    <div class="page-header">
      <h1>Mein Studio</h1>
      <p>Erstelle und verwalte deine eigene Musik als {{ state.artist?.name }}</p>
    </div>

    <div class="studio-toolbar">
      <button class="btn btn-primary" @click="openModal()">+ Neues Album</button>
    </div>

    <!-- My albums -->
    <div v-if="myAlbums.length === 0" class="empty">
      <div class="empty-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/>
        </svg>
      </div>
      <p>Noch keine Alben veröffentlicht. Starte mit deinem ersten Album!</p>
    </div>

    <div v-else class="grid">
      <div v-for="album in myAlbums" :key="album.id" class="my-album-card">
        <div class="my-cover">
          <CoverImage :src="album.coverImage" :title="album.title" />
          <button class="edit-btn" @click="openModal(album)">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
              <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
          </button>
          <button class="del-btn" @click="deleteAlbum(album)">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14H6L5 6"/><path d="M10 11v6"/><path d="M14 11v6"/><path d="M9 6V4h6v2"/>
            </svg>
          </button>
        </div>
        <div class="my-info">
          <div class="my-title">{{ album.title }}</div>
          <div class="my-meta">{{ formatDate(album.releaseDate) }} · {{ album.songs?.length || 0 }} Songs</div>
        </div>
      </div>
    </div>

    <!-- Create album modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <div class="modal-header">
          <h2>{{ editingId ? 'Album bearbeiten' : 'Neues Album erstellen' }}</h2>
          <button class="modal-close" @click="showModal = false">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
          </button>
        </div>

        <!-- Album info -->
        <h3 class="sub-heading">Album-Info</h3>
        <div class="form-group">
          <label class="label">Titel *</label>
          <input v-model="form.title" class="input" placeholder="Albumtitel" />
        </div>
        <div class="form-group">
          <label class="label">Beschreibung</label>
          <textarea v-model="form.description" class="input" placeholder="Beschreibung des Albums…" rows="2"></textarea>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label class="label">Release Datum</label>
            <input v-model="form.releaseDate" class="input" type="date" />
          </div>
          <div class="form-group">
            <label class="label">Cover Bild URL</label>
            <input v-model="form.coverImage" class="input" placeholder="https://…" />
          </div>
        </div>

        <div class="divider"></div>

        <!-- Songs -->
        <div class="songs-header">
          <h3 class="sub-heading" style="margin-bottom:0">Songs</h3>
          <button class="btn btn-ghost" @click="addSong">+ Song</button>
        </div>

        <div v-for="(song, i) in form.songs" :key="i" class="song-form">
          <div class="song-form-header">
            <span class="song-num-label"># {{ i + 1 }}</span>
            <button v-if="form.songs.length > 1" class="btn-remove" @click="removeSong(i)">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="label">Titel</label>
              <input v-model="song.title" class="input" placeholder="Song-Titel" />
            </div>
            <div class="form-group">
              <label class="label">Dauer (MM:SS)</label>
              <input v-model="song.duration" class="input" placeholder="3:30" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="label">Genre</label>
              <input v-model="song.genre" class="input" placeholder="z.B. Hip-Hop" />
            </div>
            <div class="form-group">
              <label class="label">Sprache</label>
              <select v-model="song.language" class="input">
                <option>English</option>
                <option>German</option>
                <option>French</option>
                <option>Spanish</option>
                <option>Other</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label class="label">Lyrics</label>
            <textarea v-model="song.lyrics" class="input" placeholder="Lyrics…" rows="2"></textarea>
          </div>
          <label class="explicit-toggle">
            <input type="checkbox" v-model="song.explicit" />
            <span>Explicit</span>
          </label>
        </div>

        <p v-if="saveMsg"   class="success-msg">{{ saveMsg }}</p>
        <p v-if="saveError" class="error-msg">{{ saveError }}</p>

        <div class="modal-actions">
          <button class="btn btn-ghost" @click="showModal = false">Abbrechen</button>
          <button class="btn btn-primary" @click="saveAlbum" :disabled="saving">
            {{ saving ? (editingId ? 'Speichern…' : 'Veröffentlichen…') : (editingId ? 'Speichern' : 'Album veröffentlichen') }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.studio-toolbar { margin-bottom: 24px; }

.my-album-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow: hidden;
  transition: transform 0.15s, border-color 0.15s;
}
.my-album-card:hover {
  transform: translateY(-2px);
  border-color: var(--accent);
}
.my-cover {
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
.my-album-card:hover .edit-btn { opacity: 1; }
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
.my-album-card:hover .del-btn { opacity: 1; }
.del-btn:hover { background: var(--danger); }
.my-info { padding: 12px; }
.my-title { font-size: 13px; font-weight: 600; }
.my-meta { font-size: 11px; color: var(--text-muted); margin-top: 2px; }

.sub-heading {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.06em;
  margin-bottom: 14px;
}

.songs-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

.song-form {
  background: rgba(255,255,255,0.03);
  border: 1px solid var(--border);
  border-radius: var(--radius-sm);
  padding: 14px;
  margin-bottom: 12px;
}
.song-form-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}
.song-num-label { font-size: 11px; font-weight: 600; color: var(--accent); text-transform: uppercase; letter-spacing: 0.06em; }

.btn-remove {
  background: none;
  border: none;
  color: var(--text-dim);
  cursor: pointer;
  padding: 2px 6px;
  border-radius: 4px;
  display: flex;
  align-items: center;
}
.btn-remove svg { width: 14px; height: 14px; }
.btn-remove:hover { background: rgba(239,68,68,0.1); color: var(--danger); }

.explicit-toggle {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: var(--text-muted);
  cursor: pointer;
  user-select: none;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>
