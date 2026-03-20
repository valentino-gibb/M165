<script setup>
import { ref } from 'vue'
import api from '../api.js'

defineProps({
  artists: Array
})

const emit = defineEmits(['refresh'])

const showForm = ref(false)
const editingId = ref(null)
const formData = ref({
  name: '',
  herkunftsland: '',
  debütJahr: new Date().getFullYear(),
  biografie: '',
  label: ''
})

const openForm = (artist = null) => {
  if (artist) {
    editingId.value = artist.id
    formData.value = { ...artist }
  } else {
    editingId.value = null
    formData.value = {
      name: '',
      herkunftsland: '',
      debütJahr: new Date().getFullYear(),
      biografie: '',
      label: ''
    }
  }
  showForm.value = true
}

const closeForm = () => {
  showForm.value = false
  editingId.value = null
}

const saveArtist = async () => {
  try {
    if (editingId.value) {
      await api.artists.update(editingId.value, formData.value)
    } else {
      await api.artists.create(formData.value)
    }
    closeForm()
    emit('refresh')
  } catch (error) {
    console.error('Fehler beim Speichern:', error)
  }
}

const deleteArtist = async (id) => {
  if (confirm('Sind Sie sicher, dass Sie diesen Künstler löschen möchten?')) {
    try {
      await api.artists.delete(id)
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
      <h2>🎤 Künstler</h2>
      <button class="btn-primary" @click="openForm">+ Neuer Künstler</button>
    </div>

    <!-- Formular -->
    <div v-if="showForm" class="modal-overlay" @click.self="closeForm">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ editingId ? 'Künstler bearbeiten' : 'Neuer Künstler' }}</h3>
          <button class="btn-close" @click="closeForm">✕</button>
        </div>
        <form @submit.prevent="saveArtist" class="form">
          <div class="form-group">
            <label>Name:</label>
            <input v-model="formData.name" type="text" required>
          </div>
          <div class="form-group">
            <label>Herkunftsland:</label>
            <input v-model="formData.herkunftsland" type="text">
          </div>
          <div class="form-group">
            <label>Debütjahr:</label>
            <input v-model.number="formData.debütJahr" type="number">
          </div>
          <div class="form-group">
            <label>Label:</label>
            <input v-model="formData.label" type="text">
          </div>
          <div class="form-group">
            <label>Biografie:</label>
            <textarea v-model="formData.biografie" rows="4"></textarea>
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
      <div v-if="artists.length === 0" class="empty-state">
        Keine Künstler vorhanden. Fügen Sie einen neuen hinzu!
      </div>
      <div v-else class="grid">
        <div v-for="artist in artists" :key="artist.id" class="card">
          <div class="card-header">
            <h3>{{ artist.name }}</h3>
            <div class="card-actions">
              <button class="btn-icon" @click="openForm(artist)" title="Bearbeiten">✏️</button>
              <button class="btn-icon" @click="deleteArtist(artist.id)" title="Löschen">🗑️</button>
            </div>
          </div>
          <div class="card-meta">
            <span v-if="artist.herkunftsland" class="badge">📍 {{ artist.herkunftsland }}</span>
            <span v-if="artist.debütJahr" class="badge">📅 {{ artist.debütJahr }}</span>
          </div>
          <p v-if="artist.label" class="card-label">Label: <strong>{{ artist.label }}</strong></p>
          <p v-if="artist.biografie" class="card-description">{{ artist.biografie.substring(0, 100) }}...</p>
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
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
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

.card-meta {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 12px;
}

.badge {
  display: inline-block;
  background: #f0f0f0;
  color: #666;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.85em;
  font-weight: 500;
}

.card-label {
  margin: 8px 0;
  color: #333;
  font-size: 0.9em;
}

.card-description {
  color: #666;
  font-size: 0.9em;
  margin: 0;
  line-height: 1.4;
}
</style>
