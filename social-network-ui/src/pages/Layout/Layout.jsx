// import {useEffect} from 'react';
import {Outlet} from "react-router";
import Navigation from "../../components/Navigation/Navigation";
import Header from "../../components/Header/Header";
import styles from "./Layout.module.scss";
import {useLocation} from "react-router-dom";
import MessageNotificationList from '../../components/MessageNotification/MessageNotificationList';
import ErrorConnectionMessage from "../../components/ErrorMessage/ErrorConnectionMessage";
import {useSelector} from "react-redux";
import { createPortal } from "react-dom";
export default function Layout() {
  const isVisible = useSelector(state => state.webSocket.isVisible);
  const authUser = useSelector(state => state.auth.user.obj);
  const location = useLocation();
  const excludedPaths = ['/messages', '/profile', '/login', '/favorites'];
  const showAside = !excludedPaths.some(path => location.pathname.startsWith(path));


  return (
    <div className={styles.container}>
      <Header authUser={authUser} showMessageIcon={!location.pathname.startsWith('/messages')}/>
      {showAside && <Navigation  authUser={authUser}/>}
      <main  className={`${styles.main} ${!showAside && styles.mainFullWidth}`}>
        <Outlet/>
      </main>
      {createPortal(<MessageNotificationList/>, document.body)}
      {createPortal(<ErrorConnectionMessage isVisible={isVisible}/>, document.body)}
    </div>
  );
}