import React, { useState,useRef } from "react";
import style from "./Post.module.scss";
import { ReactComponent as LikePostBtn } from "../../img/likePostBtn.svg";
import { ReactComponent as BlueLike } from "../../img/blueLike.svg";
import { ReactComponent as CommentPostBtn} from "../../img/commentPostBtn.svg";
import { ReactComponent as SharePostBtn} from "../../img/sharePostBtn.svg";
import { ReactComponent as SendCommentPost} from "../../img/sendCommentPost.svg";
import Comment from "../Comment/Comment";


const Post = () => {

  const [clickLike, setClickLike] = useState(false);
  const commentInputFocus=useRef();


  const changeClickLike = () => {
    setClickLike(state => !state);
  };

  const commentInputFocusClick = () => {
    commentInputFocus.current.focus();
  };

  // const changeClickComment = () => {
  //   setClickComment(state => !state);
  // }

  return (
    <div className={style.postWrapper}>
      <div className={style.post}>
        <div className={style.postHeaderWrapper}>
          <a href="" className={style.postAvatarLink}>
            <img src="https://risovach.ru/upload/2013/01/mem/kakoy-pacan_9771748_orig_.jpeg" alt="" className={style.postAvatar} />
          </a>
          <a className={style.postNameLink} href="">Ірина Сергіївна Надточий</a>
        </div>
        <img src="https://imgv3.fotor.com/images/slider-image/Female-portrait-photo-enhanced-with-clarity-and-higher-quality-using-Fotors-free-online-AI-photo-enhancer.jpg" alt="" className={style.postImg} />
        <div className={style.postFooterWrapper}>
          <div className={style.postFooterLikes}>
            <BlueLike className={style.postFooterLikesImg}/>
            <p className={style.postFooterLikesText}>Liked ..</p>
          </div>
          <div className={style.postFooterBtns}>         
            <button className={clickLike ? style.active : style.postBtn} onClick={changeClickLike}>
              <LikePostBtn className={style.postLikeBtnImg} />
              Like
            </button>
            <button className={style.postBtn} onClick={commentInputFocusClick}>
              <CommentPostBtn className={style.postCommentBtnImg} />
              Comment
            </button>
            <button className={style.postBtn}>
              <SharePostBtn className={style.postLikeBtnImg}/>
            Share
            </button>
          </div>
          <div className={style.postFooterComents}>
            <Comment/>
            <div className={style.postFooterAddComents}>
              <img src="https://imgv3.fotor.com/images/slider-image/Female-portrait-photo-enhanced-with-clarity-and-higher-quality-using-Fotors-free-online-AI-photo-enhancer.jpg" alt="" className={style.postFooterAddComentsImg}/>
              <input type="text" placeholder="Write a comment..." className={style.postFooterAddComentsText} ref={commentInputFocus}/>
              <button className={style.postFooterAddComentBtn}>
                <SendCommentPost className={style.postFooterAddComentBtnImg}/>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Post;