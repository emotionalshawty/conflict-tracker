import { defineStore } from 'pinia'
import { countriesApi } from '../services/api.js'
import { fallbackCountries } from '../data/countries.js'

const iso3ToIso2 = {
 USA: 'US', CAN: 'CA', MEX: 'MX', BRA: 'BR', ARG: 'AR', CHL: 'CL', COL: 'CO', PER: 'PE',
 GBR: 'GB', FRA: 'FR', DEU: 'DE', ESP: 'ES', ITA: 'IT', PRT: 'PT', NLD: 'NL', BEL: 'BE',
 CHE: 'CH', SWE: 'SE', NOR: 'NO', POL: 'PL', UKR: 'UA', TUR: 'TR', RUS: 'RU', EGY: 'EG',
 MAR: 'MA', NGA: 'NG', ZAF: 'ZA', IND: 'IN', PAK: 'PK', CHN: 'CN', JPN: 'JP', KOR: 'KR',
 AUS: 'AU', NZL: 'NZ', IDN: 'ID', THA: 'TH', VNM: 'VN', SAU: 'SA', IRN: 'IR', ISR: 'IL'
}

function normalizeCountryCode(code) {
 const raw = String(code || '').trim().toUpperCase()
 if (raw.length === 2) return raw
 if (raw.length === 3 && iso3ToIso2[raw]) return iso3ToIso2[raw]
 return raw
}

export const useCountryStore = defineStore('countries', {
 state: () => ({
 countries: [],
 loading: false,
 error: null,
 usingFallback: false
 }),

 actions: {
 async fetchAll() {
 this.loading = true
 this.error = null
 this.usingFallback = false
 try {
 const { data } = await countriesApi.getAll()
 const normalized = Array.isArray(data)
 ? data
 .filter(country => country?.code && country?.name)
 .map(country => ({
 code: normalizeCountryCode(country.code),
 name: String(country.name)
 }))
 .filter(country => country.code.length === 2)
 .sort((a, b) => a.name.localeCompare(b.name))
 : []

 if (normalized.length) {
 this.countries = normalized
 return
 }

 this.countries = [...fallbackCountries]
 this.usingFallback = true
 this.error = 'No countries returned by API. Using fallback list.'
 } catch (err) {
 this.countries = [...fallbackCountries]
 this.usingFallback = true
 this.error = err.response?.data?.message || err.message
 } finally {
 this.loading = false
 }
 }
 }
})
