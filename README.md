# MiIMC – Control del Índice de Masa Corporal

Aplicación web para calcular el **índice de masa corporal (IMC)** y llevar el historial de mediciones de cada usuario.
Evidencia final de **Computación avanzada en Java** (Universidad Tecmilenio) – Joel Antonio Valdez Gómez.

## Funcionalidad
- Registro único con nombre completo, edad, sexo, estatura, nombre de usuario y contraseña (cifrada con PBKDF2).
- Validaciones: estatura entre 1.00 y 2.50 m, edad desde 15 años y masa corporal mayor a 0.
- Inicio de sesión obligatorio para calcular el IMC.
- IMC actual, gráfica de evolución e historial de mediciones.
- El historial se consume de la base de datos a través de un servicio web REST (JSON o XML).

## Tecnologías
| Componente | Versión |
|---|---|
| Java / Jakarta EE | JDK 17+ / Jakarta EE 11 (Servlets, JSP, JSTL, JAX-RS, JSON-B) |
| Servidor | Eclipse GlassFish 8 |
| Base de datos | MySQL 8 o superior, JDBC con `mysql-connector-j` |
| Construcción | Maven |
| Interfaz | Bootstrap 5 |

## Estructura (MVC)
```
src/main/java/com/joel/miimc/
├── modelo/       Usuario, Medicion, CalculadoraIMC, Validador, SeguridadUtil
├── dao/          ConexionBD, UsuarioDAO, MedicionDAO, AccesoDatosException
├── controlador/  RegistroServlet, LoginServlet, LogoutServlet, CalculoIMCServlet, HistorialServlet, FiltroSesion
└── rest/         ApplicationConfig, HistorialResource, Representacion, ManejadorErrores, ApiException
src/main/webapp/WEB-INF/vistas/   login.jsp, registro.jsp, calcular.jsp, historial.jsp, error.jsp
sql/miimc.sql                     script de la base de datos
```

## Servicio REST
| Método y ruta | Descripción |
|---|---|
| `GET /MiIMC/api/historial` | IMC actual y todas las mediciones del usuario en sesión |
| `GET /MiIMC/api/historial/{id}` | Una medición |

Formato con el encabezado `Accept` o con `?formato=json|xml`. Errores: 401 (sin sesión), 404 (no existe), 503 (base de datos no disponible).

## Cómo ejecutarla
1. Crear la base de datos: `mysql -u root < sql/miimc.sql`
2. Revisar la conexión en `src/main/resources/db.properties`.
3. Abrir el proyecto en NetBeans, ejecutar en GlassFish y entrar a `http://localhost:8080/MiIMC/`.

Más detalle en la [Wiki](../../wiki).

## Ramas
- `master`: versión estable (tag `v1.0.0`).
- `develop`: integración de todo el desarrollo.
- `feature/*`: una rama por recurso, unida a `develop` sin borrarse.
