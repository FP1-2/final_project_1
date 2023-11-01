import React from 'react';
import Header from './components/Header/Header';
import AppRoutes from './AppRoutes';

function App() {
  const isLoginPageLogin = window.location.pathname.includes('/login');
  const isLoginPageRegister = window.location.pathname.includes('/registration');
  const isLoginPageReset = window.location.pathname.includes('/reset-password');
  const isLoginPageUpdatePass = window.location.pathname.includes('/change_password/:token');

  return (
    <div className="App">
      {!isLoginPageLogin && !isLoginPageRegister && !isLoginPageReset && !isLoginPageUpdatePass && <Header />}
      <AppRoutes />
    </div>
  );
}

export default App;
