import React, { useRef, useEffect } from "react";
import style from "./PostsPageProfile.module.scss";
import PostProfile from "../PostProfile/PostProfile";
import RepostProfile from "../RepostProfile/RepostProfile";
import ModalAddPost from "../ModalAddPost/ModalAddPost";
import { modalEditProfileState } from "../../redux-toolkit/profile/slice";
import { modalAddPostState, resetPostsState } from "../../redux-toolkit/post/slice";
import { useDispatch, useSelector } from "react-redux";
import { ReactComponent as Calendar } from "../../img/calendarProfileInformation.svg";
import { ReactComponent as Home } from "../../img/homeProfileInformation.svg";
import ModalEditPost from "../ModalEditPost/ModalEditPost";
import ModalAddRepost from "../ModalAddRepost/ModalAddRepost";
import { useParams } from "react-router";
import { postsUser } from "../../redux-toolkit/post/thunks";

const PostPageProfile = () => {

  const dispatch = useDispatch();
  let idNavigate = useParams().id;
  idNavigate = parseInt(idNavigate);
  const scrollContainerRef = useRef(null);
  const userObject = useSelector(state => state.profile.profileUser.obj);

  const {
    obj: {
      content,
    }
  } = useSelector(state => state.post.postsUser);

  useEffect(() => {
    dispatch(postsUser({ id: idNavigate, page: 0 }));
    return(()=>{dispatch(resetPostsState());});
  }, []);

  const modalAddPostOpen = () => {
    dispatch(modalAddPostState(true));
  };
  const modalEditProfileOpen = () => {
    dispatch(modalEditProfileState(true));
  };

  return (
    <>
      <ModalAddRepost />
      <ModalEditPost />
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
            <ul className={style.profilePosts}  ref={scrollContainerRef}>
              {content ? content.map((el) =>
                <li className={style.profilePost} key={el.postId}>
                  {el.type === "REPOST" ?
                    <RepostProfile el={el} />
                    : <PostProfile el={el} />}
                </li>
              ) : null}
            </ul>
          </div>
        </div>
      </div>
    </>
  );
};



export default PostPageProfile;