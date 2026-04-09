import { defineStore } from 'pinia'
import { countriesApi } from '../services/api.js'

export const useCountryStore = defineStore('countries', {
 state: () => ({
 countries: [],
 loading: false,
 error: null
 }),

 actions: {
 async fetchAll() {
 this.loading = true
 this.error = null
 try {
 const { data } = await countriesApi.getAll()
 this.countries = data
 } catch (err) {
 this.error = err.response?.data?.message || err.message
 } finally {
 this.loading = false
 }
 },

 async create(dto) {
 const { data } = await countriesApi.create(dto)
 this.countries.push(data)
 return data
 },

 async remove(id) {
 await countriesApi.delete(id)
 this.countries = this.countries.filter(c => c.id !== Number(id))
 }
 }
})
