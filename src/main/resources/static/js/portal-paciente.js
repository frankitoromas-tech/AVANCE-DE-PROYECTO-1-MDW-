document.addEventListener("DOMContentLoaded", function () {
  const body = document.body;
  const modalEl = document.getElementById("modalCita");
  const detalleEl = document.getElementById("modalDetalle");
  const sections = document.querySelectorAll(".app-section");
  const serviceCards = document.querySelectorAll(".card-servicio");
  const scheduleTables = document.querySelectorAll("[data-horario-tabla]");
  const buttonsHorario = document.querySelectorAll(".btn-horario");
  const step1 = document.getElementById("paso1");
  const step2 = document.getElementById("paso2");
  const labelPaso = document.getElementById("labelPaso");
  const barra = document.getElementById("barraProgreso");
  const btnRegistrar = document.getElementById("btnRegistrar");
  const form = document.getElementById("formCita");

  function showSection(id) {
    sections.forEach((section) => {
      section.classList.toggle("section-hidden", section.id !== id);
    });
  }

  function setStep(step) {
    const firstStep = step === 1;
    step1.classList.toggle("section-hidden", !firstStep);
    step2.classList.toggle("section-hidden", firstStep);
    labelPaso.textContent = firstStep ? "Paso 1 de 2" : "Paso 2 de 2";
    barra.style.width = firstStep ? "0%" : "50%";
  }

  function resetSelection() {
    form.reset();
    document.getElementById("servicioId").value = "";
    document.getElementById("medico").value = "";
    document.getElementById("dia").value = "";
    document.getElementById("horaSlot").value = "";
    document.getElementById("modalidad").value = "";
    btnRegistrar.disabled = true;
    serviceCards.forEach((card) => card.classList.remove("border-primary", "border-2"));
    buttonsHorario.forEach((button) => {
      button.className = "btn btn-sm btn-primary btn-horario";
      button.textContent = "Seleccionar";
    });
    scheduleTables.forEach((table) => table.classList.add("section-hidden"));
    setStep(1);
  }

  document.querySelectorAll("[data-section-target]").forEach((trigger) => {
    trigger.addEventListener("click", function (event) {
      event.preventDefault();
      showSection(this.dataset.sectionTarget);
    });
  });

  document.querySelector("[data-next-step]").addEventListener("click", function () {
    const requiredFields = ["nombre", "dni", "edad", "parentesco"];
    const valid = requiredFields.every((fieldId) => {
      const field = document.getElementById(fieldId);
      return field && field.value.trim() !== "";
    });

    if (!valid) {
      alert("Por favor completa todos los campos.");
      return;
    }

    setStep(2);
  });

  document.querySelector("[data-prev-step]").addEventListener("click", function () {
    setStep(1);
  });

  serviceCards.forEach((card) => {
    card.addEventListener("click", function () {
      const serviceId = this.dataset.servicioId;
      document.getElementById("servicioId").value = serviceId;
      document.getElementById("medico").value = "";
      document.getElementById("dia").value = "";
      document.getElementById("horaSlot").value = "";
      document.getElementById("modalidad").value = "";
      btnRegistrar.disabled = true;

      serviceCards.forEach((item) => item.classList.remove("border-primary", "border-2"));
      this.classList.add("border-primary", "border-2");

      scheduleTables.forEach((table) => {
        table.classList.toggle("section-hidden", table.dataset.horarioTabla !== serviceId);
      });
    });
  });

  buttonsHorario.forEach((button) => {
    button.addEventListener("click", function () {
      buttonsHorario.forEach((item) => {
        item.className = "btn btn-sm btn-primary btn-horario";
        item.textContent = "Seleccionar";
      });

      this.className = "btn btn-sm btn-success btn-horario";
      this.textContent = "✓ Seleccionado";
      document.getElementById("servicioId").value = this.dataset.servicioId;
      document.getElementById("medico").value = this.dataset.medico;
      document.getElementById("dia").value = this.dataset.dia;
      document.getElementById("horaSlot").value = this.dataset.hora;
      document.getElementById("modalidad").value = this.dataset.modalidad;
      btnRegistrar.disabled = false;
    });
  });

  if (modalEl) {
    modalEl.addEventListener("show.bs.modal", resetSelection);
  }

  showSection(body.dataset.vistaActiva || "dashboard");

  if (body.dataset.abrirDetalle === "true" && detalleEl) {
    new bootstrap.Modal(detalleEl).show();
  }

  if (body.dataset.toast) {
    new bootstrap.Toast(document.getElementById("toastExito")).show();
  }
});
