<template>
 <div class="page">
 <div class="page-header">
 <div>
 <h1 class="page-title">{{ t('map.title') }}</h1>
 <p class="page-subtitle">{{ t('map.subtitle') }}</p>
 </div>
 <div class="map-stats">
 <span class="map-stat">{{ markers.length }} {{ t('map.countriesWithConflicts') }}</span>
 <span class="map-stat">{{ conflictStore.conflicts.length }} {{ t('conflicts.title').toLowerCase() }}</span>
 </div>
 </div>

 <LoadingSpinner v-if="conflictStore.loading" :message="t('conflicts.loading')" />

 <div v-else-if="conflictStore.error" class="error-banner">
 {{ conflictStore.error }}
 <button class="btn btn--ghost btn--sm" @click="conflictStore.fetchAll()">{{ t('common.retry') }}</button>
 </div>

 <div v-else class="map-layout">
 <section class="map-panel">
 <div
 ref="mapViewport"
 class="world-map"
 @wheel.prevent="onWheel"
 @mousedown="startDrag"
 @mousemove="onDrag"
 @mouseup="stopDrag"
 @mouseleave="stopDrag"
 >
 <div ref="mapContent" class="map-content" :style="mapTransformStyle">
 <img
 v-if="!mapImageError"
 :src="worldMapSrc"
 class="world-image"
 :alt="t('map.imageAlt')"
 draggable="false"
 @error="mapImageError = true"
 />
 <div v-else class="world-image-fallback" aria-hidden="true"></div>

 <svg
 v-if="activeMarker && activeConnections.length"
 class="map-links"
 viewBox="0 0 100 100"
 preserveAspectRatio="none"
 aria-hidden="true"
 >
 <line
 v-for="link in activeConnections"
 :key="link.code"
 class="map-link-line"
 :x1="activeMarker.x"
 :y1="activeMarker.y"
 :x2="link.target.x"
 :y2="link.target.y"
 :stroke-width="linkStrokeWidth(link.count)"
 />
 </svg>

 <button
 v-for="marker in markers"
 :key="marker.code"
 class="map-marker"
 :class="{ 'map-marker--active': activeCode === marker.code }"
 :style="{ left: `${marker.x}%`, top: `${marker.y}%` }"
 :title="`${marker.name}: ${marker.conflicts.length}`"
 @click.stop="activeCode = marker.code"
 >
 <span class="map-marker__dot"></span>
 <span class="map-marker__label">{{ marker.code }}</span>
 </button>
 </div>

 <button class="map-reset" @click="resetView">{{ t('common.retry') }}</button>
 </div>
 <p class="map-help">{{ t('map.help') }}</p>
 </section>

 <aside class="list-panel">
 <h2 class="list-title">{{ activeMarker ? activeMarker.name : t('map.selectCountry') }}</h2>
 <p class="list-subtitle">
 <template v-if="activeMarker">
 {{ activeMarker.conflicts.length }} {{ t('conflicts.title').toLowerCase() }}
 </template>
 <template v-else>
 {{ t('map.selectCountryHint') }}
 </template>
 </p>

 <div v-if="!activeMarker" class="empty-state">
 <p class="empty-state__text">{{ t('map.selectCountryHint') }}</p>
 </div>

 <div v-else-if="!activeMarker.conflicts.length" class="empty-state">
 <p class="empty-state__text">{{ t('map.noConflictsForMarker') }}</p>
 </div>

 <div v-else class="conflicts-list">
 <router-link
 v-for="conflict in activeMarker.conflicts"
 :key="conflict.id"
 :to="`/conflicts/${conflict.id}`"
 class="conflict-item"
 >
 <StatusBadge :status="conflict.status" />
 <div class="conflict-item__content">
 <div class="conflict-item__title">{{ conflict.name }}</div>
 <div class="conflict-item__meta">{{ t('conflicts.startDate') }}: {{ formatDate(conflict.startDate) }}</div>
 </div>
 </router-link>
 </div>
 </aside>
 </div>
 </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useConflictStore } from '../stores/conflictStore.js'
import { useI18n } from '../composables/useI18n.js'
import { useCountryStore } from '../stores/countryStore.js'
import { countryCoordinates } from '../data/countryCoordinates.js'
import LoadingSpinner from '../components/LoadingSpinner.vue'
import StatusBadge from '../components/StatusBadge.vue'

const conflictStore = useConflictStore()
const countryStore = useCountryStore()
const { t } = useI18n()

const activeCode = ref('')
const mapImageError = ref(false)
const worldMapSrc = 'https://upload.wikimedia.org/wikipedia/commons/8/80/World_map_-_low_resolution.svg'
const mapViewport = ref(null)
const scale = ref(1)
const offsetX = ref(0)
const offsetY = ref(0)
const isDragging = ref(false)
const dragStartX = ref(0)
const dragStartY = ref(0)
const dragOriginX = ref(0)
const dragOriginY = ref(0)

