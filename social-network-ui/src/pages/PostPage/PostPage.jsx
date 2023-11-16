import React, {useEffect, useRef, useState} from 'react';
import style from './PostPage.module.scss';
import Comment from "../../components/Comment/Comment";
import {useDispatch, useSelector} from "react-redux";
import {
  appendComment,
  clearStatePost,
  toggleLikePost
} from "../../redux-toolkit/post/slice";
import {
  getCommentsPost,
  getPost,
  addComment,
  addLike, addRepost
} from "../../redux-toolkit/post/thunks";
import {useParams} from "react-router-dom";
import {createHandleScroll} from "../../utils/utils";
import Likes from "../../components/Icons/Likes";
import {Field, Form, Formik} from "formik";
import * as Yup from 'yup';
import {showMessage} from "../../redux-toolkit/popup/slice";

const CommentSchema = Yup.object().shape({
  comment: Yup.string()
    .min(2, 'Too Short!')
    .max(500, 'Too Long!')
    .required('Required'),
});

export default function PostPage(){
  const { id } = useParams();
  const scrollContainerRef = useRef(null);
  const dispatch = useDispatch();

  const {
    id: userId,
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

  /**
   * Додаємо коментарі:
   * По завершенню успіхом роботи асинхронної функції відправки
   * коментаря на сервер "addComment" починає працювати логіка
   * оновлення локального стану. Збирається об'єкт едентичний тому,
   * що повертається з сервера, з'єднується поточний аунтефікований
   * користувач з об'єктом відповіді з сервера, і передається
   * редьюсеру "appendComment". Ред'юсер додає об'єкт у масив стану,
   * що тримає всі коментарі.
   * */
  const {
    obj: commentCreate,
    status: statusCommentCreate
  } = useSelector(state => state.post.addComment);

  const handleCommit = (values, { resetForm }) => {
    dispatch(addComment({
      postId: id,
      content:  values.comment,
    }));
    resetForm();
  };

  useEffect(() => {
    if (statusCommentCreate === "fulfilled") {
      const newComment = {
        ...commentCreate,
        appUser: {
          userId: userId,
          name: name,
          surname: surName,
          username: userName,
          avatar: userAvatar,
        }
      };
      dispatch(appendComment(newComment));
    }
  }, [statusCommentCreate]);

  /**
   * Логіка перемикача лайків: 
   * "addLike" відправляє асинхронний запит а результат
   * відповіді керує ред'юсером "toggleLikePost" - додає або видаляє
   * ід користувача. Якщо у посту не було
   * лайків( поле "likes" було відсутнім ), ред'юсер створює
   * його в об'єкті state.post.getPost ініціалізуючи порожнім масивом.
   */
  const handleToggleLike = () => {
    dispatch(addLike(id));
  };

  const {
    status: isLikeStatus,
    obj: isLiked
  } = useSelector(state => state.post.addLike);

  const isLikedByUser  = Array.isArray(post.likes)
      && post.likes.includes(userId);

  useEffect(() => {
    if (isLikeStatus === "fulfilled"
        && !(isLikedByUser === isLiked.added)) {
      dispatch(toggleLikePost(userId));
    }
  }, [isLikeStatus]);

  /******************************** Repost logic is not implemented **************************************/
  const handleShare = () => {
    dispatch(addRepost(id));
  };

  const {
    status: repostedStatus,
    //obj: reposted
  } = useSelector(state => state.post.addRepost);

  const prevStatusRef = useRef(repostedStatus);

  useEffect(() => {
    if (prevStatusRef.current !== status){
      if (repostedStatus === 'fulfilled') dispatch(showMessage('Reposted fulfilled'));
      if(repostedStatus === 'rejected') dispatch(showMessage('Repost logic is not implemented'));
      prevStatusRef.current = status;
    }
  }, [repostedStatus]);
  /******************************** Repost logic is not implemented **************************************/

  /**
   * При першому рендерингу робимо повністю очищення стейту state.post,
   * ініціалізуємо дефолтним об'єктом стану, завантажуємо пост
   * із сервера та перші 10 об'єктів коментарів
   */
  useEffect(() => {
    if (repostedStatus === "fulfilled") {
      dispatch(toggleLikePost(userId));
    }
  }, [isLikeStatus]);

  useEffect(() => {
    dispatch(clearStatePost());
    dispatch(getPost({ id }));
    dispatch(getCommentsPost({ page: 0, id }));
  }, []);

  /**
   * Логіка обробки додавання коментарів порціями по 10 об'єктів.
   * Коментарі додаються до існуючих збільшуючи масив при кожному
   * досягненні низу прокручування.
   */
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

  /**
   * Логіка перемикання видимості секції коментарів.
   */
  const [showComments, setShowComments] = useState(true);

  const toggleComments = () => setShowComments(!showComments);

  /**
   * Логіка зуму фотографії.
   */
  const [zoomLevel, setZoomLevel] = useState(1);

  const handleZoomIn =()=> zoomLevel <= 2
      && setZoomLevel(zoomLevel + 0.5);

  const handleZoomOut =()=> zoomLevel > 1
      && setZoomLevel(zoomLevel - 0.5);

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
                  {`${post.originalPost.author.name} 
                  ${post.originalPost.author.surname}`}
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
              <span>
                {post?.likes ? post.likes.length : 0}
              </span>
            </div>
            <span className={style.commentsCount}>
              <span className={style.sprite}></span>
              {postComments ? postComments.length : 0}
            </span>
          </div>
          <div className={style.postActions}>
            <button>
              <span onClick={handleToggleLike} 
                className={
                  `${style.icon} ${isLikedByUser ? 
                    style.likeIconActive : style.likeIcon}`
                }></span>
              <span className="text">Like</span>
            </button>
            <button>
              <span onClick={toggleComments} 
                className={`${style.icon} ${style.commentIcon}`}>
              </span>
              <span className="text">Comment</span>
            </button>
            <button>
              <span onClick={handleShare} className={`${style.icon} ${style.shareIcon}`}>
              </span>
              <span className="text">Share</span>
            </button>
          </div>
          <div>
            {showComments && (
              <ul className={style.CommentsSection}>
                {postComments.map((comment, index) =>(
                  <li key={`${comment.id}-${index}`}><Comment el={comment}/></li>
                ))}
              </ul>
            )};
          </div>
          <div className={style.createCommentSection}>
            <Formik
              initialValues={{ comment: '' }}
              validationSchema={CommentSchema}
              onSubmit={handleCommit}
            >
              {({ errors, touched }) => (
                <Form>
                  <div className={style.addComment}>
                    <img src={userAvatar} alt={`${userName} ${surName}`} 
                      className={style.commentAvatar} />
                    <Field
                      name="comment"
                      as="textarea"
                      placeholder="Write a comment..."
                      className={style.commentInput}
                    />
                    {errors.comment && touched.comment ? (
                      <div className={style.error}>{errors.comment}</div>
                    ) : null}
                    <button type="submit" className={style.sendIcon}></button>
                  </div>
                </Form>
              )}
            </Formik>
          </div>
        </div>
      </div>
    </div>
  );
}
