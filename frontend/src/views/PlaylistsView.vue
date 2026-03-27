<script setup>
import { ref, computed, onMounted } from 'vue'
import { api } from '../api.js'

const playlists = ref([])
const search    = ref('')
const loading   = ref(true)

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
</script>

<template>
  <div>
    <div class="page-header">
      <h1>Playlists</h1>
      <p>{{ playlists.length }} Playlists in der Datenbank</p>
    </div>

    <div class="toolbar">
      <input v-model="search" class="input search" placeholder="🔍  Playlist oder Tag suchen…" />
    </div>

    <div v-if="loading" class="empty"><div class="empty-icon">⏳</div><p>Laden…</p></div>

    <div v-else-if="!filtered.length" class="empty">
      <div class="empty-icon">▤</div><p>Keine Playlists gefunden</p>
    </div>

    <div v-else class="grid">
      <div v-for="pl in filtered" :key="pl.id" class="pl-card">
        <img :src="pl.coverImage" :alt="pl.name" class="pl-cover" />
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
  </div>
</template>

<style scoped>
.toolbar { margin-bottom: 24px; }
.search { max-width: 360px; }

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
  object-fit: cover;
  display: block;
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
</style>
