<template>
 <div class="page">
 <!-- Page header -->
 <div class="page-header">
 <div>
 <h1 class="page-title">{{ t('conflicts.title') }}</h1>
 <p class="page-subtitle">
 {{ filteredConflicts.length }} {{ t('conflicts.title').toLowerCase() }}
 </p>
 </div>
 <router-link to="/conflicts/new" class="btn btn--primary">
 {{ t('conflicts.new') }}
 </router-link>
 </div>

 <!-- Toolbar: search (v-model) + status filter (v-model) -->
 <div class="toolbar">
 <input
 v-model="search"
 type="text"
 class="form-control toolbar__search"
 :placeholder="t('conflicts.search')"
 />
 <select v-model="statusFilter" class="form-control toolbar__filter">
 <option value="">{{ t('conflicts.filterStatus') }}</option>
 <option v-for="s in statusOptions" :key="s" :value="s">
 {{ t(`status.${s}`) }}
 </option>
 </select>
 </div>

 <!-- Loading state -->
 <LoadingSpinner v-if="conflictStore.loading" :message="t('conflicts.loading')" />

 <!-- Error state -->
 <div v-else-if="conflictStore.error" class="error-banner">
 {{ conflictStore.error }}
 <button class="btn btn--ghost btn--sm" @click="conflictStore.fetchAll()">{{ t('common.retry') }}</button>
 </div>

 <!-- Empty state -->
 <div v-else-if="filteredConflicts.length === 0" class="empty-state">
 <p class="empty-state__text">{{ t('conflicts.noResults') }}</p>
 </div>

 <!-- Conflict grid (v-for) -->
 <div v-else class="grid grid--auto">
 <ConflictCard
 v-for="c in filteredConflicts"
 :key="c.id"
 :conflict="c"
 @delete="onDeleteRequest"
 @edit="onEdit"
 />
 </div>

 <!-- Confirm delete modal -->
 <ConfirmModal
 v-model="showModal"
 :message="t('conflicts.confirmDelete')"
 @confirm="confirmDelete"
 />
 </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useConflictStore } from '../stores/conflictStore.js'
import { useI18n } from '../composables/useI18n.js'
import ConflictCard from '../components/ConflictCard.vue'
import LoadingSpinner from '../components/LoadingSpinner.vue'
import ConfirmModal from '../components/ConfirmModal.vue'

const conflictStore = useConflictStore()
const router = useRouter()
const { t } = useI18n()

// v-model bindings for search and filter
const search = ref('')
const statusFilter = ref('')
const showModal = ref(false)
const pendingDeleteId = ref(null)

const statusOptions = ['ACTIVE', 'FROZEN', 'ENDED']

onMounted(() => conflictStore.fetchAll())

// Computed: real-time search + status filter
const filteredConflicts = computed(() => {
 const q = search.value.toLowerCase().trim()
 return conflictStore.conflicts.filter(c => {
 const matchSearch =
 !q ||
 c.name.toLowerCase().includes(q) ||
 c.description?.toLowerCase().includes(q) ||
 c.countryCodes?.some(code => code.toLowerCase().includes(q))
 const matchStatus = !statusFilter.value || c.status === statusFilter.value
 return matchSearch && matchStatus
 })
})

function onDeleteRequest(id) {
 pendingDeleteId.value = id
 showModal.value = true
}

async function confirmDelete() {
 if (pendingDeleteId.value) {
 await conflictStore.remove(pendingDeleteId.value)
 pendingDeleteId.value = null
 }
}

function onEdit(id) {
 router.push(`/conflicts/${id}/edit`)
}
</script>

<style scoped>
.page-header {
 display: flex;
 align-items: flex-start;
 justify-content: space-between;
 gap: 1rem;
 margin-bottom: 1.75rem;
}

.page-subtitle { color: var(--text-muted); font-size: 0.9rem; margin: 0.25rem 0 0; }

.toolbar {
 display: flex;
 gap: 0.75rem;
 margin-bottom: 2rem;
 flex-wrap: wrap;
}

.toolbar__search { flex: 1; min-width: 220px; }
.toolbar__filter { width: 180px; flex-shrink: 0; }

@media (max-width: 600px) {
 .page-header { flex-direction: column; }
 .toolbar__filter { width: 100%; }
}
</style>
