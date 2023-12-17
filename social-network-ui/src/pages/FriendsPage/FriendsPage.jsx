import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, Outlet, useNavigate } from "react-router-dom";
import style from './FriendsPage.module.scss';
import ErrorPage from "../../components/ErrorPage/ErrorPage";
import { getMyFriends } from "../../redux-toolkit/friend/thunks";
import { loadUserProfile } from "../../redux-toolkit/profile/thunks";

const FriendsPage = () => {

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const myId = useSelector(state => state.auth.user.obj.id);
  const [linkAllFriends, setLinkAllFriends] = useState("focus");
  const [linkIncomingRequests, setLinkIncomingRequests] = useState("unfocus");
  const [linkOutgoingRequests, setLinkOutgoingRequests] = useState("unfocus");
  const [searchValue, setSearchValue] = useState(''); 
  const [isSearchFocused, setIsSearchFocused] = useState(false); 


  const clickLinkAllFriends = () => {
    setLinkAllFriends("focus");
    setLinkIncomingRequests("unfocus");
    setLinkOutgoingRequests("unfocus");
    navigate('/friends');
  };

  const clickLinkIncomingRequests = () => {
    setLinkAllFriends("unfocus");
    setLinkIncomingRequests("focus");
    setLinkOutgoingRequests("unfocus");
    navigate('/friends/incoming-requests');
  };

  const clickLinkOutgoingRequests = () => {
    setLinkAllFriends("unfocus");
    setLinkIncomingRequests("unfocus");
    setLinkOutgoingRequests("focus");
    navigate('/friends/outgoing-requests');
  };

  useEffect(() => {
    const fetchProfileAndFriends = async () => {
      await dispatch(loadUserProfile({ user: "myUser", id: myId }));
      await dispatch(getMyFriends(myId));
    };
    fetchProfileAndFriends();
  }, [dispatch, myId]);

  const {
    getMyFriends: { status, error },
  } = useSelector(state => state.friends);

  const handleInputChange = (e) => {
    setSearchValue(e.target.value);
  };

  const handleInputKeyPress = (e) => {
    if (e.key === 'Enter' && searchValue) {
      navigate(`/friends/search-friend?query=${searchValue}`);
    }
  };

  const handleFocus = () => {
    setIsSearchFocused(true);
    setLinkAllFriends("unfocus");
    setLinkIncomingRequests("unfocus");
    setLinkOutgoingRequests("unfocus");
    navigate(`/friends/search-friend?query=${searchValue}`);

  };


  const handleBlur = () => {
    setIsSearchFocused(false);
  };

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
                <a className={style.friendsHeaderLink} href="">Friends</a>
              </div>
              <div className={style.linksListWrapper}>
                <ul className={style.linksList}>
                  <li className={linkAllFriends === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkAllFriends}>
                    <Link to="" className={linkAllFriends === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick}>All friends</Link>
                  </li>
                  <li className={linkIncomingRequests === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkIncomingRequests}>
                    <Link to="incoming-requests" className={linkIncomingRequests === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick}>Friend requests</Link>
                  </li>
                  <li className={linkOutgoingRequests === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkOutgoingRequests}>
                    <Link to="outgoing-requests" className={linkOutgoingRequests === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick}>Sent requests</Link>
                  </li>
                  <li>
                    <input 
                      id="searchInput" 
                      type="text" 
                      className={`${style.friendsSearch} ${isSearchFocused ? style.friendsSearchActive : ''}`}
                      name="text" 
                      placeholder="Search" 
                      value={searchValue}
                      onChange={handleInputChange}
                      onKeyPress={handleInputKeyPress}
                      onFocus={handleFocus}
                      onBlur={handleBlur}
                    />
                  </li>
                </ul>
              </div>
              <Outlet/>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default FriendsPage;
