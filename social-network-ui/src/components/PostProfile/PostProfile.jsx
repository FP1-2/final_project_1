import React, { useState, useRef, useEffect } from "react";
import ErrorPage from "../ErrorPage/ErrorPage";
import style from "./PostProfile.module.scss";
import { ReactComponent as LikePostBtn } from "../../img/likePostBtn.svg";
import { ReactComponent as LikedPostBtn } from "../../img/likedPostBtn.svg";
import { ReactComponent as BlueLike } from "../../img/blueLike.svg";
import { ReactComponent as CommentPostBtn } from "../../img/commentPostBtn.svg";
import { ReactComponent as BlueComment } from "../../img/blueComment.svg";
import { ReactComponent as SharePostBtn } from "../../img/sharePostBtn.svg";
import { ReactComponent as SendCommentPost } from "../../img/sendCommentPost.svg";
import { ReactComponent as Pencil } from "../../img/pencil.svg";
import { ReactComponent as Delete } from "../../img/delete.svg";
import { ReactComponent as Dots } from "../../img/dots.svg";
import { ReactComponent as SavePost } from "../../img/savePost.svg";
import { NavLink } from "react-router-dom";
import { useSelector } from "react-redux";
import PropTypes from "prop-types";
import Comment from "../Comment/Comment";
import { clearComments, modalEditPostState, setPost, modalAddRepostState, deleteLocalPost, appendCommentStart } from "../../redux-toolkit/post/slice";
import { getCommentsPost, addLike, addComment, deletePost } from "../../redux-toolkit/post/thunks";
import { deleteFavourite, addToFavourites } from "../../redux-toolkit/favourite/thunks";
import { deleteLocalFavourite } from "../../redux-toolkit/favourite/slice";
import { useDispatch } from "react-redux";
import { deletLocalMainPost } from "../../redux-toolkit/main/slice";


