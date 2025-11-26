// Esperar a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    console.log('reservas.js: DOM cargado, inicializando...');

    // Buscar elementos clave
    const form = document.getElementById("reservaForm");
    const tabla = document.querySelector("#tablaReservas tbody");
    const btnEliminar = document.getElementById("btnEliminar");

    let reservas = [];
    let reservaAEliminar = null;

    console.log('reservas.js: Form encontrado:', form ? 'Sí' : 'No');
    console.log('reservas.js: Tabla encontrada:', tabla ? 'Sí' : 'No');
    console.log('reservas.js: Btn eliminar encontrado:', btnEliminar ? 'Sí' : 'No');

    // Función para actualizar la tabla (con verificación)
    function actualizarTabla() {
        if (!tabla) {
            console.warn("reservas.js: Tabla de reservas no encontrada. No se puede actualizar.");
            return;
        }

        try {
            tabla.innerHTML = ""; // Limpiar filas existentes
            reservas.forEach((res, index) => {
                const fila = document.createElement("tr");
                fila.innerHTML = `
                    <td>${res.nombre || ''}</td>
                    <td>${res.telefono || ''}</td>
                    <td>${res.personas || ''}</td>
                    <td>${res.hora || ''}</td>
                `;
                fila.addEventListener("click", () => {
                    reservaAEliminar = index;
                    const modalElement = document.getElementById("confirmarBorrar");
                    if (modalElement) {
                        try {
                            const modal = new bootstrap.Modal(modalElement);
                            modal.show();
                        } catch (error) {
                            console.error("reservas.js: Error al mostrar modal:", error);
                            showToast('Error al mostrar confirmación de eliminación.', 'danger');
                        }
                    } else {
                        console.error("reservas.js: Modal de confirmación no encontrado.");
                        showToast('Modal de confirmación no disponible.', 'warning');
                    }
                });
                tabla.appendChild(fila);
            });
            console.log('reservas.js: Tabla actualizada con', reservas.length, 'reservas.');
        } catch (error) {
            console.error("reservas.js: Error al actualizar tabla:", error);
        }
    }

    // Event listener para el formulario (solo si existe)
    if (form) {
        console.log('reservas.js: Agregando listener al formulario.');
        form.addEventListener("submit", function (e) {
            e.preventDefault();
            console.log('reservas.js: Formulario enviado.');

            try {
                // Verificar y obtener valores de inputs con chequeos de null
                const nombreInput = document.getElementById("clientenNombre");
                const telefonoInput = document.getElementById("clienteTelefono");
                const personasInput = document.getElementById("numeroPersonas");
                const horaInput = document.getElementById("fechaHora");

                console.log('reservas.js: Inputs encontrados - nombre:', nombreInput ? 'Sí' : 'No');

                if (!nombreInput || !telefonoInput || !personasInput || !horaInput) {
                    throw new Error('Uno o más campos del formulario no se encontraron en el DOM.');
                }

                const nombre = nombreInput.value.trim();
                const telefono = telefonoInput.value.trim();
                const personas = personasInput.value.trim();
                const hora = horaInput.value.trim();

                // Validación básica
                if (!nombre || !telefono || !personas || !hora) {
                    throw new Error('Todos los campos son obligatorios.');
                }

                const reserva = { nombre, telefono, personas, hora };
                reservas.push(reserva);

                actualizarTabla();
                form.reset();
                showToast('Reserva realizada con éxito!', 'success');
                console.log('reservas.js: Reserva agregada:', reserva);
            } catch (error) {
                console.error("reservas.js: Error en submit del formulario:", error);
                showToast('Error al procesar la reserva: ' + error.message, 'danger');
            }
        });
    } else {
        console.warn("reservas.js: Formulario de reserva (reservaForm) no encontrado. El script no funcionará en esta página.");
    }

    // Event listener para el botón de eliminar (solo si existe)
    if (btnEliminar) {
        console.log('reservas.js: Agregando listener al botón eliminar.');
        btnEliminar.addEventListener("click", function () {
            if (reservaAEliminar !== null) {
                try {
                    reservas.splice(reservaAEliminar, 1);
                    actualizarTabla();
                    reservaAEliminar = null;
                    const modalElement = document.getElementById("confirmarBorrar");
                    if (modalElement) {
                        const modalInstance = bootstrap.Modal.getInstance(modalElement);
                        if (modalInstance) {
                            modalInstance.hide();
                        }
                    }
                    showToast('Reserva eliminada con éxito!', 'info');
                    console.log('reservas.js: Reserva eliminada en índice', reservaAEliminar);
                } catch (error) {
                    console.error("reservas.js: Error al eliminar reserva:", error);
                    showToast('Error al eliminar la reserva.', 'danger');
                }
            }
        });
    } else {
        console.warn("reservas.js: Botón de eliminar (btnEliminar) no encontrado.");
    }

    
    actualizarTabla();
    console.log('reservas.js: Inicialización completada.');
});

// Función para formatear fecha para input datetime-local (global)
function formatDateForInput(date) {
    try {
        const d = new Date(date);
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        const hours = String(d.getHours()).padStart(2, '0');
        const minutes = String(d.getMinutes()).padStart(2, '0');
        return `${year}-${month}-${day}T${hours}:${minutes}`;
    } catch (error) {
        console.error('Error en formatDateForInput:', error);
        return '';
    }
}

// Función para mostrar notificaciones toast (global, con verificación de Bootstrap)
function showToast(message, type = 'success') {
    try {
        if (typeof bootstrap === 'undefined') {
            console.warn('showToast: Bootstrap no está cargado.');
            alert(message); // Fallback a alert si no hay Bootstrap
            return;
        }

        const toastHtml = `
            <div class="toast align-items-center text-white bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
                <div class="d-flex">
                    <div class="toast-body">${message}</div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                </div>
            </div>
        `;
        
        let toastContainer = document.querySelector('.toast-container');
        if (!toastContainer) {
            toastContainer = document.createElement('div');
            toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
            document.body.appendChild(toastContainer);
        }
        
        toastContainer.insertAdjacentHTML('beforeend', toastHtml);
        const toastElement = toastContainer.lastElementChild;
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
        
        toastElement.addEventListener('hidden.bs.toast', function() {
            this.remove();
        });
    } catch (error) {
        console.error('showToast: Error al mostrar toast:', error);
        alert(message); // Fallback
    }
}