onMounted(async () => {
 if (!countryStore.countries.length && !countryStore.loading) {
 await countryStore.fetchAll()
 }
 if (!conflictStore.conflicts.length && !conflictStore.loading) {
 await conflictStore.fetchAll()
 }
})

const countryNameByCode = computed(() => {
 const map = new Map()
 countryStore.countries.forEach(country => {
 map.set(country.code, country.name)
 })
 return map
})

function toMapPoint(lat, lng) {
 const x = ((lng + 180) / 360) * 100
 const y = ((90 - lat) / 180) * 100
 return { x, y }
}

const markers = computed(() => {
 const grouped = new Map()
 for (const conflict of conflictStore.conflicts) {
 const codes = conflict.countryCodes || []
 for (const codeRaw of codes) {
 const code = String(codeRaw || '').toUpperCase()
 if (!countryCoordinates[code]) continue
 if (!grouped.has(code)) {
 const coords = countryCoordinates[code]
 const point = toMapPoint(coords.lat, coords.lng)
 grouped.set(code, {
 code,
 name: countryNameByCode.value.get(code) || code,
 x: Math.max(2, Math.min(98, point.x)),
 y: Math.max(5, Math.min(95, point.y)),
 conflicts: []
 })
 }
 grouped.get(code).conflicts.push(conflict)
 }
 }

 return Array.from(grouped.values()).sort((a, b) => a.name.localeCompare(b.name))
})

const activeMarker = computed(() => markers.value.find(m => m.code === activeCode.value) || null)

const markerByCode = computed(() => {
 const map = new Map()
 markers.value.forEach(marker => {
 map.set(marker.code, marker)
 })
 return map
})

const activeConnections = computed(() => {
 if (!activeMarker.value) return []

 const connections = new Map()
 for (const conflict of activeMarker.value.conflicts) {
 const codes = (conflict.countryCodes || []).map(code => String(code || '').toUpperCase())
 for (const code of codes) {
 if (code === activeMarker.value.code) continue
 const target = markerByCode.value.get(code)
 if (!target) continue

 if (!connections.has(code)) {
 connections.set(code, {
 code,
 target,
 count: 0
 })
 }
 connections.get(code).count += 1
 }
 }

 return Array.from(connections.values()).sort((a, b) => b.count - a.count)
})

const mapTransformStyle = computed(() => ({
 transform: `translate(${offsetX.value}px, ${offsetY.value}px) scale(${scale.value})`
}))

watch(markers, (next) => {
 if (!next.length) {
 activeCode.value = ''
 return
 }
 if (!next.some(marker => marker.code === activeCode.value)) {
 activeCode.value = next[0].code
 }
}, { immediate: true })

function formatDate(dateStr) {
 if (!dateStr) return '—'
 return new Date(dateStr).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' })
}

function linkStrokeWidth(count) {
 return 0.22 + Math.min(count, 5) * 0.07
}

function clampOffsets() {
 const viewport = mapViewport.value
 if (!viewport) return
 const vw = viewport.clientWidth
 const vh = viewport.clientHeight
 const maxX = ((scale.value - 1) * vw) / 2
 const maxY = ((scale.value - 1) * vh) / 2

 offsetX.value = Math.max(-maxX, Math.min(maxX, offsetX.value))
 offsetY.value = Math.max(-maxY, Math.min(maxY, offsetY.value))
}

function zoomAt(clientX, clientY, nextScale) {
 const viewport = mapViewport.value
 if (!viewport) return

 const rect = viewport.getBoundingClientRect()
 const cx = clientX - rect.left - rect.width / 2
 const cy = clientY - rect.top - rect.height / 2

 const prevScale = scale.value
 const target = Math.max(1, Math.min(4, nextScale))
 if (target === prevScale) return

 const ratio = target / prevScale
 offsetX.value = (offsetX.value - cx) * ratio + cx
 offsetY.value = (offsetY.value - cy) * ratio + cy
 scale.value = target
 clampOffsets()
}

function onWheel(event) {
 const delta = event.deltaY > 0 ? -0.15 : 0.15
 zoomAt(event.clientX, event.clientY, scale.value + delta)
}

function startDrag(event) {
 if (scale.value <= 1) return
 isDragging.value = true
 dragStartX.value = event.clientX
 dragStartY.value = event.clientY
 dragOriginX.value = offsetX.value
 dragOriginY.value = offsetY.value
}

function onDrag(event) {
 if (!isDragging.value) return
 offsetX.value = dragOriginX.value + (event.clientX - dragStartX.value)
 offsetY.value = dragOriginY.value + (event.clientY - dragStartY.value)
 clampOffsets()
}

function stopDrag() {
 isDragging.value = false
}

function resetView() {
 scale.value = 1
 offsetX.value = 0
 offsetY.value = 0
}
</script>

<style scoped>
.page-header {
 display: flex;
 align-items: flex-start;
 justify-content: space-between;
 gap: 1rem;
 margin-bottom: 1.25rem;
}

