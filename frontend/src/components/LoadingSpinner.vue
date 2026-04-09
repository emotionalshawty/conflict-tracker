<template>
 <div class="spinner-wrapper" :class="`spinner--${size}`">
 <div class="spinner"></div>
 <p v-if="message" class="spinner-msg">{{ message }}</p>
 </div>
</template>

<script setup>
defineProps({
 message: { type: String, default: '' },
 size: { type: String, default: 'md' }
})
</script>

<style scoped>
.spinner-wrapper {
 display: flex;
 flex-direction: column;
 align-items: center;
 justify-content: center;
 gap: 1rem;
 padding: 3rem;
}

.spinner {
 border-radius: 50%;
 border: 3px solid var(--border);
 border-top-color: var(--accent);
 animation: spin 0.7s linear infinite;
}

.spinner--sm .spinner { width: 24px; height: 24px; }
.spinner--md .spinner { width: 40px; height: 40px; }
.spinner--lg .spinner { width: 64px; height: 64px; }

.spinner-msg {
 color: var(--text-muted);
 font-size: 0.9rem;
 margin: 0;
}

@keyframes spin {
 to { transform: rotate(360deg); }
}
</style>
