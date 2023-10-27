import React from "react";
import style from "./FriendCardProfile.module.scss";
import { Link } from "react-router-dom";
import { ReactComponent as Delete } from "../../img/delete.svg";
import { modalDeleteFriendState } from "../../redux-toolkit/profile/slice";
import { useDispatch, useSelector } from "react-redux";


const FriendCardProfile = () => {
  const dispatch = useDispatch();
  const userObject = useSelector(state => state.profile.profileUser.obj);

  const modalDeleteFriendOpen = () => {
    dispatch(modalDeleteFriendState(true));
  };

  return (
    <div className={style.friendCardWrapper}>
      <div className={style.friendCard}>
        <img className={style.friendCardImg} src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSz3WBkmQjxah1UL5l3Ze77Twdfv3PHfDxd3A&usqp=CAU" alt="" />
        <div className={style.friendCardInformWrapper}>
          <Link to="/profile" className={style.friendCardInformTitle}>Надточий Анна</Link>
          <p className={style.friendCardInformText}>Information</p>
        </div>
      </div>
      {userObject.user === "myUser" ? <button className={style.friendCardBtn} onClick={modalDeleteFriendOpen}>
        <Delete className={style.friendCardBtnImg} />
      </button> : null}
    </div>
  );
};


export default FriendCardProfile;