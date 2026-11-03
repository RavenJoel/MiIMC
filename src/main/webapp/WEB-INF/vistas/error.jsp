<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@include file="/WEB-INF/includes/head.jspf"%>
    <title>Algo salió mal · MiIMC</title>
</head>
<body>
<div class="container py-5" style="max-width: 640px;">
    <div class="card p-4 text-center">
        <div class="display-4 text-v"><i class="bi bi-heart-pulse"></i></div>
        <c:choose>
            <c:when test="${pageContext.errorData.statusCode eq 404}">
                <h2 class="fw-bold">Página no encontrada</h2>
                <p class="small-muted">La dirección que buscas no existe en MiIMC.</p>
            </c:when>
            <c:otherwise>
                <h2 class="fw-bold">Algo salió mal</h2>
                <p class="small-muted">Ocurrió un error inesperado. Si el problema continúa, revisa que la base de datos esté encendida.</p>
            </c:otherwise>
        </c:choose>
        <a class="btn btn-v mt-2" href="${pageContext.request.contextPath}/calcular">Volver al inicio</a>
    </div>
</div>
</body>
</html>
