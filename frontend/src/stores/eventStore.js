import { defineStore } from 'pinia'
import { eventsApi } from '../services/api.js'

export const useEventStore = defineStore('events', {
 state: () => ({
 events: [],
 loading: false,
 error: null
 }),

 actions: {
 async fetchAll(conflictId = null) {
 this.loading = true
 this.error = null
 try {
 const { data } = await eventsApi.getAll(conflictId)
 this.events = data
 } catch (err) {
 this.error = err.response?.data?.message || err.message
 } finally {
 this.loading = false
 }
 }
 }
})
