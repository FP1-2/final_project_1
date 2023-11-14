import React, {useRef, useState} from 'react';
import style from './PostPage.module.scss';
import Comment from "../../components/Comment/Comment";

const PostPage = () => {
  const commentInputRef = useRef(null);
  const [zoomLevel, setZoomLevel] = useState(1);

  const handleZoomIn =()=> zoomLevel <= 2 && setZoomLevel(zoomLevel + 0.5);
  const handleZoomOut =()=> zoomLevel > 1 && setZoomLevel(zoomLevel - 0.5);

  const comment = {
    id: 1,
    content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.',
    createdDate: '2023-11-12T12:00:00Z',
    appUser: {
      userId: 'user123',
      name: 'Jane Doe',
      avatar: 'https://via.placeholder.com/150/66b7d2'
    }
  };

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
      <div className={style.postContainer}>
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
          <ul className={style.CommentsSection}>
            <li><Comment el={comment}/></li>
            <li><Comment el={comment}/></li>
            <li><Comment el={comment}/></li>
            <li><Comment el={comment}/></li>
            <li><Comment el={comment}/></li>
            <li><Comment el={comment}/></li>
            <li><Comment el={comment}/></li>
            <li><Comment el={comment}/></li>
            <li><Comment el={comment}/></li>
            <li><Comment el={comment}/></li>
          </ul>
          <div className={style.createCommentSection}>
            <div className={style.addComment}>
              <img src="https://via.placeholder.com/150/66b7d2" alt="User avatar" className={style.commentAvatar} />
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
