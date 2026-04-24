import axios from 'axios'

const api = axios.create({
 baseURL: (import.meta.env.VITE_API_URL || '') + '/api/v1',
 timeout: 8000,
 headers: { 'Content-Type': 'application/json' }
})

export const conflictsApi = {
 getAll: (status) => api.get('/conflicts', { params: status ? { status } : {} }),
 getById: (id) => api.get(`/conflicts/${id}`),
 create: (data) => api.post('/conflicts', data),
 update: (id, data) => api.put(`/conflicts/${id}`, data),
 delete: (id) => api.delete(`/conflicts/${id}`)
}

export const countriesApi = {
 getAll: () => api.get('/countries'),
 getConflicts: (code) => api.get(`/countries/${code}/conflicts`)
}

export const eventsApi = {
 getAll: (conflictId) => api.get('/events', { params: conflictId ? { conflictId } : {} })
}
