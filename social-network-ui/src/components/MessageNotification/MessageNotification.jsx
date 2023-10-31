import {NavLink} from 'react-router-dom';
import styles from './MessageNotification.module.scss';
import React, {useState} from 'react';
import PropTypes from 'prop-types';
import Avatar from '../Avatar/Avatar';
import Close from '../Icons/Close';

export default function MessageNotification({message}) {
  const [isVisible, setIsVisible] = useState(true);
  return (
    <div className={`${styles.notification} ${!isVisible && styles.hidden}`}>
      <h2>Нове сповіщення</h2>
      <div className={`${styles.textWrapper}`}>
        <Avatar name={message.sender.name + " " + message.sender.username} photo={message.sender.avatar}
                additionalClass={`${styles.avatar}`}/>

        <NavLink to={`/messages/${message.chat.id}`} className={`${styles.text}`}>
          <p>Нове повідомлення від {message.sender.name} {message.sender.surname}</p>
        </NavLink>
      </div>
      <div className={`${styles.closeBtn}`} onClick={() => setIsVisible(false)}>
        <Close/>
      </div>
    </div>
  );
}

MessageNotification.propTypes = {
  message: PropTypes.object.isRequired,
}