const PostProfile = ({ el }) => {
  const dispatch = useDispatch();

  const [clickComment, setClickComment] = useState(false);
  const [btnAlso, setBtnAlso] = useState(false);
  const [isFavouritePost, setIsFavouritePost] = useState(false);
  const [countCommentsPost, setCountCommentsPost] = useState(0);
  const [stateLikePost, setStateLikePost] = useState(false);
  const [countLikePost, setCountLikePost] = useState(0);
  const commenttext = useRef();

  const {
    id: userId,
    avatar: userAvatar,
    name,
    surname,
    username,
  } = useSelector(state => state.auth.user.obj);

  const {
    getCommentsPost: {
      obj: {
        content,
        size,
        totalElements,
      },
      status,
      error
    }
  } = useSelector(state => state.post);

  useEffect(() => {
    const isLikedByUser = Array.isArray(el.likes)
    && el.likes.includes(userId);
    setStateLikePost(isLikedByUser);
    if(el.likes){
      setCountLikePost(el.likes.length);
    }
  }, []);


  useEffect(() => {
    if (el.comments) {
      setCountCommentsPost(el.comments.length);
    }
    if (el.isFavorite) {
      setIsFavouritePost(true);
    }
  }, []);


  const changeClickLike = () => {
    dispatch(addLike(el.postId));
    if(stateLikePost){
      setCountLikePost(val=>val-1);
    }else{
      setCountLikePost(val=>val+1);
    }
    setStateLikePost(val=>!val);
  };

  const sendComment = () => {
    const obj = {
      postId: el.postId,
      content: commenttext.current.value
    };
    const objForPost = {
      ...obj,
      appUser: {
        name,
        surname,
        username,
        avatar: userAvatar,
        userId
      }
    };
    dispatch(addComment(obj));
    setCountCommentsPost(val => val + 1);
    dispatch(appendCommentStart(objForPost));
    commenttext.current.value = "";
  };

  const sharePost = () => {
    dispatch(setPost(el));
    dispatch(modalAddRepostState(true));
  };

  const commentClick = () => {
    setClickComment((val => !val));
    dispatch(clearComments());
    if (el.comments) {
      dispatch(getCommentsPost({ page: 0, size: 3, id: el.postId }));
    }
  };

  const modalEditPostOpen = () => {
    setBtnAlso(false);
    dispatch(setPost(el));
    dispatch(modalEditPostState(true));
  };

  const deletePostThunk = () => {
    dispatch(deletePost(el.postId));
    dispatch(deleteLocalPost(el.postId));
    dispatch(deletLocalMainPost(el.postId));
    dispatch(deleteLocalFavourite(el.postId));
  };

  const savePostThunk = async () => {
    if (isFavouritePost) {
      await dispatch(deleteFavourite(el.postId));
      dispatch(deleteLocalFavourite(el.postId));
      setIsFavouritePost(false);
    } else {
      dispatch(addToFavourites(el.postId));
      setIsFavouritePost(true);
    }
  };

  return (
    <div className={style.post}>
      <header className={style.postHeader}>
        <div className={style.postHeaderLinksWrapper}>
          <NavLink to={`/post/${el.postId}`} className={style.postAvatarLink}>
            <img src={el.author.avatar
              ? el.author.avatar
              : "https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="" className={style.postAvatar} />
          </NavLink>
          <NavLink to={`/profile/${el.author.userId}`} className={style.postNameLink} href="">{`${el.author.name} ${el.author.surname}`}</NavLink>
        </div>
        {el.author.userId === userId ? <button className={style.postHeaderAlsoBtn} onClick={() => setBtnAlso((val) => !val)}>
          <Dots className={style.postHeaderAlsoBtnImg} />
        </button>
          : null}
        {btnAlso ? <div className={style.postHeaderBtnWrapper}>
          <button className={style.postHeaderBtn} onClick={modalEditPostOpen}>
            <Pencil className={style.postHeaderBtnImg} />
            Edit post
          </button>
          <button className={style.postHeaderBtn} onClick={deletePostThunk}>
            <Delete className={style.postHeaderBtnImg} />
            Delete post
          </button>
        </div>
          : null}
      </header>
      <p className={style.postText}>{el.body}</p>
      {el.imageUrl ?
        <NavLink to={`/post/${el.postId}`}>
          <img src={el.imageUrl} alt="Photo of post" className={style.postImg} />
        </NavLink>  
        : null}
      <div className={style.postFooterWrapper}>
        <div className={style.postFooterInfo}>
          <div className={style.postFooterLikes}>
            <BlueLike className={style.postFooterLikesImg} />
            <p className={style.postFooterLikesText}>{countLikePost}</p>
          </div>
          <div className={style.postFooterComments}>
            <BlueComment className={style.postFooterCommentsImg} />
            <p className={style.postFooterCommentsText}>{countCommentsPost}</p>
          </div>
        </div>
        <div className={style.postFooterBtns}>
          <button className={style.postBtn} onClick={changeClickLike}>
            {stateLikePost ?
              <>
                <LikedPostBtn className={style.postBtnImg} />
                Dislike</>
              : <><LikePostBtn className={style.postBtnImg} />
                Like</>}
          </button>
          <button className={style.postBtn} onClick={commentClick}>
            <CommentPostBtn className={style.postBtnImg} />
            Comment
          </button>
          <button className={style.postBtn} onClick={sharePost}>
            <SharePostBtn className={style.postBtnImg} />
            Share
          </button>
          <button className={style.postBtn} onClick={savePostThunk}>
            {isFavouritePost ?
              <><SavePost className={style.postBtnImgSaved} />
                Saved</>
              : <><SavePost className={style.postBtnImg} />
                Save</>}
          </button>
        </div>
        {clickComment ? <div className={style.postFooterComents}>
          {status === "pending" ?
            <div className={style.loderWrapper}>
              <div className={style.loder}></div>
            </div>
            : status === "rejected" ?
              <ErrorPage message={error ? error : "Oops something went wrong!"} />
              :
              (content.length ?
                <>{content.map((elem) =>
                  <Comment el={elem} key={elem.id?elem.id:el.postId} />)}
                {totalElements > size ?
                  <NavLink to={`/post/${el.postId}`} className={style.postOtherComents}>...comments</NavLink>
                  : null}</>
                : null)
          }
          <div className={style.postFooterAddComents}>
            <img src={userAvatar ? userAvatar : "https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="Avatar" className={style.postFooterAddComentsImg} />
            <input type="text" placeholder="Write a comment..." className={style.postFooterAddComentsText} ref={commenttext} />
            <button className={style.postFooterAddComentBtn} onClick={sendComment}>
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
  type: PropTypes.string
};


export default PostProfile;