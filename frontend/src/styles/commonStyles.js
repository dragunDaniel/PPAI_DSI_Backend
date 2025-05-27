// commonStyles.js
export const primaryButtonStyle = {
    backgroundColor: "#007bff",
    color: "white",
    border: "none",
    padding: "12px 25px",
    borderRadius: "5px",
    fontSize: "16px",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
    marginTop: "10px",
  };
  
  export const secondaryButtonStyle = {
    backgroundColor: "#6c757d",
    color: "white",
    border: "none",
    padding: "12px 25px",
    borderRadius: "5px",
    fontSize: "16px",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
    marginTop: "10px",
  };
  
  export const disabledButtonStyle = {
    backgroundColor: "#cccccc",
    color: "#666666",
    border: "none",
    padding: "12px 25px",
    borderRadius: "5px",
    fontSize: "16px",
    cursor: "not-allowed",
    marginTop: "10px",
  };
  
  export const purpleButtonStyle = {
    backgroundColor: "#6f42c1",
    color: "white",
    border: "none",
    padding: "12px 25px",
    borderRadius: "5px",
    fontSize: "16px",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
    marginTop: "10px",
  };
  
  export const errorMessageStyle = {
    color: "#dc3545",
    backgroundColor: "#f8d7da",
    border: "1px solid #f5c6cb",
    borderRadius: "5px",
    padding: "10px",
    margin: "15px 0",
    textAlign: "center",
  };
  
  export const successMessageStyle = {
    color: "#28a745",
    backgroundColor: "#d4edda",
    border: "1px solid #c3e6cb",
    borderRadius: "5px",
    padding: "10px",
    margin: "15px 0",
    textAlign: "center",
  };
  
  export const preStyle = {
    backgroundColor: "#e9ecef",
    padding: "15px",
    borderRadius: "8px",
    overflowX: "auto",
    whiteSpace: "pre-wrap",
    wordBreak: "break-all",
    fontSize: "0.9em",
    border: "1px solid #ced4da",
    marginBottom: "15px",
  };
  
  export const mainToggleButtonStyle = {
    backgroundColor: "#17a2b8",
    color: "white",
    border: "none",
    padding: "12px 25px",
    borderRadius: "5px",
    fontSize: "16px",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
    marginBottom: "20px",
    width: "100%",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  };
  
  export const collapsibleContainerStyle = {
    overflow: "hidden",
    transition: "max-height 0.7s ease-in-out",
  };
  
  export const groupToggleButtonStyle = {
    backgroundColor: "#6c757d",
    color: "white",
    border: "none",
    padding: "10px 15px",
    borderRadius: "5px",
    fontSize: "1em",
    cursor: "pointer",
    width: "100%",
    textAlign: "left",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "10px",
    transition: "background-color 0.3s ease",
  };
  
  export const goldenGroupToggleButtonStyle = {
    ...groupToggleButtonStyle,
    backgroundColor: "#FFD700",
    color: "#333",
    fontWeight: "bold",
    boxShadow: "0 4px 8px rgba(255,215,0,0.3)",
  };
  
  export const infoButtonStyle = {
    backgroundColor: "#17a2b8",
    color: "white",
    border: "none",
    padding: "8px 12px",
    borderRadius: "4px",
    fontSize: "0.9em",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
    marginTop: "10px",
    display: "block",
    width: "fit-content",
  };