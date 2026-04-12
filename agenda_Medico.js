function mostrarCitas() {
    document.getElementById('dashboard').classList.add('section-hidden');
    document.getElementById('vistaCitas').classList.remove('section-hidden');
}

function mostrarDashboard() {
    document.getElementById('vistaCitas').classList.add('section-hidden');
    document.getElementById('dashboard').classList.remove('section-hidden');
}

// Simulación de guardado
document.getElementById('formPaciente').onsubmit = function(e) {
    e.preventDefault();
    alert("Paciente registrado con éxito.");
    bootstrap.Modal.getInstance(document.getElementById('modalPaciente')).hide();
};

document.getElementById('formCita').onsubmit = function(e) {
    e.preventDefault();
    alert("Cita programada con éxito.");
    bootstrap.Modal.getInstance(document.getElementById('modalCita')).hide();
};