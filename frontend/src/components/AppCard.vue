<!-- Slot-based container component demonstrating the use of named slots -->
<template>
 <div class="app-card" :class="[`app-card--${variant}`, { 'app-card--fullwidth': fullwidth }]">
 <div v-if="$slots.header" class="app-card__header">
 <slot name="header" />
 </div>
 <div class="app-card__body">
 <slot />
 </div>
 <div v-if="$slots.footer" class="app-card__footer">
 <slot name="footer" />
 </div>
 </div>
</template>

<script setup>
defineProps({
 variant: {
 type: String,
 default: 'default' // 'default' | 'elevated' | 'bordered'
 },
 fullwidth: {
 type: Boolean,
 default: false
 }
})
</script>

<style scoped>
.app-card {
 background: var(--bg-card);
 border-radius: 12px;
 overflow: hidden;
 transition: box-shadow 0.2s;
}

.app-card--default { border: 1px solid var(--border); }
.app-card--elevated {
 border: 1px solid var(--border);
 box-shadow: 0 4px 24px rgba(0,0,0,0.3);
}
.app-card--bordered { border: 1px solid var(--accent); }
.app-card--fullwidth { width: 100%; }

.app-card__header {
 padding: 1.25rem 1.5rem;
 border-bottom: 1px solid var(--border);
 display: flex;
 align-items: center;
 justify-content: space-between;
 gap: 1rem;
}

.app-card__body { padding: 1.5rem; }

.app-card__footer {
 padding: 1rem 1.5rem;
 border-top: 1px solid var(--border);
 background: var(--bg-input);
}
</style>
