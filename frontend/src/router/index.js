import { createRouter, createWebHistory } from 'vue-router'
import LoginView    from '../views/LoginView.vue'
import AppLayout    from '../views/AppLayout.vue'
import HomeView     from '../views/HomeView.vue'
import ArtistsView  from '../views/ArtistsView.vue'
import AlbumsView   from '../views/AlbumsView.vue'
import PlaylistsView from '../views/PlaylistsView.vue'
import SettingsView from '../views/SettingsView.vue'
import StudioView   from '../views/StudioView.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginView },
  {
    path: '/app',
    component: AppLayout,
    meta: { requiresAuth: true },
    children: [
      { path: '',         redirect: '/app/home' },
      { path: 'home',     component: HomeView },
      { path: 'artists',  component: ArtistsView },
      { path: 'albums',   component: AlbumsView },
      { path: 'playlists',component: PlaylistsView },
      { path: 'settings', component: SettingsView },
      { path: 'studio',   component: StudioView },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to) => {
  const user = localStorage.getItem('currentUser')
  if (to.meta.requiresAuth && !user) return '/login'
  if (to.path === '/login' && user)  return '/app/home'
})

export default router
