import React, {useEffect, useRef, useState} from 'react';
import style from './PostPage.module.scss';
import Comment from "../../components/Comment/Comment";
import {useDispatch, useSelector} from "react-redux";
import {clearComments} from "../../redux-toolkit/post/slice";
import {getCommentsPost, getPost} from "../../redux-toolkit/post/thunks";
import {useParams} from "react-router-dom";
import {createHandleScroll} from "../../utils/utils";
import Likes from "../../components/Icons/Likes";

export default function PostPage(){
  const { id } = useParams();
  const scrollContainerRef = useRef(null);
  const dispatch = useDispatch();
  const {
    avatar: userAvatar,
    username:  userName,
    surname:   surName,
  } = useSelector(state => state.auth.user.obj);
  const {
    status,
    obj: {
      content: postComments,
      totalPages,
      pageable: {
        pageNumber
      }
    }
  } = useSelector(state => state.post.getCommentsPost);
  const {obj: post} = useSelector(state => state.post.getPost);
  
  useEffect(() => {
    dispatch(clearComments());
    dispatch(getPost({ id }));
    dispatch(getCommentsPost({ page: 0, id }));
  }, []);

  const getMoreComments = () => {
    if (status !== 'pending' && pageNumber < totalPages) {
      dispatch(getCommentsPost({ page: pageNumber + 1, id }));
    }
  };
  
  const handleScroll = createHandleScroll({
    scrollRef: scrollContainerRef,
    status: status,
    fetchMore:  getMoreComments,
  });

  const commentInputRef = useRef(null);
  const [zoomLevel, setZoomLevel] = useState(1);

  const handleZoomIn =()=> zoomLevel <= 2 && setZoomLevel(zoomLevel + 0.5);
  const handleZoomOut =()=> zoomLevel > 1 && setZoomLevel(zoomLevel - 0.5);

  return (
    <div className={style.postWrapper}>
      <div className={style.postImageContainer}>
        <div className={style.zoomControls}>
          <button onClick={handleZoomOut} 
            className={style.zoomIn}></button>
          <button onClick={handleZoomIn} 
            className={style.zoomOut}></button>
        </div>
        <img
          src={post.imageUrl}
          alt="Post content"
          className={style.postImage}
          style={{ transform: `scale(${zoomLevel})` }}
        />
      </div>
      <div onScroll={handleScroll} 
        ref={scrollContainerRef} 
        className={style.postContainer}>
        <div className={style.post}>
          <div className={style.postHeader}>
            <img
              src={post.author.avatar}
              alt="User avatar"
              className={style.avatar}
            />
            <span className={style.userName}>
              {`${post.author.name} ${post.author.surname}`}
            </span>
          </div>
          <div className={style.postBody}>
            {post.body}
          </div>
          {post.type ==="REPOST" && (
            <div className={style.originalPost}>
              <div className={style.originalPostHeader}>
                <img
                  src={post.originalPost.author.avatar}
                  alt="Original author's avatar"
                  className={style.originalAvatar}
                />
                <span className={style.originalUserName}>
                  {`${post.originalPost.author.name} ${post.originalPost.author.surname}`}
                </span>
              </div>
              <div className={style.originalPostBody}>
                {post.originalPost.body}
              </div>
            </div>
          )}
          <div className={style.stats}>
            <div className={style.likesContainer}>
              <Likes/>
              <span>{post?.likes ? post.likes.length : 0}</span>
            </div>
            <span className={style.commentsCount}>
              <span className={style.sprite}></span>
              {post?.comments ? post.comments.length : 0}
            </span>
          </div>
          <div className={style.postActions}>
            <button>
              <span className={`${style.icon} ${style.likeIcon}`}></span>
              <span className="text">Like</span>
            </button>
            <button>
              <span className={`${style.icon} ${style.commentIcon}`}></span>
              <span className="text">Comment</span>
            </button>
            <button>
              <span className={`${style.icon} ${style.shareIcon}`}></span>
              <span className="text">Share</span>
            </button>
          </div>
          <div >
            <ul className={style.CommentsSection}>
              {postComments.map((comment) =>(
                <li key={comment.id}><Comment el={comment}/></li>
              ))}
            </ul>
          </div>
          <div className={style.createCommentSection}>
            <div className={style.addComment}>
              <img src={userAvatar} 
                alt={`${userName} ${surName}`} 
                className={style.commentAvatar} />
              <textarea
                ref={commentInputRef}
                placeholder="Write a comment..."
              />
              <button className={style.sendIcon}></button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
