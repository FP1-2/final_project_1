import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useLocation } from "react-router-dom";
import { friendSearchRequest } from "../../redux-toolkit/friend/thunks";
import FriendCardProfile from "../FriendCardProfile/FriendCardProfile";
import ErrorPage from "../ErrorPage/ErrorPage";
import Loader from "../Loader/Loader";
import EmptyMessage from "../EmptyMessage/EmptyMessage";

export default function SearchFriend() {
  const dispatch = useDispatch();
  const location = useLocation();

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const input = queryParams.get('query');
    if (input) {
      dispatch(friendSearchRequest(input));
    }
  }, [location, dispatch]);

  const searchedFriends = useSelector(state => state.friends.friendSearchRequest.obj);
  const { error, status } = searchedFriends;

  return (
    <>
      {status === "rejected" ? (
        <ErrorPage message={error.message ? error.message : "Oops something went wrong!"} />
      ) : status === "pending" ? (
        <Loader />
      ) : searchedFriends.length > 0 ? (
        <div>
          <ul>
            {searchedFriends.map(el => (
              <li key={el.id}>
                <FriendCardProfile el={el} />
              </li>
            ))}
          </ul>
        </div>
      ) : (
        <EmptyMessage message="No matching friends found."/>
      )}
    </>
  );
}
