import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ConflictsView from '../views/ConflictsView.vue'
import ConflictDetailView from '../views/ConflictDetailView.vue'
import ConflictFormView from '../views/ConflictFormView.vue'
import CountriesView from '../views/CountriesView.vue'
import CountryConflictsView from '../views/CountryConflictsView.vue'
import EventsView from '../views/EventsView.vue'
import ConflictMapView from '../views/ConflictMapView.vue'

const routes = [
 { path: '/', component: HomeView },
 { path: '/conflicts', component: ConflictsView },
 { path: '/conflicts/new', component: ConflictFormView },
 { path: '/conflicts/:id', component: ConflictDetailView },
 { path: '/conflicts/:id/edit', component: ConflictFormView, props: true },
 { path: '/countries', component: CountriesView },
 { path: '/countries/:code/conflicts', component: CountryConflictsView },
 { path: '/map', component: ConflictMapView },
 { path: '/events', component: EventsView },
 { path: '/:pathMatch(.*)*', redirect: '/' }
]

export default createRouter({
 history: createWebHistory(),
 routes,
 scrollBehavior: () => ({ top: 0 })
})
