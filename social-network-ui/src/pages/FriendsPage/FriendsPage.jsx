import React, { useEffect } from "react";
import { useDispatch } from "react-redux";
import style from './FriendsPage.module.scss';
/* import FriendCardProfile from "../../components/FriendCardProfile/FriendCardProfile";
 */import { useSelector } from "react-redux";
import ErrorPage from "../../components/ErrorPage/ErrorPage";
import { getMyFriends } from "../../redux-toolkit/friend/thunks";
/* import Loader from "../../components/Loader/Loader";
 */import { Link, Outlet } from "react-router-dom";
import { useState } from "react";
const FriendsPage = () => {
  const dispatch = useDispatch();
  const myId = useSelector(state => state.auth.user.obj.id);
  const [linkAllFriends, setLinkAllFriends]=useState("focus");
  const [linkIncomingRequests, setLinkIncomingRequests]=useState("unfocus");
  const [linkOutgoingRequests, setLinkOutgoingRequests]=useState("unfocus");

  const clickLinkAllFriends=()=>{
    setLinkAllFriends("focus");
    setLinkIncomingRequests("unfocus");
    setLinkOutgoingRequests("unfocus");
  };
  const clickLinkIncomingRequests=()=>{
    setLinkAllFriends("unfocus");
    setLinkIncomingRequests("focus");
    setLinkOutgoingRequests("unfocus");
  };
  const clickLinkOutgoingRequests=()=>{
    setLinkAllFriends("unfocus");
    setLinkIncomingRequests("unfocus");
    setLinkOutgoingRequests("focus");
  };
  useEffect(() => {
    dispatch(getMyFriends(myId));
  }, []);

  const {
    getMyFriends: { /* obj,  */status, error },
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
              <div className={style.linksListWrapper}>
                <ul className={style.linksList}>
                  <li className={linkAllFriends === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkAllFriends}>
                    <Link to="" className={linkAllFriends === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">All friends</Link>
                  </li>
                  <li className={linkIncomingRequests === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkIncomingRequests}>
                    <Link to="incoming-requests" className={linkIncomingRequests === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Incoming requests</Link>
                  </li>
                  <li className={linkOutgoingRequests === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkOutgoingRequests}>
                    <Link to="outgoing-requests" className={linkOutgoingRequests === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Outgoing requests</Link>
                  </li>
                </ul>
              </div>
              <Outlet/>
              {/* {obj.length ? (
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
                )} */}
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default FriendsPage;
