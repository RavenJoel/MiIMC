package com.joel.miimc.controlador;

import com.joel.miimc.dao.AccesoDatosException;
import com.joel.miimc.dao.UsuarioDAO;
import com.joel.miimc.modelo.SeguridadUtil;
import com.joel.miimc.modelo.Usuario;
import com.joel.miimc.modelo.Validador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controlador del registro: muestra el formulario (GET) y registra al usuario (POST).
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String VISTA = "/WEB-INF/vistas/registro.jsp";

    private final transient Validador validador = new Validador();
    private final transient SeguridadUtil seguridad = new SeguridadUtil();
    private final transient UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = new Usuario(
                limpiar(request.getParameter("nombreCompleto")),
                limpiar(request.getParameter("nombreUsuario")),
                aEntero(request.getParameter("edad")),
                aSexo(request.getParameter("sexo")),
                aDecimal(request.getParameter("estatura")));
        String contrasena = request.getParameter("contrasena");
        String confirmacion = request.getParameter("confirmacion");

        List<String> errores = validador.validarRegistro(usuario, contrasena, confirmacion);
        try {
            if (errores.isEmpty() && usuarioDAO.existeNombreUsuario(usuario.getNombreUsuario())) {
                errores.add("El nombre de usuario «" + usuario.getNombreUsuario() + "» ya está ocupado.");
            }
            if (errores.isEmpty()) {
                usuario.setContrasenaHash(seguridad.generarHash(contrasena));
                if (usuarioDAO.registrar(usuario)) {
                    // Patrón POST-redirect-GET: evita registrar dos veces al recargar
                    response.sendRedirect(request.getContextPath() + "/login?registrado=1");
                    return;
                }
                errores.add("El nombre de usuario ya está ocupado.");
            }
        } catch (AccesoDatosException e) {
            errores.add("No fue posible conectar con la base de datos. Intenta más tarde.");
        }
        request.setAttribute("errores", errores);
        request.setAttribute("usuario", usuario);
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    private static String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private static int aEntero(String valor) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }

    private static double aDecimal(String valor) {
        try {
            return Double.parseDouble(valor.trim().replace(',', '.'));
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }

    private static char aSexo(String valor) {
        return (valor != null && valor.length() == 1) ? valor.charAt(0) : ' ';
    }
}
