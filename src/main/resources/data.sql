-- Datos maestros originales

INSERT INTO alcance_sismo (DESCRIPCION, NOMBRE) VALUES
  ('Local, bajo 50km','Local'),
  ('Regional, 50–200km','Regional'),
  ('Global, >200km','Global'),
  ('Extra Regional, 200–500km','ExtraRegional');

INSERT INTO clasificacion_sismo (KM_PROFUNDIDAD_DESDE, KM_PROFUNDIDAD_HASTA, NOMBRE) VALUES
  (0,70,'Superficial'),
  (70,300,'Intermedio'),
  (300,700,'Profundo'),
  (700, 1000, 'Muy Profundo');

INSERT INTO origen_de_generacion (DESCRIPCION, NOMBRE) VALUES
  ('Tectónico','Tectónico'),
  ('Volcánico','Volcánico'),
  ('Artificial','Artificial'),
  ('Inducido','Inducido');

INSERT INTO estado (AMBITO, NOMBRE_ESTADO) VALUES
  ('EventoSismico','AutoDetectado'),
  ('EventoSismico','PendienteDeRevision'),
  ('EventoSismico','BloqueadoEnRevision'),
  ('EventoSismico','Rechazado'),
  ('EventoSismico','Aprobado'),
  ('EventoSismico','EnRevisionFinal');

INSERT INTO tipo_de_dato (DENOMINACION, NOMBRE_UNIDAD_MEDIDA, VALOR_UMBRAL) VALUES
  ('Aceleración','g',0.01),
  ('Velocidad','m/s',0.001),
  ('Longitud','mm',0.1),
  ('Frecuencia', 'Hz', 0.05);

INSERT INTO estacion_sismologica (CODIGO_ESTACION, DOCUMENTO_CERT_ADQ, FECHA_SOLICITUD_CERT, LATITUD, LONGITUD, NOMBRE, NRO_CERT_ADQUISICION) VALUES
  ('EST-001','DOC-1001','2023-01-15',40.41,-3.70,'Madrid Centro','CERT-2001'),
  ('EST-002','DOC-1002','2023-02-20',41.38,2.17,'Barcelona','CERT-2002'),
  ('EST-003','DOC-1003','2023-03-05',37.98,-1.13,'Alicante','CERT-2003'),
  ('EST-004','DOC-1004','2023-04-10',43.36,-5.85,'Oviedo','CERT-2004'),
  ('EST-005','DOC-1005','2023-05-15',39.47,-0.38,'Valencia','CERT-2005');

INSERT INTO magnitud_ritcher (NUMERO, DESCRIPCION_MAGNITUD) VALUES
  (4.5, 'Ritcher 4.5'),
  (5.0, 'Ritcher 5.0'),
  (3.8, 'Ritcher 3.8'),
  (6.2, 'Ritcher 6.2'),
  (4.0, 'Ritcher 4.0');

INSERT INTO empleado (APELLIDO, MAIL, NOMBRE, TELEFONO) VALUES
  ('Perez', 'juan.perez@example.com', 'Juan', 1122334455),
  ('Gomez', 'maria.gomez@example.com', 'Maria', 9876543210),
  ('Lopez', 'ana.lopez@example.com', 'Ana', 2233445566),
  ('Martinez', 'carlos.martinez@example.com', 'Carlos', 3344556677);

INSERT INTO sismografo (IDENTIFICADOR, ID_ADQUISICION, NRO_SERIE, CODIGO_ESTACION) VALUES
  ('SISMO-A',101,'SN-A001','EST-001'),
  ('SISMO-B',102,'SN-B001','EST-002'),
  ('SISMO-C',103,'SN-C001','EST-003'),
  ('SISMO-D',104,'SN-D001','EST-004'),
  ('SISMO-E',105,'SN-E001','EST-005');


