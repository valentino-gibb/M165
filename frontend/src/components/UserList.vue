<script setup>
import { ref } from 'vue'
import api from '../api.js'

defineProps({
  users: Array
})

const emit = defineEmits(['refresh'])

const showForm = ref(false)
const editingId = ref(null)
const formData = ref({
  benutzername: '',
  email: '',
  password: '',
  land: '',
  profilbild: ''
})

const openForm = (user = null) => {
  if (user) {
    editingId.value = user.id
    formData.value = { ...user }
  } else {
    editingId.value = null
    formData.value = {
      benutzername: '',
      email: '',
      password: '',
      land: '',
      profilbild: ''
    }
  }
  showForm.value = true
}

const closeForm = () => {
  showForm.value = false
  editingId.value = null
}

const saveUser = async () => {
  try {
    if (editingId.value) {
      await api.users.update(editingId.value, formData.value)
    } else {
      await api.users.create(formData.value)
    }
    closeForm()
    emit('refresh')
  } catch (error) {
    console.error('Fehler beim Speichern:', error)
  }
}

const deleteUser = async (id) => {
  if (confirm('Sind Sie sicher, dass Sie diesen Benutzer löschen möchten?')) {
    try {
      await api.users.delete(id)
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
      <h2>👤 Benutzer</h2>
      <button class="btn-primary" @click="openForm">+ Neuer Benutzer</button>
    </div>

    <!-- Formular -->
    <div v-if="showForm" class="modal-overlay" @click.self="closeForm">
      <div class="modal">
        <div class="modal-header">
          <h3>{{ editingId ? 'Benutzer bearbeiten' : 'Neuer Benutzer' }}</h3>
          <button class="btn-close" @click="closeForm">✕</button>
        </div>
        <form @submit.prevent="saveUser" class="form">
          <div class="form-group">
            <label>Benutzername:</label>
            <input v-model="formData.benutzername" type="text" required>
          </div>
          <div class="form-group">
            <label>Email:</label>
            <input v-model="formData.email" type="email" required>
          </div>
          <div class="form-group">
            <label>Passwort:</label>
            <input v-model="formData.password" type="password" required>
          </div>
          <div class="form-group">
            <label>Land:</label>
            <input v-model="formData.land" type="text">
          </div>
          <div class="form-group">
            <label>Profilbild URL:</label>
            <input v-model="formData.profilbild" type="url">
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
      <div v-if="users.length === 0" class="empty-state">
        Keine Benutzer vorhanden. Erstellen Sie einen neuen!
      </div>
      <div v-else class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Benutzername</th>
              <th>Email</th>
              <th>Land</th>
              <th>Playlists</th>
              <th>Aktionen</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id" class="table-row">
              <td class="user-cell">
                <span v-if="user.profilbild" class="avatar">👤</span>
                {{ user.benutzername }}
              </td>
              <td>{{ user.email }}</td>
              <td>{{ user.land || '-' }}</td>
              <td>{{ user.playlistIds.length }}</td>
              <td class="actions-cell">
                <button class="btn-icon" @click="openForm(user)" title="Bearbeiten">✏️</button>
                <button class="btn-icon" @click="deleteUser(user.id)" title="Löschen">🗑️</button>
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

.user-cell {
  font-weight: 500;
  color: #333;
}

.avatar {
  margin-right: 8px;
  font-size: 1.1em;
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
