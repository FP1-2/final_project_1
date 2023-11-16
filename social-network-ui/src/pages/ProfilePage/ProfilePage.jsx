import React, { useRef, useState, useEffect } from "react";
import style from "./ProfilePage.module.scss";
import ModalEditProfile from "../../components/ModalEditProfile/ModalEditProfile";
import { modalDeleteFriendState, clearFriends, cancelLocalRequest, addLocalFriend, addLocalSendRequest,deleteLocalReceived} from "../../redux-toolkit/friend/slice";
import { Link, Outlet, useLocation, useNavigate } from "react-router-dom";
import { ReactComponent as HeaderCamera } from "../../img/camera_headerPhoto.svg";
import { ReactComponent as AvatarCamera } from "../../img/camera_avatarPhoto.svg";
import { ReactComponent as Pencil } from "../../img/pencil.svg";
import { ReactComponent as DeleteFriend } from "../../img/deleteFriend.svg";
import { ReactComponent as AddFriend } from "../../img/addFriend.svg";
import { ReactComponent as FacebookMessenger } from "../../img/facebookMessenger.svg";
import { useDispatch, useSelector } from "react-redux";
import { modalEditProfileState, removeUser } from "../../redux-toolkit/profile/slice";
import { getPhotoURL } from "../../utils/thunks";
import { editUser, loadUserProfile } from "../../redux-toolkit/profile/thunks";
import { postsUser } from "../../redux-toolkit/post/thunks";
import { useParams } from "react-router-dom";
import { getFriends, cancelRequest, requestToFriend, getMyFriends } from "../../redux-toolkit/friend/thunks";
import ErrorPage from "../..//components/ErrorPage/ErrorPage";
import { friend, allRequests, confirmFriendRequest } from "../../redux-toolkit/friend/thunks";
import ModalDeleteFriend from "../../components/ModalDeleteFriend/ModalDeleteFriend";
import { createChat } from "../../redux-toolkit/messenger/asyncThunk";
import { createHandleScroll } from "../../utils/utils";
import { loadAuthUser } from "../../redux-toolkit/login/thunks";
import { resetNewChat } from "../../redux-toolkit/messenger/slice";

