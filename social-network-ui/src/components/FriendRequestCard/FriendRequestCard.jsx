import styles from './FriendRequestCard.module.scss';
import PropTypes from "prop-types";
import { Link } from 'react-router-dom';
import { convertToLocalTime } from '../../utils/formatData';
import { cancelRequest, confirmFriendRequest, allRequests, requestsToMe } from '../../redux-toolkit/friend/thunks';
import { useDispatch } from 'react-redux';

const FriendRequestCard=({type, friendRequest})=>{
  const dispatch=useDispatch();
  const {name, surname, avatar, id, created_date}=friendRequest;
  const handleConfirmBtn= async ()=>{
    await dispatch(confirmFriendRequest({ 
      userId: id, 
      status: true 
    }));
    await dispatch(requestsToMe());
  };
  const handleDeleteFriendBtn=async()=>{
    await dispatch(confirmFriendRequest({ 
      userId: id, 
      status: false 
    }));
    await dispatch(requestsToMe());
  };
  const handleDeleteSentBtn=async()=>{
    await dispatch(cancelRequest({ 
      friendId: id
    }));
    await dispatch(allRequests());
  };
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
              <button className={styles.card_button} onClick={()=>{handleConfirmBtn();}}>Confirm</button>
              <button className={styles.card_buttonSecondary} onClick={()=>{handleDeleteFriendBtn();}}>Delete</button>
            </>):type==="SENT_REQUEST"?(
            <>
              <button className={styles.card_buttonSecondary} onClick={()=>{handleDeleteSentBtn();}}>Delete</button>
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
  