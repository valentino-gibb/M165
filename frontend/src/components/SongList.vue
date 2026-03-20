<script setup>
import { ref } from 'vue'
import api from '../api.js'

defineProps({
  songs: Array,
  albums: Array
})

const emit = defineEmits(['refresh'])

const showForm = ref(false)
const editingId = ref(null)
const isSaving = ref(false)
const notification = ref(null)
const formData = ref({
  title: '',
  dauer: 0,
  genre: '',
  veröffentlichungsDatum: '',
  explizit: false,
  lyrics: '',
  sprache: 'Deutsch',
  albumId: ''
})

const openForm = (song = null) => {
  if (song) {
    editingId.value = song.id
    formData.value = { ...song }
  } else {
    editingId.value = null
    formData.value = {
      title: '',
      dauer: 0,
      genre: '',
      veröffentlichungsDatum: '',
      explizit: false,
      lyrics: '',
      sprache: 'Deutsch',
      albumId: ''
    }
  }
  showForm.value = true
}

const closeForm = () => {
  showForm.value = false
  editingId.value = null
}

const showNotification = (message, type = 'success', duration = 3000) => {
  notification.value = { message, type }
  setTimeout(() => {
    notification.value = null
  }, duration)
}

const saveSong = async () => {
  if (!formData.value.title.trim()) {
    showNotification('Bitte geben Sie einen Songtitel ein', 'error', 3000)
    return
  }

  isSaving.value = true
  try {
    if (editingId.value) {
      await api.songs.update(editingId.value, formData.value)
      showNotification('✅ Song erfolgreich aktualisiert!', 'success')
    } else {
      await api.songs.create(formData.value)
      showNotification('✅ Song erfolgreich erstellt!', 'success')
    }
    closeForm()
    emit('refresh')
  } catch (error) {
    console.error('Fehler beim Speichern:', error)
    showNotification('❌ Fehler beim Speichern: Backend nicht erreichbar', 'error', 5000)
  } finally {
    isSaving.value = false
  }
}

const deleteSong = async (id) => {
  if (confirm('Sind Sie sicher, dass Sie diesen Song löschen möchten?')) {
    try {
      await api.songs.delete(id)
      showNotification('✅ Song gelöscht!', 'success')
      emit('refresh')
    } catch (error) {
      console.error('Fehler beim Löschen:', error)
      showNotification('❌ Fehler beim Löschen', 'error')
    }
  }
}

const getAlbumName = (albumId) => {
  const album = albums.find(a => a.id === albumId)
  return album ? album.title : 'Kein Album'
}

