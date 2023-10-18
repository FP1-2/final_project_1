import React from "react";
import style from "./FriendCardProfile.module.scss";
import { Link} from "react-router-dom";
// import PropTypes from "prop-types";
import { ReactComponent as Delete} from "../../img/delete.svg";
import { modalDeleteFriendState } from "../../redux-toolkit/profile/slice";
import { useDispatch} from "react-redux";


const FriendCardProfile = () => {
  const dispatch=useDispatch();

  const modalDeleteFriendOpen=()=>{
    dispatch(modalDeleteFriendState(true));
  };

  return (
    <Link to="/profile" className={style.friendCardWrapper}>
      <div className={style.friendCard}>
        <img className={style.friendCardImg} src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSz3WBkmQjxah1UL5l3Ze77Twdfv3PHfDxd3A&usqp=CAU" alt="" />
        <div className={style.friendCardInformWrapper}>
          <p className={style.friendCardInformText}>Information</p>
        </div>
      </div>
      <button className={style.friendCardBtn} onClick={modalDeleteFriendOpen}>
        <Delete className={style.friendCardBtnImg}/>
      </button>
    </Link>
  );
};
// FriendProfile.propTypes = {
// //   file: PropTypes.object
// };
// FriendProfile.defaultProps = {
// //   file: {}
// };
export default FriendCardProfile;