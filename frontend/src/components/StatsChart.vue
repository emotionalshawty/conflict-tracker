<!-- Doughnut chart showing conflict status distribution (Chart.js + vue-chartjs) -->
<template>
 <div class="chart-wrapper">
 <Doughnut :data="chartData" :options="chartOptions" />
 </div>
</template>

<script setup>
import { computed } from 'vue'
import { Doughnut } from 'vue-chartjs'
import {
 Chart as ChartJS,
 ArcElement,
 Tooltip,
 Legend
} from 'chart.js'
import { useI18n } from '../composables/useI18n.js'

ChartJS.register(ArcElement, Tooltip, Legend)

const { t } = useI18n()

const props = defineProps({
 active: { type: Number, default: 0 },
 frozen: { type: Number, default: 0 },
 ended: { type: Number, default: 0 }
})

const chartData = computed(() => ({
 labels: [t('status.ACTIVE'), t('status.FROZEN'), t('status.ENDED')],
 datasets: [{
 data: [props.active, props.frozen, props.ended],
 backgroundColor: ['rgba(239,68,68,0.8)', 'rgba(96,165,250,0.8)', 'rgba(74,222,128,0.8)'],
 borderColor: ['#ef4444', '#60a5fa', '#4ade80'],
 borderWidth: 2,
 hoverOffset: 8
 }]
}))

const chartOptions = {
 responsive: true,
 maintainAspectRatio: true,
 cutout: '65%',
 plugins: {
 legend: {
 position: 'bottom',
 labels: {
 color: '#8b949e',
 padding: 16,
 font: { size: 12, family: 'Inter, sans-serif' }
 }
 },
 tooltip: {
 callbacks: {
 label: (ctx) => ` ${ctx.label}: ${ctx.raw}`
 }
 }
 }
}
</script>

<style scoped>
.chart-wrapper {
 max-width: 260px;
 margin: 0 auto;
}
</style>
