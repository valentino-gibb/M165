const BASE_URL = 'http://localhost:8080/api'

async function request(method, path, body) {
  const options = { method, headers: { 'Content-Type': 'application/json' } }
  if (body) options.body = JSON.stringify(body)
  const res = await fetch(`${BASE_URL}${path}`, options)
  if (!res.ok) throw new Error(`HTTP ${res.status}`)
  if (res.status === 204) return null
  return res.json()
}

export const api = {
  getUsers:        ()        => request('GET',    '/users'),
  getUserById:     (id)      => request('GET',    `/users/${id}`),
  createUser:      (data)    => request('POST',   '/users', data),
  updateUser:      (id, data)=> request('PUT',    `/users/${id}`, data),
  deleteUser:      (id)      => request('DELETE', `/users/${id}`),

  getArtists:      ()        => request('GET',    '/artists'),
  getArtistById:   (id)      => request('GET',    `/artists/${id}`),
  createArtist:    (data)    => request('POST',   '/artists', data),
  updateArtist:    (id, data)=> request('PUT',    `/artists/${id}`, data),
  deleteArtist:    (id)      => request('DELETE', `/artists/${id}`),

  getAlbums:       ()        => request('GET',    '/albums'),
  getAlbumById:    (id)      => request('GET',    `/albums/${id}`),
  createAlbum:     (data)    => request('POST',   '/albums', data),
  updateAlbum:     (id, data)=> request('PUT',    `/albums/${id}`, data),
  deleteAlbum:     (id)      => request('DELETE', `/albums/${id}`),

  getPlaylists:    ()        => request('GET',    '/playlists'),
  getPlaylistById: (id)      => request('GET',    `/playlists/${id}`),
  createPlaylist:  (data)    => request('POST',   '/playlists', data),
  updatePlaylist:  (id, data)=> request('PUT',    `/playlists/${id}`, data),
  deletePlaylist:  (id)      => request('DELETE', `/playlists/${id}`),
}
