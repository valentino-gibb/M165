<script setup>
import { ref, onMounted } from 'vue'
import api from './api.js'
import ArtistList from './components/ArtistList.vue'
import AlbumList from './components/AlbumList.vue'
import SongList from './components/SongList.vue'
import UserList from './components/UserList.vue'
import PlaylistList from './components/PlaylistList.vue'

const currentView = ref('artists')
const artists = ref([])
const albums = ref([])
const songs = ref([])
const users = ref([])
const playlists = ref([])
const loading = ref(false)

const loadArtists = async () => {
  loading.value = true
  try {
    artists.value = await api.artists.getAll()
  } catch (error) {
    console.error('Fehler beim Laden der Künstler:', error)
    artists.value = []
  }
  loading.value = false
}

const loadAlbums = async () => {
  loading.value = true
  try {
    albums.value = await api.albums.getAll()
  } catch (error) {
    console.error('Fehler beim Laden der Alben:', error)
    albums.value = []
  }
  loading.value = false
}

const loadSongs = async () => {
  loading.value = true
  try {
    songs.value = await api.songs.getAll()
  } catch (error) {
    console.error('Fehler beim Laden der Songs:', error)
    songs.value = []
  }
  loading.value = false
}

const loadUsers = async () => {
  loading.value = true
  try {
    users.value = await api.users.getAll()
  } catch (error) {
    console.error('Fehler beim Laden der Benutzer:', error)
    users.value = []
  }
  loading.value = false
}

const loadPlaylists = async () => {
  loading.value = true
  try {
    playlists.value = await api.playlists.getAll()
  } catch (error) {
    console.error('Fehler beim Laden der Playlists:', error)
    playlists.value = []
  }
  loading.value = false
}

const switchView = (view) => {
  currentView.value = view
  if (view === 'artists') loadArtists()
  else if (view === 'albums') loadAlbums()
  else if (view === 'songs') loadSongs()
  else if (view === 'users') loadUsers()
  else if (view === 'playlists') loadPlaylists()
}

onMounted(() => {
  loadArtists()
})
</script>

<template>
  <div class="app">
    <!-- Header -->
    <header class="header">
      <div class="header-content">
        <h1 class="logo">🎵 Music Platform</h1>
        <p class="tagline">Deine Musikbibliothek</p>
      </div>
    </header>

    <!-- Navigation -->
    <nav class="navigation">
      <button 
        class="nav-btn" 
        :class="{ active: currentView === 'artists' }"
        @click="switchView('artists')">
        🎤 Künstler
      </button>
      <button 
        class="nav-btn" 
        :class="{ active: currentView === 'albums' }"
        @click="switchView('albums')">
        💿 Alben
      </button>
      <button 
        class="nav-btn" 
        :class="{ active: currentView === 'songs' }"
        @click="switchView('songs')">
        🎶 Songs
      </button>
      <button 
        class="nav-btn" 
        :class="{ active: currentView === 'users' }"
        @click="switchView('users')">
        👤 Benutzer
      </button>
      <button 
        class="nav-btn" 
        :class="{ active: currentView === 'playlists' }"
        @click="switchView('playlists')">
        📋 Playlists
      </button>
    </nav>

    <!-- Main Content -->
    <main class="main-content">
      <div v-if="loading" class="loading">Lädt...</div>
      <ArtistList v-else-if="currentView === 'artists'" :artists="artists" @refresh="loadArtists" />
      <AlbumList v-else-if="currentView === 'albums'" :albums="albums" :artists="artists" @refresh="loadAlbums" />
      <SongList v-else-if="currentView === 'songs'" :songs="songs" :albums="albums" @refresh="loadSongs" />
      <UserList v-else-if="currentView === 'users'" :users="users" @refresh="loadUsers" />
      <PlaylistList v-else-if="currentView === 'playlists'" :playlists="playlists" :users="users" @refresh="loadPlaylists" />
    </main>
  </div>
</template>

<style scoped>
.app {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #ffffff 100%);
}

.header {
  background: white;
  border-bottom: 1px solid #e0e0e0;
  padding: 40px 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
}

.logo {
  margin: 0;
  font-size: 2.5em;
  color: #333;
  font-weight: 700;
  letter-spacing: -1px;
}

.tagline {
  margin: 8px 0 0 0;
  font-size: 0.95em;
  color: #888;
  font-weight: 400;
}

.navigation {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: center;
}

.nav-btn {
  padding: 10px 20px;
  border: 2px solid #e0e0e0;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.95em;
  font-weight: 500;
  color: #666;
  transition: all 0.3s ease;
}

.nav-btn:hover {
  border-color: #333;
  color: #333;
}

.nav-btn.active {
  border-color: #333;
  background: #333;
  color: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.loading {
  text-align: center;
  font-size: 1.1em;
  color: #999;
  padding: 60px 20px;
}
</style>
