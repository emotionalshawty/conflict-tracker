<!-- Status badge with dynamic class binding based on conflict status -->
<template>
 <span
 class="badge"
 :class="`badge--${status.toLowerCase()}`"
 :title="label"
 >
 <span class="badge__dot"></span>
 {{ label }}
 </span>
</template>

<script setup>
import { computed } from 'vue'
import { useI18n } from '../composables/useI18n.js'

const props = defineProps({
 status: {
 type: String,
 required: true
 }
})

const { t } = useI18n()
const label = computed(() => t(`status.${props.status}`))
</script>

<style scoped>
.badge {
 display: inline-flex;
 align-items: center;
 gap: 6px;
 padding: 3px 10px;
 border-radius: 999px;
 font-size: 0.78rem;
 font-weight: 600;
 letter-spacing: 0.03em;
 text-transform: uppercase;
}

.badge__dot {
 width: 6px;
 height: 6px;
 border-radius: 50%;
 flex-shrink: 0;
}

/* ACTIVE = red */
.badge--active {
 background: rgba(239, 68, 68, 0.15);
 color: #f87171;
 border: 1px solid rgba(239, 68, 68, 0.3);
}
.badge--active .badge__dot { background: #ef4444; box-shadow: 0 0 6px #ef4444; animation: pulse 1.5s infinite; }

/* FROZEN = blue */
.badge--frozen {
 background: rgba(96, 165, 250, 0.15);
 color: #93c5fd;
 border: 1px solid rgba(96, 165, 250, 0.3);
}
.badge--frozen .badge__dot { background: #60a5fa; }

/* ENDED = green */
.badge--ended {
 background: rgba(74, 222, 128, 0.15);
 color: #86efac;
 border: 1px solid rgba(74, 222, 128, 0.3);
}
.badge--ended .badge__dot { background: #4ade80; }

@keyframes pulse {
 0%, 100% { opacity: 1; }
 50% { opacity: 0.4; }
}
</style>
