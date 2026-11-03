package com.joel.miimc.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Activa JAX-RS (servicios web REST) bajo la ruta /api.
 */
@ApplicationPath("api")
public class ApplicationConfig extends Application {
}
