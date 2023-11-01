import React from "react";
import style from "./FriendRequestCard.module.scss";
import { NavLink } from "react-router-dom";
import { useDispatch } from "react-redux";
import { confirmFriendRequest } from "../../redux-toolkit/friend/thunks";
import PropTypes from "prop-types";


const FriendRequestCard = ({ el }) => {
  const dispatch=useDispatch();

  const confirmFriend=()=>{
    dispatch(confirmFriendRequest({userId:el.id, status:true}));
  };

  return (
    <div className={style.requestCardWrapper}>
      <NavLink to={`/profile/${el.id}`} className={style.requestCard}>
        <img className={style.requestCardImg} src={el.avatar ? el.avatar : "https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="Avatar of profile" />
        <div className={style.requestCardInformWrapper}>
          <h3 className={style.requestCardInformTitle}>{`${el.name} ${el.surname}`}</h3>
          <p className={style.requestCardInformText}>{el.dateOfBirth ? `Age: ${el.dateOfBirth}` : el.address ? `Lives in: ${el.address}` : null}</p>
        </div>
      </NavLink>
      <div className={style.requestBtnsWrapper}>
        {/* <button className={style.requestBtnReject}>Reject</button> */}
        <button className={style.requestBtnConfirm} onClick={confirmFriend}>Confirm</button>
      </div>
    </div>
  );
};

FriendRequestCard.propTypes = {
  el: PropTypes.object.isRequired,
};


export default FriendRequestCard;