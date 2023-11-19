import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import style from './FriendsPage.module.scss';
import FriendCardProfile from "../../components/FriendCardProfile/FriendCardProfile";
import { useSelector } from "react-redux";
import ErrorPage from "../../components/ErrorPage/ErrorPage";
import { getMyFriends } from "../../redux-toolkit/friend/thunks";
import Loader from "../../components/Loader/Loader";

const FriendsPage = () => {
  const dispatch = useDispatch();
  const myId = useSelector(state => state.auth.user.obj.id);

  useEffect(() => {
    dispatch(getMyFriends(myId));
  }, []);

  const {
    getMyFriends: { obj, status, error },
  } = useSelector(state => state.friends);

  return (
    <>
      {status === "rejected" ? (
        <ErrorPage
          message={error.message ? error.message : "Oops something went wrong!"}
        />
      ) : (
        <div className={style.bodyWrapper}>
          <div className={style.body}>
            <div className={style.friendsWrapper}>
              <div className={style.friendsHeader}>
                <a className={style.friendsHeaderLink} href="">
                  Friends
                </a>
              </div>
              {obj.length ? (
                <ul className={style.friendsList}>
                  {obj.map(el => (
                    <li key={el.id}>
                      <FriendCardProfile el={el} />
                    </li>
                  ))}
                </ul>
              ) : status ==="pending"?
                (
                  <Loader/>
                ):
                (
                  <p>No friends available</p>
                )}
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default FriendsPage;
