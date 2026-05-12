document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.getElementById("loginForm");
  const registerForm = document.getElementById("registerForm");
  const tabLogin = document.getElementById("tabLogin");
  const tabRegister = document.getElementById("tabRegister");
  const togglePassword = document.getElementById("togglePassword");
  const password = document.getElementById("password");

  function switchTab(tab) {
    const isLogin = tab === "login";
    loginForm.classList.toggle("d-none", !isLogin);
    registerForm.classList.toggle("d-none", isLogin);
    tabLogin.classList.toggle("is-active", isLogin);
    tabRegister.classList.toggle("is-active", !isLogin);
  }

  document.querySelectorAll("[data-auth-tab]").forEach((button) => {
    button.addEventListener("click", function () {
      switchTab(this.dataset.authTab);
    });
  });

  if (togglePassword && password) {
    togglePassword.addEventListener("click", function () {
      const type = password.getAttribute("type") === "password" ? "text" : "password";
      password.setAttribute("type", type);
      this.textContent = type === "password" ? "👁" : "🙈";
    });
  }

  if (document.body.dataset.showRegister === "true") {
    switchTab("register");
  }

  if (document.body.dataset.toast) {
    new bootstrap.Toast(document.getElementById("toastExito")).show();
  }
});
