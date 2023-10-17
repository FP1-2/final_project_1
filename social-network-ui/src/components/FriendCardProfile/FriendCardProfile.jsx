import React from "react";
import style from "./FriendCardProfile.module.scss";
// import PropTypes from "prop-types";
import { ReactComponent as Delete} from "../../img/delete.svg";

const FriendCardProfile = () => {
  return (
    <div className={style.friendCardWrapper}>
      <div className={style.friendCard}>
        <a href="#">
          <img className={style.friendCardImg} src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSz3WBkmQjxah1UL5l3Ze77Twdfv3PHfDxd3A&usqp=CAU" alt="" />
        </a>
        <div className={style.friendCardInformWrapper}>
          <a href="#" className={style.friendCardInformTitle}>Надточий Анна</a>
          <p className={style.friendCardInformText}>Information</p>
        </div>
      </div>
      <button className={style.friendCardBtn}>
        <Delete className={style.friendCardBtnImg}/>
      </button>
    </div>
  );
};
// FriendProfile.propTypes = {
// //   file: PropTypes.object
// };
// FriendProfile.defaultProps = {
// //   file: {}
// };
export default FriendCardProfile;