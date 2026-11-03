<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@include file="/WEB-INF/includes/head.jspf"%>
    <title>Iniciar sesión · MiIMC</title>
</head>
<body>
<div class="container-fluid">
    <div class="row min-vh-100">
        <div class="col-md-6 panel-v text-white p-5 d-flex flex-column justify-content-center">
            <div class="brand display-5 mb-3"><i class="bi bi-heart-pulse-fill"></i> MiIMC</div>
            <p class="fs-5">Calcula tu índice de masa corporal y sigue tu progreso con el tiempo.</p>
            <ul class="list-unstyled mt-3">
                <li class="mb-2"><i class="bi bi-check2-circle"></i> Registro único con tus datos</li>
                <li class="mb-2"><i class="bi bi-check2-circle"></i> Cálculo con tu estatura registrada</li>
                <li><i class="bi bi-check2-circle"></i> Historial de todas tus mediciones</li>
            </ul>
        </div>
        <div class="col-md-6 d-flex align-items-center justify-content-center p-4 p-md-5">
            <div style="width: 100%; max-width: 380px;">
                <h2 class="fw-bold mb-1">Iniciar sesión</h2>
                <p class="small-muted mb-4">Ingresa con tu nombre de usuario y contraseña.</p>

                <c:if test="${param.registrado eq '1'}">
                    <div class="alert alert-success py-2 small"><i class="bi bi-check-circle-fill"></i>
                        Tu cuenta se creó correctamente. Ya puedes iniciar sesión.</div>
                </c:if>
                <c:if test="${param.salida eq '1'}">
                    <div class="alert alert-secondary py-2 small"><i class="bi bi-box-arrow-right"></i>
                        Cerraste sesión.</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2 small"><i class="bi bi-exclamation-triangle-fill"></i>
                        <c:out value="${error}"/></div>
                </c:if>

                <form method="post" action="${pageContext.request.contextPath}/login">
                    <label for="nombreUsuario" class="form-label fw-semibold small">Nombre de usuario</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text"><i class="bi bi-person"></i></span>
                        <input class="form-control" id="nombreUsuario" name="nombreUsuario" required
                               autocomplete="username" value="<c:out value='${nombreUsuario}'/>">
                    </div>
                    <label for="contrasena" class="form-label fw-semibold small">Contraseña</label>
                    <div class="input-group mb-4">
                        <span class="input-group-text"><i class="bi bi-lock"></i></span>
                        <input class="form-control" type="password" id="contrasena" name="contrasena" required
                               autocomplete="current-password">
                    </div>
                    <button class="btn btn-v w-100 py-2 fw-semibold" type="submit">Iniciar sesión</button>
                </form>
                <p class="text-center mt-4 small">¿Aún no tienes cuenta?
                    <a href="${pageContext.request.contextPath}/registro" class="text-v fw-semibold">Regístrate</a></p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
