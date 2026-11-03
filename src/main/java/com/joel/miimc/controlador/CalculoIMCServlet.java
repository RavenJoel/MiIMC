package com.joel.miimc.controlador;

import com.joel.miimc.dao.AccesoDatosException;
import com.joel.miimc.dao.MedicionDAO;
import com.joel.miimc.modelo.CalculadoraIMC;
import com.joel.miimc.modelo.Medicion;
import com.joel.miimc.modelo.Usuario;
import com.joel.miimc.modelo.Validador;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controlador del cálculo: muestra el formulario de masa corporal (GET),
 * calcula el IMC con la estatura del usuario y guarda la medición (POST).
 */
@WebServlet("/calcular")
public class CalculoIMCServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String VISTA = "/WEB-INF/vistas/calcular.jsp";

    private final transient Validador validador = new Validador();
    private final transient CalculadoraIMC calculadora = new CalculadoraIMC();
    private final transient MedicionDAO medicionDAO = new MedicionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        mostrarFormulario(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        String textoPeso = request.getParameter("peso");
        double peso;
        try {
            peso = Double.parseDouble(textoPeso.trim().replace(',', '.'));
        } catch (NumberFormatException | NullPointerException e) {
            peso = -1;
        }
        if (!validador.pesoValido(peso)) {
            request.setAttribute("error", "La masa corporal debe ser mayor a 0 kg.");
            request.setAttribute("peso", textoPeso);
            mostrarFormulario(request, response);
            return;
        }
        double imc = calculadora.calcular(peso, usuario.getEstatura());
        Medicion medicion = new Medicion(usuario.getIdUsuario(), peso, usuario.getEstatura(), imc);
        try {
            medicionDAO.guardar(medicion);
        } catch (AccesoDatosException e) {
            request.setAttribute("error", "No fue posible guardar tu medición: no hay conexión con la base de datos.");
            request.setAttribute("peso", textoPeso);
            mostrarFormulario(request, response);
            return;
        }
        // POST-redirect-GET: el resultado se ve en el historial
        request.getSession().setAttribute("aviso",
                "Tu IMC es " + imc + " (" + calculadora.clasificar(imc) + "). Medición guardada.");
        response.sendRedirect(request.getContextPath() + "/historial");
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        try {
            request.setAttribute("ultima", medicionDAO.obtenerUltima(usuario.getIdUsuario()));
        } catch (AccesoDatosException e) {
            request.setAttribute("error", "No fue posible conectar con la base de datos.");
        }
        request.getRequestDispatcher(VISTA).forward(request, response);
    }
}
