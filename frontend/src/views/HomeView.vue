<template>
 <div class="page">
 <!-- Hero section -->
 <div class="hero">
 <div class="hero-content">
 <h1 class="hero-title">{{ t('home.title') }}</h1>
 <p class="hero-subtitle">{{ t('home.subtitle') }}</p>
 <router-link to="/conflicts" class="btn btn--primary btn--lg">
 {{ t('home.viewAll') }} →
 </router-link>
 </div>
 <div class="hero-globe"></div>
 </div>

 <!-- Stats cards -->
 <div class="grid grid--4 stats-grid">
 <AppCard v-for="stat in stats" :key="stat.key" variant="elevated">
 <template #header>
 <span class="stat-label">{{ t(`home.${stat.key}`) }}</span>
 <span class="stat-icon" :class="`stat-icon--${stat.color}`">{{ stat.icon }}</span>
 </template>
 <div class="stat-value" :class="`stat-value--${stat.color}`">{{ stat.value }}</div>
 </AppCard>
 </div>

 <!-- Main content grid: Recent Conflicts + Chart -->
 <div class="home-grid">
 <AppCard variant="default">
 <template #header>
 <h2 class="section-title">{{ t('home.recentConflicts') }}</h2>
 <router-link to="/conflicts" class="btn btn--ghost btn--sm">{{ t('home.viewAll') }}</router-link>
 </template>

 <LoadingSpinner v-if="conflictStore.loading" :message="t('conflicts.loading')" size="sm" />

 <div v-else-if="conflictStore.error" class="error-banner">
 {{ conflictStore.error }}
 </div>

 <div v-else-if="recentConflicts.length === 0" class="empty-state">
 <div class="empty-state__icon"></div>
 <p class="empty-state__text">{{ t('home.noData') }}</p>
 </div>

 <ul v-else class="recent-list">
 <li
 v-for="c in recentConflicts"
 :key="c.id"
 class="recent-item"
 >
 <div class="recent-flags">
 <CountryFlag
 v-for="code in (c.countryCodes || []).slice(0, 3)"
 :key="code"
 :code="code"
 size="sm"
 />
 </div>
 <div class="recent-info">
 <router-link :to="`/conflicts/${c.id}`" class="recent-name">{{ c.name }}</router-link>
 <span class="recent-date">{{ formatDate(c.startDate) }}</span>
 </div>
 <StatusBadge :status="c.status" />
 </li>
 </ul>
 </AppCard>

 <AppCard variant="default">
 <template #header>
 <h2 class="section-title">{{ t('home.statusDistribution') }}</h2>
 </template>
 <div v-if="conflictStore.loading" class="chart-placeholder"></div>
 <StatsChart
 v-else
 :active="conflictStore.activeConflicts.length"
 :frozen="conflictStore.frozenConflicts.length"
 :ended="conflictStore.endedConflicts.length"
 />
 </AppCard>
 </div>
 </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useConflictStore } from '../stores/conflictStore.js'
import { useI18n } from '../composables/useI18n.js'
import AppCard from '../components/AppCard.vue'
import StatusBadge from '../components/StatusBadge.vue'
import CountryFlag from '../components/CountryFlag.vue'
import LoadingSpinner from '../components/LoadingSpinner.vue'
import StatsChart from '../components/StatsChart.vue'

const conflictStore = useConflictStore()
const { t } = useI18n()

onMounted(() => {
 if (conflictStore.conflicts.length === 0) {
 conflictStore.fetchAll()
 }
})

// Computed: last 5 conflicts sorted by startDate desc
const recentConflicts = computed(() =>
 [...conflictStore.conflicts]
 .sort((a, b) => new Date(b.startDate) - new Date(a.startDate))
 .slice(0, 5)
)

// Computed stats cards
const stats = computed(() => [
 {
 key: 'total',
 value: conflictStore.conflicts.length,
 icon: '',
 color: 'neutral'
 },
 {
 key: 'active',
 value: conflictStore.activeConflicts.length,
 icon: '',
 color: 'active'
 },
 {
 key: 'frozen',
 value: conflictStore.frozenConflicts.length,
 icon: '',
 color: 'frozen'
 },
 {
 key: 'ended',
 value: conflictStore.endedConflicts.length,
 icon: '',
 color: 'ended'
 }
])

function formatDate(dateStr) {
 if (!dateStr) return '—'
 return new Date(dateStr).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' })
}
</script>

<style scoped>
.hero {
 display: flex;
 align-items: center;
 justify-content: space-between;
 padding: 3rem 0 3.5rem;
 gap: 2rem;
}

.hero-title {
 font-family: 'Space Grotesk', sans-serif;
 font-size: 2.5rem;
 font-weight: 700;
 margin: 0 0 0.75rem;
 background: linear-gradient(135deg, #e6edf3 0%, var(--accent) 100%);
 -webkit-background-clip: text;
 -webkit-text-fill-color: transparent;
 background-clip: text;
 line-height: 1.2;
}

.hero-subtitle {
 color: var(--text-muted);
 font-size: 1.05rem;
 margin: 0 0 1.75rem;
 max-width: 500px;
 line-height: 1.7;
}

.hero-globe {
 font-size: 7rem;
 opacity: 0.15;
 user-select: none;
 flex-shrink: 0;
}

.stats-grid { margin-bottom: 2rem; }

.stat-label { font-size: 0.8rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em; }
.stat-icon { font-size: 1.2rem; }
.stat-value { font-family: 'Space Grotesk', sans-serif; font-size: 2.5rem; font-weight: 700; line-height: 1; }

.stat-value--active { color: var(--status-active); }
.stat-value--frozen { color: var(--status-frozen); }
.stat-value--ended { color: var(--status-ended); }
.stat-value--neutral { color: var(--text-primary); }

.home-grid {
 display: grid;
 grid-template-columns: 1fr 320px;
 gap: 1.25rem;
 align-items: start;
}

.section-title {
 font-family: 'Space Grotesk', sans-serif;
 font-size: 1rem;
 font-weight: 600;
 margin: 0;
 color: var(--text-primary);
}

.recent-list { list-style: none; padding: 0; margin: 0; display: flex; flex-direction: column; gap: 0; }

.recent-item {
 display: flex;
 align-items: center;
 gap: 1rem;
 padding: 0.875rem 0;
 border-bottom: 1px solid var(--border);
}
.recent-item:last-child { border-bottom: none; }

.recent-flags { display: flex; gap: 3px; flex-shrink: 0; min-width: 64px; }

.recent-info { flex: 1; min-width: 0; }

.recent-name {
 display: block;
 color: var(--text-primary);
 font-weight: 500;
 font-size: 0.9rem;
 text-decoration: none;
 white-space: nowrap;
 overflow: hidden;
 text-overflow: ellipsis;
 margin-bottom: 2px;
}
.recent-name:hover { color: var(--accent); }

.recent-date { color: var(--text-muted); font-size: 0.78rem; }

.chart-placeholder { height: 260px; }

@media (max-width: 900px) {
 .home-grid { grid-template-columns: 1fr; }
 .hero { flex-direction: column; text-align: center; padding: 2rem 0 2.5rem; }
 .hero-globe { display: none; }
 .hero-subtitle { max-width: 100%; }
}
</style>
