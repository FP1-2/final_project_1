import React, { useState, useEffect } from "react";
import style from "./PostProfile.module.scss";
import { ReactComponent as LikePostBtn } from "../../img/likePostBtn.svg";
import { ReactComponent as BlueLike } from "../../img/blueLike.svg";
import { ReactComponent as CommentPostBtn } from "../../img/commentPostBtn.svg";
import { ReactComponent as BlueComment } from "../../img/blueComment.svg";
import { ReactComponent as SharePostBtn } from "../../img/sharePostBtn.svg";
import { ReactComponent as SendCommentPost } from "../../img/sendCommentPost.svg";
import { ReactComponent as Pencil } from "../../img/pencil.svg";
import { ReactComponent as Delete } from "../../img/delete.svg";
import { ReactComponent as Dots } from "../../img/dots.svg";
import { NavLink } from "react-router-dom";
import { useSelector } from "react-redux";
import PropTypes from "prop-types";
import Comment from "../Comment/Comment";
import { addRepost } from "../../redux-toolkit/post/thunks";
import { useDispatch} from "react-redux";


const PostProfile = ({ el }) => {
  const dispatch=useDispatch();

  const [clickLike, setClickLike] = useState(false);
  const [clickComment, setClickComment] = useState(false);
  const [btnAlso, setBtnAlso] = useState(false);
  // console.log(el);

  const userAvatar = useSelector(state => state.auth.user.obj.avatar);
  const typeUser = useSelector(state => state.profile.profileUser.obj.user);

  useEffect(() => {

  }, []);


  const changeClickLike = () => {
    setClickLike(state => !state);
  };

  const sharePost = () => {
    dispatch(addRepost({imageUrl:el.imageUrl, body:el.text, title:"add repost", originalPostId:el.postId}));
  };

  const commentClick = () => {
    setClickComment((val => !val));
  };

  return (
    <div className={style.post}>
      <header className={style.postHeader}>
        <div className={style.postHeaderLinksWrapper}>
          <NavLink to={`/profile/${el.author.userId}`} className={style.postAvatarLink}>
            <img src={el.author.avatar
              ? el.author.avatar
              : "https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="" className={style.postAvatar} />
          </NavLink>
          <NavLink to={`/profile/${el.author.userId}`} className={style.postNameLink} href="">{`${el.author.name} ${el.author.surname}`}</NavLink>
        </div>
        {typeUser === "myUser" ? <button className={style.postHeaderAlsoBtn} onClick={() => setBtnAlso((val) => !val)}>
          <Dots className={style.postHeaderAlsoBtnImg} />
        </button>
          : null}
        {btnAlso ? <div className={style.postHeaderBtnWrapper}>
          <button className={style.postHeaderBtn}>
            <Pencil className={style.postHeaderBtnImg} />
            Edit post
          </button>
          <button className={style.postHeaderBtn}>
            <Delete className={style.postHeaderBtnImg} />
            Delete post
          </button>
        </div>
          : null}

      </header>
      <p className={style.postText}>{el.body}</p>
      {el.imageUrl ? <img src={el.imageUrl} alt="Photo of post" className={style.postImg} /> : null}
      <div className={style.postFooterWrapper}>
        <div className={style.postFooterInfo}>
          <div className={style.postFooterLikes}>
            <BlueLike className={style.postFooterLikesImg} />
            <p className={style.postFooterLikesText}>{el.likes ? el.likes.length : 0}</p>
          </div>
          <div className={style.postFooterComments}>
            <BlueComment className={style.postFooterCommentsImg} />
            <p className={style.postFooterCommentsText}>{el.comments ? el.comments.length : 0}</p>
          </div>
        </div>
        <div className={style.postFooterBtns}>
          <button className={clickLike ? style.active : style.postBtn} onClick={changeClickLike}>
            <LikePostBtn className={style.postLikeBtnImg} />
            Like
          </button>
          <button className={style.postBtn} onClick={commentClick}>
            <CommentPostBtn className={style.postCommentBtnImg} />
            Comment
          </button>
          <button className={style.postBtn} onClick={sharePost}>
            <SharePostBtn className={style.postLikeBtnImg} />
            Share
          </button>
        </div>
        {clickComment ? <div className={style.postFooterComents}>
          <Comment />
          <Comment />
          <div className={style.postFooterAddComents}>
            <img src={userAvatar ? userAvatar : "https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="Avatar" className={style.postFooterAddComentsImg} />
            <input type="text" placeholder="Write a comment..." className={style.postFooterAddComentsText} />
            <button className={style.postFooterAddComentBtn}>
              <SendCommentPost className={style.postFooterAddComentBtnImg} />
            </button>
          </div>
        </div>
          : null}
      </div>
    </div>
  );
};

PostProfile.propTypes = {
  el: PropTypes.object.isRequired,
};


export default PostProfile;