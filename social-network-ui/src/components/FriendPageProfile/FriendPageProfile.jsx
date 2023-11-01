import React, { useState } from "react";
import style from "./FriendPageProfile.module.scss";
import { ReactComponent as MagnifyingGlass } from "../../img/magnifyingGlass.svg";
import FriendCardProfile from "../FriendCardProfile/FriendCardProfile";
import ModalDeleteFriend from "../ModalDeleteFriend/ModalDeleteFriend";
import { useSelector, useDispatch } from "react-redux";
import ErrorPage from "../ErrorPage/ErrorPage";
import { requestsToMe } from "../../redux-toolkit/friend/thunks";
import FriendRequestCard from "../FriendRequestCard/FriendRequestCard";

const FriendProfile = () => {
  const [requestsFriends, setRequestsFriends] = useState(false);
  const dispatch=useDispatch();
  // const {} = useSelector(state => state.friends);

  const {
    getFriends: {
      obj,
      status,
      error
    }
  } = useSelector(state => state.friends);

  const friendsBtnClick = () => {
    setRequestsFriends(false);
  };
  const requestsBtnClick = () => {
    dispatch(requestsToMe());
    setRequestsFriends(true);
  };

  const userObject = useSelector(state => state.profile.profileUser.obj);
  return (<>
    <ModalDeleteFriend />
    {status === "rejected" ?
      <ErrorPage message={error.message ? error.message : "Oops something went wrong!"} />
      :
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
                {userObject.user === "myUser" ?
                  (requestsFriends ?
                    <button className={style.profileFriendsHeaderBtn} onClick={friendsBtnClick}>Friends</button>
                    : <button className={style.profileFriendsHeaderBtn} onClick={requestsBtnClick}>Friend requests</button>)
                  : null}
              </div>
            </div>
            {requestsFriends ?
              <ul className={style.profileRequests}>
                {obj.map((el) =>
                  <li key={el.id}><FriendRequestCard el={el} /></li>)}
              </ul>
              : (obj.length ?
                <ul className={style.profileFriends}>
                  {obj.map((el) =>
                    <li key={el.id}><FriendCardProfile el={el} /></li>)}
                </ul>
                : null)}
          </div>
        </div>
      </div >}
  </>
  );
};


export default FriendProfile;