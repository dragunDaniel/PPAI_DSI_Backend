import React, { useState } from "react";
import HomePage from "./components/HomePage";
import ManualRevisionPage from "./components/ManualRevisionPage";
import DisplayRegisteredDataPage from "./components/DisplayRegisteredDataPage";
import DeveloperOptionsPage from "./components/DeveloperOptionsPage";
import DataModificationPage from "./components/DataModificationPage";
import OptionSelection from "./components/OptionSelection";
import {
  purpleButtonStyle,
  secondaryButtonStyle,
} from "./styles/commonStyles";

function App() {
  const [currentPage, setCurrentPage] = useState("home");
  const [seismicEvents, setSeismicEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [isLoadingEvents, setIsLoadingEvents] = useState(false);
  const [isConfirming, setIsConfirming] = useState(false);
  const [fetchError, setFetchError] = useState(null);
  const [confirmError, setConfirmError] = useState(null);
  const [confirmSuccess, setConfirmSuccess] = useState(null);
  const [registeredData, setRegisteredData] = useState(null);

  // States for "Cambios de Estado"
  const [cambiosEstadoRawData, setCambiosEstadoRawData] = useState([]);
  const [cambiosEstadoGroupedData, setCambiosEstadoGroupedData] = useState({});
  const [isLoadingCambiosEstado, setIsLoadingCambiosEstado] = useState(false);
  const [cambiosEstadoError, setCambiosEstadoError] = useState(null);
  const [expandedCambioEstadoId, setExpandedCambioEstadoId] = useState(null);
  const [expandedEventGroup, setExpandedEventGroup] = useState(null);
  const [showCambiosEstadoHistory, setShowCambiosEstadoHistory] = useState(false);

  // Add state for option selection feedback
  const [optionActionLoading, setOptionActionLoading] = useState(false);
  const [optionActionError, setOptionActionError] = useState(null);
  const [optionActionSuccess, setOptionActionSuccess] = useState(null);

  // Estado para login
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loginUsername, setLoginUsername] = useState("");
  const [loginPassword, setLoginPassword] = useState("");
  const [loginError, setLoginError] = useState(null);
  const [loginFadeOut, setLoginFadeOut] = useState(false);

  // Handlers for page navigation and data fetching
  const handleManualRevisionClick = async () => {
    setCurrentPage("manualRevision");
    setIsLoadingEvents(true);
    setFetchError(null);
    setSeismicEvents([]);
    setSelectedEvent(null);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null);

    // Reset developer options states
    setCambiosEstadoRawData([]);
    setCambiosEstadoGroupedData({});
    setIsLoadingCambiosEstado(false);
    setCambiosEstadoError(null);
    setExpandedCambioEstadoId(null);
    setExpandedEventGroup(null);
    setShowCambiosEstadoHistory(false);

    try {
      const response = await fetch(
        "http://localhost:8080/api/gestor-revision-manual/registrarRevisionManual"
      );
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      setSeismicEvents(Array.isArray(data) ? data : []);
      if (!Array.isArray(data)) {
        setFetchError("Formato de datos inesperado del servidor.");
      }
    } catch (error) {
      setFetchError(
        `Error al cargar eventos sísmicos: ${error.message}. Por favor, intente de nuevo.`
      );
    } finally {
      setIsLoadingEvents(false);
    }
  };

  const handleDeveloperOptionsClick = async () => {
    setCurrentPage("cambiosDeEstado");
    setIsLoadingCambiosEstado(true);
    setCambiosEstadoError(null);
    setCambiosEstadoRawData([]);
    setCambiosEstadoGroupedData({});
    setExpandedCambioEstadoId(null);
    setExpandedEventGroup(null);
    setShowCambiosEstadoHistory(false);

    // Reset manual revision states
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
        setCambiosEstadoRawData(data);
        const grouped = data.reduce((acc, item) => {
          const eventId = item.eventoSismico?.id;
          if (eventId !== undefined && eventId !== null) {
            if (!acc[eventId]) acc[eventId] = [];
            acc[eventId].push(item);
          } else {
            if (!acc["N/A"]) acc["N/A"] = [];
            acc["N/A"].push(item);
          }
          return acc;
        }, {});
        setCambiosEstadoGroupedData(grouped);
      } else {
        setCambiosEstadoRawData([]);
        setCambiosEstadoGroupedData({});
        setCambiosEstadoError("Formato de datos inesperado para cambios de estado.");
      }
    } catch (error) {
      setCambiosEstadoError(
        `Error al cargar cambios de estado: ${error.message}.`
      );
    } finally {
      setIsLoadingCambiosEstado(false);
    }
  };

  const handleSelectEvent = (event) => {
    setSelectedEvent(event);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null);
  };

  const handleConfirmSelection = async () => {
    if (!selectedEvent || selectedEvent.id === undefined || selectedEvent.id === null) {
      setConfirmError("Por favor, seleccione un evento sísmico válido (ID no disponible para selección).");
      return;
    }

    setIsConfirming(true);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null);

    try {
      const response = await fetch(
        `http://localhost:8080/api/gestor-revision-manual/tomarEventoSismicoSeleccionado`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(selectedEvent),
        }
      );

      if (!response.ok) {
        const errorBody = await response.json();
        throw new Error(
          `HTTP error! status: ${response.status} - ${errorBody.error || response.statusText || "Unknown error"}`
        );
      }

      const data = await response.json();
      setConfirmSuccess("Evento sísmico seleccionado y datos cargados exitosamente!");
      setRegisteredData(data);
    } catch (error) {
      setConfirmError(`Error al seleccionar el evento: ${error.message}.`);
      setRegisteredData(null);
    } finally {
      setIsConfirming(false);
      setCurrentPage("displayRegisteredData");
    }
  };

  const handleGoBack = () => {
    setCurrentPage("home");
    setSeismicEvents([]);
    setSelectedEvent(null);
    setIsLoadingEvents(false);
    setIsConfirming(false);
    setFetchError(null);
    setConfirmError(null);
    setConfirmSuccess(null);
    setRegisteredData(null);
    setCambiosEstadoRawData([]);
    setCambiosEstadoGroupedData({});
    setIsLoadingCambiosEstado(false);
    setCambiosEstadoError(null);
    setExpandedCambioEstadoId(null);
    setExpandedEventGroup(null);
    setShowCambiosEstadoHistory(false);
  };

  const handleGoBackToManualRevision = () => {
    setCurrentPage("manualRevision");
    setRegisteredData(null);
    setConfirmSuccess(null);
    setConfirmError(null);
  };

  const handleGoBackFromCambiosEstado = () => {
    setCurrentPage("home");
    setCambiosEstadoRawData([]);
    setCambiosEstadoGroupedData({});
    setIsLoadingCambiosEstado(false);
    setCambiosEstadoError(null);
    setExpandedCambioEstadoId(null);
    setExpandedEventGroup(null);
    setShowCambiosEstadoHistory(false);
  };

  const handleToggleCambioEstadoDetails = (id) => {
    setExpandedCambioEstadoId(expandedCambioEstadoId === id ? null : id);
  };

  const handleToggleEventGroup = (eventId) => {
    setExpandedEventGroup(expandedEventGroup === eventId ? null : eventId);
  };

  const handleToggleCambiosEstadoHistory = () => {
    setShowCambiosEstadoHistory(!showCambiosEstadoHistory);
    if (showCambiosEstadoHistory) {
      setExpandedEventGroup(null);
      setExpandedCambioEstadoId(null);
    }
  };

  const handleGoToDataModification = () => {
    setCurrentPage("dataModification");
  };

  const handleModifyData = () => {
  };

  const handleCancelModifyData = () => {
    setCurrentPage("optionSelection");
  };

  const handleOptionConfirm = async () => {
    if (!selectedEvent || selectedEvent.id === undefined || selectedEvent.id === null) {
      setOptionActionError("No hay evento sísmico seleccionado para aceptar.");
      setOptionActionSuccess(null);
      return;
    }
    setOptionActionLoading(true);
    setOptionActionError(null);
    setOptionActionSuccess(null);
    try {
      const response = await fetch("http://localhost:8080/api/gestor-revision-manual/tomarRechazoModificacion", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ eventoSismicoId: selectedEvent.id, accion: "aceptar" })
      });
      const text = await response.text();
      if (!response.ok) {
        throw new Error(text);
      }
      setOptionActionSuccess("Evento sísmico aceptado correctamente.");
      setOptionActionError(null);
      // Optionally, navigate to home or another page after a short delay
      setTimeout(() => {
        setCurrentPage("home");
        setSelectedEvent(null);
        setOptionActionSuccess(null);
      }, 1500);
    } catch (error) {
      setOptionActionError(error.message || "Error al aceptar el evento.");
      setOptionActionSuccess(null);
    } finally {
      setOptionActionLoading(false);
    }
  };const handleOptionReject = async () => {
    // Initial validation: Ensure an event is selected on the frontend
    // (even if the backend endpoint doesn't directly use the ID for this specific call,
    // it's good practice for the UI to reflect a selection).
    if (!selectedEvent || selectedEvent.id === undefined || selectedEvent.id === null) {
        setOptionActionError("No hay evento sísmico seleccionado para rechazar.");
        setOptionActionSuccess(null);
        return;
    }

    setOptionActionLoading(true);
    setOptionActionError(null);
    setOptionActionSuccess(null);

    try {
        // Construct the URL for the new GET endpoint
        const url = "http://localhost:8080/api/gestor-revision-manual/rechazarEventoSismicoSeleccionado";

        const response = await fetch(url, {
            method: "GET", // Changed to GET to match the backend endpoint
            headers: {
                // Content-Type header is less critical for GET requests without a body,
                // but keeping it is generally harmless.
                "Content-Type": "application/json"
            }
            // Removed 'body' property as GET requests typically do not have a body
        });

        const text = await response.text(); // Get response as text to handle potential non-JSON errors
        if (!response.ok) {
            // If the response status is not in the 2xx range, throw an error
            throw new Error(text || "Error en la respuesta del servidor.");
        }

        // Assuming the backend returns a boolean or some confirmation,
        // you might parse it if needed. The backend returns a boolean.
        // const isRejected = JSON.parse(text); // If the backend returns "true" or "false" as a string

        setOptionActionSuccess("Evento sísmico rechazado correctamente.");
        setOptionActionError(null);

        // Timer to clear messages and navigate
        setTimeout(() => {
            setCurrentPage("home");
            setSelectedEvent(null); // Clear the selected event on the frontend
            setOptionActionSuccess(null);
        }, 1500); // 1.5 seconds delay
    } catch (error) {
        console.error("Error al rechazar el evento:", error); // Log the actual error for debugging
        setOptionActionError(error.message || "Error al rechazar el evento.");
        setOptionActionSuccess(null);
    } finally {
        setOptionActionLoading(false); // Ensure loading state is reset regardless of success or failure
    }
};

  const handleOptionExpert = () => {
    // No-op: only sparkle effect in OptionSelection
  };

  // Handler para login
  const handleLogin = (e) => {
    e.preventDefault();
    if (!loginUsername.trim() || !loginPassword.trim()) {
      setLoginError("Por favor, complete ambos campos.");
      return;
    }
    setLoginFadeOut(true);
    setTimeout(() => {
      setIsLoggedIn(true);
      setLoginError(null);
      setLoginFadeOut(false);
    }, 500);
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
      {/* Login window */}
      {!isLoggedIn && (
        <div
          style={{
            maxWidth: 350,
            margin: "40px auto 30px auto",
            padding: 24,
            border: "1px solid #e0e0e0",
            borderRadius: 10,
            background: "#f9f9f9",
            boxShadow: "0 2px 8px rgba(0,0,0,0.04)",
            opacity: loginFadeOut ? 0 : 1,
            transform: loginFadeOut ? 'scale(0.95)' : 'scale(1)',
            transition: 'opacity 0.5s, transform 0.5s',
          }}
        >
          <h2 style={{ textAlign: "center", marginBottom: 18 }}>Iniciar sesión</h2>
          <form onSubmit={handleLogin}>
            <div style={{ marginBottom: 14 }}>
              <input
                type="text"
                placeholder="Usuario"
                value={loginUsername}
                onChange={e => setLoginUsername(e.target.value)}
                style={{ width: "100%", padding: 8, borderRadius: 5, border: "1px solid #ccc" }}
              />
            </div>
            <div style={{ marginBottom: 14 }}>
              <input
                type="password"
                placeholder="Contraseña"
                value={loginPassword}
                onChange={e => setLoginPassword(e.target.value)}
                style={{ width: "100%", padding: 8, borderRadius: 5, border: "1px solid #ccc" }}
              />
            </div>
            {loginError && <div style={{ color: "red", marginBottom: 10 }}>{loginError}</div>}
            <button
              type="submit"
              style={{
                width: "100%",
                background: "#6c63ff",
                color: "#fff",
                border: "none",
                borderRadius: 6,
                padding: "10px 0",
                fontWeight: "bold",
                fontSize: "1.1em",
                cursor: "pointer"
              }}
            >
              Iniciar sesión
            </button>
          </form>
        </div>
      )}
      {isLoggedIn && (
        <div style={{ textAlign: "center", marginBottom: 30 }}>
          <h2>Bienvenido {loginUsername}</h2>
        </div>
      )}

      {currentPage === "home" && (
        <HomePage
          onManualRevisionClick={handleManualRevisionClick}
          onDeveloperOptionsClick={handleDeveloperOptionsClick}
          registrarRevisionManualDisabled={!isLoggedIn}
        />
      )}

      {currentPage === "manualRevision" && (
        <ManualRevisionPage
          seismicEvents={seismicEvents}
          selectedEvent={selectedEvent}
          isLoadingEvents={isLoadingEvents}
          isConfirming={isConfirming}
          fetchError={fetchError}
          confirmError={confirmError}
          confirmSuccess={confirmSuccess}
          onSelectEvent={handleSelectEvent}
          onConfirmSelection={handleConfirmSelection}
          onGoBack={handleGoBack}
        />
      )}

      {currentPage === "displayRegisteredData" && (
        <DisplayRegisteredDataPage
          registeredData={registeredData}
          confirmSuccess={confirmSuccess}
          confirmError={confirmError}
          onGoBackToManualRevision={handleGoBackToManualRevision}
          onNoDataEdit={handleGoToDataModification}
        />
      )}

      {currentPage === "dataModification" && (
        <DataModificationPage
          onModify={handleModifyData}
          onCancel={handleCancelModifyData}
        />
      )}

      {currentPage === "cambiosDeEstado" && (
        <DeveloperOptionsPage
          cambiosEstadoGroupedData={cambiosEstadoGroupedData}
          isLoadingCambiosEstado={isLoadingCambiosEstado}
          cambiosEstadoError={cambiosEstadoError}
          expandedCambioEstadoId={expandedCambioEstadoId}
          expandedEventGroup={expandedEventGroup}
          showCambiosEstadoHistory={showCambiosEstadoHistory}
          onToggleCambioEstadoDetails={handleToggleCambioEstadoDetails}
          onToggleEventGroup={handleToggleEventGroup}
          onToggleCambiosEstadoHistory={handleToggleCambiosEstadoHistory}
          onGoBackFromCambiosEstado={handleGoBackFromCambiosEstado}
        />
      )}

      {currentPage === "optionSelection" && (
        <>
          <OptionSelection
            onConfirm={handleOptionConfirm}
            onReject={handleOptionReject}
            onExpert={handleOptionExpert}
          />
          {(optionActionLoading || optionActionError || optionActionSuccess) && (
            <div style={{ marginTop: 24, textAlign: "center" }}>
              {optionActionLoading && <span style={{ color: '#888' }}>Procesando acción...</span>}
              {optionActionError && <span style={{ color: 'red' }}>Error: {optionActionError}</span>}
              {optionActionSuccess && <span style={{ color: 'green' }}>{optionActionSuccess}</span>}
            </div>
          )}
        </>
      )}
    </div>
  );
}

export default App;