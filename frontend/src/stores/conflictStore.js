import { defineStore } from 'pinia'
import { conflictsApi } from '../services/api.js'

export const useConflictStore = defineStore('conflicts', {
 state: () => ({
 conflicts: [],
 currentConflict: null,
 loading: false,
 error: null
 }),

 getters: {
 activeConflicts: (state) => state.conflicts.filter(c => c.status === 'ACTIVE'),
 frozenConflicts: (state) => state.conflicts.filter(c => c.status === 'FROZEN'),
 endedConflicts: (state) => state.conflicts.filter(c => c.status === 'ENDED')
 },

 actions: {
 async fetchAll(status = null) {
 this.loading = true
 this.error = null
 try {
 const { data } = await conflictsApi.getAll(status)
 this.conflicts = data
 } catch (err) {
 this.error = err.response?.data?.message || err.message
 } finally {
 this.loading = false
 }
 },

 async fetchById(id) {
 this.loading = true
 this.error = null
 this.currentConflict = null
 try {
 const { data } = await conflictsApi.getById(id)
 this.currentConflict = data
 } catch (err) {
 this.error = err.response?.data?.message || err.message
 } finally {
 this.loading = false
 }
 },

 async create(dto) {
 const { data } = await conflictsApi.create(dto)
 this.conflicts.push(data)
 return data
 },

 async update(id, dto) {
 const { data } = await conflictsApi.update(id, dto)
 const idx = this.conflicts.findIndex(c => c.id === Number(id))
 if (idx !== -1) this.conflicts[idx] = data
 return data
 },

 async remove(id) {
 await conflictsApi.delete(id)
 this.conflicts = this.conflicts.filter(c => c.id !== Number(id))
 }
 }
})
