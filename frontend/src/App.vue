<template>
 <div class="app">
 <AppNavbar />
 <main class="app-main">
 <router-view v-slot="{ Component }">
 <transition name="page" mode="out-in">
 <component :is="Component" />
 </transition>
 </router-view>
 </main>
 <footer class="app-footer">
 <p>© 2026 Global Conflict Monitor</p>
 </footer>
 </div>
</template>

<script setup>
import AppNavbar from './components/AppNavbar.vue'
</script>

<style>
/* ── CSS Design Tokens ── */
:root {
 --bg-base: #0d1117;
 --bg-card: #161b22;
 --bg-input: #1c2128;
 --bg-hover: #21262d;
 --border: #30363d;
 --text-primary: #e6edf3;
 --text-muted: #8b949e;
 --accent: #f97316;
 --accent-hover: #ea6c0a;
 --status-active: #ef4444;
 --status-frozen: #60a5fa;
 --status-ended: #4ade80;
}

*, *::before, *::after { box-sizing: border-box; }

html { font-size: 16px; scroll-behavior: smooth; }

body {
 margin: 0;
 background: var(--bg-base);
 color: var(--text-primary);
 font-family: 'Inter', system-ui, -apple-system, sans-serif;
 line-height: 1.6;
 min-height: 100vh;
}

/* ── Global utilities ── */
.container {
 max-width: 1280px;
 margin: 0 auto;
 padding: 0 1.5rem;
}

.page { padding: 2.5rem 1.5rem; max-width: 1280px; margin: 0 auto; }

.page-title {
 font-family: 'Space Grotesk', sans-serif;
 font-size: 1.75rem;
 font-weight: 700;
 color: var(--text-primary);
 margin: 0 0 0.25rem;
}

.page-subtitle {
 color: var(--text-muted);
 font-size: 0.95rem;
 margin: 0 0 2rem;
}

/* ── Button system ── */
.btn {
 display: inline-flex;
 align-items: center;
 justify-content: center;
 gap: 0.4rem;
 padding: 0.55rem 1.1rem;
 border-radius: 8px;
 border: 1px solid transparent;
 font-size: 0.88rem;
 font-weight: 600;
 font-family: inherit;
 cursor: pointer;
 text-decoration: none;
 transition: all 0.2s;
 white-space: nowrap;
}

.btn--primary {
 background: var(--accent);
 color: #fff;
 border-color: var(--accent);
}
.btn--primary:hover { background: var(--accent-hover); border-color: var(--accent-hover); }

.btn--ghost {
 background: transparent;
 color: var(--text-muted);
 border-color: var(--border);
}
.btn--ghost:hover { background: var(--bg-hover); color: var(--text-primary); }

.btn--danger {
 background: rgba(239,68,68,0.15);
 color: #f87171;
 border-color: rgba(239,68,68,0.3);
}
.btn--danger:hover { background: rgba(239,68,68,0.25); }

.btn--sm { padding: 0.35rem 0.75rem; font-size: 0.82rem; }
.btn--lg { padding: 0.75rem 1.5rem; font-size: 1rem; }
.btn:disabled { opacity: 0.5; cursor: not-allowed; }

/* ── Form controls ── */
.form-control {
 width: 100%;
 padding: 0.6rem 0.875rem;
 background: var(--bg-input);
 border: 1px solid var(--border);
 border-radius: 8px;
 color: var(--text-primary);
 font-size: 0.9rem;
 font-family: inherit;
 transition: border-color 0.2s;
 outline: none;
}
.form-control:focus { border-color: var(--accent); }
.form-control::placeholder { color: var(--text-muted); }

select.form-control { cursor: pointer; }
select.form-control option { background: var(--bg-card); }

textarea.form-control { resize: vertical; min-height: 100px; }

.form-label {
 display: block;
 font-size: 0.85rem;
 font-weight: 600;
 color: var(--text-muted);
 margin-bottom: 0.4rem;
 letter-spacing: 0.03em;
 text-transform: uppercase;
}

.form-group { margin-bottom: 1.25rem; }

/* ── Grid layouts ── */
.grid { display: grid; gap: 1.25rem; }
.grid--2 { grid-template-columns: repeat(2, 1fr); }
.grid--3 { grid-template-columns: repeat(3, 1fr); }
.grid--4 { grid-template-columns: repeat(4, 1fr); }
.grid--auto { grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); }

/* ── Error/empty states ── */
.error-banner {
 background: rgba(239,68,68,0.1);
 border: 1px solid rgba(239,68,68,0.3);
 border-radius: 10px;
 padding: 1rem 1.25rem;
 color: #f87171;
 display: flex;
 align-items: center;
 gap: 0.75rem;
 font-size: 0.9rem;
}

.empty-state {
 text-align: center;
 padding: 4rem 2rem;
 color: var(--text-muted);
}
.empty-state__icon { font-size: 3rem; margin-bottom: 1rem; }
.empty-state__text { font-size: 1rem; }

/* ── Page transitions ── */
.page-enter-active, .page-leave-active { transition: opacity 0.2s, transform 0.2s; }
.page-enter-from { opacity: 0; transform: translateY(8px); }
.page-leave-to { opacity: 0; transform: translateY(-8px); }

/* ── Footer ── */
.app-footer {
 text-align: center;
 padding: 2rem 1.5rem;
 color: var(--text-muted);
 font-size: 0.82rem;
 border-top: 1px solid var(--border);
 margin-top: 4rem;
}
.app-footer p { margin: 0; }

/* ── Responsive ── */
@media (max-width: 1024px) {
 .grid--4 { grid-template-columns: repeat(2, 1fr); }
 .grid--3 { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 640px) {
 .page { padding: 1.5rem 1rem; }
 .grid--2, .grid--3, .grid--4 { grid-template-columns: 1fr; }
}
</style>

<style scoped>
.app { display: flex; flex-direction: column; min-height: 100vh; }
.app-main { flex: 1; }
</style>
