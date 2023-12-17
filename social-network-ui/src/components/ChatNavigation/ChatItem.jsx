import styles from "./ChatNavigation.module.scss";
import Avatar from "../Avatar/Avatar";
import PropTypes from 'prop-types';
import {getTimeAgo} from "../../utils/formatData";

export default function ChatItem({
  chatItemClass, message, photo, name,
  isUnread, isRead, additionalClass, additionalClass2,
  clickHandler, time, showTime, isMess
})
{
  return (
    <div className={`${styles.chatItemContainer} ${chatItemClass}`}
      onClick={clickHandler}>
      <div className={styles.chatItemContainer__photo}>
        <Avatar photo={photo} name={name} additionalClass={additionalClass}/>
      </div>
      <div className={styles.chatItemContainer__infoWrap}>
        <div className={`${styles.chatItemContainer__info} ${!isMess && styles.chatItemContainer__info__notMess}`}  >
          <p className={`${styles.chatItemContainer__info__username} ${!isMess && styles.chatItemContainer__info__username__notMess}`}>{name}</p>
          {isMess && <div className={styles.chatItemContainer__info__text}>
            <span
              className={`${styles.chatItemContainer__info__text__lastMessage} ${isUnread && styles.unread}`}>{message}
            </span>
            <span
              className={`${styles.chatItemContainer__info__text__time} ${!showTime && styles.none}`}>
              {getTimeAgo(time)}
            </span>
          </div>}
        </div>
        {isMess && <div className={styles.chatItemContainer__status}>
          {isUnread && <div className={styles.chatItemContainer__status__unread}></div>}
          {isRead && <div className={styles.chatItemContainer__status__read}>
            <Avatar photo={photo} additionalClass={additionalClass2} name={name}/>
          </div>}
        </div>}
      </div>
    </div>
  );
}

ChatItem.propTypes = {
  message: PropTypes.string,
  photo: PropTypes.string,
  name: PropTypes.string,
  isUnread: PropTypes.bool,
  isRead: PropTypes.bool,
  additionalClass: PropTypes.string,
  additionalClass2: PropTypes.string,
  clickHandler: PropTypes.func,
  showTime: PropTypes.bool,
  time: PropTypes.string,
  chatItemClass: PropTypes.string,
  isMess: PropTypes.bool
};