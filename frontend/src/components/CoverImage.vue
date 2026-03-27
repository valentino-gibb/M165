<script setup>
defineProps({
  src:   { type: String, default: '' },
  title: { type: String, required: true },
  size:  { type: String, default: '100%' },
})
</script>

<template>
  <img v-if="src" :src="src" :alt="title" class="cover-img" />
  <div v-else class="cover-letter">
    <span>{{ title?.charAt(0)?.toUpperCase() || '?' }}</span>
  </div>
</template>

<style scoped>
.cover-img {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  display: block;
}

.cover-letter {
  width: 100%;
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--accent-dim);
  color: #a78bfa;
  font-size: clamp(24px, 30%, 64px);
  font-weight: 700;
  letter-spacing: -1px;
  user-select: none;
}
</style>
