import React, {useEffect} from 'react';
import Notification from '../../components/Notification/Notification';
import styles from './NotificationsPage.module.scss';
import { loadNotifications,
  // loadNotification,
  // notificationMarkAsRead,
  // loadUnreadCount,
} from '../../redux-toolkit/notification/thunks';
import {useDispatch, useSelector} from "react-redux";

function NotificationsPage() {

  const dispatch = useDispatch();

  const notifications = useSelector((state) => state.notifications.notifications.obj.content);
  const loading = useSelector((state) => state.notifications.loading);
  const error = useSelector((state) => state.notifications.error);

  useEffect(() => {
    dispatch(loadNotifications());
  }, [dispatch]);


  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    <div className={styles.container}>
      {notifications.map((notification) => (
        <Notification key={notification.id} notification={notification} />
      ))}
    </div>
  );
}

export default NotificationsPage;