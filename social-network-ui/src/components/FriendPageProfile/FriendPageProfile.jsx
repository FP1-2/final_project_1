import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import style from "./FriendPageProfile.module.scss";
import { Link } from "react-router-dom";
import FriendCardProfile from "../FriendCardProfile/FriendCardProfile";
import { useSelector } from "react-redux";
import ErrorPage from "../ErrorPage/ErrorPage";
import { getFriends } from "../../redux-toolkit/friend/thunks";
import { useParams } from "react-router";
import { clearFriends, clearMyFriends } from "../../redux-toolkit/friend/slice";

const FriendProfile = () => {
  const dispatch = useDispatch();
  const { id } = useParams();

  useEffect(() => {
    dispatch(getFriends(id));
    return (() => {
      dispatch(clearFriends());
      dispatch(clearMyFriends());
    });
  }, []);


  const {
    getFriends: { obj, status, error },
  } = useSelector(state => state.friends);
  const userObj = useSelector(state => state.profile.profileUser.obj);


  return (
    <>
      {status === "rejected" ? (
        <ErrorPage
          message={error.message ? error.message : "Oops something went wrong!"}
        />
      ) : (
        <div className={style.profileBodyWrapper}>
          <div className={style.profileBody}>
            <div className={style.profileFriendsWrapper}>
              <div className={style.profileFriendsHeader}>
                <a className={style.profileFriendsHeaderLink} href="">
                  Friends
                </a>
                {userObj.user === "myUser" ?
                  <div className={style.profileFriendsHeaderBtnWrapper}>
                    <Link className={style.profileFriendsHeaderBtn} to="/friends">Requests</Link>
                    <Link className={style.profileFriendsHeaderBtn} to="/friends">Find friends</Link>
                  </div>
                  :null
                }
              </div>
              {obj.length ? (
                <ul className={style.profileFriends}>
                  {obj.map(el => (
                    <li key={el.id}>
                      <FriendCardProfile el={el} />
                    </li>
                  ))}
                </ul>
              ) : (
                <p>No friends available</p>
              )}
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default FriendProfile;
