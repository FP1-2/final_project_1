import React, {useEffect, useRef, useState} from 'react';
import style from './PostPage.module.scss';
import Comment from "../../components/Comment/Comment";
import {useDispatch, useSelector} from "react-redux";
import {clearComments} from "../../redux-toolkit/post/slice";
import {getCommentsPost} from "../../redux-toolkit/post/thunks";
import {useParams} from "react-router-dom";
import {createHandleScroll} from "../../utils/utils";

const PostPage = () => {
  const { id } = useParams();

  const {
    avatar: userAvatar,
    username:  userName,
    surname:   surName,
  } = useSelector(state => state.auth.user.obj);

  const scrollContainerRef = useRef(null);
  const dispatch = useDispatch();

  const {
    status,
    obj: {
      content: comments,
      totalPages,
      pageable: {
        pageNumber
      }
    }
  } = useSelector(state => state.post.getCommentsPost);

  useEffect(() => {
    dispatch(clearComments());
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
          <button onClick={handleZoomOut} className={style.zoomIn}></button>
          <button onClick={handleZoomIn} className={style.zoomOut}></button>
        </div>
        <img
          src="https://source.unsplash.com/random?wallpapers"
          alt="Post content"
          className={style.postImage}
          style={{ transform: `scale(${zoomLevel})` }}
        />
      </div>
      <div onScroll={handleScroll} ref={scrollContainerRef} className={style.postContainer}>
        <div className={style.post}>
          <div className={style.postHeader}>
            <img
              src="https://via.placeholder.com/150/66b7d2"
              alt="User avatar"
              className={style.avatar}
            />
            <span className={style.userName}>User Name</span>
          </div>
          <div className={style.postBody}>
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
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
              {comments.map((comment) =>(
                <li key={comment.id}><Comment el={comment}/></li>
              ))}
            </ul>
          </div>
          <div className={style.createCommentSection}>
            <div className={style.addComment}>
              <img src={userAvatar} alt={`${userName} ${surName}`} className={style.commentAvatar} />
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
};

export default PostPage;
