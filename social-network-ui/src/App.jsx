import React, {useEffect} from 'react';
import AppRoutes from './AppRoutes';
import {useSelector, useDispatch} from "react-redux";
import {useCallback} from 'react';

function App() {
  const authUser = useSelector(state => state.auth.user.obj);
  const isAuth = Object.keys(authUser).length > 0;

  const dispatch = useDispatch();
  const connectWebSocket = useCallback(() => {
      dispatch({type: 'webSocket/connect'});
  }, [authUser, dispatch]);

  useEffect(() => {
    if (isAuth) {
      connectWebSocket();
    }
  }, [isAuth, authUser, dispatch]);

  return (
    <div className="App">
      <AppRoutes isAuth={isAuth}/>
    </div>
  );
}

export default App;
