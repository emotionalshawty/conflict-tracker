<template>
 <div class="page">
 <router-link to="/conflicts" class="back-link">
 ← {{ t('detail.back') }}
 </router-link>

 <LoadingSpinner v-if="conflictStore.loading" :message="t('common.loading')" />

 <div v-else-if="conflictStore.error" class="error-banner">
 {{ conflictStore.error }}
 </div>

 <template v-else-if="conflict">
 <!-- Header section -->
 <div class="detail-hero" :class="`detail-hero--${conflict.status.toLowerCase()}`">
 <div class="detail-hero__accent"></div>
 <div class="detail-hero__content">
 <div class="detail-hero__meta">
 <StatusBadge :status="conflict.status" />
 <span class="detail-date"> {{ formatDate(conflict.startDate) }}</span>
 </div>
 <h1 class="detail-title">{{ conflict.name }}</h1>
 <p v-if="conflict.description" class="detail-description">{{ conflict.description }}</p>
 </div>
 <div class="detail-hero__actions">
 <router-link :to="`/conflicts/${conflict.id}/edit`" class="btn btn--ghost">
 {{ t('conflicts.edit') }}
 </router-link>
 <button class="btn btn--danger" @click="showModal = true">
 {{ t('conflicts.delete') }}
 </button>
 </div>
 </div>

 <!-- Three-column detail grid -->
 <div class="detail-grid">
 <!-- Countries -->
 <AppCard variant="default">
 <template #header>
 <h2 class="section-title"> {{ t('detail.countries') }}</h2>
 <span class="section-count">{{ conflict.countries?.length ?? 0 }}</span>
 </template>

 <div v-if="!conflict.countries?.length" class="empty-mini">
 {{ t('detail.noCountries') }}
 </div>
 <ul v-else class="country-list">
 <li v-for="country in conflict.countries" :key="country.id" class="country-item">
 <CountryFlag :code="country.code" :country="country.name" size="md" />
 <div class="country-info">
 <span class="country-name">{{ country.name }}</span>
 <span class="country-code">{{ country.code }}</span>
 </div>
 </li>
 </ul>
 </AppCard>

 <!-- Factions -->
 <AppCard variant="default">
 <template #header>
 <h2 class="section-title"> {{ t('detail.factions') }}</h2>
 <span class="section-count">{{ conflict.factions?.length ?? 0 }}</span>
 </template>

 <div v-if="!conflict.factions?.length" class="empty-mini">
 {{ t('detail.noFactions') }}
 </div>
 <ul v-else class="faction-list">
 <li v-for="faction in conflict.factions" :key="faction.id" class="faction-item">
 <div class="faction-name">{{ faction.name }}</div>
 <div v-if="faction.supportingCountryCodes?.length" class="faction-countries">
 <CountryFlag
 v-for="code in faction.supportingCountryCodes"
 :key="code"
 :code="code"
 size="sm"
 />
 </div>
 </li>
 </ul>
 </AppCard>

 <!-- Event count summary -->
 <AppCard variant="default">
 <template #header>
 <h2 class="section-title"> {{ t('detail.overview') }}</h2>
 </template>
 <div class="overview-stats">
 <div class="ov-stat">
 <span class="ov-value">{{ conflict.events?.length ?? 0 }}</span>
 <span class="ov-label">{{ t('nav.events') }}</span>
 </div>
 <div class="ov-stat">
 <span class="ov-value">{{ conflict.factions?.length ?? 0 }}</span>
 <span class="ov-label">{{ t('detail.factions') }}</span>
 </div>
 <div class="ov-stat">
 <span class="ov-value">{{ conflict.countries?.length ?? 0 }}</span>
 <span class="ov-label">{{ t('nav.countries') }}</span>
 </div>
 </div>
 </AppCard>
 </div>

 <!-- Event Timeline -->
 <AppCard variant="default" class="timeline-card">
 <template #header>
 <h2 class="section-title"> {{ t('detail.timeline') }}</h2>
 <span class="section-count">{{ conflict.events?.length ?? 0 }}</span>
 </template>

 <div v-if="!conflict.events?.length" class="empty-mini">
 {{ t('detail.noEvents') }}
 </div>
 <div v-else class="timeline">
 <div
 v-for="event in sortedEvents"
 :key="event.id"
 class="timeline-item"
 >
 <div class="timeline-dot"></div>
 <div class="timeline-content">
 <div class="timeline-header">
 <span class="timeline-date">{{ formatDate(event.eventDate) }}</span>
 <span v-if="event.location" class="timeline-location"> {{ event.location }}</span>
 </div>
 <p class="timeline-desc">{{ event.description || '—' }}</p>
 </div>
 </div>
 </div>
 </AppCard>
 </template>

 <!-- Delete confirm modal -->
 <ConfirmModal v-model="showModal" :message="t('conflicts.confirmDelete')" @confirm="handleDelete" />
 </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useConflictStore } from '../stores/conflictStore.js'
import { useI18n } from '../composables/useI18n.js'
import AppCard from '../components/AppCard.vue'
import StatusBadge from '../components/StatusBadge.vue'
import CountryFlag from '../components/CountryFlag.vue'
import LoadingSpinner from '../components/LoadingSpinner.vue'
import ConfirmModal from '../components/ConfirmModal.vue'

const route = useRoute()
const router = useRouter()
const conflictStore = useConflictStore()
const { t } = useI18n()

const showModal = ref(false)

// Dynamic route: /conflicts/:id
const id = computed(() => route.params.id)

async function loadConflict() {
 if (!id.value) return
 await conflictStore.fetchById(id.value)
}

onMounted(loadConflict)

watch(id, (newId, oldId) => {
 if (newId && newId !== oldId) {
 loadConflict()
 }
})

