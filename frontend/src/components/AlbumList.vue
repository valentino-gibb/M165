<script setup>
import { ref } from 'vue'
import api from '../api.js'

defineProps({
  albums: Array,
  artists: Array
})

const emit = defineEmits(['refresh'])

const showForm = ref(false)
const editingId = ref(null)
const formData = ref({
  title: '',
  veröffentlichungsDatum: '',
  beschreibung: '',
  coverbild: '',
  artistId: ''
})

const openForm = (album = null) => {
  if (album) {
    editingId.value = album.id
    formData.value = { ...album }
  } else {
    editingId.value = null
    formData.value = {
      title: '',
      veröffentlichungsDatum: '',
      beschreibung: '',
      coverbild: '',
      artistId: ''
    }
  }
  showForm.value = true
}

const closeForm = () => {
  showForm.value = false
  editingId.value = null
}

const saveAlbum = async () => {
  try {
    if (editingId.value) {
      await api.albums.update(editingId.value, formData.value)
    } else {
      await api.albums.create(formData.value)
    }
    closeForm()
    emit('refresh')
  } catch (error) {
    console.error('Fehler beim Speichern:', error)
  }
}

const deleteAlbum = async (id) => {
  if (confirm('Sind Sie sicher, dass Sie dieses Album löschen möchten?')) {
    try {
      await api.albums.delete(id)
      emit('refresh')
    } catch (error) {
      console.error('Fehler beim Löschen:', error)
    }
  }
}

const getArtistName = (artistId) => {
  const artist = artists.find(a => a.id === artistId)
  return artist ? artist.name : 'Unbekannter Künstler'
}
</script>

<template>
  <div class="container">
    <div class="header-section">
      <h2>💿 Alben</h2>
      <button class="btn-primary" @click="openForm">+ Neues Album</button>
    </div>

    <!-- Formular -->
    <div v-if="showForm" class="modal-overlay" @click.self="closeForm">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ editingId ? 'Album bearbeiten' : 'Neues Album' }}</h3>
          <button class="btn-close" @click="closeForm">✕</button>
        </div>
        <form @submit.prevent="saveAlbum" class="form">
          <div class="form-group">
            <label>Titel:</label>
            <input v-model="formData.title" type="text" required>
          </div>
          <div class="form-group">
            <label>Künstler:</label>
            <select v-model="formData.artistId" required>
              <option value="">-- Künstler wählen --</option>
              <option v-for="artist in artists" :key="artist.id" :value="artist.id">
                {{ artist.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Veröffentlichungsdatum:</label>
            <input v-model="formData.veröffentlichungsDatum" type="date">
          </div>
          <div class="form-group">
            <label>Coverbild URL:</label>
            <input v-model="formData.coverbild" type="url">
          </div>
          <div class="form-group">
            <label>Beschreibung:</label>
            <textarea v-model="formData.beschreibung" rows="4"></textarea>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn-primary">Speichern</button>
            <button type="button" class="btn-secondary" @click="closeForm">Abbrechen</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Liste -->
    <div class="list-container">
      <div v-if="albums.length === 0" class="empty-state">
        Keine Alben vorhanden. Erstellen Sie ein neues!
      </div>
      <div v-else class="grid">
        <div v-for="album in albums" :key="album.id" class="card">
          <div v-if="album.coverbild" class="card-cover">
            <img :src="album.coverbild" :alt="album.title">
          </div>
          <div v-else class="card-cover-placeholder">🎵</div>
          <div class="card-content">
            <div class="card-header">
              <h3>{{ album.title }}</h3>
              <div class="card-actions">
                <button class="btn-icon" @click="openForm(album)" title="Bearbeiten">✏️</button>
                <button class="btn-icon" @click="deleteAlbum(album.id)" title="Löschen">🗑️</button>
              </div>
            </div>
            <p class="card-artist">{{ getArtistName(album.artistId) }}</p>
            <p v-if="album.beschreibung" class="card-description">{{ album.beschreibung.substring(0, 80) }}...</p>
            <div class="card-meta">
              <span class="meta">{{ album.songIds.length }} Songs</span>
              <span v-if="album.veröffentlichungsDatum" class="meta">{{ new Date(album.veröffentlichungsDatum).getFullYear() }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.header-section h2 {
  margin: 0;
  font-size: 1.8em;
  color: #333;
}

.btn-primary {
  padding: 10px 24px;
  background: #333;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background: #555;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.btn-secondary {
  padding: 10px 24px;
  background: white;
  color: #333;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  border-color: #333;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 12px;
  padding: 0;
  max-width: 500px;
  width: 90%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid #e0e0e0;
}

.modal-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.3em;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.5em;
  cursor: pointer;
  color: #999;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-close:hover {
  color: #333;
}

.form {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 600;
  font-size: 0.95em;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 0.95em;
  font-family: inherit;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #333;
  box-shadow: 0 0 0 3px rgba(0, 0, 0, 0.1);
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 24px;
}

.form-actions button {
  flex: 1;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
  font-size: 1.1em;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.card {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  overflow: hidden;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.card:hover {
  border-color: #ccc;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transform: translateY(-4px);
}

.card-cover {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #f0f0f0;
}

.card-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-cover-placeholder {
  width: 100%;
  height: 200px;
  background: linear-gradient(135deg, #f0f0f0 0%, #e0e0e0 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 4em;
}

.card-content {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.card-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.1em;
  flex: 1;
}

.card-actions {
  display: flex;
  gap: 8px;
}

.btn-icon {
  background: none;
  border: none;
  font-size: 1.2em;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.btn-icon:hover {
  background: #f0f0f0;
}

.card-artist {
  margin: 0 0 8px 0;
  color: #999;
  font-size: 0.9em;
  font-weight: 500;
}

.card-description {
  color: #666;
  font-size: 0.85em;
  margin: 0 0 12px 0;
  flex: 1;
}

.card-meta {
  display: flex;
  gap: 12px;
  border-top: 1px solid #f0f0f0;
  padding-top: 12px;
}

.meta {
  color: #999;
  font-size: 0.85em;
}
</style>
