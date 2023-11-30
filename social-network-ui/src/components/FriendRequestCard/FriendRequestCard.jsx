import styles from './FriendRequestCard.module.scss';
import PropTypes from "prop-types";
import { Link } from 'react-router-dom';
import { convertToLocalTime } from '../../utils/formatData';
const FriendRequestCard=({type, friendRequest})=>{
  const {name, surname, avatar, id, created_date}=friendRequest;
  console.log(friendRequest);
  return(
    <div className={styles.card}>
      <div className={styles.card_aside}>
        <div className={styles.card_aside_avatar}>
          <img src={avatar} alt={`${name}'s avatar`} />
        </div>
      </div>
      <div className={styles.card_info}>
        <span className={styles.card_info_link_wrapper}>
          <Link to={`/profile/${id}`} className={styles.card_info_link}>
            {name} {surname}
          </Link>
        </span>
        <div className={styles.card_info_date}>
          {convertToLocalTime(created_date)}
        </div>
        {type==="FRIEND_REQUEST"?(
          <>
            <div className={styles.card_content}>
              <p className={styles.card_message}>You sent friend request</p>
            </div>
              
          </>):type==="SENT_REQUEST"?(
          <>
            <div className={styles.card_content}>
              <p className={styles.card_message}>You sent friend request</p>
            </div>
          </>
        ):null}
        <div className={styles.card_footer}>
          {type==="FRIEND_REQUEST"?(
            <>
              <button className={styles.card_button}>Confirm</button>
              <button className={styles.card_buttonSecondary}>Delete</button>
            </>):type==="SENT_REQUEST"?(
            <>
              <button className={styles.card_buttonSecondary}>Delete</button>
            </>
          ):null}
        </div>
      </div>
    </div>
  )
  ; 
};
export default FriendRequestCard;

FriendRequestCard.propTypes = {
  type: PropTypes.oneOf([
    "FRIEND_REQUEST",
    "SENT_REQUEST"
  ]),
  friendRequest: PropTypes.object.isRequired,
};
  