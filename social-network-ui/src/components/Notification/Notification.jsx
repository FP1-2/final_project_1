import React from 'react';
import styles from './Notification.module.scss';
import PropTypes from "prop-types";

function Notification({ notification }) {
  const isFriendRequest = notification.type === "FRIEND_REQUEST";
  const isUnread = !notification.read;

  return (
    <div className={`${styles.card} ${isUnread ? styles.card_unread : ''}`}>
      <div className={styles.card_aside}>
        <div className={styles.card_aside_avatar}>
          <img src={notification.initiator.avatar} alt={`${notification.initiator.name}'s avatar`} />
        </div>
      </div>
      <div className={styles.card_info}>
        <a href={`/profile/${notification.initiator.userId}`} className={styles.card_info_link}>
          {notification.initiator.name} {notification.initiator.surname}
        </a>
        <div className={styles.card_info_date}>
          {new Date(notification.createdDate).toLocaleString()}
        </div>
        <div className={styles.card_content}>
          <p className={styles.card_message}>{notification.message}</p>
        </div>
        {isFriendRequest && (
          <div className={styles.card_footer}>
            <button className={styles.card_button}>Accept Friend Request</button>
            <button className={styles.card_buttonSecondary}>Ignore</button>
          </div>
        )}
      </div>
    </div>
  );
}

Notification.propTypes = {
  notification: PropTypes.shape({
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


export default Notification;





