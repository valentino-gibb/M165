// API Service für Backend-Kommunikation
const API_BASE_URL = 'http://localhost:8080/api';

const api = {
  // Artists
  artists: {
    getAll: () => fetch(`${API_BASE_URL}/artists`).then(res => res.json()),
    getById: (id) => fetch(`${API_BASE_URL}/artists/${id}`).then(res => res.json()),
    create: (data) => fetch(`${API_BASE_URL}/artists`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    update: (id, data) => fetch(`${API_BASE_URL}/artists/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    delete: (id) => fetch(`${API_BASE_URL}/artists/${id}`, { method: 'DELETE' })
  },

  // Songs
  songs: {
    getAll: () => fetch(`${API_BASE_URL}/songs`).then(res => res.json()),
    getById: (id) => fetch(`${API_BASE_URL}/songs/${id}`).then(res => res.json()),
    getByGenre: (genre) => fetch(`${API_BASE_URL}/songs/genre/${genre}`).then(res => res.json()),
    getByAlbum: (albumId) => fetch(`${API_BASE_URL}/songs/album/${albumId}`).then(res => res.json()),
    create: (data) => fetch(`${API_BASE_URL}/songs`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    update: (id, data) => fetch(`${API_BASE_URL}/songs/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    delete: (id) => fetch(`${API_BASE_URL}/songs/${id}`, { method: 'DELETE' }),
    incrementStream: (id) => fetch(`${API_BASE_URL}/songs/${id}/stream`, { method: 'POST' }).then(res => res.json())
  },

  // Albums
  albums: {
    getAll: () => fetch(`${API_BASE_URL}/albums`).then(res => res.json()),
    getById: (id) => fetch(`${API_BASE_URL}/albums/${id}`).then(res => res.json()),
    getByArtist: (artistId) => fetch(`${API_BASE_URL}/albums/artist/${artistId}`).then(res => res.json()),
    create: (data) => fetch(`${API_BASE_URL}/albums`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    update: (id, data) => fetch(`${API_BASE_URL}/albums/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    delete: (id) => fetch(`${API_BASE_URL}/albums/${id}`, { method: 'DELETE' }),
    addSong: (albumId, songId) => fetch(`${API_BASE_URL}/albums/${albumId}/songs/${songId}`, { method: 'POST' }).then(res => res.json()),
    removeSong: (albumId, songId) => fetch(`${API_BASE_URL}/albums/${albumId}/songs/${songId}`, { method: 'DELETE' }).then(res => res.json())
  },

  // Users
  users: {
    getAll: () => fetch(`${API_BASE_URL}/users`).then(res => res.json()),
    getById: (id) => fetch(`${API_BASE_URL}/users/${id}`).then(res => res.json()),
    create: (data) => fetch(`${API_BASE_URL}/users`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    update: (id, data) => fetch(`${API_BASE_URL}/users/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    delete: (id) => fetch(`${API_BASE_URL}/users/${id}`, { method: 'DELETE' })
  },

  // Playlists
  playlists: {
    getAll: () => fetch(`${API_BASE_URL}/playlists`).then(res => res.json()),
    getById: (id) => fetch(`${API_BASE_URL}/playlists/${id}`).then(res => res.json()),
    getByUser: (userId) => fetch(`${API_BASE_URL}/playlists/user/${userId}`).then(res => res.json()),
    create: (data) => fetch(`${API_BASE_URL}/playlists`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    update: (id, data) => fetch(`${API_BASE_URL}/playlists/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    }).then(res => res.json()),
    delete: (id) => fetch(`${API_BASE_URL}/playlists/${id}`, { method: 'DELETE' }),
    addSong: (playlistId, songId) => fetch(`${API_BASE_URL}/playlists/${playlistId}/songs/${songId}`, { method: 'POST' }).then(res => res.json()),
    removeSong: (playlistId, songId) => fetch(`${API_BASE_URL}/playlists/${playlistId}/songs/${songId}`, { method: 'DELETE' }).then(res => res.json())
  }
};

export default api;
