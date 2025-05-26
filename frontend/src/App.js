import React, { useState } from "react";

function App() {
  // State to manage which "page" is currently active.
  const [currentPage, setCurrentPage] = useState("home");

  // State to store fetched data (list of seismic events for manual revision)
  const [seismicEvents, setSeismicEvents] = useState([]);
  // State to store the currently selected seismic event object
  const [selectedEvent, setSelectedEvent] = useState(null);
  // State to manage loading status for fetching events (manual revision)
  const [isLoadingEvents, setIsLoadingEvents] = useState(false);
  // State to manage loading status for confirming selection (manual revision)
  const [isConfirming, setIsConfirming] = useState(false);
  // State to store error messages for fetching events (manual revision)
  const [fetchError, setFetchError] = useState(null);
  // State to store error messages for confirming selection (manual revision)
  const [confirmError, setConfirmError] = useState(null);
  // State to store success message after confirming selection (manual revision)
  const [confirmSuccess, setConfirmSuccess] = useState(null);
  // State to store the DatosRegistradosDTO received after selection (manual revision)
  const [registeredData, setRegisteredData] = useState(null);

  // --- NEW STATES FOR "CAMBIOS DE ESTADO" ---
  const [cambiosEstadoData, setCambiosEstadoData] = useState([]);
  const [isLoadingCambiosEstado, setIsLoadingCambiosEstado] = useState(false);
  const [cambiosEstadoError, setCambiosEstadoError] = useState(null);
  // ------------------------------------------

  // Function to handle the "Registrar Revisión Manual" button click and change the page, also fetches data.
  const handleManualRevisionClick = async () => {
    setCurrentPage("manualRevision"); // Change to the manual revision page

    setIsLoadingEvents(true); // Set loading state to true for events fetch
    setFetchError(null); // Clear any previous fetch errors
    setSeismicEvents([]); // Clear any previous seismic events
    setSelectedEvent(null); // Clear any previous selection
    setConfirmError(null); // Clear any previous confirmation errors
    setConfirmSuccess(null); // Clear any previous confirmation success messages
    setRegisteredData(null); // Clear any previous registered data

    try {
      const response = await fetch(
        "http://localhost:8080/api/gestor-revision-manual/registrarRevisionManual"
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();

      if (Array.isArray(data)) {
        setSeismicEvents(data);
      } else {
        console.warn("API response is not an array:", data);
        setSeismicEvents([]);
        setFetchError("Formato de datos inesperado del servidor.");
      }

      console.log("Fetched data:", data);
    } catch (error) {
      console.error("Error fetching seismic events:", error);
      setFetchError(
        `Error al cargar eventos sísmicos: ${error.message}. Por favor, intente de nuevo.`
      );
    } finally {
      setIsLoadingEvents(false);
    }
  };

  // Function to handle the "Opciones de developer" button click
  const handleDeveloperOptionsClick = async () => {
    setCurrentPage("cambiosDeEstado"); // Set to the new page for developer options

    setIsLoadingCambiosEstado(true); // Start loading for this specific data
    setCambiosEstadoError(null); // Clear previous errors
    setCambiosEstadoData([]); // Clear previous data

    try {
      const response = await fetch(
        "http://localhost:8080/api/cambiosDeEstado/cambiEstado"
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();

      if (Array.isArray(data)) {
        setCambiosEstadoData(data);
      } else {
        console.warn("API response for cambios de estado is not an array:", data);
        setCambiosEstadoData([]);
        setCambiosEstadoError("Formato de datos inesperado para cambios de estado.");
      }
      console.log("Fetched cambios de estado data:", data);
    } catch (error) {
      console.error("Error fetching cambios de estado:", error);
      setCambiosEstadoError(
        `Error al cargar cambios de estado: ${error.message}.`
      );
    } finally {
      setIsLoadingCambiosEstado(false);
    }
  };

  // Function to handle selecting a seismic event from the list (manual revision)
  const handleSelectEvent = (event) => {
    setSelectedEvent(event);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null); // Clear registered data on new selection
  };

  // Function to send the selected event data to the backend (manual revision)
  const handleConfirmSelection = async () => {
    if (
      !selectedEvent ||
      selectedEvent.id === undefined ||
      selectedEvent.id === null
    ) {
      setConfirmError(
        "Por favor, seleccione un evento sísmico válido (ID no disponible para selección)."
      );
      return;
    }

    setIsConfirming(true);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null); // Clear registered data before new confirmation attempt

    try {
      const response = await fetch(
        `http://localhost:8080/api/gestor-revision-manual/tomarEventoSismicoSeleccionado`,
        {
          method: "POST", // Specify the HTTP method as POST
          headers: {
            "Content-Type": "application/json", // Indicate that the request body is JSON
          },
          body: JSON.stringify(selectedEvent), // Send the full selectedEvent object as JSON in the request body
        }
      );

      if (!response.ok) {
        const errorBody = await response.json();
        throw new Error(
          `HTTP error! status: ${response.status} - ${
            errorBody.error || response.statusText || "Unknown error"
          }`
        );
      }

      const data = await response.json(); // This will be your DatosRegistradosDTO
      console.log("Selection successful, registered data:", data);
      setConfirmSuccess(
        "Evento sísmico seleccionado y datos cargados exitosamente!"
      );
      setRegisteredData(data); // Store the received DatosRegistradosDTO
    } catch (error) {
      console.error("Error selecting seismic event:", error);
      setConfirmError(`Error al seleccionar el evento: ${error.message}.`);
      setRegisteredData(null); // Clear registered data on error to ensure displayRegisteredData shows "No data"
    } finally {
      setIsConfirming(false);
      // Always navigate to this page regardless of success or failure
      setCurrentPage("displayRegisteredData");
    }
  };

  // Function to go back to the home page.
  const handleGoBack = () => {
    setCurrentPage("home");
    // Clear all states related to other pages when going back to home
    setSeismicEvents([]);
    setSelectedEvent(null);
    setIsLoadingEvents(false);
    setIsConfirming(false);
    setFetchError(null);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null);
    setCambiosEstadoData([]);
    setIsLoadingCambiosEstado(false);
    setCambiosEstadoError(null);
  };

  // Function to go back to the manual revision page from the display data page
  const handleGoBackToManualRevision = () => {
    setCurrentPage("manualRevision");
    setRegisteredData(null); // Clear displayed data when going back
    setConfirmSuccess(null); // Clear success message
    setConfirmError(null); // Clear any errors
    // If you want to re-load events when going back to manual revision:
    // handleManualRevisionClick(); // Re-fetch events
  };

  // Function to go back from "cambiosDeEstado" page to home
  const handleGoBackFromCambiosEstado = () => {
    setCurrentPage("home");
    setCambiosEstadoData([]);
    setIsLoadingCambiosEstado(false);
    setCambiosEstadoError(null);
  };

  // Common styles for buttons
  const buttonStyle = {
    padding: "12px 25px",
    fontSize: "16px",
    borderRadius: "5px",
    cursor: "pointer",
    transition: "background-color 0.3s ease, transform 0.2s ease",
    border: "none",
    color: "white",
    boxShadow: "0 2px 4px rgba(0,0,0,0.2)",
    margin: "10px 5px",
  };

  // Style for primary action buttons
  const primaryButtonStyle = {
    ...buttonStyle,
    backgroundColor: "#4CAF50", // Green
  };

  // Style for disabled buttons
  const disabledButtonStyle = {
    ...buttonStyle,
    backgroundColor: "#cccccc",
    cursor: "not-allowed",
    color: "#666", // Darker text for greyed out
  };

  // Style for secondary buttons (e.g., back buttons)
  const secondaryButtonStyle = {
    ...buttonStyle,
    backgroundColor: "#555", // Dark gray
  };

  // Style for purple buttons
  const purpleButtonStyle = {
    ...buttonStyle,
    backgroundColor: "#800080", // Purple
  };

  // Style for error messages
  const errorMessageStyle = {
    color: "#d32f2f",
    backgroundColor: "#ffebee",
    padding: "10px",
    borderRadius: "4px",
    border: "1px solid #ef9a9a",
    marginTop: "15px",
  };

  // Style for success messages
  const successMessageStyle = {
    color: "#2e7d32",
    backgroundColor: "#e8f5e9",
    padding: "10px",
    borderRadius: "4px",
    border: "1px solid #a5d6a7",
    marginTop: "15px",
  };

  // Style for preformatted JSON data display
  const preStyle = {
    backgroundColor: "#f8f8f8",
    padding: "15px",
    borderRadius: "8px",
    border: "1px solid #ddd",
    overflowX: "auto",
    marginTop: "10px",
    fontFamily: "monospace",
    whiteSpace: "pre-wrap",
    wordBreak: "break-all",
  };

  return (
    <div
      style={{
        fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
        padding: "20px",
        maxWidth: "800px",
        margin: "20px auto",
        backgroundColor: "#ffffff",
        borderRadius: "10px",
        boxShadow: "0 8px 16px rgba(0,0,0,0.1)",
        lineHeight: "1.6",
        color: "#333",
      }}
    >
      {/* Home Page */}
      {currentPage === "home" && (
        <div style={{ textAlign: "center", padding: "50px 0" }}>
          <h1 style={{ color: "#2c3e50", marginBottom: "30px" }}>
            Herramienta de Análisis Sísmico
          </h1>
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: "10px",
            }}
          >
            <button onClick={handleManualRevisionClick} style={primaryButtonStyle}>
              Registrar Revisión Manual
            </button>
            {/* Functional "Opciones de developer" button */}
            <button onClick={handleDeveloperOptionsClick} style={secondaryButtonStyle}>
              Opciones de Developer
            </button>
            <button disabled style={disabledButtonStyle}>
              Administrar Parámetros (Próximamente)
            </button>
          </div>
        </div>
      )}

      {/* Manual Revision Page (Event Selection) */}
      {currentPage === "manualRevision" && (
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
                    onClick={() => handleSelectEvent(event)}
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
                          ? "#eaf6ff" // Light blue for selected
                          : "#ffffff", // White for unselected
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
                      <strong style={{ color: "#0056b3" }}>Epicentro:</strong>{" "}
                      Lat{" "}
                      {event.latitudEpicentro !== undefined
                        ? event.latitudEpicentro.toFixed(4)
                        : "N/A"}
                      , Lon{" "}
                      {event.longitudEpicentro !== undefined
                        ? event.longitudEpicentro.toFixed(4)
                        : "N/A"}
                    </p>
                    <p style={{ margin: 0 }}>
                      <strong style={{ color: "#0056b3" }}>Hipocentro:</strong>{" "}
                      Lat{" "}
                      {event.latitudHipocentro !== undefined
                        ? event.latitudHipocentro.toFixed(4)
                        : "N/A"}
                      , Lon{" "}
                      {event.longitudHipocentro !== undefined
                        ? event.longitudHipocentro.toFixed(4)
                        : "N/A"}
                    </p>
                  </div>
                ))}
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
                    Detalles del Evento Seleccionado
                  </h2>
                  <pre style={preStyle}>
                    {JSON.stringify(selectedEvent, null, 2)}
                  </pre>

                  {/* Confirmation button */}
                  <button
                    onClick={handleConfirmSelection}
                    disabled={isConfirming}
                    style={
                      isConfirming ? disabledButtonStyle : primaryButtonStyle
                    }
                  >
                    {isConfirming ? "Confirmando..." : "Confirmar Selección"}
                  </button>
                </div>
              )}
            </div>
          )}
          {confirmSuccess && (
            <p style={successMessageStyle}>{confirmSuccess}</p>
          )}
          {confirmError && (
            <p style={errorMessageStyle}>Error: {confirmError}</p>
          )}
          <button onClick={handleGoBack} style={purpleButtonStyle}>
            Volver atrás
          </button>
        </div>
      )}

      {/* Display Registered Data Page (Manual Revision Result) */}
      {currentPage === "displayRegisteredData" && (
        <div style={{ padding: "20px 0" }}>
          <h1 style={{ color: "#2c3e50", marginBottom: "20px" }}>
            Detalle de la Operación
          </h1>
          {confirmSuccess && (
            <p style={successMessageStyle}>{confirmSuccess}</p>
          )}
          {confirmError && (
            <p style={errorMessageStyle}>Error: {confirmError}</p>
          )}

          {registeredData ? (
            <div>
              <h3 style={{ color: "#34495e", marginBottom: "10px" }}>
                Datos Registrados para Revisión:
              </h3>
              <pre style={preStyle}>
                {JSON.stringify(registeredData, null, 2)}
              </pre>
            </div>
          ) : (
            // Only show this message if there was an error AND no registered data
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
            <button
              onClick={handleGoBackToManualRevision}
              style={purpleButtonStyle}
            >
              Volver a Selección Manual
            </button>
            <button onClick={handleGoBack} style={secondaryButtonStyle}>
              Volver al Inicio
            </button>
          </div>
        </div>
      )}

      {/* NEW: Cambios de Estado Page */}
      {currentPage === "cambiosDeEstado" && (
        <div style={{ padding: "20px 0" }}>
          <h1 style={{ color: "#2c3e50", marginBottom: "20px" }}>
            Opciones de Developer - Cambios de Estado
          </h1>

          {isLoadingCambiosEstado && (
            <p style={{ textAlign: "center", color: "#555" }}>
              Cargando datos de cambios de estado...
            </p>
          )}

          {cambiosEstadoError && (
            <p style={errorMessageStyle}>Error: {cambiosEstadoError}</p>
          )}

          {!isLoadingCambiosEstado &&
            !cambiosEstadoError &&
            cambiosEstadoData.length === 0 && (
              <p
                style={{ textAlign: "center", color: "#666", padding: "20px 0" }}
              >
                No hay datos de cambios de estado disponibles.
              </p>
            )}

          {!isLoadingCambiosEstado &&
            !cambiosEstadoError &&
            cambiosEstadoData.length > 0 && (
              <div>
                <h2 style={{ color: "#34495e", marginBottom: "15px" }}>
                  Datos de Cambios de Estado:
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
                  {cambiosEstadoData.map((item, index) => (
                    <div
                      key={index} // Using index as key is okay if items don't reorder or get deleted
                      style={{
                        border: "1px solid #e0e0e0",
                        padding: "15px",
                        margin: "10px 0",
                        borderRadius: "8px",
                        backgroundColor: "#ffffff",
                        boxShadow: "0 2px 4px rgba(0,0,0,0.05)",
                      }}
                    >
                      <pre style={preStyle}>
                        {JSON.stringify(item, null, 2)}
                      </pre>
                    </div>
                  ))}
                </div>
              </div>
            )}
          <button onClick={handleGoBackFromCambiosEstado} style={purpleButtonStyle}>
            Volver a la Pantalla Principal
          </button>
        </div>
      )}
    </div>
  );
}

export default App;