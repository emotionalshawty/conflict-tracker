import { defineStore } from 'pinia'
import en from '../i18n/locales/en.js'
import ca from '../i18n/locales/ca.js'

const locales = { en, ca }

export const useUiStore = defineStore('ui', {
 state: () => ({
 locale: localStorage.getItem('locale') || 'en'
 }),

 getters: {
 messages: (state) => locales[state.locale] || locales.en
 },

 actions: {
 setLocale(locale) {
 if (locales[locale]) {
 this.locale = locale
 localStorage.setItem('locale', locale)
 }
 }
 }
})
