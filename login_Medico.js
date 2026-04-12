document.addEventListener("DOMContentLoaded", function() {
    
    // 1. Lógica para el botón de mostrar/ocultar contraseña
    const togglePassword = document.querySelector("#togglePassword");
    const password = document.querySelector("#password");

    if(togglePassword && password) {
        togglePassword.addEventListener("click", function () {
            const type = password.getAttribute("type") === "password" ? "text" : "password";
            password.setAttribute("type", type);
            this.textContent = type === "password" ? "👁" : "🙈";
        });
    }

    // 2. Lógica de Simulación de Login
    const loginForm = document.getElementById('loginForm');
    
    if(loginForm) {
        loginForm.addEventListener('submit', function(event) {
            // Esto evita que la página se recargue al dar clic en Ingresar
            event.preventDefault(); 
            
            // Obtenemos lo que el usuario escribió
            const user = document.getElementById('username').value;
            const pass = document.getElementById('password').value;

            // DEFINIMOS EL USUARIO Y CONTRASEÑA PREDETERMINADOS
            const usuarioCorrecto = "admin";
            const passwordCorrecto = "1234";
            const usuarioCorrecto2 = "JGUERRA";
            const passwordCorrecto2 = "1234";

            // Comparamos si lo que escribió coincide con las credenciales correctas
            if(user === usuarioCorrecto && pass === passwordCorrecto) {
                alert("¡Bienvenido, acceso concedido!");
                
                // Si es correcto, lo mandamos al archivo de la agenda
                window.location.href = "agenda_Medico.html"; 
                
            } else if(user === usuarioCorrecto2 && pass === passwordCorrecto2) {
                alert("¡Bienvenido, acceso concedido!");
                
                // Si es correcto, lo mandamos al archivo de la agenda
                window.location.href = "agenda_Medico.html";

            } else {
                // Si se equivoca, le mostramos un error
                alert("Usuario o contraseña incorrectos. Pista: Usa 'admin' y '1234'");
            }
        });
    }
});