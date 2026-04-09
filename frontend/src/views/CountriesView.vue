<template>
 <div class="page">
 <h1 class="page-title">{{ t('countries.title') }}</h1>

 <!-- Search bar (v-model) -->
 <div class="toolbar">
 <input
 v-model="search"
 type="text"
 class="form-control toolbar__search"
 :placeholder="t('countries.search')"
 />
 </div>

 <LoadingSpinner v-if="countryStore.loading" :message="t('countries.loading')" />

 <div v-else-if="countryStore.error" class="error-banner">
 {{ countryStore.error }}
 <button class="btn btn--ghost btn--sm" @click="countryStore.fetchAll()">{{ t('common.retry') }}</button>
 </div>

 <div v-else-if="filteredCountries.length === 0" class="empty-state">
 <div class="empty-state__icon"></div>
 <p class="empty-state__text">{{ t('countries.noResults') }}</p>
 </div>

 <!-- Country grid (v-for) -->
 <div v-else class="grid grid--auto">
 <div
 v-for="country in filteredCountries"
 :key="country.id"
 class="country-card"
 >
 <div class="country-card__flag">
 <CountryFlag :code="country.code" :country="country.name" size="lg" />
 </div>
 <div class="country-card__info">
 <h3 class="country-card__name">{{ country.name }}</h3>
 <span class="country-card__code">
 <!-- v-bind for dynamic tooltip -->
 <span
 class="code-badge"
 :title="`ISO code: ${country.code}`"
 >{{ country.code }}</span>
 </span>
 </div>
 </div>
 </div>
 </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useCountryStore } from '../stores/countryStore.js'
import { useI18n } from '../composables/useI18n.js'
import CountryFlag from '../components/CountryFlag.vue'
import LoadingSpinner from '../components/LoadingSpinner.vue'

const countryStore = useCountryStore()
const { t } = useI18n()

const search = ref('')

onMounted(() => countryStore.fetchAll())

// Computed: real-time search filter
const filteredCountries = computed(() => {
 const q = search.value.toLowerCase().trim()
 if (!q) return countryStore.countries
 return countryStore.countries.filter(c =>
 c.name.toLowerCase().includes(q) || c.code.toLowerCase().includes(q)
 )
})
</script>

<style scoped>
.toolbar { margin-bottom: 2rem; }
.toolbar__search { max-width: 400px; }

.country-card {
 background: var(--bg-card);
 border: 1px solid var(--border);
 border-radius: 12px;
 padding: 1.25rem;
 display: flex;
 align-items: center;
 gap: 1rem;
 transition: transform 0.2s, box-shadow 0.2s;
}
.country-card:hover {
 transform: translateY(-2px);
 box-shadow: 0 6px 24px rgba(0,0,0,0.25);
 border-color: rgba(249,115,22,0.3);
}

.country-card__flag {
 flex-shrink: 0;
 display: flex;
 align-items: center;
 justify-content: center;
 width: 60px;
 height: 48px;
 background: var(--bg-input);
 border-radius: 8px;
 border: 1px solid var(--border);
 overflow: hidden;
}

.country-card__info { flex: 1; min-width: 0; }

.country-card__name {
 font-family: 'Space Grotesk', sans-serif;
 font-size: 0.95rem;
 font-weight: 600;
 margin: 0 0 0.3rem;
 color: var(--text-primary);
}

.code-badge {
 display: inline-block;
 background: var(--bg-input);
 color: var(--text-muted);
 font-size: 0.75rem;
 font-weight: 700;
 letter-spacing: 0.08em;
 padding: 2px 8px;
 border-radius: 4px;
 border: 1px solid var(--border);
 font-family: monospace;
}
</style>
