<!-- Country flag using flagcdn.com API with fallback -->
<template>
 <span class="flag-wrapper" :title="country">
 <img
 v-if="!error"
 :src="flagUrl"
 :alt="`${country} flag`"
 class="flag-img"
 :class="`flag--${size}`"
 @error="error = true"
 loading="lazy"
 />
 <span v-else class="flag-fallback" :class="`flag--${size}`">
 {{ code.toUpperCase() }}
 </span>
 </span>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
 code: { type: String, required: true },
 country: { type: String, default: '' },
 size: { type: String, default: 'md' } // 'sm' | 'md' | 'lg'
})

const error = ref(false)

// flagcdn.com provides country flag images by 2-letter ISO code (lowercase)
const flagUrl = computed(() =>
 `https://flagcdn.com/40x30/${props.code.toLowerCase()}.png`
)
</script>

<style scoped>
.flag-wrapper {
 display: inline-flex;
 align-items: center;
}

.flag-img {
 border-radius: 3px;
 object-fit: cover;
 display: block;
 box-shadow: 0 1px 4px rgba(0,0,0,0.3);
}

.flag-fallback {
 display: inline-flex;
 align-items: center;
 justify-content: center;
 background: var(--bg-input);
 color: var(--text-muted);
 border-radius: 3px;
 font-size: 0.65rem;
 font-weight: 700;
 border: 1px solid var(--border);
}

.flag--sm { width: 20px; height: 15px; font-size: 0.5rem; }
.flag--md { width: 32px; height: 24px; }
.flag--lg { width: 48px; height: 36px; }
</style>
