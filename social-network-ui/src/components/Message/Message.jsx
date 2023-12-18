import styles from './Message.module.scss';
import Avatar from "../Avatar/Avatar";
import {ContentType, fromString} from "../../utils/contentType";
import {checkSentStatus, checkFailedStatus} from "../../utils/statusType";
import PropTypes from 'prop-types';
import Like from '../Icons/Like';

export default function Message({
  content,
  contentType,
  authUser,
  datetime,
  photo,
  name,
  status,
  index,
  handleOpenImg,
  readerPhoto,
  readerName
}) {

  return (
    <>
      <div className={styles.messageWrapper}>
        <div className={`${styles.messageWrapper__cont} ${authUser && styles.auth}`}>
          {!authUser && <div className={styles.messageWrapper__avatar}>
            <Avatar photo={photo} additionalClass={styles.messageWrapper__avatar} name={name}/>
          </div>}

          <div
            className={`${styles.messageWrapper__cont__text} ${authUser ? styles.auth__authUser : styles.auth__chatUser} 
                                ${(fromString(contentType) === ContentType.IMAGE || fromString(contentType) === ContentType.LIKE) && styles.image}`}>
            <div
              className={`${styles.messageWrapper__cont__text__datatime} ${authUser ? styles.auth__authUser__date : styles.auth__chatUser__date}`}>
              <p>{datetime}</p>
            </div>

            {(fromString(contentType) === ContentType.IMAGE) &&
              <img src={content} alt={content} onClick={() => handleOpenImg(content)} className={styles.img}/>}
            {(fromString(contentType) === ContentType.TEXT) && <p>{content}</p>}
            {(fromString(contentType) === ContentType.LIKE) && <Like size={"54"}/>}
          </div>
        </div>
      </div>
      <div className={(index !== 0) || !authUser ? `${styles.messageStatus} ${styles.none}` : styles.messageStatus}>
        {checkSentStatus(status) ? <div className={styles.messageStatus__unread}>Sent</div>
          : (checkFailedStatus(status) ? <div className={styles.messageStatus__error}>Error</div>
            : <div className={styles.messageStatus__read}>
              <Avatar photo={readerPhoto} name={readerName}/>
            </div>)
        }
      </div>
    </>
  );
}
Message.propTypes = {
  className: PropTypes.string,
  contentType: PropTypes.string.isRequired,
  content: PropTypes.string.isRequired,
  authUser: PropTypes.bool.isRequired,
  datetime: PropTypes.string.isRequired,
  photo: PropTypes.string,
  name: PropTypes.string.isRequired,
  status: PropTypes.string.isRequired,
  index: PropTypes.number.isRequired,
  handleOpenImg: PropTypes.func.isRequired,
  readerPhoto:PropTypes.string,
  readerName: PropTypes.string
};