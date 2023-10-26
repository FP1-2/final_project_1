import React, { useRef, useState, useEffect } from "react";
import style from "./ProfilePage.module.scss";
import ModalEditProfile from "../../components/ModalEditProfile/ModalEditProfile";
// import ModalDeleteFriend from "../../components/ModalDeleteFriend/ModalDeleteFriend";
// import { modalDeleteFriendState } from "../../redux-toolkit/profile/slice";
import { Link, Outlet } from "react-router-dom";
import { ReactComponent as HeaderCamera } from "../../img/camera_headerPhoto.svg";
import { ReactComponent as AvatarCamera } from "../../img/camera_avatarPhoto.svg";
import { ReactComponent as Pencil } from "../../img/pencil.svg";
// import { ReactComponent as DeleteFriend } from "../../img/deleteFriend.svg";
// import { ReactComponent as AddFriend } from "../../img/addFriend.svg";
// import { ReactComponent as FacebookMessenger } from "../../img/facebookMessenger.svg";
import { useDispatch, useSelector } from "react-redux";
import { modalEditProfileState } from "../../redux-toolkit/profile/slice";
import { getPhotoURL } from "../../redux-toolkit/profile/thunks";
import PropTypes from "prop-types";
import { getTokenFromLocalStorage } from "../../utils/localStorageHelper";
import { getUserThunkRequest,editUserThunkRequest} from "../../redux-toolkit/profile/thunks";

