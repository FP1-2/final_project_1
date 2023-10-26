import styles from './Message.module.scss';
import Avatar from "../Avatar/Avatar";
import { ContentType, fromString } from "../../utils/contentType";
import { checkSentType, checkFaildType } from "../../utils/statusType";
import PropTypes from 'prop-types';
import Like from '../Icons/Like'
export default function Message({ content, contentType, authUser, datetime, isHovered, photo, name, status, index,handleOpenImg }) {

    return (
        <>
            <div className={styles.messageWrapper}>
                <div className={`${styles.messageWrapper__cont} ${authUser && styles.auth}`} >
                    {!authUser && <div className={styles.messageWrapper__avatar}>
                        <Avatar photo={photo} additionalClass={styles.messageWrapper__avatar} name={name} />
                    </div>}

                    <div className={`${styles.messageWrapper__cont__text} ${authUser ? styles.authUser : styles.chatUser} 
                                ${(fromString(contentType) === ContentType.IMAGE || fromString(contentType) === ContentType.LIKE) && styles.image}`}>
                        {isHovered && <div className={styles.messageWrapper__cont__text__datatime}>
                            <p>{datetime}</p>
                        </div>}

                        {(fromString(contentType) === ContentType.IMAGE) && <img src={content} alt={content} onClick={()=>handleOpenImg(content)}/>}
                        {(fromString(contentType) === ContentType.TEXT) && <p>{content}</p>}
                        {(fromString(contentType) === ContentType.LIKE) && <Like size={"54"} />}
                    </div>
                </div>
            </div>
            <div className={(index !== 0) ? `${styles.messageStatus} ${styles.none}` : styles.messageStatus} >
                {checkSentType(status) ? <div className={styles.messageStatus__unread}>Надіслано</div>
                    : (checkFaildType(status) ? <div className={styles.messageStatus__error}>Error</div>
                        : <div className={styles.messageStatus__read}>
                            <Avatar photo={photo} name={name} />
                        </div>)
                }
            </div>
        </>
    )
}
Message.propTypes = {
    className: PropTypes.string,
    contentType: PropTypes.string.isRequired,
    content: PropTypes.string.isRequired,
    authUser: PropTypes.bool.isRequired,
    datetime: PropTypes.string.isRequired,
    isHovered: PropTypes.bool.isRequired,
    photo: PropTypes.string,
    name: PropTypes.string.isRequired,
    status: PropTypes.string.isRequired,
    index: PropTypes.number.isRequired,
    handleOpenImg: PropTypes.func.isRequired
};