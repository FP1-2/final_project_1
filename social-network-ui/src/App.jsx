import React from 'react';
import Header from './components/Header/Header';
import AppRoutes from './AppRoutes';

function App() {
  const isLoginPage = window.location.pathname.includes('/login');

  return (
    <div className="App">
      {!isLoginPage && <Header />}
      <AppRoutes />
    </div>
  );
}

export default App;
