import React from "react";
import style from "./FriendCardProfile.module.scss";
import { NavLink } from "react-router-dom";
import { ReactComponent as Delete } from "../../img/delete.svg";
import { modalDeleteFriendState } from "../../redux-toolkit/friend/slice";
import { useDispatch, useSelector } from "react-redux";
import ModalDeleteFriend from "../ModalDeleteFriend/ModalDeleteFriend";
import { friend } from "../../redux-toolkit/friend/thunks";
import PropTypes from "prop-types";


const FriendCardProfile = ({ el }) => {
  const dispatch = useDispatch();
  const userObject = useSelector(state => state.profile.profileUser.obj);

  const modalDeleteFriendOpen = async () => {
    await dispatch(friend(el.id));
    await dispatch(modalDeleteFriendState(true));
  };

  return (
    <div className={style.friendCardWrapper}>
      <ModalDeleteFriend />
      <NavLink to={`/profile/${el.id}`} className={style.friendCard}>
        <img className={style.friendCardImg} src={el.avatar ? el.avatar : "https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="Avatar of profile" />
        <div className={style.friendCardInformWrapper}>
          <h3 className={style.friendCardInformTitle}>{`${el.name} ${el.surname}`}</h3>
          <p className={style.friendCardInformText}>{el.dateOfBirth ? `Age: ${el.dateOfBirth}` : el.address ? `Lives in: ${el.address}` : null}</p>
        </div>
      </NavLink>
      {userObject.user === "myUser" ? <button className={style.friendCardBtn} onClick={modalDeleteFriendOpen}>
        <Delete className={style.friendCardBtnImg} />
      </button> : null}
    </div>
  );
};

FriendCardProfile.propTypes = {
  el: PropTypes.object.isRequired,
};


export default FriendCardProfile;