const ProfilePage = () => {
  const dispatch = useDispatch();
  const userObject = useSelector(state => state.profile.profileUser.obj);

  const [linkPosts, setLinkPosts] = useState("focus");
  const [linkFriends, setLinkFriends] = useState("unfocus");
  const [linkLiked, setLinkLiked] = useState("unfocus");

  const indexSlash = window.location.pathname.lastIndexOf('/');
  const word = window.location.pathname.slice(indexSlash + 1);

  const inputHeaderPicture = useRef();
  const inputAvatarPicture = useRef();


  useEffect(() => {
    if (!Object.keys(userObject).length) {
      const token = getTokenFromLocalStorage();
      const decodedToken = parseJwt(token);
      const userId = decodedToken.sub; // `sub` содержит ваш userId
      const obj = {
        token: token,
        id: userId,
      };
      dispatch(getUserThunkRequest(obj));
    }
  }
  );


  const clickInputHeaderPicture = () => {
    inputHeaderPicture.current.click();
  };

  const downloadInputHeaderPicture = async (e) => {
    const file = e.target.files[0];
    const photo=(await getPhotoURL(file)).data.url;
    dispatch(editUserThunkRequest({headerPhoto:photo}));
  };

  const clickInputAvatarPicture = () => {
    inputHeaderPicture.current.click();
  };

  const modalEditProfileOpen = () => {
    dispatch(modalEditProfileState(true));
  };



  const clickLinkPosts = () => {
    setLinkPosts("focus");
    setLinkFriends("unfocus");
    setLinkLiked("unfocus");
  };

  const clickLinkFriends = () => {
    setLinkPosts("unfocus");
    setLinkFriends("focus");
    setLinkLiked("unfocus");
  };

  const clickLinkLiked = () => {
    setLinkPosts("unfocus");
    setLinkFriends("unfocus");
    setLinkLiked("focus");
  };


  if (word === "profile" && linkPosts !== "focus") {
    clickLinkPosts();
  } else if (word === "friends" && linkFriends !== "focus") {
    clickLinkFriends();
  } else if (word === "liked" && linkLiked !== "focus") {
    clickLinkLiked();
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
    {/* {typeProfile === "myUser" ? <> */}
    <ModalEditProfile />
    <div className={style.profilePage}>
      <div className={style.headerWrapper}>
        <header className={style.header}>
          <img className={style.headerImg} src={userObject.headerPhoto?userObject.headerPhoto:"https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="" />
          <input type="file" name="" id="" style={{ display: "none" }} />
          <button className={style.headerBtn} onClick={clickInputHeaderPicture}>
            <HeaderCamera className={style.headerBtnCamera} />
            <input type="file" style={{ display: "none" }} ref={inputHeaderPicture} onChange={(e) => downloadInputHeaderPicture(e)} />
            <p className={style.headerBtnText}>Upload header picture</p>
          </button>
        </header>
      </div>
      <div className={style.infoWrapper}>
        <div className={style.info}>
          <div className={style.infoAvatarWrapper}>
            <div className={style.infoAvatar}>
              <img className={style.infoAvatarImg} src={userObject.avatar?userObject.avatar:"https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="" />
              <button className={style.infoAvatarBtn} onClick={clickInputAvatarPicture}>
                <input type="file" style={{ display: "none" }} ref={inputAvatarPicture} />
                <AvatarCamera className={style.infoAvatarBtnCamera} />
              </button>
            </div>
          </div>
          <div className={style.infoNameWrapper}>
            <h2 className={style.infoName}>{`${userObject.name} ${userObject.surname}`}</h2>
            <p className={style.infoFriends} href="">Friends: </p>
          </div>
          <button className={style.infoBtnEdit} onClick={modalEditProfileOpen}>
            <Pencil className={style.infoBtnPencil} />
            Edit profile
          </button>
        </div>
      </div>
      <div className={style.linksListWrapper}>
        <ul className={style.linksList}>
          <li className={linkPosts === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkPosts}>
            <Link to="" className={linkPosts === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Posts</Link>
          </li>
          <li className={linkFriends === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkFriends}>
            <Link to="friends" className={linkFriends === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Friends</Link>
          </li>
          <li className={linkLiked === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkLiked}>
            <Link to="liked" className={linkLiked === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Liked</Link>
          </li>
        </ul>
      </div>
      <Outlet/>
    </div>
    {/* </> */}
    {/* // : <> */}
    {/* //   <ModalDeleteFriend />
      //   <div className={style.profilePage}>
      //     <div className={style.headerWrapper}>
      //       <header className={style.header}>
      //         <img className={style.headerImg} src="https://bipbap.ru/wp-content/uploads/2017/04/0_7c779_5df17311_orig.jpg" alt="" />
      //       </header>
      //     </div>
      //     <div className={style.infoWrapper}>
      //       <div className={style.info}>
      //         <div className={style.infoAvatarWrapper}>
      //           <div className={style.infoAvatar}>
      //             <img className={style.infoAvatarImg} src="https://risovach.ru/upload/2013/01/mem/kakoy-pacan_9771748_orig_.jpeg" alt="" />
      //           </div>
      //         </div>
      //         <div className={style.infoNameWrapper}>
      //           <h2 className={style.infoName}>Інший користувач</h2>
      //         </div>
      //         <div className={style.infoBtnsWrapper}>
      //           <button className={style.infoBtnDeleteFriend} onClick={modalDeleteFriendOpen}>
      //             <DeleteFriend className={style.infoBtnDeleteFriendImg} />
      //             Delete
      //           </button>
      //           {/* <button className={style.infoBtnAddFriend}>
      //             <AddFriend className={style.infoBtnAddFriendImg}/>
      //             Add Friend
      //           </button> */}
    {/* //           <button className={style.infoBtnMessage}>
      //             <FacebookMessenger className={style.infoBtnMessageImg} />
      //             Message
      //           </button>
      //         </div> */}
    {/* //       </div> */}
    {/* //     </div> */}
    {/* //     <div className={style.linksListWrapper}>
      //       <ul className={style.linksList}>
      //         <li className={linkPosts === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkPosts}>
      //           <Link to="" className={linkPosts === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Posts</Link>
      //         </li>
      //         <li className={linkFriends === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkFriends}>
      //           <Link to="friends" className={linkFriends === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Friends</Link>
      //         </li>
      //         <li className={linkLiked === "unfocus" ? style.linksListElem : style.linksListElemClick} onClick={clickLinkLiked}>
      //           <Link to="liked" className={linkLiked === "unfocus" ? style.linksListElemLink : style.linksListElemLinkClick} href="">Liked</Link>
      //         </li>
      //       </ul>
      //     </div>
      //     <Outlet />
        </div> */}
    {/* // </>} */}
  </>);
};

ProfilePage.propTypes = {
  typeProfile: PropTypes.string
};
ProfilePage.defaultProps = {
  typeProfile: "myUser"
};

export default ProfilePage;
