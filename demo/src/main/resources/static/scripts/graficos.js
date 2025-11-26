// Datos para el gráfico de opiniones (estrellas) pasados desde Thymeleaf
    const etiquetasOpiniones = /*[[${estrellasLabels}]]*/ [];
    const valoresOpiniones = /*[[${estrellasData}]]*/ [];

    console.log('Etiquetas Opiniones:', etiquetasOpiniones);
    console.log('Valores Opiniones:', valoresOpiniones);

// Gráfico de Barras: Opiniones del Público (por Estrellas)
const ctxBarras = document.getElementById('graficoBarras').getContext('2d');
new Chart(ctxBarras, {
    type: 'bar',
    data: {
        labels: etiquetasOpiniones,
        datasets: [{
            label: 'Opiniones por Estrellas',
            data: valoresOpiniones,
            backgroundColor: 'rgba(255, 240, 36, 0.71)',
            borderColor: 'rgba(255, 240, 36, 0.71)',
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});

// Datos para el gráfico de torta (hamburguesas más vendidas)
const etiquetasTorta = /*[[${tortaLabels}]]*/ [];
const valoresTorta = /*[[${tortaData}]]*/ [];

// Gráfico de Torta: Hamburguesas Más Vendidas
const ctxTorta = document.getElementById('graficoTorta').getContext('2d');
new Chart(ctxTorta, {
    type: 'pie',
    data: {
        labels: etiquetasTorta,
        datasets: [{
            label: 'Hamburguesas Más Vendidas',
            data: valoresTorta,
            backgroundColor: [
                'rgba(75, 192, 192, 0.6)',
                'rgba(153, 102, 255, 0.6)',
                'rgba(255, 159, 64, 0.6)',
                'rgba(255, 99, 132, 0.6)',
                'rgba(54, 162, 235, 0.6)'
            ],
            borderColor: 'rgba(0, 0, 0, 0.5)',
            borderWidth: 1
        }]
    },
    options: {
        responsive: true
    }
});

// Datos para el gráfico de líneas (ventas mensuales)
const etiquetasLineas = /*[[${lineasLabels}]]*/ [];
const valoresLineas = /*[[${lineasData}]]*/ [];

// Gráfico de Líneas: Ventas Mensuales
const ctxLineas = document.getElementById('graficoLineas').getContext('2d');
new Chart(ctxLineas, {
    type: 'line',
    data: {
        labels: etiquetasLineas,
        datasets: [{
            label: 'Ventas Mensuales',
            data: valoresLineas,
            fill: false,
            borderColor: 'rgba(75, 192, 192, 1)',
            tension: 0.1
        }]
    },
    options: {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});


