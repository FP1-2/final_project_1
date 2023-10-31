import {Outlet} from "react-router";
import Aside from "../components/Aside/Aside";
import Header from "../components/Header/Header";
import styles from "../components/Header/Header.module.scss";
import {useLocation} from "react-router-dom";
import MessageNotificationList from '../components/MessageNotification/MessageNotificationList';
import ErrorConnectionMessage from "../components/ErrorMessage/ErrorConnectionMessage";
import {useSelector} from "react-redux";
export default function Layout() {
  const isVisible = useSelector(state => state.webSocket.isVisible);
  const location = useLocation();
  const excludedPaths = ['/messages', '/profile', '/login'];
  const showAside = !excludedPaths.some(path => location.pathname.startsWith(path));

  return (
    <div className={styles.container}>
      <Header/>
      {showAside && <Aside/>}
      <Outlet/>
      <MessageNotificationList/>
      <ErrorConnectionMessage isVisible={isVisible}/>
    </div>
  );
}