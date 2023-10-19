import React from 'react';
import Header from './components/Header/Header';
import AppRoutes from './AppRoutes';

function App() {
  const isHeader = !window.location.pathname.includes('/login') && !window.location.pathname.includes('/registration');

  if (isHeader) {
    return (
      <div className="App">
        <Header />
        <AppRoutes />
      </div>
    );
  }

  return (
    <div className="App">
      <AppRoutes />
    </div>
  );
}

export default App;
