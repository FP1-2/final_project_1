import React, { useRef,useState} from "react";
import style from "./ProfilePage.module.scss";
import ModalEditProfile from "../../components/ModalEditProfile/ModalEditProfile";
import { Link, Outlet } from "react-router-dom";
import { ReactComponent as HeaderCamera } from "../../img/camera_headerPhoto.svg";
import { ReactComponent as AvatarCamera } from "../../img/camera_avatarPhoto.svg";
import { ReactComponent as Pencil } from "../../img/pencil.svg";
import { useDispatch} from "react-redux";
import { modalEditProfileState} from "../../redux-toolkit/profile/slice";
import { getPhotoURL } from "../../redux-toolkit/profile/thunks";

const ProfilePage = () => {
  const dispatch=useDispatch();
  const [linkPosts, setLinkPosts] = useState("focus");
  const [linkFriends, setLinkFriends] = useState("unfocus");
  const [linkLiked, setLinkLiked] = useState("unfocus");

  const indexSlash = window.location.pathname.lastIndexOf('/');
  const word=window.location.pathname.slice(indexSlash+1);


  const inputHeaderPicture = useRef();
  const inputAvatarPicture = useRef();

  const clickInputHeaderPicture = () => {
    inputHeaderPicture.current.click();
  };

  const downloadInputHeaderPicture= async(e)=>{
    const file=e.target.files[0];
    console.log((await getPhotoURL(file)).data.url);
  };

  const clickInputAvatarPicture = () => {
    inputHeaderPicture.current.click();
  };



  const modalEditProfileOpen=()=>{
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


  if (word==="profile" && linkPosts!=="focus"){
    clickLinkPosts();
  }else if (word==="friends" && linkFriends!=="focus"){
    clickLinkFriends();
  }else if (word==="liked" && linkLiked!=="focus"){
    clickLinkLiked();
  }

  return (<>
    <ModalEditProfile/>
    <div className={style.profilePage}>
      <div className={style.headerImgWrapper}>
        <img className={style.headerImg} src="https://bipbap.ru/wp-content/uploads/2017/04/0_7c779_5df17311_orig.jpg" alt="" />           <input type="file" name="" id="" style={{ display: "none" }} />
        <button className={style.headerBtn} onClick={clickInputHeaderPicture}>
          <HeaderCamera className={style.headerBtnCamera} />
          <input type="file" style={{ display: "none" }} ref={inputHeaderPicture} onChange={(e)=>downloadInputHeaderPicture(e)}/>
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
            <Link to='friends' className={style.infoFriends} href="">Friends: </Link>
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
  </>);
};
export default ProfilePage;
