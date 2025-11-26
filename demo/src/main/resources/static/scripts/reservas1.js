
// Referencias a los elementos del DOM
const fechaInput = document.getElementById("fecha");
const categoriaSelect = document.getElementById("categoria");
const tablaBody = document.getElementById("tablaReservas");


document.addEventListener("DOMContentLoaded", function () {
  const fechaInput = document.querySelector("#fecha");
  const categoriaSelect = document.querySelector("#categoria");
  const tbody = document.querySelector("#tablaReservas");

  // Función para cargar las reservas
  async function cargarReservas() {
    const fecha = fechaInput.value;
    const categoria = categoriaSelect.value;

    let url = "/api/reservas1";

    // Si hay filtros, los agregamos
    if (fecha || categoria) {
      url += "?";
      if (fecha) url += `fecha=${fecha}&`;
      if (categoria) url += `categoria=${encodeURIComponent(categoria)}`;
    }

    console.log("➡️ Consultando:", url);

    try {
      const response = await fetch(url);
      const data = await response.json();

      console.log("📦 Respuesta del backend:", data);

      tbody.innerHTML = "";

      if (data.length === 0) {
        tbody.innerHTML = `
          <tr><td colspan="7" class="text-center">No hay reservas para los filtros seleccionados</td></tr>
        `;
        return;
      }

      data.forEach(reserva => {
        const fila = `
          <tr>
            <td>${reserva.idReserva}</td>
            <td>${reserva.nombreCliente}</td>
            <td>${reserva.telefono}</td>
            <td>${reserva.fecha}</td>
            <td>${reserva.hora}</td>
            <td>${reserva.categoria}</td>
            <td>${reserva.personas}</td>
          </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", fila);
      });
    } catch (error) {
      console.error("❌ Error al cargar reservas:", error);
      tbody.innerHTML = `
        <tr><td colspan="7" class="text-center text-danger">Error al cargar reservas</td></tr>
      `;
    }
  }

  // Eventos para actualizar la tabla cuando cambian los filtros
  fechaInput.addEventListener("change", cargarReservas);
  categoriaSelect.addEventListener("change", cargarReservas);

  // ✅ Solo cargar si el usuario selecciona algo
fechaInput.addEventListener("change", cargarReservas);
categoriaSelect.addEventListener("change", cargarReservas);

// Mostrar mensaje vacío por defecto
tbody.innerHTML = `
  <tr>
    <td colspan="7" class="text-center">Selecciona filtros para mostrar las reservas</td>
  </tr>
`;

});
