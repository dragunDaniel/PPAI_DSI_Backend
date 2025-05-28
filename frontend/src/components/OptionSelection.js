import React, { useRef } from "react";

function OptionSelection({ onConfirm, onReject, onExpert }) {
  const expertBtnRef = useRef(null);
  const expertSparkleContainerRef = useRef(null);

  const handleExpertClick = () => {
    const btn = expertBtnRef.current;
    const container = expertSparkleContainerRef.current;
    if (!btn || !container) return;
    container.style.position = "absolute";
    container.style.left = `${btn.offsetLeft}px`;
    container.style.top = `${btn.offsetTop}px`;
    container.style.width = `${btn.offsetWidth}px`;
    container.style.height = `${btn.offsetHeight}px`;
    container.style.pointerEvents = "none";
    container.style.zIndex = 10;
    for (let i = 0; i < 32; i++) {
      const sparkle = document.createElement("span");
      sparkle.style.position = "absolute";
      sparkle.style.pointerEvents = "none";
      sparkle.style.width = "6px";
      sparkle.style.height = "6px";
      sparkle.style.borderRadius = "50%";
      sparkle.style.background = "gold";
      sparkle.style.opacity = "0.85";
      sparkle.style.boxShadow = "0 0 8px 2px gold";
      sparkle.style.left = `${50 + 60 * Math.cos((i / 32) * 2 * Math.PI)}%`;
      sparkle.style.top = `${50 + 60 * Math.sin((i / 32) * 2 * Math.PI)}%`;
      sparkle.style.transform = "translate(-50%, -50%) scale(0.7)";
      sparkle.style.transition = "all 0.8s cubic-bezier(.4,2,.6,1)";
      container.appendChild(sparkle);
      setTimeout(() => {
        sparkle.style.opacity = "0";
        sparkle.style.transform = `translate(-50%, -50%) scale(2.2)`;
      }, 10);
      setTimeout(() => {
        sparkle.remove();
      }, 900);
    }
    if (onExpert) onExpert();
  };

  return (
    <div style={{ padding: "40px 0", textAlign: "center", position: "relative" }}>
      <h2 style={{ color: "#2c3e50", marginBottom: "30px" }}>
        Seleccione una acción
      </h2>
      <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: "25px", marginTop: "40px", position: "relative" }}>
        <button
          onClick={onConfirm}
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
            maxWidth: "90%"
          }}
        >
          Confirmar evento
        </button>
        <button
          onClick={onReject}
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
            maxWidth: "90%"
          }}
        >
          Rechazar evento
        </button>
        <div style={{ position: "relative", width: "320px", maxWidth: "90%" }}>
          <button
            ref={expertBtnRef}
            onClick={handleExpertClick}
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
              zIndex: 2
            }}
          >
            Solicitar revisión a experto
          </button>
          <div ref={expertSparkleContainerRef} style={{ position: "absolute", left: 0, top: 0, width: "100%", height: "100%", pointerEvents: "none", zIndex: 1 }} />
        </div>
      </div>
    </div>
  );
}

export default OptionSelection;
