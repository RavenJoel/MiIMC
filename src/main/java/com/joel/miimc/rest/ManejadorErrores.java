package com.joel.miimc.rest;

import com.joel.miimc.dao.AccesoDatosException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Convierte cualquier excepción del servicio REST en una respuesta de error
 * con su código HTTP, en JSON o en XML según lo que pidió el cliente.
 */
@Provider
public class ManejadorErrores implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = Logger.getLogger(ManejadorErrores.class.getName());

    @Context
    private HttpHeaders headers;
    @Context
    private UriInfo uriInfo;

    @Override
    public jakarta.ws.rs.core.Response toResponse(Exception e) {
        int estado;
        String mensaje;
        if (e instanceof ApiException api) {
            estado = api.getEstado();
            mensaje = api.getMessage();
        } else if (e instanceof AccesoDatosException) {
            estado = 503;
            mensaje = "La base de datos no está disponible. Intenta más tarde.";
        } else if (e instanceof WebApplicationException web) {
            estado = web.getResponse().getStatus();
            mensaje = estado == 404 ? "El recurso solicitado no existe." : e.getMessage();
        } else {
            estado = 500;
            mensaje = "Ocurrió un error inesperado.";
            LOGGER.log(Level.SEVERE, "Error no controlado en la API", e);
        }
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("estado", estado);
        datos.put("error", nombre(estado));
        datos.put("mensaje", mensaje);
        datos.put("ruta", uriInfo == null ? "" : uriInfo.getPath());
        String formato = uriInfo == null ? null : uriInfo.getQueryParameters().getFirst("formato");
        return Representacion.construir(estado, "error", datos, formato, headers);
    }

    private static String nombre(int estado) {
        return switch (estado) {
            case 401 -> "No autorizado";
            case 404 -> "No encontrado";
            case 503 -> "Servicio no disponible";
            default -> "Error";
        };
    }
}
