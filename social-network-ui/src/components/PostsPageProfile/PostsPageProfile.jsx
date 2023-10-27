import React from "react";
import style from "./PostsPageProfile.module.scss";
import PostProfile from "../PostProfile/PostProfile";
import RepostProfile from "../RepostProfile/RepostProfile";
import ModalAddPost from "../ModalAddPost/ModalAddPost";
import { modalAddPostState, modalEditProfileState } from "../../redux-toolkit/profile/slice";
import { useDispatch, useSelector } from "react-redux";
import { ReactComponent as Calendar } from "../../img/calendarProfileInformation.svg";
import { ReactComponent as Home } from "../../img/homeProfileInformation.svg";

const PostPageProfile = () => {

  const dispatch = useDispatch();
  const userObject = useSelector(state => state.profile.profileUser.obj);

  const modalAddPostOpen = () => {
    dispatch(modalAddPostState(true));
  };
  const modalEditProfileOpen = () => {
    dispatch(modalEditProfileState(true));
  };

  return (
    <>

      <ModalAddPost />
      <div className={style.profileBodyWrapper}>
        <div className={style.profileBody}>
          <div className={style.profileInformation}>
            <h2 className={style.profileInformationTitile}>Information</h2>
            <ul className={style.profileInformationList}>
              {userObject.dateOfBirth ?
                <li className={style.profileInformationElem}>
                  <Calendar className={style.profileInformationElemImg} />
                  Age: {userObject.dateOfBirth}</li> : null}
              {userObject.address ?
                <li className={style.profileInformationElem}>
                  <Home className={style.profileInformationElemImg} />
                  Lives in: {userObject.address}
                </li> : null}
            </ul>
            {userObject.user === "myUser" ?
              <button className={style.profileInformationBtn} onClick={modalEditProfileOpen}>
                Add information
              </button> : null}
          </div>
          <div className={style.profileBodyPostsWrapper}>
            {userObject.user === "myUser" ?
              <div className={style.profileAddPost}>
                <img src={userObject.avatar ? userObject.avatar : "https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="" className={style.profileAddPostImg} />
                <button className={style.profileAddPostBtn} onClick={modalAddPostOpen}>Add post</button>
              </div> : null}
            <ul className={style.profilePosts}>
              <li className={style.profilePost}><PostProfile /></li>
              <li className={style.profilePost}><RepostProfile /></li>
              <li className={style.profilePost}><PostProfile /></li>
              <li className={style.profilePost}><PostProfile /></li>
            </ul>
          </div>
        </div>
      </div>
    </>
  );
};



export default PostPageProfile;