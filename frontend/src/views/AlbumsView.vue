<script setup>
import { ref, computed, onMounted } from 'vue'
import { api } from '../api.js'

const albums   = ref([])
const search   = ref('')
const expanded = ref(null)
const loading  = ref(true)

onMounted(async () => {
  albums.value  = await api.getAlbums()
  loading.value = false
})

const filtered = computed(() =>
  albums.value.filter(a =>
    a.title.toLowerCase().includes(search.value.toLowerCase())
  )
)

function toggleExpand(id) {
  expanded.value = expanded.value === id ? null : id
}

function formatDate(d) {
  return d ? new Date(d).toLocaleDateString('de-CH', { year: 'numeric', month: 'short', day: 'numeric' }) : '—'
}

function formatDuration(secs) {
  if (!secs) return '—'
  const m = Math.floor(secs / 60)
  const s = secs % 60
  return `${m}:${String(s).padStart(2, '0')}`
}
</script>

<template>
  <div>
    <div class="page-header">
      <h1>Alben</h1>
      <p>{{ albums.length }} Alben in der Datenbank</p>
    </div>

    <div class="toolbar">
      <input v-model="search" class="input search" placeholder="🔍  Album suchen…" />
    </div>

    <div v-if="loading" class="empty"><div class="empty-icon">⏳</div><p>Laden…</p></div>

    <div v-else-if="!filtered.length" class="empty">
      <div class="empty-icon">💿</div><p>Keine Alben gefunden</p>
    </div>

    <div v-else class="album-list">
      <div v-for="album in filtered" :key="album.id" class="album-row">
        <div class="album-main" @click="toggleExpand(album.id)">
          <img :src="album.coverImage" :alt="album.title" class="cover" />
          <div class="album-info">
            <div class="album-title">{{ album.title }}</div>
            <div class="album-desc">{{ album.description }}</div>
            <div class="album-tags">
              <span class="tag">{{ formatDate(album.releaseDate) }}</span>
              <span class="tag">{{ album.songs?.length || 0 }} Songs</span>
              <span class="tag">{{ formatDuration(album.totalDuration) }} min</span>
            </div>
          </div>
          <span class="expand-icon">{{ expanded === album.id ? '▲' : '▼' }}</span>
        </div>

        <div v-if="expanded === album.id" class="songs-list">
          <div v-if="!album.songs?.length" class="empty-songs">Keine Songs</div>
          <div v-for="(song, i) in album.songs" :key="i" class="song-row">
            <span class="song-num">{{ i + 1 }}</span>
            <div class="song-info">
              <span class="song-title">{{ song.title }}</span>
              <span class="song-meta">{{ song.genre }} · {{ song.language }}</span>
            </div>
            <div class="song-right">
              <span v-if="song.explicit" class="explicit-badge">E</span>
              <span class="song-streams">{{ (song.streamCount / 1e6).toFixed(0) }}M</span>
              <span class="song-dur">{{ formatDuration(song.duration) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.toolbar { margin-bottom: 24px; }
.search { max-width: 360px; }

.album-list { display: flex; flex-direction: column; gap: 8px; }

.album-row {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow: hidden;
}

.album-main {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 14px 16px;
  cursor: pointer;
  transition: background 0.15s;
}
.album-main:hover { background: var(--card-hover); }

.cover {
  width: 56px; height: 56px;
  border-radius: 8px;
  object-fit: cover;
  flex-shrink: 0;
}

.album-info { flex: 1; min-width: 0; }
.album-title { font-size: 15px; font-weight: 600; }
.album-desc {
  font-size: 12px;
  color: var(--text-muted);
  margin: 2px 0 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.album-tags { display: flex; gap: 6px; flex-wrap: wrap; }

.expand-icon { color: var(--text-dim); font-size: 12px; flex-shrink: 0; }

.songs-list {
  border-top: 1px solid var(--border);
  padding: 4px 0;
}

.song-row {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 9px 16px;
  transition: background 0.12s;
}
.song-row:hover { background: rgba(255,255,255,0.03); }

.song-num { width: 20px; text-align: right; font-size: 12px; color: var(--text-dim); flex-shrink: 0; }
.song-info { flex: 1; min-width: 0; }
.song-title { font-size: 13px; font-weight: 500; display: block; }
.song-meta { font-size: 11px; color: var(--text-muted); }
.song-right { display: flex; align-items: center; gap: 10px; flex-shrink: 0; }
.song-streams { font-size: 11px; color: var(--text-dim); }
.song-dur { font-size: 12px; color: var(--text-muted); min-width: 36px; text-align: right; }

.explicit-badge {
  background: rgba(239,68,68,0.15);
  color: var(--danger);
  border-radius: 3px;
  font-size: 10px;
  font-weight: 700;
  padding: 1px 4px;
}

.empty-songs { padding: 12px 16px; font-size: 12px; color: var(--text-dim); }
</style>
