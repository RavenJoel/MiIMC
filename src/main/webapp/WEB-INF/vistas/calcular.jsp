<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@include file="/WEB-INF/includes/head.jspf"%>
    <title>Calcular IMC · MiIMC</title>
</head>
<body>
<fmt:setLocale value="es_MX"/>
<c:set var="activo" value="calcular"/>
<%@include file="/WEB-INF/includes/nav.jspf"%>
<div class="container-fluid p-3 p-md-4">
    <div class="row g-4">
        <div class="col-lg-7">
            <div class="card p-4 h-100">
                <h2 class="fw-bold mb-1">Hola, <c:out value="${sessionScope.usuario.primerNombre}"/> 👋</h2>
                <p class="small-muted">Registra tu masa corporal de hoy para calcular tu IMC.</p>
                <div class="row g-3 mb-3">
                    <div class="col-4"><div class="dato"><div class="small-muted">Estatura registrada</div>
                        <div class="fs-4 fw-bold"><fmt:formatNumber value="${sessionScope.usuario.estatura}"
                            minFractionDigits="2" maxFractionDigits="2"/> m</div></div></div>
                    <div class="col-4"><div class="dato"><div class="small-muted">Edad</div>
                        <div class="fs-4 fw-bold">${sessionScope.usuario.edad} años</div></div></div>
                    <div class="col-4"><div class="dato"><div class="small-muted">Último IMC</div>
                        <div class="fs-4 fw-bold">${empty ultima ? '—' : ultima.imc}</div></div></div>
                </div>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2 small"><i class="bi bi-exclamation-triangle-fill"></i>
                        <c:out value="${error}"/></div>
                </c:if>
                <form method="post" action="${pageContext.request.contextPath}/calcular" novalidate>
                    <label for="peso" class="form-label fw-semibold">Masa corporal (peso)</label>
                    <div class="input-group input-group-lg mb-2">
                        <input class="form-control ${not empty error ? 'is-invalid' : ''}" type="number"
                               step="0.1" min="0.1" id="peso" name="peso" placeholder="Ej. 72.5"
                               value="<c:out value='${peso}'/>">
                        <span class="input-group-text">kg</span>
                    </div>
                    <p class="small-muted mb-4"><i class="bi bi-info-circle"></i> Debe ser mayor a 0 kg.</p>
                    <button class="btn btn-v btn-lg fw-semibold" type="submit">
                        <i class="bi bi-calculator"></i> Calcular IMC</button>
                </form>
            </div>
        </div>
        <div class="col-lg-5">
            <div class="card p-4 h-100">
                <h5 class="fw-bold">¿Qué es el IMC?</h5>
                <p class="small">Es el peso en kilogramos dividido entre la estatura en metros al cuadrado.</p>
                <div class="text-center my-3 p-3 rounded-3 border fs-5">IMC = <b>peso</b> / <b>estatura²</b></div>
                <h6 class="fw-bold mt-2">Clasificación (OMS)</h6>
                <table class="table table-sm small mb-2">
                    <tr><td><span class="badge badge-b">Bajo peso</span></td><td>menos de 18.5</td></tr>
                    <tr><td><span class="badge badge-n">Peso normal</span></td><td>18.5 – 24.9</td></tr>
                    <tr><td><span class="badge badge-s">Sobrepeso</span></td><td>25.0 – 29.9</td></tr>
                    <tr><td><span class="badge badge-o">Obesidad</span></td><td>30.0 o más</td></tr>
                </table>
                <p class="small-muted mb-0">La clasificación es orientativa y no sustituye la opinión de un profesional de la salud.</p>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
