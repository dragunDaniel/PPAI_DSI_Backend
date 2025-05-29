import React from "react";
import {
  primaryButtonStyle,
  secondaryButtonStyle,
  disabledButtonStyle,
  purpleButtonStyle,
  errorMessageStyle,
  successMessageStyle,
} from "../styles/commonStyles";

function ManualRevisionPage({
  seismicEvents,
  selectedEvent,
  isLoadingEvents,
  isConfirming,
  fetchError,
  confirmError,
  confirmSuccess,
  onSelectEvent,
  onConfirmSelection,
  onGoBack,
}) {
  return (
    <div style={{ padding: "20px 0" }}>
      <h1 style={{ color: "#2c3e50", marginBottom: "20px" }}>
        Registrar Revisión Manual
      </h1>

      {isLoadingEvents && (
        <p style={{ textAlign: "center", color: "#555" }}>
          Cargando eventos sísmicos...
        </p>
      )}

      {fetchError && <p style={errorMessageStyle}>Error: {fetchError}</p>}

      {!isLoadingEvents && !fetchError && seismicEvents.length === 0 && (
        <p style={{ textAlign: "center", color: "#666", padding: "20px 0" }}>
          No hay eventos sísmicos no revisados para mostrar.
        </p>
      )}

      {!isLoadingEvents && !fetchError && seismicEvents.length > 0 && (
        <>
          <div>
            <h2 style={{ color: "#34495e", marginBottom: "15px" }}>
              Seleccione un evento sísmico:
            </h2>
            <div
              style={{
                maxHeight: "400px",
                overflowY: "auto",
                border: "1px solid #e0e0e0",
                borderRadius: "8px",
                padding: "10px",
                backgroundColor: "#f9f9f9",
              }}
            >
              {seismicEvents.map((event) => (
                <div
                  key={event.id}
                  onClick={() => onSelectEvent(event)}
                  style={{
                    border:
                      selectedEvent && selectedEvent.id === event.id
                        ? "2px solid #007bff"
                        : "1px solid #e0e0e0",
                    padding: "15px",
                    margin: "10px 0",
                    borderRadius: "8px",
                    cursor: "pointer",
                    backgroundColor:
                      selectedEvent && selectedEvent.id === event.id
                        ? "#eaf6ff"
                        : "#ffffff",
                    boxShadow:
                      selectedEvent && selectedEvent.id === event.id
                        ? "0 4px 8px rgba(0, 123, 255, 0.2)"
                        : "0 2px 4px rgba(0,0,0,0.05)",
                    transition: "all 0.3s ease",
                    display: "flex",
                    flexDirection: "column",
                    gap: "5px",
                  }}
                >
                  <p style={{ margin: 0, fontWeight: "bold", color: "#333" }}>
                    ID: {event.id}
                  </p>
                  <p style={{ margin: 0 }}>
                    <strong style={{ color: "#0056b3" }}>
                      Fecha y hora ocurrencia:
                    </strong>{" "}
                    {event.fechaHoraOcurrencia
                      ? new Date(event.fechaHoraOcurrencia).toLocaleString()
                      : "N/A"}
                  </p>
                  <p style={{ margin: 0 }}>
                    <strong style={{ color: "#0056b3" }}>Epicentro:</strong> Lat{" "}
                    {event.latitudEpicentro !== undefined
                      ? event.latitudEpicentro.toFixed(4)
                      : "N/A"}
                    , Lon{" "}
                    {event.longitudEpicentro !== undefined
                      ? event.longitudEpicentro.toFixed(4)
                      : "N/A"}
                  </p>
                  <p style={{ margin: 0 }}>
                    <strong style={{ color: "#0056b3" }}>Hipocentro:</strong> Lat{" "}
                    {event.latitudHipocentro !== undefined
                      ? event.latitudHipocentro.toFixed(4)
                      : "N/A"}
                    , Lon{" "}
                    {event.longitudHipocentro !== undefined
                      ? event.longitudHipocentro.toFixed(4)
                      : "N/A"}
                  </p>
                  {/* Corrected: Replaced "Sismógrafo Identificador" with "Valor Magnitud" */}
                  <p style={{ margin: 0 }}>
                    <strong style={{ color: "#0056b3" }}>Valor Magnitud:</strong>{" "}
                    {event.valorMagnitud !== undefined
                      ? event.valorMagnitud.toFixed(2)
                      : "N/A"}
                  </p>
                </div>
              ))}
            </div>
          </div>

          {selectedEvent && (
            <div
              style={{
                marginTop: "20px",
                padding: "15px",
                border: "1px solid #e0e0e0",
                borderRadius: "8px",
                backgroundColor: "#fdfdfd",
              }}
            >
              <h2 style={{ color: "#34495e", marginBottom: "10px" }}>
                Evento Seleccionado
              </h2>
              <button
                onClick={onConfirmSelection}
                disabled={isConfirming}
                style={isConfirming ? disabledButtonStyle : primaryButtonStyle}
              >
                {isConfirming ? "Confirmando..." : "Confirmar Selección"}
              </button>
            </div>
          )}
        </>
      )}
      {confirmSuccess && <p style={successMessageStyle}>{confirmSuccess}</p>}
      {confirmError && <p style={errorMessageStyle}>Error: {confirmError}</p>}
      <div style={{
        marginTop: "30px",
        textAlign: "right",
        borderTop: "1px solid #eee",
        paddingTop: "20px"
      }}>
        <button onClick={onGoBack} style={purpleButtonStyle}>
          Volver al inicio
        </button>
      </div>
    </div>
  );
}

export default ManualRevisionPage;