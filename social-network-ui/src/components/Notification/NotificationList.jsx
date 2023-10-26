import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import styles from './Notification.module.scss';
import PropTypes from 'prop-types';
import Notification from './Notification'
export default function NotificationList({ authUser }) {

  const newMess = useSelector(state => state.webSocket.newMessage);
  const [notifications, setNotifications] = useState([])
  useEffect(() => {
    if (newMess !== null && newMess.sender.username !== authUser.username) {
      setNotifications(prev => [...prev, { ...newMess }]);
      console.log(notifications)
      setTimeout(() => {
          setNotifications(prevNotifications => prevNotifications.slice(1));
        }, 5000);
    }
  }, [newMess])

  return (
    (!window.location.pathname.includes('/messages') && notifications.length > 0) && (
      <ul className={`${styles.notificationList}`}>
        {notifications.map((message) => (
          <li key={message.id}>
            <Notification message={message} />
          </li>
        ))}
      </ul>
    )
  );
}
NotificationList.propTypes = {
  authUser: PropTypes.object.isRequired,
}
