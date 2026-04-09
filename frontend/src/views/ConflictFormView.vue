<template>
 <div class="page">
 <router-link to="/conflicts" class="back-link">← {{ t('detail.back') }}</router-link>

 <h1 class="page-title">{{ isEdit ? t('form.editConflict') : t('form.newConflict') }}</h1>

 <LoadingSpinner v-if="loading" :message="t('common.loading')" />

 <form v-else class="conflict-form" @submit.prevent="handleSubmit" novalidate>
 <AppCard variant="elevated">
 <template #header>
 <span class="form-section-title"> {{ t('detail.overview') }}</span>
 </template>

 <!-- Name field — v-model -->
 <div class="form-group">
 <label class="form-label" for="name">{{ t('form.name') }}</label>
 <input
 id="name"
 v-model="form.name"
 type="text"
 class="form-control"
 :class="{ 'form-control--error': errors.name }"
 :placeholder="t('form.namePlaceholder')"
 required
 />
 <span v-if="errors.name" class="field-error">{{ errors.name }}</span>
 </div>

 <!-- Status — v-model -->
 <div class="form-group">
 <label class="form-label" for="status">{{ t('form.status') }}</label>
 <select id="status" v-model="form.status" class="form-control" required>
 <option value="" disabled>—</option>
 <option v-for="s in statusOptions" :key="s" :value="s">
 {{ t(`status.${s}`) }}
 </option>
 </select>
 </div>

 <!-- Start date — v-model -->
 <div class="form-group">
 <label class="form-label" for="startDate">{{ t('form.startDate') }}</label>
 <input id="startDate" v-model="form.startDate" type="date" class="form-control" />
 </div>

 <!-- Description — v-model -->
 <div class="form-group">
 <label class="form-label" for="description">{{ t('form.description') }}</label>
 <textarea
 id="description"
 v-model="form.description"
 class="form-control"
 :placeholder="t('form.descriptionPlaceholder')"
 rows="4"
 ></textarea>
 </div>
 </AppCard>

 <!-- Countries section -->
 <AppCard variant="default" class="countries-card">
 <template #header>
 <span class="form-section-title"> {{ t('form.countries') }}</span>
 </template>

 <div v-if="countryStore.loading" class="empty-mini">{{ t('common.loading') }}</div>
 <div v-else class="country-checkboxes">
 <label
 v-for="country in countryStore.countries"
 :key="country.code"
 class="country-check"
 :class="{ 'country-check--selected': form.countryCodes.includes(country.code) }"
 >
 <input
 type="checkbox"
 :value="country.code"
 v-model="form.countryCodes"
 class="sr-only"
 />
 <CountryFlag :code="country.code" :country="country.name" size="sm" />
 <span class="check-name">{{ country.name }}</span>
 <span v-if="form.countryCodes.includes(country.code)" class="check-tick">✓</span>
 </label>
 </div>
 </AppCard>

 <!-- Form actions -->
 <div class="form-actions">
 <router-link to="/conflicts" class="btn btn--ghost">{{ t('form.cancel') }}</router-link>
 <button type="submit" class="btn btn--primary" :disabled="submitting">
 <span v-if="submitting">{{ t('form.saving') }}</span>
 <span v-else>✓ {{ t('form.save') }}</span>
 </button>
 </div>

 <div v-if="submitError" class="error-banner" style="margin-top:1rem">
 {{ submitError }}
 </div>
 </form>
 </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useConflictStore } from '../stores/conflictStore.js'
import { useCountryStore } from '../stores/countryStore.js'
import { useI18n } from '../composables/useI18n.js'
import AppCard from '../components/AppCard.vue'
import CountryFlag from '../components/CountryFlag.vue'
import LoadingSpinner from '../components/LoadingSpinner.vue'

const route = useRoute()
const router = useRouter()
const conflictStore = useConflictStore()
const countryStore = useCountryStore()
const { t } = useI18n()

const isEdit = computed(() => !!route.params.id)
const loading = ref(false)
const submitting = ref(false)
const submitError = ref('')

const statusOptions = ['ACTIVE', 'FROZEN', 'ENDED']

const form = reactive({
 name: '',
 status: '',
 startDate: '',
 description: '',
 countryCodes: []
})

const errors = reactive({ name: '' })

onMounted(async () => {
 // Always load countries for the checkbox list
 if (!countryStore.countries.length) {
 await countryStore.fetchAll()
 }

 // If editing, populate the form
 if (isEdit.value) {
 loading.value = true
 await conflictStore.fetchById(route.params.id)
 const c = conflictStore.currentConflict
 if (c) {
 form.name = c.name || ''
 form.status = c.status || ''
 form.startDate = c.startDate || ''
 form.description = c.description || ''
 // currentConflict is ConflictDetailDTO (has countries Set<CountryDTO>)
 form.countryCodes = c.countries?.map(co => co.code) || []
 }
 loading.value = false
 }
})

function validate() {
 errors.name = form.name.trim() ? '' : 'Name is required'
 return !errors.name
}

async function handleSubmit() {
 if (!validate()) return
 submitting.value = true
 submitError.value = ''

 const dto = {
 name: form.name.trim(),
 status: form.status || null,
 startDate: form.startDate || null,
 description: form.description.trim() || null,
 countryCodes: form.countryCodes
 }

 try {
 if (isEdit.value) {
 await conflictStore.update(route.params.id, dto)
 } else {
 await conflictStore.create(dto)
 }
 router.push('/conflicts')
 } catch (err) {
 submitError.value = err.response?.data?.message || err.message
 } finally {
 submitting.value = false
 }
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
 transition: color 0.2s;
}
.back-link:hover { color: var(--text-primary); }

.conflict-form { display: flex; flex-direction: column; gap: 1.25rem; max-width: 720px; }

.form-section-title { font-family: 'Space Grotesk', sans-serif; font-weight: 600; color: var(--text-primary); }

.form-control--error { border-color: var(--status-active); }

.field-error { color: #f87171; font-size: 0.8rem; margin-top: 0.25rem; display: block; }

.countries-card { }

.country-checkboxes {
 display: grid;
 grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
 gap: 0.5rem;
}

.country-check {
 display: flex;
 align-items: center;
 gap: 0.5rem;
 padding: 0.5rem 0.75rem;
 border: 1px solid var(--border);
 border-radius: 8px;
 cursor: pointer;
 transition: all 0.2s;
 position: relative;
 background: var(--bg-input);
}
.country-check:hover { border-color: var(--accent); }
.country-check--selected { border-color: var(--accent); background: rgba(249,115,22,0.1); }

.check-name { font-size: 0.82rem; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.check-tick { color: var(--accent); font-size: 0.8rem; font-weight: 700; }

.sr-only { position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px; overflow: hidden; clip: rect(0,0,0,0); white-space: nowrap; border: 0; }

.form-actions { display: flex; gap: 0.75rem; justify-content: flex-end; }

.empty-mini { color: var(--text-muted); font-size: 0.88rem; padding: 1rem 0; }
</style>
