package com.joel.miimc.rest;

import com.joel.miimc.dao.MedicionDAO;
import com.joel.miimc.modelo.Medicion;
import com.joel.miimc.modelo.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio web REST del historial de mediciones del usuario en sesión.
 *   GET /api/historial        → IMC actual y todas las mediciones
 *   GET /api/historial/{id}   → una medición
 * Responde JSON o XML e incluye enlaces (hipermedia) a los recursos relacionados.
 */
@Path("historial")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class HistorialResource {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final MedicionDAO medicionDAO = new MedicionDAO();

    @Context
    private HttpServletRequest request;
    @Context
    private HttpHeaders headers;
    @Context
    private UriInfo uriInfo;

    @GET
    public Response obtenerHistorial(@QueryParam("formato") String formato) {
        Usuario usuario = usuarioEnSesion();
        List<Medicion> mediciones = medicionDAO.listarPorUsuario(usuario.getIdUsuario());

        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("usuario", usuario.getNombreUsuario());
        datos.put("nombre", usuario.getNombreCompleto());
        datos.put("estatura", usuario.getEstatura());
        datos.put("total", mediciones.size());
        datos.put("actual", mediciones.isEmpty() ? null : aMapa(mediciones.get(0)));
        List<Object> lista = new ArrayList<>();
        for (Medicion m : mediciones) {
            lista.add(aMapa(m));
        }
        datos.put("mediciones", lista);
        datos.put("enlaces", List.of(
                enlace("self", base() + "historial"),
                enlace("calcular", contexto() + "/calcular"),
                enlace("logout", contexto() + "/logout")));
        return Representacion.construir(200, "historial", datos, formato, headers);
    }

    @GET
    @Path("{id}")
    public Response obtenerMedicion(@PathParam("id") int id, @QueryParam("formato") String formato) {
        Usuario usuario = usuarioEnSesion();
        Medicion m = medicionDAO.buscarPorId(usuario.getIdUsuario(), id);
        if (m == null) {
            throw new ApiException(404, "No existe la medición " + id + " en tu historial.");
        }
        Map<String, Object> datos = aMapa(m);
        datos.put("enlaces", List.of(
                enlace("self", base() + "historial/" + id),
                enlace("historial", base() + "historial")));
        return Representacion.construir(200, "medicion", datos, formato, headers);
    }

    private Usuario usuarioEnSesion() {
        HttpSession sesion = request.getSession(false);
        Usuario usuario = (sesion == null) ? null : (Usuario) sesion.getAttribute("usuario");
        if (usuario == null) {
            throw new ApiException(401, "Inicia sesión para consultar tu historial.");
        }
        return usuario;
    }

    private Map<String, Object> aMapa(Medicion m) {
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.put("id", m.getIdMedicion());
        mapa.put("fecha", m.getFechaMedicion().format(FECHA));
        mapa.put("peso", m.getPeso());
        mapa.put("estatura", m.getEstatura());
        mapa.put("imc", m.getImc());
        mapa.put("clasificacion", m.getClasificacion());
        mapa.put("enlaces", List.of(enlace("self", base() + "historial/" + m.getIdMedicion())));
        return mapa;
    }

    private static Map<String, Object> enlace(String rel, String href) {
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("rel", rel);
        e.put("href", href);
        return e;
    }

    private String base() {
        return uriInfo.getBaseUri().getPath();   // /MiIMC/api/
    }

    private String contexto() {
        return request.getContextPath();          // /MiIMC
    }
}
