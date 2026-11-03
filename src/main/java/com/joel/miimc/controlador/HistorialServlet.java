package com.joel.miimc.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controlador que muestra la vista del historial. Los datos no los envía
 * este servlet: la vista los pide al servicio REST GET /api/historial.
 */
@WebServlet("/historial")
public class HistorialServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // El aviso del último cálculo se muestra una sola vez
        Object aviso = request.getSession().getAttribute("aviso");
        if (aviso != null) {
            request.setAttribute("aviso", aviso);
            request.getSession().removeAttribute("aviso");
        }
        request.getRequestDispatcher("/WEB-INF/vistas/historial.jsp").forward(request, response);
    }
}
