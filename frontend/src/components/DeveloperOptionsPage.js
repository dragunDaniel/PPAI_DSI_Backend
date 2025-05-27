import React from "react";
import {
  mainToggleButtonStyle,
  collapsibleContainerStyle,
  groupToggleButtonStyle,
  goldenGroupToggleButtonStyle,
  infoButtonStyle,
  purpleButtonStyle,
  errorMessageStyle,
  preStyle,
} from "../styles/commonStyles";

function DeveloperOptionsPage({
  cambiosEstadoGroupedData,
  isLoadingCambiosEstado,
  cambiosEstadoError,
  expandedCambioEstadoId,
  expandedEventGroup,
  showCambiosEstadoHistory,
  onToggleCambioEstadoDetails,
  onToggleEventGroup,
  onToggleCambiosEstadoHistory,
  onGoBackFromCambiosEstado,
}) {
  const wasRecent = (fechaHoraInicio) => {
    if (!fechaHoraInicio) return false;
    const startTime = new Date(fechaHoraInicio).getTime();
    const now = new Date().getTime();
    const thirtyMinutesAgo = now - 30 * 60 * 1000;
    return startTime >= thirtyMinutesAgo;
  };

  const anyChangeRecentInGroup = (changes) => {
    return changes.some((change) => wasRecent(change.fechaHoraInicio));
  };

  return (
    <div style={{ padding: "20px 0" }}>
      <h1 style={{ color: "#2c3e50", marginBottom: "20px" }}>
        Opciones de Developer
      </h1>

      <button
        onClick={onToggleCambiosEstadoHistory}
        style={mainToggleButtonStyle}
        disabled={
          isLoadingCambiosEstado ||
          cambiosEstadoError ||
          Object.keys(cambiosEstadoGroupedData).length === 0
        }
      >
        <span>
          {showCambiosEstadoHistory ? "Ocultar " : "Historial de cambios de estados"}
        </span>
        <span>{showCambiosEstadoHistory ? "▲" : "▼"}</span>
      </button>

      {isLoadingCambiosEstado && (
        <p style={{ textAlign: "center", color: "#555" }}>
          Cargando datos de cambios de estado...
        </p>
      )}

      {cambiosEstadoError && <p style={errorMessageStyle}>Error: {cambiosEstadoError}</p>}

      {!isLoadingCambiosEstado &&
        !cambiosEstadoError &&
        Object.keys(cambiosEstadoGroupedData).length === 0 && (
          <p style={{ textAlign: "center", color: "#666", padding: "20px 0" }}>
            No hay datos de cambios de estado disponibles para mostrar.
          </p>
        )}

      <div
        style={{
          ...collapsibleContainerStyle,
          maxHeight: showCambiosEstadoHistory ? "1000px" : "0", // Control max-height here
        }}
      >
        {!isLoadingCambiosEstado &&
          !cambiosEstadoError &&
          Object.keys(cambiosEstadoGroupedData).length > 0 && (
            <div>
              <h2 style={{ color: "#34495e", marginBottom: "15px" }}>
                Historial de Cambios de Estado por Evento Sísmico:
              </h2>
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
                {Object.entries(cambiosEstadoGroupedData)
                  .sort(([idA], [idB]) => {
                    if (idA === "N/A") return 1;
                    if (idB === "N/A") return -1;
                    return parseInt(idA, 10) - parseInt(idB, 10);
                  })
                  .map(([eventId, changes]) => (
                    <div
                      key={eventId}
                      style={{
                        border: "1px solid #d0d0d0",
                        padding: "10px",
                        margin: "15px 0",
                        borderRadius: "8px",
                        backgroundColor: "#f0f8ff",
                        boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                      }}
                    >
                      <button
                        onClick={() => onToggleEventGroup(eventId)}
                        style={
                          anyChangeRecentInGroup(changes)
                            ? goldenGroupToggleButtonStyle
                            : groupToggleButtonStyle
                        }
                      >
                        <span>
                          EventoSismico ID:{" "}
                          <strong style={{ fontSize: "1.1em" }}>{eventId}</strong>{" "}
                          ({changes.length} cambios)
                        </span>
                        {anyChangeRecentInGroup(changes) && (
                          <span
                            style={{
                              fontSize: "0.8em",
                              color: "#8B4513",
                              marginLeft: "10px",
                            }}
                          >
                            (Cambios recientes)
                          </span>
                        )}
                        <span>{expandedEventGroup === eventId ? "▲" : "▼"}</span>
                      </button>

                      {expandedEventGroup === eventId && (
                        <div style={{ padding: "10px" }}>
                          {changes
                            .sort(
                              (a, b) =>
                                new Date(a.fechaHoraInicio) -
                                new Date(b.fechaHoraInicio)
                            )
                            .map((item) => (
                              <div
                                key={item.id}
                                style={{
                                  border: "1px solid #e0e0e0",
                                  padding: "15px",
                                  margin: "10px 0",
                                  borderRadius: "8px",
                                  backgroundColor: "#ffffff",
                                  boxShadow: "0 2px 4px rgba(0,0,0,0.05)",
                                }}
                              >
                                <p style={{ margin: "5px 0" }}>
                                  <strong style={{ color: "#0056b3" }}>
                                    ID de Cambio:
                                  </strong>{" "}
                                  {item.id}
                                </p>
                                <p style={{ margin: "5px 0" }}>
                                  <strong style={{ color: "#0056b3" }}>
                                    Estado:
                                  </strong>{" "}
                                  {item.estado?.nombreEstado || "N/A"} (Ámbito:{" "}
                                  {item.estado?.ambito || "N/A"})
                                </p>
                                <p style={{ margin: "5px 0" }}>
                                  <strong style={{ color: "#0056b3" }}>
                                    Inicio:
                                  </strong>{" "}
                                  {item.fechaHoraInicio
                                    ? new Date(item.fechaHoraInicio).toLocaleString()
                                    : "N/A"}
                                </p>
                                <p style={{ margin: "5px 0" }}>
                                  <strong style={{ color: "#0056b3" }}>Fin:</strong>{" "}
                                  {item.fechaHoraFin
                                    ? new Date(item.fechaHoraFin).toLocaleString()
                                    : "Activo"}
                                </p>
                                <p style={{ margin: "5px 0" }}>
                                  <strong style={{ color: "#0056b3" }}>
                                    Responsable:
                                  </strong>{" "}
                                  {item.responsable
                                    ? `${item.responsable.nombre} ${item.responsable.apellido} (${item.responsable.mail})`
                                    : "N/A"}
                                </p>

                                <button
                                  onClick={() => onToggleCambioEstadoDetails(item.id)}
                                  style={infoButtonStyle}
                                >
                                  {expandedCambioEstadoId === item.id
                                    ? "Ocultar Detalles JSON"
                                    : "Ver Detalles JSON Completos"}
                                </button>

                                {expandedCambioEstadoId === item.id && (
                                  <pre style={preStyle}>
                                    {JSON.stringify(item, null, 2)}
                                  </pre>
                                )}
                              </div>
                            ))}
                        </div>
                      )}
                    </div>
                  ))}
              </div>
            </div>
          )}
      </div>
      <button onClick={onGoBackFromCambiosEstado} style={purpleButtonStyle}>
        Volver a la Pantalla Principal
      </button>
    </div>
  );
}

export default DeveloperOptionsPage;