import { reactive } from 'vue'
import { api } from '../api.js'

const state = reactive({
  user:   JSON.parse(localStorage.getItem('currentUser')   || 'null'),
  artist: JSON.parse(localStorage.getItem('currentArtist') || 'null'),
})

export function useAuth() {
  async function login(username, password) {
    const users = await api.getUsers()
    const user = users.find(u => u.username === username)
    if (!user) throw new Error('User not found')
    if (user.passwordHash !== password) throw new Error('Wrong password')
    state.user = user
    localStorage.setItem('currentUser', JSON.stringify(user))
    if (user.artistId) {
      const artist = await api.getArtistById(user.artistId)
      state.artist = artist
      localStorage.setItem('currentArtist', JSON.stringify(artist))
    }
    return user
  }

  function setUser(user) {
    state.user = user
    localStorage.setItem('currentUser', JSON.stringify(user))
  }

  function setArtist(artist) {
    state.artist = artist
    localStorage.setItem('currentArtist', JSON.stringify(artist))
  }

  function logout() {
    state.user   = null
    state.artist = null
    localStorage.removeItem('currentUser')
    localStorage.removeItem('currentArtist')
  }

  return { state, login, setUser, setArtist, logout }
}