const ProfilePage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  let idNavigate = useParams().id;
  idNavigate = parseInt(idNavigate);
  const {
    profileUser: {
      obj,
      status,
      error
    }
  } = useSelector(state => state.profile);

  const {
    obj: {
      totalPages,
      pageable: {
        pageNumber,
      }
    }
  } = useSelector(state => state.post.postsUser);
  const postsStatus = useSelector(state => state.post.postsUser.status);

  const deleteStatus = useSelector(state => state.friends.deleteMyFriend);
  const profileName = useSelector(state => state.profile.profileUser.obj);
  const editUserStatus = useSelector(state => state.profile.editUser);
  const friends = useSelector(state => state.friends.getFriends);
  const myFriends = useSelector(state => state.friends.getMyFriends);
  const myId = useSelector(state => state.auth.user.obj.id);
  const newChat = useSelector(state => state.messenger.newChat);
  const requests = useSelector(state => state.friends.allRequests.obj);

  const [linkPosts, setLinkPosts] = useState("focus");
  const [linkFriends, setLinkFriends] = useState("unfocus");
  const [sendRequest, setSendRequest] = useState("no request");
  const [isMyFriend, setIsMyFriend] = useState();



  useEffect(() => {
    if (newChat.status === 'fulfilled') {
      navigate(`/messages/${newChat.obj.id}`);
      dispatch(resetNewChat());
    }
  }, [newChat]);

  useEffect(() => {
    return (() => dispatch(clearFriends()));
  }, []);


  useEffect(() => {
    for (const el of requests.send) {
      if (el.id === idNavigate) {
        setSendRequest("my request");
      }
    }
    for (const el of requests.received) {
      if (el.id === idNavigate) {
        setSendRequest("request to me");
      }
    }
  }, [requests, sendRequest, myFriends]);



  const location = useLocation();
  const indexSlash = location.pathname.lastIndexOf('/');
  const word = location.pathname.slice(indexSlash + 1);



  const inputHeaderPicture = useRef();
  const inputAvatarPicture = useRef();
  const scrollContainerRef = useRef(null);

  useEffect(() => {
    if (Object.keys(obj)) {
      dispatch(removeUser());
    }
    getUser(myId);
    dispatch(getMyFriends(myId));
    dispatch(postsUser({ id: idNavigate, page: 0 }));
  }, [idNavigate, deleteStatus, editUserStatus]);

  useEffect(() => {
    if (myFriends.status === "fulfilled" && myFriends.obj.length !== 0)
      for (const el of myFriends.obj) {
        if (el.id === idNavigate)
          setIsMyFriend(true);
      }
    else if (myFriends.obj.length === 0)
      setIsMyFriend(false);
  }, [myFriends.status, myFriends.obj, idNavigate]);

  useEffect(() => {
    if (editUserStatus.status === "fulfilled") {
      dispatch(loadAuthUser(idNavigate));
    }
  }, [editUserStatus]);

  const getUser = (userId) => {
    let newObj = {};
    idNavigate = parseInt(idNavigate);
    if (userId === idNavigate) {
      newObj = {
        user: "myUser",
        id: idNavigate
      };
      dispatch(allRequests());
    } else {
      newObj = {
        user: "anotherUser",
        id: idNavigate
      };
      dispatch(getFriends(idNavigate));
    }
    dispatch(loadUserProfile(newObj));

  };

  const downloadInputHeaderPicture = async (e) => {
    const file = e.target.files[0];
    const photo = (await getPhotoURL(file)).data.url;
    dispatch(editUser({ headerPhoto: photo }));
  };

  const downloadInputAvatarPicture = async (e) => {
    const file = e.target.files[0];
    const photo = (await getPhotoURL(file)).data.url;
    dispatch(editUser({ avatar: photo }));
  };

  const clickInputHeaderPicture = () => {
    inputHeaderPicture.current.click();
  };

  const clickInputAvatarPicture = () => {
    inputAvatarPicture.current.click();
  };

  const modalEditProfileOpen = () => {
    dispatch(modalEditProfileState(true));
  };
  const modalDeleteFriendOpen = () => {
    dispatch(friend(idNavigate));
    dispatch(modalDeleteFriendState(true));
  };

  const clickSendRequest = () => {
    dispatch(requestToFriend({ friendId: idNavigate }));
    dispatch(addLocalSendRequest(obj));
    setSendRequest("myRequest");
  };

  const addFriend = () => {
    dispatch(confirmFriendRequest({ userId: idNavigate, status: true }));
    dispatch(addLocalFriend(obj));
    dispatch(deleteLocalReceived(idNavigate));
    setIsMyFriend(true);
  };
  // const ignor = () => {
  //   dispatch(confirmFriendRequest({ userId: idNavigate, status: false }));
  //   setSendRequest("no request" );
  // };

  const clickCancelRequest = () => {
    dispatch(cancelRequest({ friendId: myId }));
    dispatch(cancelLocalRequest(idNavigate));
    setSendRequest("no request");
  };

  const clickLinkPosts = () => {
    setLinkPosts("focus");
    setLinkFriends("unfocus");
  };

  const clickLinkFriends = () => {
    setLinkPosts("unfocus");
    setLinkFriends("focus");
  };


  const createNewChat = () => {
    dispatch(createChat({ username: profileName.username }));
  };



  if (word === "profile" && linkPosts !== "focus") {
    clickLinkPosts();
  } else if (linkFriends !== "focus" && word === "friends") {
    clickLinkFriends();
  } else if (word !== "friends" && linkFriends === "focus") {
    clickLinkPosts();
  }

  const getMorePosts = () => {
    if (postsStatus !== 'pending' && pageNumber < totalPages) {
      dispatch(postsUser({ page: pageNumber + 1, id: idNavigate }));
    }
  };

  const handleScroll = createHandleScroll({
    scrollRef: scrollContainerRef,
    status: postsStatus,
    fetchMore: getMorePosts,
  });


  return (<>
    {status === "pending" ?
      <div className={style.loderWrapper}>
        <div className={style.loder}></div>
      </div>
      : status === "rejected" ?
        <ErrorPage message={error ? error : "Oops something went wrong!"} />
        :
        <>
          <ModalDeleteFriend />
          <ModalEditProfile />
          <div className={style.profilePage} onScroll={() => handleScroll()} ref={scrollContainerRef}>
            <div className={style.headerWrapper}>
              <header className={style.header}>
                <img className={style.headerImg} src={obj.headerPhoto ? obj.headerPhoto : "https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="" />
                {obj.user === "myUser" ? <button className={style.headerBtn} onClick={clickInputHeaderPicture}>
                  <HeaderCamera className={style.headerBtnCamera} />
                  <input type="file" style={{ display: "none" }} ref={inputHeaderPicture} onChange={(e) => downloadInputHeaderPicture(e)} />
                  <p className={style.headerBtnText}>Upload header picture</p>
                </button> : null}
              </header>
            </div>
            <div className={style.infoWrapper}>
              <div className={style.info}>
                <div className={style.infoAvatarWrapper}>
                  <div className={style.infoAvatar}>
                    <img className={style.infoAvatarImg} src={obj.avatar ? obj.avatar : "https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="" />
                    {obj.user === "myUser" ? <button className={style.infoAvatarBtn} onClick={clickInputAvatarPicture}>
                      <input type="file" style={{ display: "none" }} ref={inputAvatarPicture} onChange={(e) => downloadInputAvatarPicture(e)} />
                      <AvatarCamera className={style.infoAvatarBtnCamera} />
                    </button> : null}
                  </div>
                </div>
                <div className={style.infoNameWrapper}>
                  <h2 className={style.infoName}>{`${obj.name} ${obj.surname}`}</h2>
                  <p className={style.infoFriends} href="">Friends: {myId === idNavigate ? myFriends.obj.length : friends.obj.length}</p>
                </div>
                {obj.user === "myUser" ? <button className={style.infoBtnEdit} onClick={modalEditProfileOpen}>
                  <Pencil className={style.infoBtnPencil} />
                  Edit profile
                </button>
                  :
                  <div className={style.infoBtnsWrapper}>
                    {isMyFriend ?
                      <button className={style.infoBtnDeleteFriend} onClick={modalDeleteFriendOpen}>
                        <DeleteFriend className={style.infoBtnDeleteFriendImg} />
                        Delete
                      </button>
                      :
                      (sendRequest === "no request" ?
                        <button className={style.infoBtnFriend} onClick={clickSendRequest}>
                          <AddFriend className={style.infoBtnFriendImg} />
                          Send request
                        </button>
                        : sendRequest === "request to me" ?
                          // <div className={style.infoBtnsFriendWrapper}>
                          // <button className={style.infoBtnFriend} onClick={ignor}>
                          //   <AddFriend className={style.infoBtnFriendImg} />
                          //   Ignore
                          // </button>
                          <button className={style.infoBtnFriend} onClick={addFriend}>
                            <AddFriend className={style.infoBtnFriendImg} />
                            Add to friends
                          </button>
                          // </div>
                          :
                          <button className={style.infoBtnFriend} onClick={clickCancelRequest}>
                            <AddFriend className={style.infoBtnFriendImg} />
                            Cancel request
                          </button>)
                    }
                    <button className={style.infoBtnMessage} onClick={createNewChat}>
                      <FacebookMessenger className={style.infoBtnMessageImg} />
                      Message
                    </button>
                  </div>}
              </div>
            </div>
            <div className={style.linksListWrapper}>
              <ul className={style.linksList}>
                <li className={linkPosts === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkPosts}>
                  <Link to="" className={linkPosts === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Posts</Link>
                </li>
                <li className={linkFriends === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkFriends}>
                  {obj.user === "myUser" ?
                    <Link to="/friends" className={linkFriends === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Friends</Link>
                    : <Link to="friends" className={linkFriends === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Friends</Link>}
                </li>
              </ul>
            </div>
            <Outlet />
            {pageNumber === totalPages && <h4 className={style.container_allCard}>That`s all for now!</h4>}
          </div>
        </>
    }
  </>);
};
export default ProfilePage;
