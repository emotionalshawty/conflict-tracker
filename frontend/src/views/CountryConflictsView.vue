<template>
 <div class="page">
 <router-link to="/countries" class="back-link">← {{ t('detail.back') }}</router-link>

 <h1 class="page-title">{{ countryName }}</h1>
 <p class="page-subtitle">{{ t('countries.conflictsFor') }} {{ countryCode }}</p>

 <LoadingSpinner v-if="loading" :message="t('common.loading')" />

 <div v-else-if="error" class="error-banner">
 {{ error }}
 </div>

 <div v-else-if="!conflicts.length" class="empty-state">
 <p class="empty-state__text">{{ t('countries.noConflictsForCountry') }}</p>
 </div>

 <div v-else class="grid grid--auto">
 <router-link
 v-for="conflict in conflicts"
 :key="conflict.id"
 :to="`/conflicts/${conflict.id}`"
 class="conflict-card"
 >
 <StatusBadge :status="conflict.status" />
 <h3 class="conflict-card__title">{{ conflict.name }}</h3>
 <p class="conflict-card__meta">
 {{ t('conflicts.startDate') }}: {{ formatDate(conflict.startDate) }}
 </p>
 </router-link>
 </div>
 </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { countriesApi } from '../services/api.js'
import { useCountryStore } from '../stores/countryStore.js'
import { useI18n } from '../composables/useI18n.js'
import LoadingSpinner from '../components/LoadingSpinner.vue'
import StatusBadge from '../components/StatusBadge.vue'

const route = useRoute()
const countryStore = useCountryStore()
const { t } = useI18n()

const loading = ref(false)
const error = ref('')
const conflicts = ref([])

const countryCode = computed(() => String(route.params.code || '').toUpperCase())

const countryName = computed(() => {
 const match = countryStore.countries.find(c => c.code === countryCode.value)
 if (match?.name) return match.name
 return countryCode.value || t('countries.title')
})

async function loadConflicts() {
 if (!countryCode.value) return
 loading.value = true
 error.value = ''
 try {
 const { data } = await countriesApi.getConflicts(countryCode.value)
 conflicts.value = Array.isArray(data) ? data : []
 } catch (err) {
 error.value = err.response?.data?.message || err.message
 conflicts.value = []
 } finally {
 loading.value = false
 }
}

onMounted(async () => {
 if (!countryStore.countries.length && !countryStore.loading) {
 await countryStore.fetchAll()
 }
 await loadConflicts()
})

watch(countryCode, async (newCode, oldCode) => {
 if (newCode && newCode !== oldCode) {
 await loadConflicts()
 }
})

function formatDate(dateStr) {
 if (!dateStr) return '—'
 return new Date(dateStr).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' })
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
 margin-bottom: 1rem;
}
.back-link:hover { color: var(--text-primary); }

.page-subtitle {
 color: var(--text-muted);
 margin-top: -0.25rem;
 margin-bottom: 1.25rem;
 font-size: 0.9rem;
}

.conflict-card {
 display: block;
 background: var(--bg-card);
 border: 1px solid var(--border);
 border-radius: 12px;
 padding: 1rem;
 text-decoration: none;
 color: inherit;
 transition: transform 0.2s, box-shadow 0.2s, border-color 0.2s;
}
.conflict-card:hover {
 transform: translateY(-2px);
 border-color: rgba(249,115,22,0.35);
 box-shadow: 0 8px 24px rgba(0,0,0,0.22);
}

.conflict-card__title {
 margin: 0.75rem 0 0.5rem;
 color: var(--text-primary);
 font-size: 1rem;
}

.conflict-card__meta {
 margin: 0;
 color: var(--text-muted);
 font-size: 0.85rem;
}
</style>
