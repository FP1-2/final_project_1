import React from 'react';
import { loadNotifications,
  loadNotification,
  notificationMarkAsRead,
  loadUnreadCount,} from '../../redux-toolkit/notification/thunks';
import {useDispatch} from "react-redux";

function NotificationsPage() {

  const buttonStyle = {
    display: 'block',
    margin: '10px',
    padding: '10px 20px',
    cursor: 'pointer',
    border: '1px solid black'
  };

  const dispatch = useDispatch();

  const testNotificationId = '459';

  return (
    <div>
      <h1>NotificationsPage</h1>
      <button style={buttonStyle} onClick={() => dispatch(loadNotifications())}>
                Загрузить все уведомления
      </button>
      <button style={buttonStyle} onClick={() => dispatch(loadNotification(testNotificationId))}>
                Загрузить уведомление {testNotificationId}
      </button>
      <button style={buttonStyle} onClick={() => dispatch(notificationMarkAsRead(testNotificationId))}>
                Отметить как прочитанное {testNotificationId}
      </button>
      <button style={buttonStyle} onClick={() => dispatch(loadUnreadCount())}>
                Загрузить количество непрочитанных
      </button>
    </div>
  );
}

export default NotificationsPage;