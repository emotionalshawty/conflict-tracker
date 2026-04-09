import axios from 'axios'

const api = axios.create({
 baseURL: '/api/v1',
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
 getById: (id) => api.get(`/countries/${id}`),
 getByCode: (code) => api.get(`/countries/code/${code}`),
 create: (data) => api.post('/countries', data),
 update: (id, data) => api.put(`/countries/${id}`, data),
 delete: (id) => api.delete(`/countries/${id}`)
}

export const eventsApi = {
 getAll: (conflictId) => api.get('/events', { params: conflictId ? { conflictId } : {} }),
 getById: (id) => api.get(`/events/${id}`),
 create: (data) => api.post('/events', data),
 update: (id, data) => api.put(`/events/${id}`, data),
 delete: (id) => api.delete(`/events/${id}`)
}

export const factionsApi = {
 getAll: (conflictId) => api.get('/factions', { params: conflictId ? { conflictId } : {} }),
 getById: (id) => api.get(`/factions/${id}`),
 create: (data) => api.post('/factions', data),
 update: (id, data) => api.put(`/factions/${id}`, data),
 delete: (id) => api.delete(`/factions/${id}`)
}
