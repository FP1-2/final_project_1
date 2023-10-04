import "./App.scss";
import React from "react";
import Header from "./components/Header/Header";
import AppRoutes from "./AppRoutes";

function App() {
  return (
    <div className="App">
      <Header/>
      <AppRoutes></AppRoutes>
    </div>
  );
}

export default App;
