import React from "react";
import style from "./FriendPageProfile.module.scss";
import { ReactComponent as MagnifyingGlass } from "../../img/magnifyingGlass.svg";
import FriendCardProfile from "../FriendCardProfile/FriendCardProfile";
import ModalDeleteFriend from "../ModalDeleteFriend/ModalDeleteFriend";
import PropTypes from "prop-types";

const FriendProfile = ({ typeProfile }) => {
  return (<>
    {typeProfile === "myUser" ?
      <>
        <ModalDeleteFriend />
        <div className={style.profileBodyWrapper}>
          <div className={style.profileBody}>
            <div className={style.profileFriendsWrapper}>
              <div className={style.profileFriendsHeader}>
                <a className={style.profileFriendsHeaderLink} href="">Friends</a>
                <div className={style.profileFriendsHeaderBtnWrapper}>
                  <label className={style.profileFriendsHeaderInputWrapper} htmlFor="">
                    <MagnifyingGlass className={style.profileFriendsHeaderInputImg} />
                    <input type="text" className={style.profileFriendsHeaderInput} placeholder="Search" />
                  </label>
                  <button className={style.profileFriendsHeaderBtn}>Friend requests</button>
                </div>
              </div>
              <div className={style.profileFriends}>
                <FriendCardProfile />
                <FriendCardProfile />
                <FriendCardProfile />
              </div>
            </div>
          </div>
        </div>
      </>
      : <>
        <div className={style.profileBodyWrapper}>
          <div className={style.profileBody}>
            <div className={style.profileFriendsWrapper}>
              <div className={style.profileFriendsHeader}>
                <a className={style.profileFriendsHeaderLink} href="">Friends</a>
                <div className={style.profileFriendsHeaderBtnWrapper}>
                  <label className={style.profileFriendsHeaderInputWrapper} htmlFor="">
                    <MagnifyingGlass className={style.profileFriendsHeaderInputImg} />
                    <input type="text" className={style.profileFriendsHeaderInput} placeholder="Search" />
                  </label>
                </div>
              </div>
              <div className={style.profileFriends}>
                <FriendCardProfile />
                <FriendCardProfile />
                <FriendCardProfile />
              </div>
            </div>
          </div>
        </div>
      </>}
  </>
  );
};

FriendProfile.propTypes = {
  typeProfile: PropTypes.string
};
FriendProfile.defaultProps = {
  typeProfile: ""
};

export default FriendProfile;