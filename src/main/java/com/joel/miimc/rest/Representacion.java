package com.joel.miimc.rest;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Arma la respuesta en JSON o XML. El cliente elige con el encabezado Accept
 * o con el parámetro ?formato=json|xml. Por defecto responde JSON.
 */
public final class Representacion {

    private Representacion() {
    }

    public static boolean quiereXml(String formato, HttpHeaders headers) {
        if (formato != null) {
            return "xml".equalsIgnoreCase(formato);
        }
        for (MediaType tipo : headers.getAcceptableMediaTypes()) {
            if (tipo.isCompatible(MediaType.APPLICATION_JSON_TYPE) && !tipo.isWildcardType()) {
                return false;
            }
            if (tipo.isCompatible(MediaType.APPLICATION_XML_TYPE) && !tipo.isWildcardType()) {
                return true;
            }
        }
        return false;
    }

    public static Response construir(int estado, String raiz, Map<String, Object> datos,
            String formato, HttpHeaders headers) {
        if (quiereXml(formato, headers)) {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + aXml(raiz, datos, "");
            return Response.status(estado).type(MediaType.APPLICATION_XML + ";charset=UTF-8").entity(xml).build();
        }
        // JSON-B (incluido en GlassFish) convierte el mapa a JSON
        return Response.status(estado).type(MediaType.APPLICATION_JSON + ";charset=UTF-8").entity(datos).build();
    }

    /** Convierte mapas, listas y valores simples a XML. */
    private static String aXml(String etiqueta, Object valor, String sangria) {
        StringBuilder sb = new StringBuilder();
        if (valor instanceof Map<?, ?> mapa) {
            sb.append(sangria).append('<').append(etiqueta).append(">\n");
            for (Map.Entry<?, ?> e : mapa.entrySet()) {
                sb.append(aXml(String.valueOf(e.getKey()), e.getValue(), sangria + "  "));
            }
            sb.append(sangria).append("</").append(etiqueta).append(">\n");
        } else if (valor instanceof List<?> lista) {
            String hijo = switch (etiqueta) { case "mediciones" -> "medicion"; case "enlaces" -> "enlace"; default -> "elemento"; };
            sb.append(sangria).append('<').append(etiqueta).append(">\n");
            for (Object item : lista) {
                sb.append(aXml(hijo, item, sangria + "  "));
            }
            sb.append(sangria).append("</").append(etiqueta).append(">\n");
        } else if (valor != null) {
            sb.append(sangria).append('<').append(etiqueta).append('>')
              .append(escapar(String.valueOf(valor)))
              .append("</").append(etiqueta).append(">\n");
        }
        return sb.toString();
    }

    private static String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
