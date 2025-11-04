document.addEventListener("DOMContentLoaded", () => {
  const logoutBtn = document.querySelector('a[href="#"] .bx-log-out')?.closest('a');

  if (logoutBtn) {
    logoutBtn.addEventListener("click", (e) => {
      e.preventDefault();
      if (confirm("¿Deseas cerrar sesión?")) {
        localStorage.removeItem("usuario");
        sessionStorage.removeItem("usuario");
        window.location.href = "/AgenciaServicios/src/main/resources/static/login.html";
      }
    });
  }
});
