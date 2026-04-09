<!-- Confirmation modal with slot for custom message -->
<template>
 <Teleport to="body">
 <div v-if="modelValue" class="modal-overlay" @click.self="$emit('update:modelValue', false)">
 <div class="modal" role="dialog" aria-modal="true">
 <div class="modal__icon"></div>
 <h3 class="modal__title">
 <slot name="title">{{ t('common.confirm') }}</slot>
 </h3>
 <p class="modal__body">
 <slot>{{ message }}</slot>
 </p>
 <div class="modal__actions">
 <button class="btn btn--ghost" @click="$emit('update:modelValue', false)">
 {{ t('common.cancel') }}
 </button>
 <button class="btn btn--danger" @click="onConfirm">
 {{ t('common.confirm') }}
 </button>
 </div>
 </div>
 </div>
 </Teleport>
</template>

<script setup>
import { useI18n } from '../composables/useI18n.js'

const { t } = useI18n()

defineProps({
 modelValue: { type: Boolean, default: false },
 message: { type: String, default: '' }
})

const emit = defineEmits(['update:modelValue', 'confirm'])

function onConfirm() {
 emit('confirm')
 emit('update:modelValue', false)
}
</script>

<style scoped>
.modal-overlay {
 position: fixed;
 inset: 0;
 background: rgba(0,0,0,0.7);
 display: flex;
 align-items: center;
 justify-content: center;
 z-index: 1000;
 backdrop-filter: blur(4px);
 animation: fadeIn 0.15s ease;
}

.modal {
 background: var(--bg-card);
 border: 1px solid var(--border);
 border-radius: 16px;
 padding: 2rem;
 max-width: 420px;
 width: 90%;
 text-align: center;
 animation: slideUp 0.2s ease;
}

.modal__icon { font-size: 2.5rem; margin-bottom: 0.75rem; }
.modal__title { font-family: 'Space Grotesk', sans-serif; font-size: 1.2rem; margin: 0 0 0.75rem; color: var(--text-primary); }
.modal__body { color: var(--text-muted); font-size: 0.95rem; line-height: 1.6; margin: 0 0 1.5rem; }

.modal__actions {
 display: flex;
 gap: 0.75rem;
 justify-content: center;
}

@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideUp { from { transform: translateY(20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
</style>
