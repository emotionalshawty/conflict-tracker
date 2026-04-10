<template>
 <nav class="navbar">
 <div class="nav-container">
 <!-- Logo -->
 <router-link to="/" class="nav-brand">
 <span class="brand-text">{{ t('home.title') }}</span>
 </router-link>

 <!-- Desktop links -->
 <ul class="nav-links">
 <li v-for="link in navLinks" :key="link.to">
 <router-link :to="link.to" class="nav-link" active-class="nav-link--active">
 {{ t(link.label) }}
 </router-link>
 </li>
 </ul>

 <!-- Right section: language selector + mobile toggle -->
 <div class="nav-right">
 <div class="lang-selector">
 <button
 v-for="lang in languages"
 :key="lang.code"
 class="lang-btn"
 :class="{ 'lang-btn--active': uiStore.locale === lang.code }"
 @click="uiStore.setLocale(lang.code)"
 >
 {{ lang.label }}
 </button>
 </div>

 <button class="mobile-toggle" @click="mobileOpen = !mobileOpen" aria-label="Toggle menu">
 <span class="hamburger" :class="{ open: mobileOpen }"></span>
 </button>
 </div>
 </div>

 <!-- Mobile menu -->
 <div v-if="mobileOpen" class="mobile-menu">
 <router-link
 v-for="link in navLinks"
 :key="link.to"
 :to="link.to"
 class="mobile-link"
 active-class="mobile-link--active"
 @click="mobileOpen = false"
 >
 {{ t(link.label) }}
 </router-link>
 </div>
 </nav>
</template>

<script setup>
import { ref } from 'vue'
import { useUiStore } from '../stores/uiStore.js'
import { useI18n } from '../composables/useI18n.js'

const uiStore = useUiStore()
const { t } = useI18n()
const mobileOpen = ref(false)

const navLinks = [
 { to: '/', label: 'nav.home' },
 { to: '/conflicts', label: 'nav.conflicts' },
 { to: '/countries', label: 'nav.countries' },
 { to: '/events', label: 'nav.events' }
]

const languages = [
 { code: 'en', label: 'EN' },
 { code: 'ca', label: 'CA' }
]
</script>

<style scoped>
.navbar {
 background: var(--bg-card);
 border-bottom: 1px solid var(--border);
 position: sticky;
 top: 0;
 z-index: 100;
 backdrop-filter: blur(10px);
}

.nav-container {
 max-width: 1280px;
 margin: 0 auto;
 padding: 0 1.5rem;
 display: flex;
 align-items: center;
 height: 64px;
 gap: 2rem;
}

.nav-brand {
 display: flex;
 align-items: center;
 gap: 0.5rem;
 text-decoration: none;
 color: var(--text-primary);
 font-family: 'Space Grotesk', sans-serif;
 font-weight: 700;
 font-size: 1rem;
 white-space: nowrap;
}

.nav-links {
 display: flex;
 list-style: none;
 gap: 0.25rem;
 flex: 1;
 padding: 0;
 margin: 0;
}

.nav-link {
 display: flex;
 align-items: center;
 gap: 0.4rem;
 padding: 0.5rem 0.875rem;
 border-radius: 8px;
 text-decoration: none;
 color: var(--text-muted);
 font-size: 0.9rem;
 font-weight: 500;
 transition: all 0.2s;
}

.nav-link:hover { background: var(--bg-hover); color: var(--text-primary); }
.nav-link--active { background: var(--bg-hover); color: var(--accent); }

.nav-right {
 display: flex;
 align-items: center;
 gap: 0.75rem;
 margin-left: auto;
}

.lang-selector {
 display: flex;
 gap: 4px;
 background: var(--bg-input);
 padding: 3px;
 border-radius: 8px;
}

.lang-btn {
 padding: 4px 10px;
 border: none;
 background: transparent;
 color: var(--text-muted);
 font-size: 0.8rem;
 font-weight: 600;
 border-radius: 6px;
 cursor: pointer;
 transition: all 0.2s;
}

.lang-btn:hover { color: var(--text-primary); }
.lang-btn--active { background: var(--accent); color: #fff; }

.mobile-toggle {
 display: none;
 background: none;
 border: none;
 cursor: pointer;
 padding: 0.5rem;
}

.hamburger, .hamburger::before, .hamburger::after {
 display: block;
 width: 22px;
 height: 2px;
 background: var(--text-muted);
 border-radius: 2px;
 transition: all 0.3s;
}

.hamburger { position: relative; }
.hamburger::before, .hamburger::after {
 content: '';
 position: absolute;
}
.hamburger::before { top: -6px; }
.hamburger::after { top: 6px; }

.mobile-menu {
 display: flex;
 flex-direction: column;
 padding: 0.5rem 1.5rem 1rem;
 border-top: 1px solid var(--border);
 gap: 0.25rem;
}

.mobile-link {
 padding: 0.75rem 1rem;
 border-radius: 8px;
 text-decoration: none;
 color: var(--text-muted);
 font-weight: 500;
 transition: all 0.2s;
}

.mobile-link:hover { background: var(--bg-hover); color: var(--text-primary); }
.mobile-link--active { color: var(--accent); }

@media (max-width: 768px) {
 .nav-links { display: none; }
 .mobile-toggle { display: block; }
 .brand-text { display: none; }
}
</style>
