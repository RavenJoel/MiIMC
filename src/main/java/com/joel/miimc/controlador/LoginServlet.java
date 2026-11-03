package com.joel.miimc.controlador;

import com.joel.miimc.dao.AccesoDatosException;
import com.joel.miimc.dao.UsuarioDAO;
import com.joel.miimc.modelo.SeguridadUtil;
import com.joel.miimc.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controlador del inicio de sesión.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String VISTA = "/WEB-INF/vistas/login.jsp";

    private final transient UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final transient SeguridadUtil seguridad = new SeguridadUtil();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        if (sesion != null && sesion.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/calcular");
            return;
        }
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombreUsuario = request.getParameter("nombreUsuario");
        String contrasena = request.getParameter("contrasena");
        try {
            Usuario usuario = (nombreUsuario == null) ? null
                    : usuarioDAO.buscarPorNombreUsuario(nombreUsuario.trim());
            if (usuario != null && seguridad.verificar(contrasena, usuario.getContrasenaHash())) {
                request.getSession(true);
                request.changeSessionId();          // evita la fijación de sesión
                usuario.setContrasenaHash(null);    // el hash no se guarda en la sesión
                request.getSession().setAttribute("usuario", usuario);
                response.sendRedirect(request.getContextPath() + "/calcular");
                return;
            }
            request.setAttribute("error", "Usuario o contraseña incorrectos.");
        } catch (AccesoDatosException e) {
            request.setAttribute("error", "No fue posible conectar con la base de datos. Intenta más tarde.");
        }
        request.setAttribute("nombreUsuario", nombreUsuario);
        request.getRequestDispatcher(VISTA).forward(request, response);
    }
}
