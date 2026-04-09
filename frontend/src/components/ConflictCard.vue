<!-- ConflictCard – receives conflict as prop, emits delete/edit actions to parent -->
<template>
 <div class="conflict-card" :class="`conflict-card--${conflict.status.toLowerCase()}`">
 <!-- Status accent bar -->
 <div class="card-accent"></div>

 <div class="card-body">
 <div class="card-header">
 <StatusBadge :status="conflict.status" />
 <span class="card-date"> {{ formatDate(conflict.startDate) }}</span>
 </div>

 <h3 class="card-title">{{ conflict.name }}</h3>

 <p v-if="conflict.description" class="card-desc">{{ truncate(conflict.description, 120) }}</p>

 <!-- Countries with flags -->
 <div v-if="conflict.countryCodes?.length" class="card-countries">
 <CountryFlag
 v-for="code in conflict.countryCodes"
 :key="code"
 :code="code"
 :country="code"
 size="sm"
 />
 <span class="country-count">
 {{ conflict.countryCodes.length }}
 {{ conflict.countryCodes.length === 1 ? 'country' : 'countries' }}
 </span>
 </div>
 </div>

 <!-- Actions -->
 <div class="card-actions">
 <router-link :to="`/conflicts/${conflict.id}`" class="btn btn--primary btn--sm">
 {{ t('conflicts.viewDetails') }}
 </router-link>
 <button class="btn btn--ghost btn--sm" @click="$emit('edit', conflict.id)">
 {{ t('conflicts.edit') }}
 </button>
 <button class="btn btn--danger btn--sm" @click="$emit('delete', conflict.id)">
 
 </button>
 </div>
 </div>
</template>

<script setup>
import StatusBadge from './StatusBadge.vue'
import CountryFlag from './CountryFlag.vue'
import { useI18n } from '../composables/useI18n.js'

const { t } = useI18n()

defineProps({
 conflict: {
 type: Object,
 required: true
 }
})

// emit 'edit' and 'delete' actions up to parent
defineEmits(['delete', 'edit'])

function formatDate(dateStr) {
 if (!dateStr) return '—'
 return new Date(dateStr).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' })
}

function truncate(str, max) {
 return str.length > max ? str.slice(0, max) + '…' : str
}
</script>

<style scoped>
.conflict-card {
 background: var(--bg-card);
 border: 1px solid var(--border);
 border-radius: 12px;
 overflow: hidden;
 display: flex;
 flex-direction: column;
 transition: transform 0.2s, box-shadow 0.2s, border-color 0.2s;
 position: relative;
}

.conflict-card:hover {
 transform: translateY(-2px);
 box-shadow: 0 8px 32px rgba(0,0,0,0.3);
}

.conflict-card--active { border-color: rgba(239, 68, 68, 0.3); }
.conflict-card--frozen { border-color: rgba(96, 165, 250, 0.3); }
.conflict-card--ended { border-color: rgba(74, 222, 128, 0.3); }

.card-accent {
 height: 3px;
 width: 100%;
}
.conflict-card--active .card-accent { background: var(--status-active); }
.conflict-card--frozen .card-accent { background: var(--status-frozen); }
.conflict-card--ended .card-accent { background: var(--status-ended); }

.card-body { padding: 1.25rem; flex: 1; display: flex; flex-direction: column; gap: 0.75rem; }

.card-header {
 display: flex;
 align-items: center;
 justify-content: space-between;
 flex-wrap: wrap;
 gap: 0.5rem;
}

.card-date { color: var(--text-muted); font-size: 0.8rem; }

.card-title {
 font-family: 'Space Grotesk', sans-serif;
 font-size: 1.05rem;
 font-weight: 600;
 color: var(--text-primary);
 margin: 0;
 line-height: 1.4;
}

.card-desc { color: var(--text-muted); font-size: 0.88rem; line-height: 1.6; margin: 0; flex: 1; }

.card-countries {
 display: flex;
 align-items: center;
 gap: 0.4rem;
 flex-wrap: wrap;
}

.country-count { color: var(--text-muted); font-size: 0.78rem; }

.card-actions {
 padding: 0.875rem 1.25rem;
 border-top: 1px solid var(--border);
 display: flex;
 gap: 0.5rem;
 align-items: center;
 background: var(--bg-input);
}

.card-actions .btn:first-child { flex: 1; }
</style>
