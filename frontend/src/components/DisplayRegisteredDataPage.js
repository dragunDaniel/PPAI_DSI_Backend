import React from "react";
import {
  purpleButtonStyle,
  secondaryButtonStyle,
  successMessageStyle,
  errorMessageStyle,
} from "../styles/commonStyles";

function DisplayRegisteredDataPage({
  registeredData,
  confirmSuccess,
  confirmError,
  onGoBack,
}) {
  return (
    <div style={{ padding: "20px 0" }}>
      <h1 style={{ color: "#2c3e50", marginBottom: "20px" }}>
        Detalle de la Operación
      </h1>
      {confirmSuccess && <p style={successMessageStyle}>{confirmSuccess}</p>}
      {confirmError && <p style={errorMessageStyle}>Error: {confirmError}</p>}

      {registeredData ? (
        <div style={{ marginBottom: "20px" }}>
          <h3
            style={{
              color: "#34495e",
              marginBottom: "10px",
              borderBottom: "1px solid #eee",
              paddingBottom: "5px",
            }}
          >
            Datos Generales del Sismo:
          </h3>
          <div
            style={{
              padding: "15px",
              border: "1px solid #e0e0e0",
              borderRadius: "8px",
              backgroundColor: "#fdfdfd",
              boxShadow: "0 2px 4px rgba(0,0,0,0.05)",
            }}
          >
            <p>
              <strong>Alcance Sismo:</strong>{" "}
              {registeredData.alcanceSismoNombre || "N/A"}
            </p>
            <p>
              <strong>Clasificación Sismo:</strong>{" "}
              {registeredData.clasificacionSismoNombre || "N/A"}
            </p>
            <p>
              <strong>Origen Generación:</strong>{" "}
              {registeredData.origenGeneracionNombre || "N/A"}
            </p>
          </div>

          <h3
            style={{
              color: "#34495e",
              marginTop: "30px",
              marginBottom: "10px",
              borderBottom: "1px solid #eee",
              paddingBottom: "5px",
            }}
          >
            Series Temporales con Detalles:
          </h3>
          {registeredData.seriesTemporalesConDetalles &&
          registeredData.seriesTemporalesConDetalles.length > 0 ? (
            <div
              style={{
                maxHeight: "500px",
                overflowY: "auto",
                border: "1px solid #e0e0e0",
                borderRadius: "8px",
                padding: "10px",
                backgroundColor: "#f9f9f9",
              }}
            >
              {registeredData.seriesTemporalesConDetalles.map((serie) => (
                <div
                  key={serie.id}
                  style={{
                    border: "1px solid #d0d0d0",
                    padding: "15px",
                    margin: "10px 0",
                    borderRadius: "8px",
                    backgroundColor: "#eaf6ff",
                    boxShadow: "0 2px 4px rgba(0,0,0,0.08)",
                  }}
                >
                  <h4>
                    Serie Temporal ID: <strong>{serie.id}</strong>
                  </h4>
                  <p>
                    <strong>Condición Alarma:</strong>{" "}
                    {serie.condicionAlarma || "N/A"}
                  </p>
                  <p>
                    <strong>Fecha y Hora Registros:</strong>{" "}
                    {serie.fechaHoraRegistros
                      ? new Date(serie.fechaHoraRegistros).toLocaleString()
                      : "N/A"}
                  </p>
                  <p>
                    <strong>Sismógrafo Identificador:</strong>{" "}
                    {serie.sismografoIdentificador || "N/A"}
                  </p>
                  <p>
                    <strong>Código Estación:</strong>{" "}
                    {serie.codigoEstacion || "N/A"}
                  </p>

                  <h5
                    style={{
                      marginTop: "20px",
                      marginBottom: "10px",
                      borderBottom: "1px dotted #ccc",
                      paddingBottom: "5px",
                    }}
                  >
                    Muestras Sísmicas:
                  </h5>
                  {serie.muestrasSismicas &&
                  serie.muestrasSismicas.length > 0 ? (
                    <div>
                      {serie.muestrasSismicas.map((muestra) => (
                        <div
                          key={muestra.id}
                          style={{
                            border: "1px solid #c0e0c0",
                            padding: "10px",
                            margin: "10px 0 0 20px",
                            borderRadius: "6px",
                            backgroundColor: "#f0fff0",
                            boxShadow: "0 1px 2px rgba(0,0,0,0.05)",
                          }}
                        >
                          <h6>
                            Muestra Sísmica ID: <strong>{muestra.id}</strong>
                          </h6>
                          <p>
                            <strong>Fecha y Hora Muestra:</strong>{" "}
                            {muestra.fechaHoraMuestra
                              ? new Date(muestra.fechaHoraMuestra).toLocaleString()
                              : "N/A"}
                          </p>

                          <h6
                            style={{
                              marginTop: "15px",
                              marginBottom: "5px",
                              borderBottom: "1px dotted #ddd",
                              paddingBottom: "3px",
                            }}
                          >
                            Detalles de la Muestra:
                          </h6>
                          {muestra.detallesMuestra &&
                          muestra.detallesMuestra.length > 0 ? (
                            <div>
                              {muestra.detallesMuestra.map((detalle) => (
                                <div
                                  key={detalle.id}
                                  style={{
                                    border: "1px solid #ffe0b2",
                                    padding: "8px",
                                    margin: "8px 0 0 20px",
                                    borderRadius: "5px",
                                    backgroundColor: "#fffaf0",
                                  }}
                                >
                                  <p style={{ margin: 0 }}>
                                    <strong>ID Detalle:</strong> {detalle.id}
                                  </p>
                                  <p style={{ margin: 0 }}>
                                    <strong>Denominación:</strong>{" "}
                                    {detalle.denominacion || "N/A"}
                                  </p>
                                  <p style={{ margin: 0 }}>
                                    <strong>Valor:</strong>{" "}
                                    {detalle.valor !== null &&
                                    detalle.valor !== undefined
                                      ? detalle.valor.toFixed(6)
                                      : "N/A"}
                                  </p>
                                </div>
                              ))}
                            </div>
                          ) : (
                            <p style={{ marginLeft: "20px", color: "#777" }}>
                              No hay detalles para esta muestra.
                            </p>
                          )}
                        </div>
                      ))}
                    </div>
                  ) : (
                    <p style={{ marginLeft: "20px", color: "#777" }}>
                      No hay muestras sísmicas para esta serie temporal.
                    </p>
                  )}
                </div>
              ))}
            </div>
          ) : (
            <p style={{ textAlign: "center", color: "#666" }}>
              No hay series temporales registradas para este evento.
            </p>
          )}
        </div>
      ) : (
        !confirmSuccess &&
        !confirmError && (
          <p style={{ textAlign: "center", color: "#666" }}>
            No se encontraron datos para mostrar.
          </p>
        )
      )}

      <div
        style={{
          marginTop: "30px",
          display: "flex",
          justifyContent: "center",
          gap: "10px",
        }}
      >
        <button style={purpleButtonStyle}>
          Visualizar en un mapa evento sismico y estaciones involucradas
        </button>
        <button onClick={onGoBack} style={secondaryButtonStyle}>
          No visualizar
        </button>
      </div>
    </div>
  );
}

export default DisplayRegisteredDataPage;