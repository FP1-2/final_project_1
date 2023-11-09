import React, {useEffect, useRef} from 'react';
import styles from './Notification.module.scss';
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {notificationMarkAsRead, updateFriendRequest} from "../../redux-toolkit/notification/thunks";
import {showMessage} from "../../redux-toolkit/popup/slice";
import {resetFriendRequest, resetMarkAsRead} from "../../redux-toolkit/notification/slice";

export default function Notification({ notification }) {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(resetMarkAsRead());
    dispatch(resetFriendRequest());
  }, []);

  const { status } = useSelector(state => state.notifications.mark_as_read);

  const {
    status: friendStatus,
    obj: friendMassage,
  } = useSelector(state => state.notifications.update_status_friend);

  const friendStatusRef = useRef(friendStatus);
  const prevStatusRef = useRef(status);

  const isFriendRequest = notification.type === "FRIEND_REQUEST";
  const isUnread = !notification.read;

  const handleAcceptRequest = () => {
    dispatch(updateFriendRequest({ 
      userId: notification.initiator.userId, 
      status: true 
    }));
  };

  const handleRejectRequest = () => {
    dispatch(updateFriendRequest({
      userId: notification.initiator.userId,
      status: false
    }));
  };

  const handleMarkAsRead = () => {
    if (!notification.read) {
      dispatch(notificationMarkAsRead(notification.id));
    }
  };

  //Спливаюче повідомлення про обробку "позначено як прочитане"
  useEffect(() => {
    if (prevStatusRef.current !== status){
      if (status === 'fulfilled') dispatch(showMessage('Notification marked as read.'));
      if(status === 'rejected') dispatch(showMessage('Failed to mark notification as read.'));
      prevStatusRef.current = status;
    }
  }, [status]);

  //Повідомлення про обробку відповіді на запит у друзі.
  useEffect(() => {
    if (friendStatusRef.current !== friendStatus){
      if (friendStatus === 'fulfilled') dispatch(showMessage(friendMassage));
      if(friendStatus === 'rejected') dispatch(showMessage('Failed to the request'));
      friendStatusRef.current = friendStatus;
    }
  }, [friendStatus]);
  
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
            <button className={styles.card_button}
              onClick={handleAcceptRequest}>
              Accept Friend Request
            </button>
            <button className={styles.card_buttonSecondary}
              onClick={handleRejectRequest}>
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





