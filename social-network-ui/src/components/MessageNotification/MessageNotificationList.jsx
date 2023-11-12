import React, {useState, useEffect} from 'react';
import {useDispatch, useSelector} from "react-redux";
import styles from './MessageNotification.module.scss';
import MessageNotification from './MessageNotification';
import {useLocation} from "react-router-dom";
import {setNewMessage} from "../../redux-toolkit/ws/slice";

export default function MessageNotificationList() {
  const location = useLocation();
  const dispatch = useDispatch();
  const authUser = useSelector(state => state.auth?.user?.obj);
  const newMess = useSelector(state => state.webSocket.newMessage);
  const [notifications, setNotifications] = useState([]);
  const showList = !location.pathname.startsWith('/messages') && notifications.length > 0;

  useEffect(() => {
    if (newMess !== null && newMess.sender.username !== authUser.username) {
      setNotifications(prev => [...prev, {...newMess}]);
      dispatch(setNewMessage(null));
      setTimeout(() => {
        setNotifications(prevNotifications => prevNotifications.slice(1));
      }, 5000);
    }
  }, [newMess]);

  return (
    (showList && notifications.length > 0) && (
      <ul className={`${styles.notificationList}`}>
        {notifications.map((message) => (
          <li key={message.id}>
            <MessageNotification message={message}/>
          </li>
        ))}
      </ul>
    )
  );
}
