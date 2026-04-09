<template>
 <div class="page">
 <h1 class="page-title">{{ t('events.title') }}</h1>

 <!-- Toolbar: search (v-model) -->
 <div class="toolbar">
 <input
 v-model="search"
 type="text"
 class="form-control toolbar__search"
 :placeholder="t('events.search')"
 />
 </div>

 <LoadingSpinner v-if="eventStore.loading" :message="t('events.loading')" />

 <div v-else-if="eventStore.error" class="error-banner">
 {{ eventStore.error }}
 <button class="btn btn--ghost btn--sm" @click="eventStore.fetchAll()">{{ t('common.retry') }}</button>
 </div>

 <div v-else-if="filteredEvents.length === 0" class="empty-state">
 <div class="empty-state__icon"></div>
 <p class="empty-state__text">{{ t('events.noResults') }}</p>
 </div>

 <!-- Events table / cards (v-for + v-if) -->
 <div v-else class="events-list">
 <div
 v-for="event in filteredEvents"
 :key="event.id"
 class="event-row"
 >
 <div class="event-date-col">
 <span class="event-day">{{ getDay(event.eventDate) }}</span>
 <span class="event-month">{{ getMonth(event.eventDate) }}</span>
 <span class="event-year">{{ getYear(event.eventDate) }}</span>
 </div>

 <div class="event-body">
 <p class="event-desc">{{ event.description || '—' }}</p>
 <div class="event-meta">
 <span v-if="event.location" class="event-meta-item">
 {{ event.location }}
 </span>
 <span v-if="event.conflictName" class="event-meta-item event-conflict">
 <router-link :to="`/conflicts/${event.conflictId}`" class="conflict-link">
 {{ event.conflictName }}
 </router-link>
 </span>
 </div>
 </div>
 </div>
 </div>
 </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useEventStore } from '../stores/eventStore.js'
import { useI18n } from '../composables/useI18n.js'
import LoadingSpinner from '../components/LoadingSpinner.vue'

const eventStore = useEventStore()
const { t } = useI18n()

const search = ref('')

onMounted(() => eventStore.fetchAll())

// Computed: real-time text filter across all event fields
const filteredEvents = computed(() => {
 const q = search.value.toLowerCase().trim()
 if (!q) return sortedEvents.value
 return sortedEvents.value.filter(e =>
 e.description?.toLowerCase().includes(q) ||
 e.location?.toLowerCase().includes(q) ||
 e.conflictName?.toLowerCase().includes(q)
 )
})

// Sort events newest first
const sortedEvents = computed(() =>
 [...eventStore.events].sort((a, b) => new Date(b.eventDate) - new Date(a.eventDate))
)

function getDay(d) { return d ? new Date(d).getDate() : '?' }
function getMonth(d) { return d ? new Date(d).toLocaleString('default', { month: 'short' }) : '' }
function getYear(d) { return d ? new Date(d).getFullYear() : '' }
</script>

<style scoped>
.toolbar { margin-bottom: 2rem; }
.toolbar__search { max-width: 420px; }

.events-list { display: flex; flex-direction: column; gap: 0.75rem; }

.event-row {
 background: var(--bg-card);
 border: 1px solid var(--border);
 border-radius: 10px;
 display: flex;
 align-items: stretch;
 overflow: hidden;
 transition: border-color 0.2s, transform 0.2s;
}
.event-row:hover { border-color: rgba(249,115,22,0.3); transform: translateX(2px); }

.event-date-col {
 display: flex;
 flex-direction: column;
 align-items: center;
 justify-content: center;
 min-width: 68px;
 padding: 0.875rem 0.5rem;
 background: var(--bg-input);
 border-right: 1px solid var(--border);
 flex-shrink: 0;
}
.event-day { font-family: 'Space Grotesk', sans-serif; font-size: 1.5rem; font-weight: 700; color: var(--accent); line-height: 1; }
.event-month { font-size: 0.7rem; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em; color: var(--text-muted); }
.event-year { font-size: 0.7rem; color: var(--text-muted); }

.event-body {
 padding: 0.875rem 1.25rem;
 flex: 1;
 display: flex;
 flex-direction: column;
 justify-content: center;
 gap: 0.35rem;
}
.event-desc { margin: 0; color: var(--text-primary); font-size: 0.9rem; line-height: 1.5; }

.event-meta { display: flex; gap: 1rem; flex-wrap: wrap; }
.event-meta-item { color: var(--text-muted); font-size: 0.8rem; }

.conflict-link {
 color: var(--text-muted);
 text-decoration: none;
 transition: color 0.2s;
}
.conflict-link:hover { color: var(--accent); }

@media (max-width: 500px) {
 .event-date-col { min-width: 56px; }
 .event-day { font-size: 1.2rem; }
}
</style>
