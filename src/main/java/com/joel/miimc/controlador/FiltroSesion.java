package com.joel.miimc.controlador;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Sin sesión no es posible calcular ni ver el historial:
 * las páginas redirigen al login y la API responde 401.
 */
@WebFilter(urlPatterns = {"/calcular", "/historial", "/api/*"})
public class FiltroSesion extends HttpFilter {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession sesion = request.getSession(false);
        boolean conSesion = sesion != null && sesion.getAttribute("usuario") != null;
        if (conSesion) {
            // Las páginas con datos del usuario no se guardan en caché
            response.setHeader("Cache-Control", "no-store");
            chain.doFilter(request, response);
        } else if (request.getRequestURI().startsWith(request.getContextPath() + "/api/")) {
            // La API responde el error en la representación que pidió el cliente
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String mensaje = "Inicia sesión para consultar tu historial.";
            String formato = request.getParameter("formato");
            String accept = request.getHeader("Accept");
            boolean xml = "xml".equalsIgnoreCase(formato) || (formato == null && accept != null
                    && accept.contains("xml") && !accept.contains("json"));
            if (xml) {
                response.setContentType("application/xml;charset=UTF-8");
                response.getWriter().write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<error><estado>401</estado>"
                        + "<error>No autorizado</error><mensaje>" + mensaje + "</mensaje></error>");
            } else {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"estado\":401,\"error\":\"No autorizado\",\"mensaje\":\""
                        + mensaje + "\"}");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
