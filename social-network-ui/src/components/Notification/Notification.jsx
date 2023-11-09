import React, {useEffect, useRef} from 'react';
import styles from './Notification.module.scss';
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {notificationMarkAsRead} from "../../redux-toolkit/notification/thunks";
import {showMessage} from "../../redux-toolkit/popup/slice";
import {resetMarkAsRead} from "../../redux-toolkit/notification/slice";

export default function Notification({ notification }) {
  const dispatch = useDispatch();
  const { status } = useSelector(state => state.notifications.mark_as_read);
  const prevStatusRef = useRef(status);
  const isFriendRequest = notification.type === "FRIEND_REQUEST";
  const isUnread = !notification.read;

  const handleMarkAsRead = () => {
    if (!notification.read) {
      dispatch(notificationMarkAsRead(notification.id));
    }
  };

  useEffect(() => {dispatch(resetMarkAsRead());}, []);

  useEffect(() => {
    if (prevStatusRef.current !== status && status === 'fulfilled') {
      dispatch(showMessage('Notification marked as read.'));
    }
    prevStatusRef.current = status;
  }, [status]);

  if(status === 'rejected') {
    dispatch(showMessage('Failed to mark notification as read.'));
  }

  return (
    <div className={`${styles.card} 
    ${isUnread ? styles.card_unread : ''}`}
    onClick={handleMarkAsRead}>
      <div className={styles.card_aside}>
        <div className={styles.card_aside_avatar}>
          <img src={notification.initiator.avatar} 
            alt={`${notification.initiator.name}'s avatar`} />
        </div>
      </div>
      <div className={styles.card_info}>
        <span className={styles.card_info_link_wrapper}>
          <Link to={`/profile/${notification.initiator.userId}`} 
            className={styles.card_info_link}>
            {notification.initiator.name} {notification.initiator.surname}
          </Link>
        </span>
        <div className={styles.card_info_date}>
          {new Date(notification.createdDate).toLocaleString()}
        </div>
        <div className={styles.card_content}>
          <p className={styles.card_message}>{notification.message}</p>
        </div>
        {isFriendRequest && (
          <div className={styles.card_footer}>
            <button className={styles.card_button}>
              Accept Friend Request
            </button>
            <button className={styles.card_buttonSecondary}>
              Ignore
            </button>
          </div>
        )}
      </div>
      <div
        className={`${styles.card_status_indicator} 
        ${isUnread ? styles.card_status_indicator_unread : ''}`}>
      </div>
    </div>
  );
}

Notification.propTypes = {
  notification: PropTypes.shape({
    id: PropTypes.number,
    read: PropTypes.bool.isRequired,
    initiator: PropTypes.shape({
      userId: PropTypes.number,
      avatar: PropTypes.string,
      name: PropTypes.string,
      surname: PropTypes.string,
      username: PropTypes.string,
    }),
    createdDate: PropTypes.string,
    message: PropTypes.string,
    type: PropTypes.oneOf([
      "POST_LIKED",
      "POST_REPOSTED",
      "POST_COMMENTED",
      "FRIEND_POSTED",
      "FRIEND_REQUEST",
    ]).isRequired,
  }).isRequired,
};