.page-subtitle {
 color: var(--text-muted);
 margin-top: 0.25rem;
 margin-bottom: 0;
 font-size: 0.9rem;
}

.map-stats {
 display: flex;
 gap: 0.5rem;
 flex-wrap: wrap;
 justify-content: flex-end;
}

.map-stat {
 background: var(--bg-input);
 border: 1px solid var(--border);
 color: var(--text-muted);
 padding: 0.35rem 0.55rem;
 border-radius: 999px;
 font-size: 0.76rem;
}

.map-layout {
 display: grid;
 grid-template-columns: 2fr 1fr;
 gap: 1rem;
 align-items: start;
}

.map-panel {
 background: var(--bg-card);
 border: 1px solid var(--border);
 border-radius: 12px;
 padding: 0.75rem;
}

.world-map {
 position: relative;
 border-radius: 12px;
 overflow: hidden;
 border: 1px solid rgba(255,255,255,0.08);
 min-height: 480px;
 cursor: grab;
 user-select: none;
}

.world-map:active {
 cursor: grabbing;
}

.map-content {
 position: absolute;
 inset: 0;
 transform-origin: center center;
}

.map-links {
 position: absolute;
 inset: 0;
 width: 100%;
 height: 100%;
 pointer-events: none;
 z-index: 2;
}

.map-link-line {
 stroke: rgba(249, 115, 22, 0.75);
 stroke-linecap: round;
 stroke-dasharray: 0.9 0.5;
 filter: drop-shadow(0 0 4px rgba(249, 115, 22, 0.45));
}

.world-image,
.world-image-fallback {
 width: 100%;
 height: 100%;
 display: block;
 min-height: 480px;
}

.world-image {
 object-fit: cover;
 background: #0f172a;
}

.world-image-fallback {
 background:
 radial-gradient(circle at 20% 30%, rgba(56, 189, 248, 0.2), transparent 35%),
 radial-gradient(circle at 75% 20%, rgba(34, 197, 94, 0.16), transparent 30%),
 linear-gradient(135deg, #0b1220 0%, #111f36 55%, #0b1627 100%);
}

.map-marker {
 position: absolute;
 transform: translate(-50%, -50%);
 background: rgba(15, 23, 42, 0.8);
 border: 1px solid rgba(249,115,22,0.45);
 color: #f8fafc;
 border-radius: 8px;
 padding: 0.1rem 0.35rem;
 display: inline-flex;
 align-items: center;
 gap: 0.25rem;
 font-size: 0.68rem;
 cursor: pointer;
 transition: all 0.2s;
 z-index: 3;
}

.map-reset {
 position: absolute;
 right: 0.75rem;
 top: 0.75rem;
 z-index: 5;
 border: 1px solid var(--border);
 background: rgba(15, 23, 42, 0.86);
 color: var(--text-primary);
 border-radius: 8px;
 padding: 0.3rem 0.5rem;
 font-size: 0.75rem;
 cursor: pointer;
}

.map-reset:hover {
 border-color: rgba(249,115,22,0.6);
}

.map-marker:hover,
.map-marker--active {
 background: rgba(249,115,22,0.2);
 border-color: rgba(249,115,22,0.9);
}

.map-marker__dot {
 width: 7px;
 height: 7px;
 border-radius: 50%;
 background: var(--accent);
 box-shadow: 0 0 0 4px rgba(249,115,22,0.15);
}

.map-help {
 margin: 0.75rem 0 0;
 color: var(--text-muted);
 font-size: 0.82rem;
}

.list-panel {
 background: var(--bg-card);
 border: 1px solid var(--border);
 border-radius: 12px;
 padding: 1rem;
 min-height: 560px;
}

.list-title {
 margin: 0;
 font-size: 1.05rem;
 color: var(--text-primary);
}

.list-subtitle {
 margin: 0.35rem 0 1rem;
 color: var(--text-muted);
 font-size: 0.85rem;
}

.conflicts-list {
 display: flex;
 flex-direction: column;
 gap: 0.6rem;
}

.conflict-item {
 display: flex;
 gap: 0.6rem;
 align-items: flex-start;
 border: 1px solid var(--border);
 border-radius: 10px;
 padding: 0.65rem;
 text-decoration: none;
 color: inherit;
 background: var(--bg-input);
 transition: border-color 0.2s, transform 0.2s;
}

.conflict-item:hover {
 border-color: rgba(249,115,22,0.45);
 transform: translateY(-1px);
}

.conflict-item__content {
 min-width: 0;
}

.conflict-item__title {
 font-size: 0.9rem;
 color: var(--text-primary);
 line-height: 1.35;
}

.conflict-item__meta {
 font-size: 0.78rem;
 color: var(--text-muted);
 margin-top: 0.2rem;
}

@media (max-width: 980px) {
 .map-layout {
 grid-template-columns: 1fr;
 }

 .list-panel {
 min-height: auto;
 }

 .world-map,
 .world-image,
 .world-image-fallback {
 min-height: 360px;
 }
}
</style>
