<script setup>
import { ref, onMounted } from 'vue'
import { api } from '../api.js'
import { useAuth } from '../stores/auth.js'
import CoverImage from '../components/CoverImage.vue'

const { state } = useAuth()
const artists   = ref([])
const albums    = ref([])
const users     = ref([])
const playlists = ref([])
const loading   = ref(true)

onMounted(async () => {
  const [a, al, u, p] = await Promise.all([
    api.getArtists(), api.getAlbums(), api.getUsers(), api.getPlaylists()
  ])
  artists.value   = a
  albums.value    = al
  users.value     = u
  playlists.value = p
  loading.value   = false
})

function totalSongs() {
  return albums.value.reduce((sum, a) => sum + (a.songs?.length || 0), 0)
}

function recentAlbums() {
  return [...albums.value]
    .sort((a, b) => new Date(b.releaseDate) - new Date(a.releaseDate))
    .slice(0, 8)
}

function year(dateStr) {
  return dateStr ? new Date(dateStr).getFullYear() : '—'
}
</script>

<template>
  <div>
    <div class="page-header">
      <h1>Hallo, {{ state.user?.username }} JAMAN!</h1>
    </div>

    <div class="stats-row" v-if="!loading">
      <div class="stat-card">
        <div class="stat-value">{{ artists.length }}</div>
        <div class="stat-label">Artists</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ albums.length }}</div>
        <div class="stat-label">Alben</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ totalSongs() }}</div>
        <div class="stat-label">Songs</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ playlists.length }}</div>
        <div class="stat-label">Playlists</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ users.length }}</div>
        <div class="stat-label">Nutzer</div>
      </div>
    </div>

    <section class="section">
      <h2 class="section-title">Neueste Alben</h2>
      <div v-if="loading" class="loading">Laden…</div>
      <div v-else class="grid">
        <div v-for="album in recentAlbums()" :key="album.id" class="album-card">
          <CoverImage :src="album.coverImage" :title="album.title" />
          <div class="album-info">
            <div class="album-title">{{ album.title }}</div>
            <div class="album-meta">{{ year(album.releaseDate) }} · {{ album.songs?.length || 0 }} Songs</div>
          </div>
        </div>
      </div>
    </section>

    <section class="section">
      <h2 class="section-title">Artists</h2>
      <div v-if="!loading" class="artists-row">
        <div v-for="artist in artists.slice(0, 6)" :key="artist.id" class="artist-chip">
          <div class="artist-initial">{{ artist.name[0] }}</div>
          <div class="artist-name">{{ artist.name }}</div>
          <div class="artist-country">{{ artist.country }}</div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.stats-row {
  display: flex;
  gap: 12px;
  margin-bottom: 36px;
  flex-wrap: wrap;
}
.stat-card {
  flex: 1;
  min-width: 100px;
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 20px;
  text-align: center;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--accent);
  line-height: 1;
}
.stat-label {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 4px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.section { margin-bottom: 40px; }
.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.06em;
  font-size: 11px;
}

.album-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  overflow: hidden;
  transition: transform 0.15s, border-color 0.15s;
  cursor: default;
}
.album-card:hover {
  transform: translateY(-2px);
  border-color: rgba(255,255,255,0.12);
}
.album-cover {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  display: block;
}
.album-info { padding: 12px; }
.album-title {
  font-size: 13px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.album-meta { font-size: 11px; color: var(--text-muted); margin-top: 2px; }

.artists-row {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
.artist-chip {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 14px 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  min-width: 110px;
}
.artist-initial {
  width: 42px; height: 42px;
  border-radius: 50%;
  background: var(--accent-dim);
  color: #a78bfa;
  font-size: 18px;
  font-weight: 700;
  display: flex; align-items: center; justify-content: center;
}
.artist-name { font-size: 12px; font-weight: 600; text-align: center; }
.artist-country { font-size: 11px; color: var(--text-muted); }

.loading { color: var(--text-muted); padding: 20px 0; }
</style>