-- Insert EventoSismico records (order matters for foreign keys)
INSERT INTO evento_sismico (
    FECHA_HORA_FIN,
    FECHA_HORA_OCURRENCIA,
    LATITUD_EPICENTRO,
    LONGITUD_EPICENTRO,
    LATITUD_HIPOCENTRO,
    LONGITUD_HIPOCENTRO,
    VALOR_MAGNITUD,
    ALCANCE_SISMO_ID,
    CLASIFICACION_SISMO_ID,
    ESTADO_ACTUAL_ID,
    MAGNITUD_RITCHER_ID,
    ORIGEN_GENERACION_ID
) VALUES
  ('2025-05-01 08:01:00','2025-05-01 08:00:00',40.42,-3.69,39.90,-3.50,4.5,1,1,1,1,1),
  ('2025-05-01 09:01:00','2025-05-01 09:00:00',41.39,2.16,40.00,1.50,5.0,2,2,2,2,2),
  ('2025-05-01 10:01:00','2025-05-01 10:00:00',37.99,-1.12,36.50,-1.00,3.8,3,3,1,3,3),
  ('2025-05-02 08:01:00','2025-05-02 08:00:00',43.37,-5.84,43.00,-5.50,6.2,4,4,1,4,4),
  ('2025-05-02 09:01:00','2025-05-02 09:00:00',39.48,-0.37,39.00,-0.30,4.0,1,1,2,5,1),
  ('2025-05-02 10:01:00','2025-05-02 10:00:00',40.50,-3.60,40.10,-3.20,5.5,2,3,1,2,2),
  ('2025-05-02 11:01:00','2025-05-02 11:00:00',41.00,2.00,40.50,1.80,3.9,3,2,3,3,3),
  ('2025-05-02 12:01:00','2025-05-02 12:00:00',38.00,-1.10,37.50,-1.00,4.2,1,1,2,1,1);


--- NEW DATA STRUCTURE DEMONSTRATION ---

-- EventoSismico (1) -> (Many) SerieTemporal (1) -> (Many) MuestraSismica (1) -> (Many) DetalleMuestraSismica

-- SerieTemporal 1 (linked to EventoSismico ID 1)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('NORMAL_OPERACION', '2025-05-28 08:00:00', '2025-05-28 08:00:10', 100.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 1);
SET @serie_id_1 = LAST_INSERT_ID();

  -- MuestraSismica for SerieTemporal 1
  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 08:00:01', @serie_id_1);
  SET @muestra_id_s1_m1 = LAST_INSERT_ID();
    -- DetalleMuestraSismica for MuestraSismica @muestra_id_s1_m1
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.015, @muestra_id_s1_m1),
      (2, 0.003, @muestra_id_s1_m1);

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 08:00:02', @serie_id_1);
  SET @muestra_id_s1_m2 = LAST_INSERT_ID();
    -- DetalleMuestraSismica for MuestraSismica @muestra_id_s1_m2
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (3, 0.120, @muestra_id_s1_m2),
      (4, 0.060, @muestra_id_s1_m2);


-- SerieTemporal 2 (linked to EventoSismico ID 1, demonstrating multiple series per event)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('ALERTA_TEMPORAL', '2025-05-28 09:00:00', '2025-05-28 09:00:10', 120.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 1);
SET @serie_id_2 = LAST_INSERT_ID();

  -- MuestraSismica for SerieTemporal 2
  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 09:00:01', @serie_id_2);
  SET @muestra_id_s2_m1 = LAST_INSERT_ID();
    -- DetalleMuestraSismica for MuestraSismica @muestra_id_s2_m1
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.025, @muestra_id_s2_m1),
      (2, 0.005, @muestra_id_s2_m1);

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 09:00:02', @serie_id_2);
  SET @muestra_id_s2_m2 = LAST_INSERT_ID();
    -- DetalleMuestraSismica for MuestraSismica @muestra_id_s2_m2
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (3, 0.150, @muestra_id_s2_m2),
      (4, 0.045, @muestra_id_s2_m2);


