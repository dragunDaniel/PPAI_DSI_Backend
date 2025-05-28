import React, { useRef } from "react";

function DataModificationPage({ onModify, onCancel }) {
  const modifyBtnRef = useRef(null);

  const handleModifyClick = () => {
    // Crear los brillitos
    const btn = modifyBtnRef.current;
    if (!btn) return;
    for (let i = 0; i < 18; i++) {
      const sparkle = document.createElement("span");
      sparkle.style.position = "absolute";
      sparkle.style.pointerEvents = "none";
      sparkle.style.width = "10px";
      sparkle.style.height = "10px";
      sparkle.style.borderRadius = "50%";
      sparkle.style.background = "gold";
      sparkle.style.opacity = "0.8";
      sparkle.style.boxShadow = "0 0 8px 2px gold";
      sparkle.style.left = `${50 + 40 * Math.cos((i / 18) * 2 * Math.PI)}%`;
      sparkle.style.top = `${50 + 40 * Math.sin((i / 18) * 2 * Math.PI)}%`;
      sparkle.style.transform = "translate(-50%, -50%) scale(0.7)";
      sparkle.style.transition = "all 0.8s cubic-bezier(.4,2,.6,1)";
      sparkle.style.zIndex = 2;
      btn.parentElement.style.position = "relative";
      btn.parentElement.appendChild(sparkle);
      setTimeout(() => {
        sparkle.style.opacity = "0";
        sparkle.style.transform = `translate(-50%, -50%) scale(2.2)`;
      }, 10);
      setTimeout(() => {
        sparkle.remove();
      }, 900);
    }
  };

  return (
    <div style={{ padding: "40px 0", textAlign: "center" }}>
      <h2 style={{ color: "#2c3e50", marginBottom: "30px" }}>
        ¿Desea modificar los datos del evento sismico?
      </h2>
      <div style={{ display: "flex", justifyContent: "center", gap: "30px" }}>
        <button
          ref={modifyBtnRef}
          onClick={handleModifyClick}
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
            position: "relative",
          }}
        >
          Modificar datos
        </button>
        <button
          onClick={onCancel}
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
          }}
        >
          No modificar datos
        </button>
      </div>
    </div>
  );
}

export default DataModificationPage;
