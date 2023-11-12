import React from "react";
import style from "./FriendPageProfile.module.scss";
import FriendCardProfile from "../FriendCardProfile/FriendCardProfile";
import { useSelector} from "react-redux";
import ErrorPage from "../ErrorPage/ErrorPage";

const FriendProfile = () => {

  const {
    getFriends: {
      obj,
      status,
      error
    }
  } = useSelector(state => state.friends);

  return (<>
    {status === "rejected" ?
      <ErrorPage message={error.message ? error.message : "Oops something went wrong!"} />
      :
      <div className={style.profileBodyWrapper}>
        <div className={style.profileBody}>
          <div className={style.profileFriendsWrapper}>
            <div className={style.profileFriendsHeader}>
              <a className={style.profileFriendsHeaderLink} href="">Friends</a>
            </div>
            {obj.length ?
              <ul className={style.profileFriends}>
                {obj.map((el) => <li key={el.id}><FriendCardProfile el={el} /></li>)}
              </ul>
              : null}
          </div>
        </div>
      </div >}
  </>
  );
};


export default FriendProfile;