const conflict = computed(() => conflictStore.currentConflict)

// Sort events chronologically
const sortedEvents = computed(() =>
 conflict.value?.events
 ? [...conflict.value.events].sort((a, b) => new Date(a.eventDate) - new Date(b.eventDate))
 : []
)

function formatDate(dateStr) {
 if (!dateStr) return '—'
 return new Date(dateStr).toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' })
}

async function handleDelete() {
 await conflictStore.remove(id.value)
 router.push('/conflicts')
}
</script>

<style scoped>
.back-link {
 display: inline-flex;
 align-items: center;
 gap: 0.4rem;
 color: var(--text-muted);
 text-decoration: none;
 font-size: 0.88rem;
 margin-bottom: 1.5rem;
 transition: color 0.2s;
}
.back-link:hover { color: var(--text-primary); }

.detail-hero {
 border-radius: 16px;
 border: 1px solid var(--border);
 overflow: hidden;
 display: flex;
 flex-direction: column;
 margin-bottom: 1.5rem;
}
.detail-hero--active { border-color: rgba(239,68,68,0.4); }
.detail-hero--frozen { border-color: rgba(96,165,250,0.4); }
.detail-hero--ended { border-color: rgba(74,222,128,0.4); }

.detail-hero__accent { height: 4px; }
.detail-hero--active .detail-hero__accent { background: linear-gradient(90deg, var(--status-active), transparent); }
.detail-hero--frozen .detail-hero__accent { background: linear-gradient(90deg, var(--status-frozen), transparent); }
.detail-hero--ended .detail-hero__accent { background: linear-gradient(90deg, var(--status-ended), transparent); }

.detail-hero__content {
 padding: 1.75rem 2rem 1.25rem;
 flex: 1;
}
.detail-hero__meta { display: flex; align-items: center; gap: 1rem; margin-bottom: 0.875rem; }
.detail-date { color: var(--text-muted); font-size: 0.85rem; }

.detail-title {
 font-family: 'Space Grotesk', sans-serif;
 font-size: 1.75rem;
 font-weight: 700;
 margin: 0 0 0.75rem;
 color: var(--text-primary);
}

.detail-description {
 color: var(--text-muted);
 font-size: 0.95rem;
 line-height: 1.7;
 margin: 0;
 max-width: 700px;
}

.detail-hero__actions {
 display: flex;
 gap: 0.75rem;
 padding: 1rem 2rem;
 border-top: 1px solid var(--border);
 background: var(--bg-input);
}

.detail-grid {
 display: grid;
 grid-template-columns: repeat(3, 1fr);
 gap: 1.25rem;
 margin-bottom: 1.5rem;
}

.section-title { font-family: 'Space Grotesk', sans-serif; font-size: 0.95rem; font-weight: 600; margin: 0; color: var(--text-primary); }
.section-count {
 display: inline-flex;
 align-items: center;
 justify-content: center;
 background: var(--bg-input);
 color: var(--text-muted);
 border-radius: 999px;
 padding: 2px 8px;
 font-size: 0.78rem;
 font-weight: 600;
 min-width: 24px;
}

.empty-mini { color: var(--text-muted); font-size: 0.88rem; text-align: center; padding: 1.5rem 0; }

.country-list { list-style: none; padding: 0; margin: 0; display: flex; flex-direction: column; gap: 0.75rem; }
.country-item { display: flex; align-items: center; gap: 0.875rem; }
.country-info { display: flex; flex-direction: column; }
.country-name { font-weight: 500; font-size: 0.9rem; }
.country-code { color: var(--text-muted); font-size: 0.78rem; text-transform: uppercase; }

.faction-list { list-style: none; padding: 0; margin: 0; display: flex; flex-direction: column; gap: 0.75rem; }
.faction-item {
 padding: 0.75rem;
 background: var(--bg-input);
 border-radius: 8px;
 border: 1px solid var(--border);
}
.faction-name { font-weight: 500; font-size: 0.9rem; margin-bottom: 0.35rem; }
.faction-countries { display: flex; gap: 4px; flex-wrap: wrap; }

.overview-stats { display: flex; justify-content: space-around; padding: 0.5rem 0; }
.ov-stat { text-align: center; }
.ov-value { display: block; font-family: 'Space Grotesk', sans-serif; font-size: 2rem; font-weight: 700; color: var(--accent); }
.ov-label { font-size: 0.78rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em; }

.timeline-card { margin-bottom: 0; }

.timeline { position: relative; padding-left: 1.5rem; }
.timeline::before {
 content: '';
 position: absolute;
 left: 0;
 top: 8px;
 bottom: 8px;
 width: 2px;
 background: var(--border);
}

.timeline-item {
 position: relative;
 padding-bottom: 1.5rem;
 padding-left: 1.5rem;
}
.timeline-item:last-child { padding-bottom: 0; }

.timeline-dot {
 position: absolute;
 left: -1.625rem;
 top: 6px;
 width: 12px;
 height: 12px;
 border-radius: 50%;
 background: var(--accent);
 border: 2px solid var(--bg-card);
 box-shadow: 0 0 0 2px var(--accent);
}

.timeline-header { display: flex; align-items: center; gap: 1rem; margin-bottom: 0.35rem; flex-wrap: wrap; }
.timeline-date { font-weight: 600; font-size: 0.88rem; color: var(--text-primary); }
.timeline-location { color: var(--text-muted); font-size: 0.82rem; }
.timeline-desc { color: var(--text-muted); font-size: 0.88rem; line-height: 1.6; margin: 0; }

@media (max-width: 900px) {
 .detail-grid { grid-template-columns: 1fr; }
}
</style>
