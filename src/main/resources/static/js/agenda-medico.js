document.addEventListener("DOMContentLoaded", function () {
  const sections = document.querySelectorAll(".medico-section");
  const cards = document.querySelectorAll(".cita-card");
  const filtro = document.getElementById("filtroCitas");

  function showSection(id) {
    sections.forEach((section) => {
      section.classList.toggle("section-hidden", section.id !== id);
      if (section.id === id) {
        section.style.opacity = 1;
      }
    });
  }

  document.querySelectorAll("[data-medico-target]").forEach((trigger) => {
    trigger.addEventListener("click", function (event) {
      event.preventDefault();
      showSection(this.dataset.medicoTarget);
    });
  });

  if (filtro) {
    filtro.addEventListener("input", function () {
      const term = this.value.trim().toLowerCase();
      cards.forEach((card) => {
        const matches = card.dataset.search.toLowerCase().includes(term);
        card.classList.toggle("d-none", !matches);
      });
    });
  }

  showSection(document.body.dataset.vistaMedico || "dashboard");

  if (document.body.dataset.toast) {
    new bootstrap.Toast(document.getElementById("toastExito")).show();
  }
});
