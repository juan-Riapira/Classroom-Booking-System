document.addEventListener("DOMContentLoaded", () => {
  const menuButton = document.getElementById('menu-toggle');
  const menu = document.getElementById('menu');

  if (!menuButton || !menu) return;

  // Abrir / cerrar menú
  menuButton.addEventListener('click', () => {
    const isOpen = menu.classList.toggle('open');
    menuButton.classList.toggle('open');
    document.body.classList.toggle('menu-active', isOpen);
  });

  // Cerrar el menú al hacer clic en un enlace
  const links = menu.querySelectorAll('a');
  links.forEach(link => {
    link.addEventListener('click', () => {
      menu.classList.remove('open');
      menuButton.classList.remove('open');
      document.body.classList.remove('menu-active');
    });
  });
});

function loadSection(section) {
  switch (section) {
    case 'reportes':
      window.location.href = "http://localhost:8083"; // Microservicio Reporting
      break;

    case 'aulas':
      window.location.href = "http://localhost:8081"; // Microservicio Classroom (CORREGIDO)
      break;

    case 'prestamos':
      window.location.href = "http://localhost:8082"; // Microservicio Loan (CORREGIDO) 
      break;

    default:
      alert("Sección no encontrada.");
  }
}
