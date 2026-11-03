<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@include file="/WEB-INF/includes/head.jspf"%>
    <title>Mi historial · MiIMC</title>
</head>
<body>
<c:set var="activo" value="historial"/>
<%@include file="/WEB-INF/includes/nav.jspf"%>
<div class="container-fluid p-3 p-md-4">
    <c:if test="${not empty aviso}">
        <div class="alert alert-success py-2"><i class="bi bi-check-circle-fill"></i> <c:out value="${aviso}"/></div>
    </c:if>
    <div id="error" class="alert alert-danger d-none"></div>
    <div id="vacio" class="card p-4 d-none">
        <h5 class="fw-bold">Todavía no tienes mediciones</h5>
        <p class="mb-3">Calcula tu IMC por primera vez para empezar tu historial.</p>
        <a class="btn btn-v align-self-start" href="${pageContext.request.contextPath}/calcular">
            <i class="bi bi-calculator"></i> Calcular mi IMC</a>
    </div>
    <div id="contenido" class="row g-4 d-none">
        <div class="col-lg-4">
            <div class="card p-4 h-100">
                <div class="small-muted">Tu IMC actual · <span id="fechaActual"></span></div>
                <div class="d-flex align-items-end gap-2">
                    <span id="imcActual" class="imc-grande"></span><span class="pb-2 small-muted">kg/m²</span>
                </div>
                <span id="clasifActual" class="badge fs-6 mt-2 align-self-start"></span>
                <div class="mt-4 position-relative">
                    <div class="escala">
                        <div style="width:14%;background:#8db8ea"></div><div style="width:26%;background:#7cc9a0"></div>
                        <div style="width:20%;background:#f2c46b"></div><div style="width:40%;background:#ee8f8f"></div>
                    </div>
                    <div id="marcador" class="marcador"></div>
                    <div class="d-flex justify-content-between small-muted mt-1">
                        <span>15</span><span>18.5</span><span>25</span><span>30</span><span>40</span></div>
                </div>
                <p id="cambio" class="small mt-3 mb-0"></p>
            </div>
        </div>
        <div class="col-lg-8">
            <div class="card p-4 h-100">
                <h5 class="fw-bold mb-2">Evolución de tu IMC</h5>
                <div id="grafica"></div>
            </div>
        </div>
        <div class="col-12">
            <div class="card p-4">
                <div class="d-flex flex-wrap align-items-center mb-2 gap-2">
                    <h5 class="fw-bold mb-0">Historial de mediciones</h5>
                    <span class="ms-auto small-muted"><i class="bi bi-cloud-arrow-down"></i>
                        Datos del servicio REST <code>GET /api/historial</code> ·
                        <a href="${pageContext.request.contextPath}/api/historial?formato=json" target="_blank">JSON</a> ·
                        <a href="${pageContext.request.contextPath}/api/historial?formato=xml" target="_blank">XML</a></span>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light"><tr><th>Fecha</th><th>Peso</th><th>Estatura</th><th>IMC</th><th>Clasificación</th></tr></thead>
                        <tbody id="filas"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script>
    const API = '${pageContext.request.contextPath}/api/historial';
</script>
<script src="${pageContext.request.contextPath}/js/historial.js"></script>
</body>
</html>
