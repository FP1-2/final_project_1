import React, { useEffect } from "react";
import style from "./FriendPageProfile.module.scss";
import { ReactComponent as MagnifyingGlass } from "../../img/magnifyingGlass.svg";
import FriendCardProfile from "../FriendCardProfile/FriendCardProfile";
import ModalDeleteFriend from "../ModalDeleteFriend/ModalDeleteFriend";
import { useSelector, useDispatch } from "react-redux";
// import { useParams } from "react-router-dom";
import { getFriends } from "../../redux-toolkit/friend/thunks";

const FriendProfile = () => {
  // const { id } = useParams();
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(getFriends());
  }, []);

  const userObject = useSelector(state => state.profile.profileUser.obj);
  return (<>
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
              {userObject.user === "myUser" ? <button className={style.profileFriendsHeaderBtn}>Friend requests</button> : null}
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
  );
};


export default FriendProfile;