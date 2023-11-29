import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import style from './AllFriends.module.scss';
import FriendCardProfile from "../FriendCardProfile/FriendCardProfile";
import { useSelector } from "react-redux";
import ErrorPage from "../ErrorPage/ErrorPage";
import { getMyFriends } from "../../redux-toolkit/friend/thunks";
import Loader from "../Loader/Loader";

const AllFriends = () => {
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
        <>
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
        </>
      )}
    </>
  );
};

export default AllFriends;
