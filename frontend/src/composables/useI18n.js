import { useUiStore } from '../stores/uiStore.js'

/**
 * Composable for reactive i18n translations.
 * `t` is a plain function so it works in both templates and script setup.
 */
export function useI18n() {
 const uiStore = useUiStore()

 function t(key) {
 const keys = key.split('.')
 let val = uiStore.messages
 for (const k of keys) {
 val = val?.[k]
 }
 return val !== undefined ? val : key
 }

 return { t }
}
