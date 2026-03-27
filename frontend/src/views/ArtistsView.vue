<script setup>
import { ref, computed, onMounted } from 'vue'
import { api } from '../api.js'

const artists = ref([])
const search  = ref('')
const loading = ref(true)

onMounted(async () => {
  artists.value = await api.getArtists()
  loading.value = false
})

const filtered = computed(() =>
  artists.value.filter(a =>
    a.name.toLowerCase().includes(search.value.toLowerCase()) ||
    a.country?.toLowerCase().includes(search.value.toLowerCase()) ||
    a.label?.toLowerCase().includes(search.value.toLowerCase())
  )
)
</script>

<template>
  <div>
    <div class="page-header">
      <h1>Artists</h1>
      <p>{{ artists.length }} Artists in der Datenbank</p>
    </div>

    <div class="toolbar">
      <input v-model="search" class="input search" placeholder="🔍  Artist, Label, Land suchen…" />
    </div>

    <div v-if="loading" class="empty"><div class="empty-icon">⏳</div><p>Laden…</p></div>

    <div v-else-if="!filtered.length" class="empty">
      <div class="empty-icon">🔍</div>
      <p>Keine Artists gefunden</p>
    </div>

    <div v-else class="grid-wide">
      <div v-for="artist in filtered" :key="artist.id" class="artist-card">
        <div class="artist-header">
          <div class="initial-avatar">{{ artist.name[0] }}</div>
          <div>
            <div class="artist-name">{{ artist.name }}</div>
            <div class="artist-meta">{{ artist.country }} · seit {{ artist.debutYear }}</div>
          </div>
        </div>
        <div class="artist-label">{{ artist.label }}</div>
        <p class="artist-bio">{{ artist.biography }}</p>
        <div class="artist-footer">
          <span class="tag">{{ artist.albumIds?.length || 0 }} Alben</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.toolbar { margin-bottom: 24px; }
.search { max-width: 360px; }

.artist-card {
  background: var(--card);
  border: 1px solid var(--border);
  border-radius: var(--radius);
  padding: 18px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  transition: border-color 0.15s, transform 0.15s;
}
.artist-card:hover {
  border-color: rgba(255,255,255,0.12);
  transform: translateY(-2px);
}

.artist-header { display: flex; align-items: center; gap: 12px; }
.initial-avatar {
  width: 46px; height: 46px;
  border-radius: 50%;
  background: var(--accent-dim);
  color: #a78bfa;
  font-size: 20px;
  font-weight: 700;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}
.artist-name { font-size: 15px; font-weight: 600; }
.artist-meta { font-size: 12px; color: var(--text-muted); margin-top: 1px; }
.artist-label { font-size: 12px; color: var(--text-dim); }
.artist-bio {
  font-size: 12px;
  color: var(--text-muted);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex: 1;
}
.artist-footer { margin-top: auto; }
</style>
