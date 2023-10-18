import React from "react";
import style from "./PostsPageProfile.module.scss";
import PostProfile from "../PostProfile/PostProfile";
import ModalAddPost from "../ModalAddPost/ModalAddPost";
import { modalAddPostState,modalEditProfileState} from "../../redux-toolkit/profile/slice";
import { useDispatch } from "react-redux";

const PostPageProfile = () => {

  const dispatch = useDispatch();

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
              <li className={style.profileInformationElem}>Навчалась в</li>
              <li className={style.profileInformationElem} >Мешкає в</li>
            </ul>
            <button className={style.profileInformationBtn} onClick={modalEditProfileOpen}>
              Add information
            </button>
          </div>
          <div className={style.profileBodyPostsWrapper}>
            <div className={style.profileAddPost}>
              <img src="https://risovach.ru/upload/2013/01/mem/kakoy-pacan_9771748_orig_.jpeg" alt="" className={style.profileAddPostImg} />
              <button className={style.profileAddPostBtn} onClick={modalAddPostOpen}>Add post</button>
            </div>
            <ul className={style.profilePosts}>
              <li className={style.profilePost}><PostProfile /></li>
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