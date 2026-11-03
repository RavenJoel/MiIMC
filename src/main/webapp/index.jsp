<%-- Punto de entrada: manda al cálculo; si no hay sesión, FiltroSesion redirige al login --%>
<% response.sendRedirect(request.getContextPath() + "/calcular"); %>
