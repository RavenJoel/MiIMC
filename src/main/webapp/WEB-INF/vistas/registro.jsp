<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@include file="/WEB-INF/includes/head.jspf"%>
    <title>Crear cuenta · MiIMC</title>
</head>
<body>
<div class="container py-4">
    <div class="d-flex align-items-center mb-3">
        <span class="brand fs-4 text-v"><i class="bi bi-heart-pulse-fill"></i> MiIMC</span>
        <a class="ms-auto small text-v" href="${pageContext.request.contextPath}/login">
            <i class="bi bi-arrow-left"></i> Ya tengo cuenta</a>
    </div>
    <div class="card p-4 mx-auto" style="max-width: 860px;">
        <h2 class="fw-bold mb-1">Crear cuenta</h2>
        <p class="small-muted">Solo te registras una vez. Estos datos se usan para calcular tu IMC.</p>

        <c:if test="${not empty errores}">
            <div class="alert alert-danger py-2 small mb-3">
                <i class="bi bi-exclamation-triangle-fill"></i> Revisa los datos:
                <ul class="mb-0 mt-1">
                    <c:forEach items="${errores}" var="e"><li><c:out value="${e}"/></li></c:forEach>
                </ul>
            </div>
        </c:if>

        <%-- novalidate: las reglas las revisa el servidor (Validador) y se muestran arriba --%>
        <form method="post" action="${pageContext.request.contextPath}/registro" novalidate>
            <div class="row g-3">
                <div class="col-12">
                    <label for="nombreCompleto" class="form-label fw-semibold small">Nombre completo</label>
                    <input class="form-control" id="nombreCompleto" name="nombreCompleto" maxlength="100"
                           value="<c:out value='${usuario.nombreCompleto}'/>">
                </div>
                <div class="col-md-6">
                    <label for="nombreUsuario" class="form-label fw-semibold small">Nombre de usuario</label>
                    <input class="form-control" id="nombreUsuario" name="nombreUsuario" maxlength="30"
                           autocomplete="username" value="<c:out value='${usuario.nombreUsuario}'/>">
                    <div class="form-text">De 4 a 30 caracteres: letras, números, punto o guion bajo.</div>
                </div>
                <div class="col-md-3">
                    <label for="contrasena" class="form-label fw-semibold small">Contraseña</label>
                    <input class="form-control" type="password" id="contrasena" name="contrasena"
                           autocomplete="new-password">
                    <div class="form-text">Mínimo 8 caracteres.</div>
                </div>
                <div class="col-md-3">
                    <label for="confirmacion" class="form-label fw-semibold small">Confirmar contraseña</label>
                    <input class="form-control" type="password" id="confirmacion" name="confirmacion"
                           autocomplete="new-password">
                </div>
                <div class="col-md-3">
                    <label for="edad" class="form-label fw-semibold small">Edad (años)</label>
                    <input class="form-control" type="number" id="edad" name="edad" min="15" max="120"
                           value="${usuario.edad gt 0 ? usuario.edad : ''}">
                    <div class="form-text">Desde 15 años.</div>
                </div>
                <div class="col-md-4">
                    <span class="form-label fw-semibold small d-block mb-2">Sexo</span>
                    <div class="btn-group w-100" role="group" aria-label="Sexo">
                        <input type="radio" class="btn-check" name="sexo" id="sexoM" value="M"
                               ${usuario.sexoTexto eq 'M' ? 'checked' : ''}>
                        <label class="btn btn-outline-secondary" for="sexoM">Masculino</label>
                        <input type="radio" class="btn-check" name="sexo" id="sexoF" value="F"
                               ${usuario.sexoTexto eq 'F' ? 'checked' : ''}>
                        <label class="btn btn-outline-secondary" for="sexoF">Femenino</label>
                    </div>
                </div>
                <div class="col-md-5">
                    <label for="estatura" class="form-label fw-semibold small">Estatura (metros)</label>
                    <div class="input-group">
                        <input class="form-control" type="number" id="estatura" name="estatura"
                               step="0.01" min="1" max="2.5"
                               value="${usuario.estatura gt 0 ? usuario.estatura : ''}">
                        <span class="input-group-text">m</span>
                    </div>
                    <div class="form-text">Entre 1.00 y 2.50 m.</div>
                </div>
            </div>
            <div class="d-flex align-items-center mt-4">
                <span class="small-muted"><i class="bi bi-shield-lock"></i> Tu contraseña se guarda cifrada.</span>
                <button class="btn btn-v ms-auto px-4 fw-semibold" type="submit">Crear cuenta</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
