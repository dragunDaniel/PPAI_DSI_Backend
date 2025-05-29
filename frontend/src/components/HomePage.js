import React from "react";
import {
  primaryButtonStyle,
  secondaryButtonStyle,
  disabledButtonStyle,
} from "../styles/commonStyles";

function HomePage({
  onManualRevisionClick,
  onDeveloperOptionsClick,
  registrarRevisionManualDisabled,
}) {
  return (
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
        <button
          onClick={onManualRevisionClick}
          style={registrarRevisionManualDisabled ? disabledButtonStyle : primaryButtonStyle}
          disabled={registrarRevisionManualDisabled}
        >
          Registrar Revisión Manual
        </button>
        <button onClick={onDeveloperOptionsClick} style={secondaryButtonStyle}>
          Opciones de Developer
        </button>
        <button disabled style={disabledButtonStyle}>
          ...
        </button>
      </div>
    </div>
  );
}

export default HomePage;