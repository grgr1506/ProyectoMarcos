
// 📊 --- CONFIGURACIÓN INICIAL ---
const tipoSelect = document.getElementById("tipoEstadistica");
const mesSelect = document.getElementById("mes");
const ctx = document.getElementById("chartEstadisticas").getContext("2d");
let chart; // variable global para destruir el gráfico anterior

// 🔹 Función para obtener datos desde el backend
async function fetchData(tipo) {
  try {
    const response = await fetch(`/api/estadisticas/${tipo}`);
    if (!response.ok) throw new Error(`Error ${response.status}: ${response.statusText}`);
    return await response.json();
  } catch (error) {
    console.error("Error al obtener datos:", error);
    return { labels: [], data: [] };
  }
}

// async function fetchData(tipo) {
//   const mes = document.getElementById("mes").value;
//   const url = mes && tipo === "ventas"
//     ? `/api/estadisticas/${tipo}?mes=${mes}`
//     : `/api/estadisticas/${tipo}`;

//   const response = await fetch(url);
//   return await response.json();
// }

// 🔹 Función principal para renderizar el gráfico
async function renderChart(tipo) {
  const datos = await fetchData(tipo);

  // Extraemos etiquetas y valores del JSON
  const labels = datos.labels || [];
  const values = datos.data || [];

  let chartType;
  let bgColor, borderColor;

  // 🔸 Determinamos el tipo de gráfico y colores
  switch (tipo) {
    case "opiniones":
      chartType = "bar";
      bgColor = "rgba(255, 205, 86, 0.8)";
      borderColor = "rgba(255, 193, 7, 1)";
      break;

    case "ventas":
      chartType = "line";
      bgColor = "rgba(54, 162, 235, 0.2)";
      borderColor = "rgba(54, 162, 235, 1)";
      break;

    case "bestsellers":
      chartType = "pie";
      bgColor = [
        "rgba(255, 99, 132, 0.6)",
        "rgba(54, 162, 235, 0.6)",
        "rgba(255, 206, 86, 0.6)",
        "rgba(75, 192, 192, 0.6)",
        "rgba(153, 102, 255, 0.6)"
      ];
      borderColor = "#fff";
      break;
  }

  // 🔸 Si ya existe un gráfico, destruirlo antes de crear otro
  if (chart) chart.destroy();

  // 🔸 Crear el nuevo gráfico
  chart = new Chart(ctx, {
    type: chartType,
    data: {
      labels: labels,
      datasets: [{
        label:
          tipo === "opiniones" ? "Opiniones por Estrellas" :
          tipo === "ventas" ? "Ventas Mensuales (S/)" :
          "Productos Más Vendidos",
        data: values,
        backgroundColor: bgColor,
        borderColor: borderColor,
        borderWidth: 2,
        fill: tipo === "ventas" // solo las líneas se rellenan
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: tipo === "bestsellers" ? "bottom" : "top",
          labels: {
            color: "#333"
          }
        }
      },
      scales: tipo !== "bestsellers" ? {
        y: {
          beginAtZero: true,
          grid: { color: "rgba(200,200,200,0.2)" },
          ticks: { color: "#333" }
        },
        x: {
          ticks: { color: "#333" }
        }
      } : {}
    }
  });
}

// 🔸 Al cargar la página por defecto muestra "ventas"
document.addEventListener("DOMContentLoaded", () => renderChart("ventas"));

// 🔸 Cuando cambia el tipo de estadística
tipoSelect.addEventListener("change", () => renderChart(tipoSelect.value));

// 🔸 Si cambia el mes, vuelve a renderizar con el mismo tipo (puedes ampliar luego para filtrar por mes)
mesSelect.addEventListener("change", () => renderChart(tipoSelect.value));