const formatDuration = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs < 10 ? '0' : ''}${secs}`
}
</script>

<template>
  <div class="container">
    <!-- Notification -->
    <div v-if="notification" :class="['notification', notification.type]">
      {{ notification.message }}
    </div>

    <div class="header-section">
      <h2>🎶 Songs</h2>
      <button class="btn-primary" @click="openForm" :disabled="isSaving">
        {{ isSaving ? '⏳ Erstelle...' : '+ Neuer Song' }}
      </button>
    </div>

    <!-- Formular -->
    <div v-if="showForm" class="modal-overlay" @click.self="closeForm">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ editingId ? 'Song bearbeiten' : 'Neuer Song' }}</h3>
          <button class="btn-close" @click="closeForm">✕</button>
        </div>
        <form @submit.prevent="saveSong" class="form">
          <div class="form-group">
            <label>Titel:</label>
            <input v-model="formData.title" type="text" required>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Dauer (Sekunden):</label>
              <input v-model.number="formData.dauer" type="number">
            </div>
            <div class="form-group">
              <label>Genre:</label>
              <input v-model="formData.genre" type="text">
            </div>
          </div>
          <div class="form-group">
            <label>Sprache:</label>
            <select v-model="formData.sprache">
              <option>Deutsch</option>
              <option>Englisch</option>
              <option>Französisch</option>
              <option>Spanisch</option>
              <option>Italienisch</option>
            </select>
          </div>
          <div class="form-group">
            <label>Album:</label>
            <select v-model="formData.albumId">
              <option value="">-- Kein Album --</option>
              <option v-for="album in albums" :key="album.id" :value="album.id">
                {{ album.title }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Veröffentlichungsdatum:</label>
            <input v-model="formData.veröffentlichungsDatum" type="date">
          </div>
          <div class="form-group checkbox">
            <input v-model="formData.explizit" type="checkbox" id="explizit">
            <label for="explizit">Expliziter Inhalt</label>
          </div>
          <div class="form-group">
            <label>Lyrics:</label>
            <textarea v-model="formData.lyrics" rows="4" placeholder="Geben Sie die Lyrics ein..."></textarea>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn-primary" :disabled="isSaving">
              {{ isSaving ? 'Speichern...' : 'Speichern' }}
            </button>
            <button type="button" class="btn-secondary" @click="closeForm" :disabled="isSaving">Abbrechen</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Liste -->
    <div class="list-container">
      <div v-if="songs.length === 0" class="empty-state">
        Keine Songs vorhanden. Fügen Sie einen neuen hinzu!
      </div>
      <div v-else class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Titel</th>
              <th>Genre</th>
              <th>Album</th>
              <th>Dauer</th>
              <th>Sprache</th>
              <th>Streams</th>
              <th>Aktionen</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="song in songs" :key="song.id" class="table-row">
              <td class="title-cell">
                <span v-if="song.explizit" class="explicit-badge">🔞</span>
                {{ song.title }}
              </td>
              <td>{{ song.genre }}</td>
              <td>{{ getAlbumName(song.albumId) }}</td>
              <td>{{ formatDuration(song.dauer) }}</td>
              <td>{{ song.sprache }}</td>
              <td>{{ song.anzahlStreams }}</td>
              <td class="actions-cell">
                <button class="btn-icon" @click="openForm(song)" title="Bearbeiten">✏️</button>
                <button class="btn-icon" @click="deleteSong(song.id)" title="Löschen">🗑️</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  position: relative;
}

.notification {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 16px 24px;
  border-radius: 8px;
  font-weight: 600;
  z-index: 2000;
  animation: slideIn 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.notification.success {
  background: #10b981;
  color: white;
}

.notification.error {
  background: #ef4444;
  color: white;
}

@keyframes slideIn {
  from {
    transform: translateX(400px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
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

.btn-primary:hover:not(:disabled) {
  background: #555;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
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

.btn-secondary:hover:not(:disabled) {
  border-color: #333;
}

.btn-secondary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
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
  overflow-y: auto;
}

.modal {
  background: white;
  border-radius: 12px;
  padding: 0;
  max-width: 500px;
  width: 90%;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  margin: 20px;
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
  max-height: 70vh;
  overflow-y: auto;
}

.form-group {
  margin-bottom: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
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

.form-group.checkbox {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.form-group.checkbox input {
  width: auto;
  margin-right: 10px;
}

.form-group.checkbox label {
  margin-bottom: 0;
  margin-left: 0;
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

.table-wrapper {
  background: white;
  border-radius: 10px;
  border: 1px solid #e0e0e0;
  overflow: hidden;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table thead {
  background: #f8f8f8;
  border-bottom: 1px solid #e0e0e0;
}

.table th {
  padding: 16px 12px;
  text-align: left;
  font-weight: 600;
  color: #333;
  font-size: 0.9em;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.table-row {
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.2s ease;
}

.table-row:hover {
  background: #f8f8f8;
}

.table td {
  padding: 16px 12px;
  color: #666;
  font-size: 0.95em;
}

.title-cell {
  font-weight: 500;
  color: #333;
}

.explicit-badge {
  margin-right: 6px;
}

.actions-cell {
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
  background: #e0e0e0;
}
</style>
