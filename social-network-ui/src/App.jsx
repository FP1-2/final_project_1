import React, {useEffect} from 'react';
import AppRoutes from './AppRoutes';
import {useSelector, useDispatch} from "react-redux";
import {useCallback} from 'react';
import { setIsVisible } from './redux-toolkit/ws/slice';

function App() {
  const authUser = useSelector(state => state.auth.user.obj);
  const isAuth = Object.keys(authUser).length > 0;

  const dispatch = useDispatch();
  const connectWebSocket = useCallback(() => {
    dispatch(setIsVisible(false));
    dispatch({type: 'webSocket/connect'});
  }, [authUser]);

  useEffect(() => {
    if (isAuth) {
      connectWebSocket();
    }
  }, [isAuth]);

  return (
    <div className="App">
      <AppRoutes isAuth={!isAuth}/>
    </div>
  );
}

export default App;
