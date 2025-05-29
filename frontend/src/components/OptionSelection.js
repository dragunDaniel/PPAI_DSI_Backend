import React, { useState } from "react";

function OptionSelection({ onConfirm, onReject, onDeriveToExpert }) { // Added onDeriveToExpert here
  const [disabled, setDisabled] = useState(false);

  const handleConfirm = () => {
    setDisabled(true);
    onConfirm && onConfirm();
  };

  const handleReject = () => {
    setDisabled(true);
    onReject && onReject();
  };

  // Renamed and updated this handler to call onDeriveToExpert
  const handleDeriveToExpert = () => {
    setDisabled(true);
    onDeriveToExpert && onDeriveToExpert(); // Call the prop passed from App.js
  };

  return (
    <div style={{ padding: "40px 0", textAlign: "center", position: "relative" }}>
      <h2 style={{ color: "#2c3e50", marginBottom: "30px" }}>
        Seleccione una acción
      </h2>
      <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: "25px", marginTop: "40px", position: "relative" }}>
        <button
          onClick={handleConfirm}
          disabled={disabled}
          style={{
            background: "#27ae60",
            color: "#fff",
            border: "none",
            borderRadius: "8px",
            padding: "15px 30px",
            fontSize: "1.1em",
            cursor: "pointer",
            fontWeight: "bold",
            boxShadow: "0 2px 8px rgba(39,174,96,0.08)",
            transition: "background 0.2s",
            width: "320px",
            maxWidth: "90%",
            opacity: disabled ? 0.6 : 1,
            pointerEvents: disabled ? "none" : "auto"
          }}
        >
          Confirmar evento
        </button>
        <button
          onClick={handleReject}
          disabled={disabled}
          style={{
            background: "#e74c3c",
            color: "#fff",
            border: "none",
            borderRadius: "8px",
            padding: "15px 30px",
            fontSize: "1.1em",
            cursor: "pointer",
            fontWeight: "bold",
            boxShadow: "0 2px 8px rgba(231,76,60,0.08)",
            transition: "background 0.2s",
            width: "320px",
            maxWidth: "90%",
            opacity: disabled ? 0.6 : 1,
            pointerEvents: disabled ? "none" : "auto"
          }}
        >
          Rechazar evento
        </button>
        <div style={{ position: "relative", width: "320px", maxWidth: "90%" }}>
          <button
            onClick={handleDeriveToExpert} // Changed from handleExpertClick to handleDeriveToExpert
            disabled={disabled}
            style={{
              background: "#00bfff",
              color: "#fff",
              border: "none",
              borderRadius: "8px",
              padding: "15px 30px",
              fontSize: "1.1em",
              cursor: "pointer",
              fontWeight: "bold",
              boxShadow: "0 2px 8px rgba(0,191,255,0.08)",
              transition: "background 0.2s",
              width: "100%",
              position: "relative",
              zIndex: 2,
              opacity: disabled ? 0.6 : 1,
              pointerEvents: disabled ? "none" : "auto"
            }}
          >
            Derivar a experto
          </button>
        </div>
      </div>
    </div>
  );
}

export default OptionSelection;