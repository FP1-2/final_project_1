import React, { useRef, useState, useEffect } from "react";
import style from "./ProfilePage.module.scss";
import ModalEditProfile from "../../components/ModalEditProfile/ModalEditProfile";
import ModalDeleteFriend from "../../components/ModalDeleteFriend/ModalDeleteFriend";
import { modalDeleteFriendState } from "../../redux-toolkit/friend/slice";
import { Link, Outlet } from "react-router-dom";
import { ReactComponent as HeaderCamera } from "../../img/camera_headerPhoto.svg";
import { ReactComponent as AvatarCamera } from "../../img/camera_avatarPhoto.svg";
import { ReactComponent as Pencil } from "../../img/pencil.svg";
import { ReactComponent as DeleteFriend } from "../../img/deleteFriend.svg";
import { ReactComponent as AddFriend } from "../../img/addFriend.svg";
import { ReactComponent as FacebookMessenger } from "../../img/facebookMessenger.svg";
import { useDispatch, useSelector } from "react-redux";
import { modalEditProfileState, removeUser } from "../../redux-toolkit/profile/slice";
import { getPhotoURL } from "../../redux-toolkit/profile/thunks";
import { getTokenFromLocalStorage } from "../../utils/localStorageHelper";
import { editUser, loadUserProfile} from "../../redux-toolkit/profile/thunks";
import { postsUser } from "../../redux-toolkit/post/thunks";
import { useParams } from "react-router-dom";
import { getFriends } from "../../redux-toolkit/friend/thunks";
import ErrorPage from "../..//components/ErrorPage/ErrorPage";
import { friend, requestToFriend } from "../../redux-toolkit/friend/thunks";

const ProfilePage = () => {
  const dispatch = useDispatch();
  const { id } = useParams();
  const {
    profileUser: {
      obj,
      status,
      error
    }
  } = useSelector(state => state.profile);
  const deleteStatus = useSelector(state => state.friends.deleteMyFriend);
  const editUserStatus = useSelector(state => state.profile.editUser.obj);
  const friends = useSelector(state => state.friends.getFriends.obj);
  const myId = useSelector(state => state.auth.user.obj.id);

  const [linkPosts, setLinkPosts] = useState("focus");
  const [linkFriends, setLinkFriends] = useState("unfocus");


  const indexSlash = window.location.pathname.lastIndexOf('/');
  const word = window.location.pathname.slice(indexSlash + 1);

  let isMyFriend;
  for (const el of friends) {
    if (el.id === myId) {
      isMyFriend = true;
    }
  }

  const inputHeaderPicture = useRef();
  const inputAvatarPicture = useRef();


  useEffect(() => {
    const token = getTokenFromLocalStorage();
    const decodedToken = parseJwt(token);
    const userId = decodedToken.sub;
    if (Object.keys(obj)) {
      dispatch(removeUser());
    }
    getUser(userId, token);
    dispatch(getFriends(id));
    dispatch(postsUser(id));
  }, [id, deleteStatus, editUserStatus]);

  const getUser = (userId) => {
    let newObj = {};
    if (userId === id) {
      newObj = {
        user: "myUser",
        id: id
      };
    } else {
      newObj = {
        user: "anotherUser",
        id: id
      };
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
    dispatch(friend(id));
    dispatch(modalDeleteFriendState(true));
  };

  const addFriend = () => {
    dispatch(requestToFriend({ friendId: id }));
  };


  const clickLinkPosts = () => {
    setLinkPosts("focus");
    setLinkFriends("unfocus");
  };

  const clickLinkFriends = () => {
    setLinkPosts("unfocus");
    setLinkFriends("focus");
  };



  if (word === "profile" && linkPosts !== "focus") {
    clickLinkPosts();
  } else if (linkFriends !== "focus" && word === "friends") {
    clickLinkFriends();
  }else if (word !== "friends" && linkFriends === "focus"){
    clickLinkPosts();
  }


  function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
  }

  return (<>
    {status === "pending" ?
      <div className={style.loderWrapper}>
        <div className={style.loder}></div>
      </div>
      : status === "rejected" ?
        <ErrorPage message={error ? error : "Oops something went wrong!"} />
        :
        <>
          <ModalEditProfile />
          <ModalDeleteFriend />
          <div className={style.profilePage}>
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
                  <p className={style.infoFriends} href="">Friends: {friends.length}</p>
                </div>
                {obj.user === "myUser" ? <button className={style.infoBtnEdit} onClick={modalEditProfileOpen}>
                  <Pencil className={style.infoBtnPencil} />
                  Edit profile
                </button>
                  :
                  <div className={style.infoBtnsWrapper}>
                    {isMyFriend && deleteStatus !== "fulfilled" ?
                      <button className={style.infoBtnDeleteFriend} onClick={modalDeleteFriendOpen}>
                        <DeleteFriend className={style.infoBtnDeleteFriendImg} />
                        Delete
                      </button>
                      :
                      <button className={style.infoBtnAddFriend} onClick={addFriend}>
                        <AddFriend className={style.infoBtnAddFriendImg} />
                        Add Friend
                      </button>}
                    <button className={style.infoBtnMessage}>
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
          </div>
        </>
    }
  </>);
};
export default ProfilePage;