-- SerieTemporal 3 (linked to EventoSismico ID 2)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('ALARMA_ACTIVA', '2025-05-28 10:00:00', '2025-05-28 10:00:10', 50.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 2);
SET @serie_id_3 = LAST_INSERT_ID();

  -- MuestraSismica for SerieTemporal 3
  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 10:00:01', @serie_id_3);
  SET @muestra_id_s3_m1 = LAST_INSERT_ID();
    -- DetalleMuestraSismica for MuestraSismica @muestra_id_s3_m1
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.030, @muestra_id_s3_m1),
      (2, 0.006, @muestra_id_s3_m1);

-- SerieTemporal 4 (linked to EventoSismico ID 2, using a different Sismografo/Estacion)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('NORMAL_OPERACION', '2025-05-28 10:30:00', '2025-05-28 10:30:10', 80.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 2);
SET @serie_id_4 = LAST_INSERT_ID();

  -- MuestraSismica for SerieTemporal 4
  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 10:30:01', @serie_id_4);
  SET @muestra_id_s4_m1 = LAST_INSERT_ID();
    -- DetalleMuestraSismica for MuestraSismica @muestra_id_s4_m1
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.020, @muestra_id_s4_m1),
      (2, 0.004, @muestra_id_s4_m1);

-- SerieTemporal 5 (linked to EventoSismico ID 2, using another different Sismografo/Estacion)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('NORMAL_OPERACION', '2025-05-28 11:00:00', '2025-05-28 11:00:10', 90.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 2);
SET @serie_id_5 = LAST_INSERT_ID();

  -- MuestraSismica for SerieTemporal 5
  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 11:00:01', @serie_id_5);
  SET @muestra_id_s5_m1 = LAST_INSERT_ID();
    -- DetalleMuestraSismica for MuestraSismica @muestra_id_s5_m1
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.018, @muestra_id_s5_m1),
      (2, 0.0035, @muestra_id_s5_m1);

-- SerieTemporal 6 (linked to EventoSismico ID 3)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('NORMAL_OPERACION', '2025-05-28 12:00:00', '2025-05-28 12:00:10', 110.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 3);
SET @serie_id_6 = LAST_INSERT_ID();

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 12:00:01', @serie_id_6);
  SET @muestra_id_s6_m1 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.010, @muestra_id_s6_m1),
      (2, 0.002, @muestra_id_s6_m1);

-- SerieTemporal 7 (linked to EventoSismico ID 4)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('ALARMA_ACTIVA', '2025-05-28 13:00:00', '2025-05-28 13:00:10', 150.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 4);
SET @serie_id_7 = LAST_INSERT_ID();

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 13:00:01', @serie_id_7);
  SET @muestra_id_s7_m1 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (3, 0.200, @muestra_id_s7_m1),
      (4, 0.080, @muestra_id_s7_m1);

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 13:00:02', @serie_id_7);
  SET @muestra_id_s7_m2 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.035, @muestra_id_s7_m2),
      (2, 0.007, @muestra_id_s7_m2);

-- SerieTemporal 8 (linked to EventoSismico ID 5)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('NORMAL_OPERACION', '2025-05-28 14:00:00', '2025-05-28 14:00:10', 95.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 5);
SET @serie_id_8 = LAST_INSERT_ID();

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 14:00:01', @serie_id_8);
  SET @muestra_id_s8_m1 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.012, @muestra_id_s8_m1);

-- SerieTemporal 9 (linked to EventoSismico ID 6)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('ALERTA_TEMPORAL', '2025-05-28 15:00:00', '2025-05-28 15:00:10', 115.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 6);
SET @serie_id_9 = LAST_INSERT_ID();

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 15:00:01', @serie_id_9);
  SET @muestra_id_s9_m1 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (2, 0.004, @muestra_id_s9_m1);
  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 15:00:02', @serie_id_9);
  SET @muestra_id_s9_m2 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.022, @muestra_id_s9_m2);


