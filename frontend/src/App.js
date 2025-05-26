import React, { useState } from "react";

function App() {
  // State to manage which "page" is currently active.
  const [currentPage, setCurrentPage] = useState("home");

  // State to store fetched data (list of seismic events)
  const [seismicEvents, setSeismicEvents] = useState([]);
  // State to store the currently selected seismic event object
  const [selectedEvent, setSelectedEvent] = useState(null);
  // State to manage loading status for fetching events
  const [isLoadingEvents, setIsLoadingEvents] = useState(false);
  // State to manage loading status for confirming selection
  const [isConfirming, setIsConfirming] = useState(false);
  // State to store error messages for fetching events
  const [fetchError, setFetchError] = useState(null);
  // State to store error messages for confirming selection
  const [confirmError, setConfirmError] = useState(null);
  // State to store success message after confirming selection
  const [confirmSuccess, setConfirmSuccess] = useState(null);
  // NEW STATE: To store the DatosRegistradosDTO received after selection
  const [registeredData, setRegisteredData] = useState(null);

  // Function to handle the button click and change the page, also fetches data.
  const handleButtonClick = async () => {
    setCurrentPage("manualRevision"); // Change to the manual revision page

    setIsLoadingEvents(true); // Set loading state to true for events fetch
    setFetchError(null); // Clear any previous fetch errors
    setSeismicEvents([]); // Clear any previous seismic events
    setSelectedEvent(null); // Clear any previous selection
    setConfirmError(null); // Clear any previous confirmation errors
    setConfirmSuccess(null); // Clear any previous confirmation success messages
    setRegisteredData(null); // NEW: Clear any previous registered data

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

  // Function to handle selecting a seismic event from the list
  const handleSelectEvent = (event) => {
    setSelectedEvent(event);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null); // NEW: Clear registered data on new selection
  };

  // Function to send the selected event data to the backend
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
          // IMPORTANT CHANGE: Send the full selectedEvent object as JSON in the request body
          body: JSON.stringify(selectedEvent),
        }
      );
  
      if (!response.ok) {
        // If the response is not OK (e.g., 4xx or 5xx status),
        // try to parse the error body if available.
        const errorBody = await response.json(); // Backend now sends JSON error objects
        throw new Error(
          `HTTP error! status: ${response.status} - ${
            errorBody.error || response.statusText || 'Unknown error'
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
    } finally {
      setIsConfirming(false);
    }
  };

  // Function to go back to the home page.
  const handleGoBack = () => {
    setCurrentPage("home");
    setSeismicEvents([]);
    setSelectedEvent(null);
    setIsLoadingEvents(false);
    setIsConfirming(false);
    setFetchError(null);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null); // NEW: Clear registered data when going back
  };

  // Conditional rendering based on the currentPage state.
  return (
    <div>
      {currentPage === "home" && (
        <div>
          <h1>Herramienta análisis de sismos</h1>
          <button onClick={handleButtonClick}>
            Opción registrar revisión manual
          </button>
        </div>
      )}

      {currentPage === "manualRevision" && (
        <div>
          <h1>Registrar revisión manual</h1>

          {isLoadingEvents && <p>Cargando eventos sísmicos...</p>}

          {fetchError && <p style={{ color: "red" }}>Error: {fetchError}</p>}

          {!isLoadingEvents && !fetchError && seismicEvents.length === 0 && (
            <p>No hay eventos sísmicos no revisados para mostrar.</p>
          )}

          {!isLoadingEvents && !fetchError && seismicEvents.length > 0 && (
            <div>
              <h2>Seleccione un evento sísmico:</h2>
              <div>
                {seismicEvents.map((event) => (
                  <div
                    key={event.id}
                    onClick={() => handleSelectEvent(event)}
                    style={{
                      border: "1px solid gray",
                      padding: "10px",
                      margin: "5px",
                      cursor: "pointer",
                      backgroundColor:
                        selectedEvent && selectedEvent.id === event.id
                          ? "lightblue"
                          : "white",
                    }}
                  >
                    <p>
                      Fecha y hora ocurrencia:{" "}
                      {event.fechaHoraOcurrencia
                        ? new Date(event.fechaHoraOcurrencia).toLocaleString()
                        : "N/A"}
                    </p>
                    <p>
                      Epicentro: Lat{" "}
                      {event.latitudEpicentro !== undefined
                        ? event.latitudEpicentro.toFixed(4)
                        : "N/A"}
                      , Lon{" "}
                      {event.longitudEpicentro !== undefined
                        ? event.longitudEpicentro.toFixed(4)
                        : "N/A"}
                    </p>
                    <p>
                      Hipocentro: Lat{" "}
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
                <div>
                  <h2>Detalles del Evento Seleccionado</h2>
                  <pre
                    style={{
                      border: "1px solid lightgray",
                      padding: "10px",
                      backgroundColor: "#f0f0f0",
                      overflow: "auto",
                    }}
                  >
                    {JSON.stringify(selectedEvent, null, 2)}
                  </pre>

                  {/* Confirmation button */}
                  <button
                    onClick={handleConfirmSelection}
                    disabled={isConfirming}
                    style={{
                      padding: "10px 20px",
                      fontSize: "16px",
                      backgroundColor: isConfirming ? "gray" : "green",
                      color: "white",
                      border: "none",
                      cursor: isConfirming ? "not-allowed" : "pointer",
                      marginTop: "10px",
                    }}
                  >
                    {isConfirming ? "Confirmando..." : "Confirmar Selección"}
                  </button>

                  {confirmSuccess && (
                    <p style={{ color: "green", marginTop: "10px" }}>
                      {confirmSuccess}
                    </p>
                  )}
                  {confirmError && (
                    <p style={{ color: "red", marginTop: "10px" }}>
                      Error: {confirmError}
                    </p>
                  )}

                  {/* NEW: Display DatosRegistradosDTO if available */}
                  {registeredData && (
                    <div
                      style={{
                        marginTop: "20px",
                        borderTop: "1px solid #ccc",
                        paddingTop: "15px",
                      }}
                    >
                      <h3>Datos Registrados para Revisión:</h3>
                      <pre
                        style={{
                          border: "1px solid lightblue",
                          padding: "10px",
                          backgroundColor: "#e0f7fa",
                          overflow: "auto",
                        }}
                      >
                        {JSON.stringify(registeredData, null, 2)}
                      </pre>
                    </div>
                  )}
                </div>
              )}
            </div>
          )}

          <button
            onClick={handleGoBack}
            style={{
              padding: "10px 20px",
              fontSize: "16px",
              backgroundColor: "purple",
              color: "white",
              border: "none",
              cursor: "pointer",
              marginTop: "20px",
            }}
          >
            Volver atrás
          </button>
        </div>
      )}
    </div>
  );
}

export default App;
