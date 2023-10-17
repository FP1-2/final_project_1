import React, { useRef, useState } from "react";
import style from "./ProfilePage.module.scss";
import { Link, Outlet } from "react-router-dom";
import { ReactComponent as HeaderCamera } from "../../img/camera_headerPhoto.svg";
import { ReactComponent as AvatarCamera } from "../../img/camera_avatarPhoto.svg";
import { ReactComponent as Pencil } from "../../img/pencil.svg";

const ProfilePage = () => {
  const [linkPosts, setLinkPosts] = useState("focus");
  const [linkFriends, setLinkFriends] = useState("unfocus");
  const [linkLiked, setLinkLiked] = useState("unfocus");


  const inputHeaderPicture = useRef();
  const inputAvatarPicture = useRef();

  const clickInputHeaderPicture = () => {
    inputHeaderPicture.current.click();
  };

  const clickInputAvatarPicture = () => {
    inputHeaderPicture.current.click();
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




  return (<>
    <div className={style.profilePage}>
      <div className={style.headerImgWrapper}>
        <img className={style.headerImg} src="https://bipbap.ru/wp-content/uploads/2017/04/0_7c779_5df17311_orig.jpg" alt="" />           <input type="file" name="" id="" style={{ display: "none" }} />
        <button className={style.headerBtn} onClick={clickInputHeaderPicture}>
          <HeaderCamera className={style.headerBtnCamera} />
          <input type="file" style={{ display: "none" }} ref={inputHeaderPicture} />
          Upload header picture
        </button>
      </div>
      <div className={style.infoWrapper}>
        <div className={style.info}>
          <div className={style.infoAvatarWrapper}>
            <img className={style.infoAvatar} src="https://risovach.ru/upload/2013/01/mem/kakoy-pacan_9771748_orig_.jpeg" alt="" />
            <button className={style.infoAvatarBtn} onClick={clickInputAvatarPicture}>
              <input type="file" style={{ display: "none" }} ref={inputAvatarPicture} />
              <AvatarCamera className={style.infoAvatarBtnCamera} />
            </button>
          </div>
          <div className={style.infoNameWrapper}>
            <h2 className={style.infoName}>Ірина Надточий</h2>
            <Link to='friends' onClick={clickLinkFriends} className={style.infoFriends} href="">Friends: </Link>
          </div>
          <button className={style.infoBtnEdit}>
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
  </>);
};
export default ProfilePage;
