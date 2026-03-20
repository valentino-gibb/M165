<script setup>
import { ref } from 'vue'
import api from '../api.js'

defineProps({
  artists: Array,
  playlists: Array,
  songs: Array,
  users: Array,
  albums: Array
})

const emit = defineEmits(['refresh'])

const showForm = ref(false)
const editingId = ref(null)
const formData = ref({
  name: '',
  beschreibung: '',
  userId: ''
})

const openForm = (playlist = null) => {
  if (playlist) {
    editingId.value = playlist.id
    formData.value = { ...playlist }
  } else {
    editingId.value = null
    formData.value = { name: '', beschreibung: '', userId: '' }
  }
  showForm.value = true
}

const closeForm = () => {
  showForm.value = false
  editingId.value = null
}

const savePlaylist = async () => {
  try {
    if (editingId.value) {
      await api.playlists.update(editingId.value, formData.value)
    } else {
      await api.playlists.create(formData.value)
    }
    closeForm()
    emit('refresh')
  } catch (error) {
    console.error('Fehler beim Speichern:', error)
  }
}

const deletePlaylist = async (id) => {
  if (confirm('Sind Sie sicher, dass Sie diese Playlist löschen möchten?')) {
    try {
      await api.playlists.delete(id)
      emit('refresh')
    } catch (error) {
      console.error('Fehler beim Löschen:', error)
    }
  }
}
</script>

<template>
  <div class="container">
    <div class="header-section">
      <h2>📋 Playlists</h2>
      <button class="btn-primary" @click="openForm">+ Neue Playlist</button>
    </div>

    <!-- Formular -->
    <div v-if="showForm" class="modal-overlay" @click.self="closeForm">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ editingId ? 'Playlist bearbeiten' : 'Neue Playlist' }}</h3>
          <button class="btn-close" @click="closeForm">✕</button>
        </div>
        <form @submit.prevent="savePlaylist" class="form">
          <div class="form-group">
            <label>Name:</label>
            <input v-model="formData.name" type="text" required>
          </div>
          <div class="form-group">
            <label>Beschreibung:</label>
            <textarea v-model="formData.beschreibung" rows="3"></textarea>
          </div>
          <div class="form-group">
            <label>Benutzer ID:</label>
            <select v-model="formData.userId">
              <option value="">-- Benutzer wählen --</option>
              <option v-for="user in users" :key="user.id" :value="user.id">
                {{ user.benutzername }}
              </option>
            </select>
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
      <div v-if="playlists.length === 0" class="empty-state">
        Keine Playlists vorhanden. Erstellen Sie eine neue!
      </div>
      <div v-else class="grid">
        <div v-for="playlist in playlists" :key="playlist.id" class="card">
          <div class="card-header">
            <h3>{{ playlist.name }}</h3>
            <div class="card-actions">
              <button class="btn-icon" @click="openForm(playlist)" title="Bearbeiten">✏️</button>
              <button class="btn-icon" @click="deletePlaylist(playlist.id)" title="Löschen">🗑️</button>
            </div>
          </div>
          <p class="card-description">{{ playlist.beschreibung }}</p>
          <div class="card-info">
            <span v-if="playlist.tags.length > 0" class="tag">{{ playlist.tags.join(', ') }}</span>
            <span class="meta">{{ playlist.songIds.length }} Songs</span>
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
  padding: 20px;
  transition: all 0.3s ease;
}

.card:hover {
  border-color: #ccc;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transform: translateY(-4px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.card-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.15em;
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

.card-description {
  color: #666;
  font-size: 0.9em;
  margin: 0 0 12px 0;
  line-height: 1.4;
}

.card-info {
  display: flex;
  gap: 8px;
  align-items: center;
  flex-wrap: wrap;
}

.tag {
  display: inline-block;
  background: #f0f0f0;
  color: #333;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.85em;
  font-weight: 500;
}

.meta {
  color: #999;
  font-size: 0.85em;
}
</style>
