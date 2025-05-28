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

  const handleOptionConfirm = () => {
    // Acción para confirmar evento
  };
  const handleOptionReject = () => {
    // Acción para rechazar evento
  };
  const handleOptionExpert = () => {
    // Acción para solicitar revisión a experto
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
      {currentPage === "home" && (
        <HomePage
          onManualRevisionClick={handleManualRevisionClick}
          onDeveloperOptionsClick={handleDeveloperOptionsClick}
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
        <OptionSelection
          onConfirm={handleOptionConfirm}
          onReject={handleOptionReject}
          onExpert={handleOptionExpert}
        />
      )}
    </div>
  );
}

export default App;