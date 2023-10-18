import React from "react";
import style from "./FriendPageProfile.module.scss";
// import PropTypes from "prop-types";
import { ReactComponent as MagnifyingGlass} from "../../img/magnifyingGlass.svg";
import FriendCardProfile from "../FriendCardProfile/FriendCardProfile";
import ModalDeleteFriend from "../ModalDeleteFriend/ModalDeleteFriend";

const FriendProfile = () => {
  return (<>
    <ModalDeleteFriend/>
    <div className={style.profileBodyWrapper}>
      <div className={style.profileBody}>
        <div className={style.profileFriendsWrapper}>
          <div className={style.profileFriendsHeader}>
            <a className={style.profileFriendsHeaderLink} href="">Friends</a>
            <div className={style.profileFriendsHeaderBtnWrapper}>
              <label className={style.profileFriendsHeaderInputWrapper} htmlFor="">
                <MagnifyingGlass className={style.profileFriendsHeaderInputImg}/>
                <input type="text" className={style.profileFriendsHeaderInput} placeholder="Search"/>
              </label>
              <button className={style.profileFriendsHeaderBtn}>Friend requests</button>
            </div>
          </div>
          <div className={style.profileFriends}>
            <FriendCardProfile/>
            <FriendCardProfile/>
            <FriendCardProfile/>
          </div>
        </div>
      </div>
    </div>
  </>
    
  );
};
export default FriendProfile;