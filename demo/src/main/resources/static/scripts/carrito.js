// ============================================================
// CARRITO DE COMPRAS - SCRIPTS
// ============================================================

document.addEventListener('DOMContentLoaded', function () {

    // Animación al agregar producto al carrito
    const forms = document.querySelectorAll('.add-to-cart-form');
    forms.forEach(form => {
        form.addEventListener('submit', function (e) {
            const button = this.querySelector('.btn-add-to-cart');

            // Cambiar texto del botón temporalmente
            const originalText = button.innerHTML;
            button.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Agregando...';
            button.disabled = true;

            // Restaurar después de 1 segundo
            setTimeout(() => {
                button.innerHTML = '<i class="fa-solid fa-check"></i> ¡Agregado!';

                setTimeout(() => {
                    button.innerHTML = originalText;
                    button.disabled = false;
                }, 1000);
            }, 500);
        });
    });

    // Auto-cerrar alertas después de 5 segundos
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
});

// Función para cambiar cantidad (ya está en carrito.html pero la dejamos aquí también)
function cambiarCantidad(btn, cambio) {
    const itemId = btn.getAttribute('data-item-id');
    const input = document.querySelector(`input[data-item-id="${itemId}"]`);
    let nuevaCantidad = parseInt(input.value) + cambio;

    if (nuevaCantidad < 1) {
        if (confirm('¿Eliminar este producto del carrito?')) {
            window.location.href = `/carrito/eliminar/${itemId}`;
        }
        return;
    }

    input.value = nuevaCantidad;

    // Mostrar loading
    btn.disabled = true;
    const originalContent = btn.innerHTML;
    btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i>';

    // Enviar actualización al servidor
    fetch(`/carrito/actualizar/${itemId}?cantidad=${nuevaCantidad}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        }
    })
        .then(response => response.text())
        .then(data => {
            if (data === 'OK') {
                // Recargar la página para actualizar los totales
                location.reload();
            } else {
                alert('Error al actualizar la cantidad');
                btn.innerHTML = originalContent;
                btn.disabled = false;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al actualizar la cantidad');
            btn.innerHTML = originalContent;
            btn.disabled = false;
        });
}

// Animación del badge del carrito
function animarBadgeCarrito() {
    const badge = document.querySelector('.badge-carrito-nav');
    if (badge) {
        badge.style.animation = 'none';
        setTimeout(() => {
            badge.style.animation = 'pulse-badge 0.5s ease';
        }, 10);
    }
}

// Confirmar antes de vaciar el carrito
document.addEventListener('DOMContentLoaded', function () {
    const btnVaciar = document.querySelector('.btn-vaciar-carrito');
    if (btnVaciar) {
        btnVaciar.addEventListener('click', function (e) {
            if (!confirm('¿Estás seguro de vaciar todo el carrito?')) {
                e.preventDefault();
                return false;
            }
        });
    }
});

// Animación suave al scroll
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        const href = this.getAttribute('href');
        if (href !== '#' && href !== '') {
            e.preventDefault();
            const target = document.querySelector(href);
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        }
    });
});