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
  // Store raw fetched data
  const [cambiosEstadoRawData, setCambiosEstadoRawData] = useState([]);
  // Store grouped data { 'eventoSismicoId': [change1, change2], ... }
  const [cambiosEstadoGroupedData, setCambiosEstadoGroupedData] = useState({});
  const [isLoadingCambiosEstado, setIsLoadingCambiosEstado] = useState(false);
  const [cambiosEstadoError, setCambiosEstadoError] = useState(null);
  // State to manage the ID of the expanded item in 'cambiosDeEstado' details (individual change)
  const [expandedCambioEstadoId, setExpandedCambioEstadoId] = useState(null);
  // State to manage the ID of the expanded seismic event group
  const [expandedEventGroup, setExpandedEventGroup] = useState(null);
  // NEW: State to control visibility of the whole "Cambios de Estado" history section
  const [showCambiosEstadoHistory, setShowCambiosEstadoHistory] = useState(false);
  // ------------------------------------------

  // Function to handle the "Registrar Revisión Manual" button click and change the page, also fetches data.
  const handleManualRevisionClick = async () => {
    setCurrentPage("manualRevision"); // Change to the manual revision page

    // Reset states for manual revision page
    setIsLoadingEvents(true);
    setFetchError(null);
    setSeismicEvents([]);
    setSelectedEvent(null);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null);

    // Reset states for developer options page just in case
    setCambiosEstadoRawData([]);
    setCambiosEstadoGroupedData({});
    setIsLoadingCambiosEstado(false);
    setCambiosEstadoError(null);
    setExpandedCambioEstadoId(null);
    setExpandedEventGroup(null);
    setShowCambiosEstadoHistory(false); // Ensure history is hidden when switching pages

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

    // Reset states for developer options page
    setIsLoadingCambiosEstado(true);
    setCambiosEstadoError(null);
    setCambiosEstadoRawData([]);
    setCambiosEstadoGroupedData({});
    setExpandedCambioEstadoId(null);
    setExpandedEventGroup(null);
    setShowCambiosEstadoHistory(false); // Keep history hidden initially on page load

    // Reset states for manual revision page just in case
    setSeismicEvents([]);
    setSelectedEvent(null);
    setIsLoadingEvents(false);
    setIsConfirming(false);
    setFetchError(null);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null);

    try {
      const response = await fetch(
        "http://localhost:8080/api/cambiosDeEstado/cambiEstado"
      );

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();

      if (Array.isArray(data)) {
        setCambiosEstadoRawData(data); // Store raw data

        // Group the data by eventoSismico.id
        const grouped = data.reduce((acc, item) => {
          const eventId = item.eventoSismico?.id;
          if (eventId !== undefined && eventId !== null) {
            if (!acc[eventId]) {
              acc[eventId] = [];
            }
            acc[eventId].push(item);
          } else {
            // Handle items without an associated seismic event, e.g., group them under 'N/A'
            if (!acc['N/A']) {
              acc['N/A'] = [];
            }
            acc['N/A'].push(item);
          }
          return acc;
        }, {});
        setCambiosEstadoGroupedData(grouped); // Store grouped data

      } else {
        console.warn("API response for cambios de estado is not an array:", data);
        setCambiosEstadoRawData([]);
        setCambiosEstadoGroupedData({});
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
    setCambiosEstadoRawData([]); // Clear raw data for dev options
    setCambiosEstadoGroupedData({}); // Clear grouped data for dev options
    setIsLoadingCambiosEstado(false);
    setCambiosEstadoError(null);
    setExpandedCambioEstadoId(null); // Also clear expanded item for dev options
    setExpandedEventGroup(null); // Clear expanded event group
    setShowCambiosEstadoHistory(false); // Ensure history is hidden
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
    setCambiosEstadoRawData([]); // Clear raw data
    setCambiosEstadoGroupedData({}); // Clear grouped data
    setIsLoadingCambiosEstado(false);
    setCambiosEstadoError(null);
    setExpandedCambioEstadoId(null); // Clear expanded item
    setExpandedEventGroup(null); // Clear expanded event group
    setShowCambiosEstadoHistory(false); // Ensure history is hidden
  };

  // NEW: Function to toggle the expanded state of a Cambio de Estado item (individual JSON)
  const handleToggleCambioEstadoDetails = (id) => {
    setExpandedCambioEstadoId(expandedCambioEstadoId === id ? null : id);
  };

  // NEW: Function to toggle the expanded state of a seismic event group
  const handleToggleEventGroup = (eventId) => {
    setExpandedEventGroup(expandedEventGroup === eventId ? null : eventId);
  };

  // NEW: Function to toggle the visibility of the entire "Cambios de Estado" history section
  const handleToggleCambiosEstadoHistory = () => {
    setShowCambiosEstadoHistory(!showCambiosEstadoHistory);
    // Optionally, reset expanded groups/items when hiding the whole section
    if (showCambiosEstadoHistory) {
      setExpandedEventGroup(null);
      setExpandedCambioEstadoId(null);
    }
  };

  // NEW: Function to check if a Cambio de Estado happened in the last 30 minutes
  const wasRecent = (fechaHoraInicio) => {
    if (!fechaHoraInicio) return false;
    const startTime = new Date(fechaHoraInicio).getTime();
    const now = new Date().getTime();
    const thirtyMinutesAgo = now - (30 * 60 * 1000); // 30 minutes in milliseconds
    return startTime >= thirtyMinutesAgo;
  };

  // NEW: Function to check if any change in a group is recent
  const anyChangeRecentInGroup = (changes) => {
    return changes.some(change => wasRecent(change.fechaHoraInicio));
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

  // Style for info/detail buttons
  const infoButtonStyle = {
    ...buttonStyle,
    backgroundColor: "#007bff", // Blue
    padding: "8px 15px", // Slightly smaller for detail toggles
    fontSize: "14px",
    margin: "5px 0",
  };

  // Style for group toggle buttons
  const groupToggleButtonStyle = {
    ...buttonStyle,
    backgroundColor: "#3498db", // Lighter blue for group toggle
    padding: "10px 20px",
    fontSize: "15px",
    marginTop: "10px",
    marginBottom: "15px",
    width: "100%", // Make it span full width of the group
    textAlign: "left",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  };

  const mainToggleButtonStyle = {
    ...buttonStyle,
    backgroundColor: "#2c3e50", // Darker, professional color
    fontSize: "18px",
    padding: "15px 30px",
    marginBottom: "25px",
    width: "fit-content", // Adjusts to content width
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    gap: "10px",
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

  // NEW: Styles for the animated collapsible section
  const collapsibleContainerStyle = {
    maxHeight: showCambiosEstadoHistory ? '1000px' : '0', // Adjust max-height as needed, larger for more content
    overflow: 'hidden',
    transition: 'max-height 0.7s ease-in-out, opacity 0.5s ease-in-out', // Smooth transition
    opacity: showCambiosEstadoHistory ? '1' : '0',
    marginTop: showCambiosEstadoHistory ? '20px' : '0',
    padding: showCambiosEstadoHistory ? '10px' : '0',
    border: showCambiosEstadoHistory ? '1px solid #e0e0e0' : 'none',
    borderRadius: '8px',
    backgroundColor: showCambiosEstadoHistory ? '#f9f9f9' : 'transparent',
  };
  
  // NEW: Golden button style for individual changes
  const goldenInfoButtonStyle = {
    ...infoButtonStyle, // Base it on infoButtonStyle
    backgroundColor: "#FFD700", // Golden color
    color: "#333", // Darker text for contrast
    fontWeight: "bold",
    boxShadow: "0 4px 8px rgba(255,215,0,0.4)", // More prominent shadow
  };

  // NEW: Golden button style for group toggles
  const goldenGroupToggleButtonStyle = {
    ...groupToggleButtonStyle,
    backgroundColor: "#FFD700", // Golden color
    color: "#333", // Darker text for contrast
    fontWeight: "bold",
    boxShadow: "0 4px 8px rgba(255,215,0,0.4)", // More prominent shadow
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

      {/* NEW: Cambios de Estado Page (Grouped by EventoSismico ID) */}
      {currentPage === "cambiosDeEstado" && (
        <div style={{ padding: "20px 0" }}>
          <h1 style={{ color: "#2c3e50", marginBottom: "20px" }}>
            Opciones de Developer
          </h1>

          {/* Main Toggle Button for the whole history section */}
          <button
            onClick={handleToggleCambiosEstadoHistory}
            style={mainToggleButtonStyle}
            disabled={isLoadingCambiosEstado || cambiosEstadoError || Object.keys(cambiosEstadoGroupedData).length === 0}
          >
            {showCambiosEstadoHistory ? "Ocultar " : "Historial de cambios de estados"}
            {showCambiosEstadoHistory ? "▲" : "▼"}
          </button>

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
            Object.keys(cambiosEstadoGroupedData).length === 0 && (
              <p
                style={{ textAlign: "center", color: "#666", padding: "20px 0" }}
              >
                No hay datos de cambios de estado disponibles para mostrar.
              </p>
            )}

          {/* Collapsible Container for the History */}
          <div style={collapsibleContainerStyle}>
            {!isLoadingCambiosEstado &&
              !cambiosEstadoError &&
              Object.keys(cambiosEstadoGroupedData).length > 0 && (
                <div>
                  <h2 style={{ color: "#34495e", marginBottom: "15px" }}>
                    Historial de Cambios de Estado por Evento Sísmico:
                  </h2>
                  <div
                    style={{
                      maxHeight: "500px", // Adjusted max-height for the inner scroll area
                      overflowY: "auto",
                      border: "1px solid #e0e0e0",
                      borderRadius: "8px",
                      padding: "10px",
                      backgroundColor: "#f9f9f9",
                    }}
                  >
                    {/* Iterate through grouped data */}
                    {Object.entries(cambiosEstadoGroupedData)
                      .sort(([idA], [idB]) => {
                        // Sort numerically, handling 'N/A' case if it exists
                        if (idA === 'N/A') return 1;
                        if (idB === 'N/A') return -1;
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
                            backgroundColor: "#f0f8ff", // Light blue for event groups
                            boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
                          }}
                        >
                          <button
                            onClick={() => handleToggleEventGroup(eventId)}
                            style={anyChangeRecentInGroup(changes) ? goldenGroupToggleButtonStyle : groupToggleButtonStyle}
                        >
                            <span>
                                EventoSismico ID:{" "}
                                <strong style={{ fontSize: "1.1em" }}>{eventId}</strong>{" "}
                                ({changes.length} cambios)
                            </span>
                            {/* NEW: Display "Cambios recientes" if the button is golden */}
                            {anyChangeRecentInGroup(changes) && (
                                <span style={{ fontSize: "0.8em", color: "#8B4513", marginLeft: "10px" }}>
                                    (Cambios recientes)
                                </span>
                            )}
                            <span>
                                {expandedEventGroup === eventId ? "▲" : "▼"}
                            </span>
                        </button>

                          {expandedEventGroup === eventId && (
                            <div style={{ padding: "10px" }}>
                              {changes
                                .sort((a, b) => new Date(a.fechaHoraInicio) - new Date(b.fechaHoraInicio)) // Sort changes within group by date
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
                                      <strong style={{ color: "#0056b3" }}>ID de Cambio:</strong>{" "}
                                      {item.id}
                                    </p>
                                    <p style={{ margin: "5px 0" }}>
                                      <strong style={{ color: "#0056b3" }}>Estado:</strong>{" "}
                                      {item.estado?.nombreEstado || "N/A"} (Ámbito:{" "}
                                      {item.estado?.ambito || "N/A"})
                                    </p>
                                    <p style={{ margin: "5px 0" }}>
                                      <strong style={{ color: "#0056b3" }}>Inicio:</strong>{" "}
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
                                      <strong style={{ color: "#0056b3" }}>Responsable:</strong>{" "}
                                      {item.responsable
                                        ? `${item.responsable.nombre} ${item.responsable.apellido} (${item.responsable.mail})`
                                        : "N/A"}
                                    </p>

                                    <button
                                      onClick={() => handleToggleCambioEstadoDetails(item.id)}
                                      // Still applying the goldenInfoButtonStyle to individual "Ver Detalles" button
                                      // but the prompt was about the group button, so this stays as is.
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
          </div> {/* End collapsibleContainerStyle */}

          <button onClick={handleGoBackFromCambiosEstado} style={purpleButtonStyle}>
            Volver a la Pantalla Principal
          </button>
        </div>
      )}
    </div>
  );
}

export default App;