-- SerieTemporal 10 (linked to EventoSismico ID 7)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('NORMAL_OPERACION', '2025-05-28 16:00:00', '2025-05-28 16:00:10', 105.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 7);
SET @serie_id_10 = LAST_INSERT_ID();

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 16:00:01', @serie_id_10);
  SET @muestra_id_s10_m1 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (3, 0.100, @muestra_id_s10_m1);


-- SerieTemporal 11 (linked to EventoSismico ID 8)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('ALARMA_ACTIVA', '2025-05-28 17:00:00', '2025-05-28 17:00:10', 130.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 8);
SET @serie_id_11 = LAST_INSERT_ID();

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 17:00:01', @serie_id_11);
  SET @muestra_id_s11_m1 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.028, @muestra_id_s11_m1),
      (4, 0.055, @muestra_id_s11_m1);

-- SerieTemporal 12 (linked to EventoSismico ID 1)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('NORMAL_OPERACION', '2025-05-28 18:00:00', '2025-05-28 18:00:10', 90.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 1);
SET @serie_id_12 = LAST_INSERT_ID();

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 18:00:01', @serie_id_12);
  SET @muestra_id_s12_m1 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (1, 0.017, @muestra_id_s12_m1);

-- SerieTemporal 13 (linked to EventoSismico ID 2)
INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_SISMOGRAFO, EVENTO_SISMICO_ID)
VALUES ('ALERTA_TEMPORAL', '2025-05-28 19:00:00', '2025-05-28 19:00:10', 100.0, (SELECT IDENTIFICADOR FROM sismografo ORDER BY RAND() LIMIT 1), 2);
SET @serie_id_13 = LAST_INSERT_ID();

  INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_SERIE) VALUES ('2025-05-28 19:00:01', @serie_id_13);
  SET @muestra_id_s13_m1 = LAST_INSERT_ID();
    INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR, ID_MUESTRA_SISMICA) VALUES
      (2, 0.003, @muestra_id_s13_m1);


-- --- END NEW DATA STRUCTURE DEMONSTRATION ---


-- Evento 1: Estado actual = Aprobado (NO pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id, evento_sismico_id)
VALUES (5, '2025-05-01 08:00:00', NULL, 1, 1);

-- Evento 2: Estado actual = PendienteDeRevision (SÍ pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id, evento_sismico_id)
VALUES (2, '2025-05-01 09:00:00', NULL, 2, 2);

-- Evento 3: Estado actual = Rechazado (NO pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id, evento_sismico_id)
VALUES (4, '2025-05-01 10:00:00', NULL, 3, 3);

-- Evento 4: Estado actual = AutoDetectado (SÍ pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id, evento_sismico_id)
VALUES (1, '2025-05-02 08:00:00', NULL, 4, 4);

-- Evento 5: Estado actual = BloqueadoEnRevision (NO pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id, evento_sismico_id)
VALUES (3, '2025-05-02 09:00:00', NULL, 1, 5);

-- Evento 6: Estado actual = PendienteDeRevision (NO pasa el filtro por fechaHoraFin NO nula)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id, evento_sismico_id)
VALUES (2, '2025-05-02 10:00:00', '2025-05-02 10:00:00', 2, 6);

-- Evento 7: Estado actual = EnRevisionFinal (NO pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id, evento_sismico_id)
VALUES (6, '2025-05-02 11:00:00', NULL, 3, 7);

-- Evento 8: Estado actual = AutoDetectado (SÍ pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id, evento_sismico_id)
VALUES (1, '2025-05-02 12:00:00', NULL, 4, 8);

-- Usuario Logueado (AS)
-- Asumiendo que ya existe un empleado con id = 1
INSERT INTO usuario (nombre_usuario, contraseña, empleado_id)
VALUES ('juanPerez', 'password123', 1);
