import React from 'react';
import AppRoutes from './AppRoutes';
import {useSelector} from "react-redux";

function App() {
  const { id } = useSelector(state => state.auth.token.obj) || null;
  const isAuth = !!id;

  return (
    <div className="App">
      <AppRoutes isAuth={isAuth} />
    </div>
  );
}

export default App;
