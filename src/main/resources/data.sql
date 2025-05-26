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
  ('Desplazamiento','mm',0.1),
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

INSERT INTO detalle_muestra_sismica (ID_TIPO, VALOR) VALUES
  (1,0.015),
  (1,0.020),
  (2,0.003),
  (3,0.120),
  (2,0.005),
  (1,0.025),
  (4,0.060),
  (2,0.004),
  (3,0.150),
  (4,0.045);

INSERT INTO muestra_sismica (FECHA_HORA_MUESTRA, ID_DETALLE_MUESTRA) VALUES
  ('2025-05-01 08:00:00',1),
  ('2025-05-01 08:00:01',2),
  ('2025-05-01 08:00:02',3),
  ('2025-05-01 08:00:03',4),
  ('2025-05-01 08:00:04',5),
  ('2025-05-02 08:00:00',6),
  ('2025-05-02 08:00:01',7),
  ('2025-05-02 08:00:02',8),
  ('2025-05-02 08:00:03',9),
  ('2025-05-02 08:00:04',10);

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

INSERT INTO serie_temporal (CONDICION_ALARMA, FECHA_HORA_INICIO_REG_MUESTREO, FECHA_HORA_REGISTROS, FRECUENCIA_MUESTREO, ID_MUESTRA_SISMICA, ID_SISMOGRAFO, EVENTO_SISMICO_ID) VALUES
  ('OK','2025-05-01 08:00:00','2025-05-01 08:00:10',100.0,1,'SISMO-A',1),
  ('ALARMA','2025-05-01 09:00:00','2025-05-01 09:00:10',50.0,2,'SISMO-B',2),
  ('OK','2025-05-01 10:00:00','2025-05-01 10:00:10',200.0,3,'SISMO-C',3),
  ('OK','2025-05-02 08:00:00','2025-05-02 08:00:10',120.0,6,'SISMO-D',4),
  ('ALARMA','2025-05-02 09:00:00','2025-05-02 09:00:10',80.0,7,'SISMO-E',5),
  ('OK','2025-05-02 10:00:00','2025-05-02 10:00:10',60.0,8,'SISMO-A',6),
  ('OK','2025-05-02 11:00:00','2025-05-02 11:00:10',90.0,9,'SISMO-B',7),
  ('ALARMA','2025-05-02 12:00:00','2025-05-02 12:00:10',110.0,10,'SISMO-C',8);

-- Evento 1: Estado actual = Aprobado (NO pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id)
VALUES (5, '2025-05-01 08:00:00', NULL, 1);

-- Evento 2: Estado actual = PendienteDeRevision (SÍ pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id)
VALUES (2, '2025-05-01 09:00:00', NULL, 2);

-- Evento 3: Estado actual = Rechazado (NO pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id)
VALUES (4, '2025-05-01 10:00:00', NULL, 3);

-- Evento 4: Estado actual = AutoDetectado (SÍ pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id)
VALUES (1, '2025-05-02 08:00:00', NULL, 4);

-- Evento 5: Estado actual = BloqueadoEnRevision (NO pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id)
VALUES (3, '2025-05-02 09:00:00', NULL, 1);

-- Evento 6: Estado actual = PendienteDeRevision (SÍ pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id)
VALUES (2, '2025-05-02 10:00:00', NULL, 2);

-- Evento 7: Estado actual = EnRevisionFinal (NO pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id)
VALUES (6, '2025-05-02 11:00:00', NULL, 3);

-- Evento 8: Estado actual = AutoDetectado (SÍ pasa el filtro)
INSERT INTO cambio_estado (estado_id, fecha_hora_inicio, fecha_hora_fin, responsable_id)
VALUES (1, '2025-05-02 12:00:00', NULL, 4);
