-- =====================================================================
-- Base de datos MiIMC (MySQL 8 / 9)
-- Ejecutar como root:  mysql -u root < sql/miimc.sql
-- =====================================================================
DROP DATABASE IF EXISTS miimc;
CREATE DATABASE miimc CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE miimc;

CREATE TABLE usuario (
  id_usuario       INT AUTO_INCREMENT PRIMARY KEY,
  nombre_completo  VARCHAR(100) NOT NULL,
  nombre_usuario   VARCHAR(30)  NOT NULL UNIQUE,
  contrasena_hash  VARCHAR(255) NOT NULL,
  edad             TINYINT      NOT NULL CHECK (edad >= 15),
  sexo             CHAR(1)      NOT NULL CHECK (sexo IN ('F', 'M')),
  estatura         DECIMAL(3,2) NOT NULL
                   CHECK (estatura BETWEEN 1.00 AND 2.50),
  fecha_registro   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clasificacion_imc (
  id_clasificacion TINYINT PRIMARY KEY,
  nombre           VARCHAR(30)  NOT NULL,
  imc_minimo       DECIMAL(4,1) NOT NULL,
  imc_maximo       DECIMAL(4,1) NULL
);

CREATE TABLE medicion (
  id_medicion      INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario       INT          NOT NULL,
  peso             DECIMAL(5,2) NOT NULL CHECK (peso > 0),
  estatura         DECIMAL(3,2) NOT NULL
                   CHECK (estatura BETWEEN 1.00 AND 2.50),
  imc              DECIMAL(4,1) NOT NULL,
  id_clasificacion TINYINT      NOT NULL,
  fecha_medicion   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
    ON DELETE CASCADE,
  FOREIGN KEY (id_clasificacion)
    REFERENCES clasificacion_imc (id_clasificacion),
  INDEX idx_usuario_fecha (id_usuario, fecha_medicion)
);

INSERT INTO clasificacion_imc VALUES
  (1, 'Bajo peso',           0.0, 18.4),
  (2, 'Peso normal',        18.5, 24.9),
  (3, 'Sobrepeso',          25.0, 29.9),
  (4, 'Obesidad grado I',   30.0, 34.9),
  (5, 'Obesidad grado II',  35.0, 39.9),
  (6, 'Obesidad grado III', 40.0, NULL);

-- Usuario de la aplicación: solo los permisos que necesita
DROP USER IF EXISTS 'miimc_app'@'localhost';
CREATE USER 'miimc_app'@'localhost' IDENTIFIED BY 'MiIMC2026';
GRANT SELECT, INSERT, UPDATE, DELETE ON miimc.* TO 'miimc_app'@'localhost';
FLUSH PRIVILEGES;
