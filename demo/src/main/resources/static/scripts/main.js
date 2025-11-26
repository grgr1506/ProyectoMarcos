// Esperar a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    console.log('main.js: DOM cargado, inicializando...');

    // --- Botón de Menú Hamburguesa ---
    const menuToggle = document.querySelector(".menu-toggle");
    const navMenu = document.querySelector(".nav-menu");

    console.log('main.js: Menu toggle encontrado:', menuToggle ? 'Sí' : 'No');
    console.log('main.js: Nav menu encontrado:', navMenu ? 'Sí' : 'No');

    if (menuToggle && navMenu) {
        console.log('main.js: Agregando listener al menú toggle.');
        // Usar addEventListener en lugar de onclick para mejor compatibilidad
        menuToggle.addEventListener('onclick', function(e) {
            e.preventDefault();
            navMenu.classList.toggle("active");
            console.log('main.js: Menú toggled.');
        });
    } else {
        console.warn("main.js: Menu toggle o nav menu no encontrados. El menú no funcionará en esta página.");
    }

    // --- Botón Scroll to Top ---
    const scrollTopBtn = document.getElementById('scrollTop');
    const scrollUp = document.querySelector(".scroll-up"); // Alternativa si existe

    const targetScrollBtn = scrollTopBtn || scrollUp;
    console.log('main.js: Scroll button encontrado:', targetScrollBtn ? 'Sí' : 'No');

    // Función para manejar visibilidad y scroll suave del botón
    function alternarBotonScrollUp(pixeles) {
        if (!targetScrollBtn) {
            console.warn("main.js: Botón scroll to top no encontrado.");
            return;
        }

        // Listener para scroll (debounce opcional para rendimiento)
        let ticking = false;
        function updateScroll() {
            if (!ticking) {
                requestAnimationFrame(() => {
                    const scroll = window.pageYOffset || document.documentElement.scrollTop;
                    if (scroll > pixeles) {
                        targetScrollBtn.classList.add("show");
                    } else {
                        targetScrollBtn.classList.remove("show");
                    }
                    ticking = false;
                });
                ticking = true;
            }
        }

        window.addEventListener("scroll", updateScroll);

        // Listener para click en scroll button
        targetScrollBtn.addEventListener("click", (e) => {
            e.preventDefault();
            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });
            console.log('main.js: Scroll to top activado.');
        });

        console.log('main.js: Scroll handler inicializado con', pixeles, 'px.');
    }

    // Inicializar scroll button (400px como en el PDF)
    alternarBotonScrollUp(400);

    // --- Scroll Suave para Enlaces Internos ---
    const internalLinks = document.querySelectorAll('a[href^="#"]');
    console.log('main.js: Enlaces internos encontrados:', internalLinks.length);
    internalLinks.forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const target = document.querySelector(targetId);
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
                console.log('main.js: Scroll suave a', targetId);
            } else {
                console.warn('main.js: Target no encontrado:', targetId);
            }
        });
    });

    // --- Efecto de Scroll en Navbar ---
    const navbar = document.querySelector('.navbar-custom');
    if (navbar) {
        console.log('main.js: Navbar encontrado, agregando efecto de scroll.');
        window.addEventListener('scroll', function () {
            const scroll = window.pageYOffset;
            if (scroll > 100) {
                navbar.style.padding = '10px 0';
                navbar.style.boxShadow = '0 6px 25px rgba(0,0,0,0.15)';
            } else {
                navbar.style.padding = '20px 0';
                navbar.style.boxShadow = '0 4px 20px rgba(0,0,0,0.08)';
            }
        });
    } else {
        console.warn("main.js: Navbar (.navbar-custom) no encontrado.");
    }

    // --- Validación de Formularios (Bootstrap) ---
    const forms = document.querySelectorAll('.needs-validation');
    console.log('main.js: Formularios para validar encontrados:', forms.length);
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // --- Acordeón FAQ ---
    const faqQuestions = document.querySelectorAll('.faq-question');
    console.log('main.js: Preguntas FAQ encontradas:', faqQuestions.length);
    faqQuestions.forEach(question => {
        question.addEventListener('click', function() {
            const faqItem = this.parentElement;
            const faqAnswer = faqItem.querySelector('.faq-answer');
            const icon = this.querySelector('.faq-icon');
            
            faqItem.classList.toggle('active');
            
            if (faqAnswer) {
                faqAnswer.style.display = faqAnswer.style.display === 'block' ? 'none' : 'block';
                if (icon) {
                    icon.classList.toggle('fa-minus');
                    icon.classList.toggle('fa-plus');
                }
            }
        });
    });

    // --- Inicialización de Tooltips (Bootstrap) ---
    const tooltipTriggers = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    console.log('main.js: Tooltips encontrados:', tooltipTriggers.length);
    tooltipTriggers.forEach(trigger => {
        try {
            new bootstrap.Tooltip(trigger);
        } catch (error) {
            console.warn('main.js: Error inicializando tooltip:', error);
        }
    });

    // --- Auto-dismiss de Alerts ---
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    console.log('main.js: Alerts para auto-dismiss encontrados:', alerts.length);
    alerts.forEach(function(alert) {
        setTimeout(function() {
            try {
                const bsAlert = bootstrap.Alert.getInstance(alert) || new bootstrap.Alert(alert);
                bsAlert.close();
            } catch (error) {
                console.warn('main.js: Error cerrando alert:', error);
                alert.style.display = 'none'; // Fallback
            }
        }, 5000);
    });

    // --- Carrusel Bootstrap (si existe) ---
    const carousels = document.querySelectorAll('.carousel');
    console.log('main.js: Carousels encontrados:', carousels.length);
    carousels.forEach(carousel => {
        try {
            const bsCarousel = new bootstrap.Carousel(carousel, {
                interval: 6000,
                ride: 'carousel'
            });

            carousel.addEventListener('mouseenter', () => {
                bsCarousel.pause();
            });

            carousel.addEventListener('mouseleave', () => {
                bsCarousel.cycle();
            });
            console.log('main.js: Carrusel inicializado.');
        } catch (error) {
            console.warn('main.js: Error inicializando carrusel:', error);
        }
    });

    console.log('main.js: Inicialización completada.');
});