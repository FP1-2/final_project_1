import styles from './MainPage.module.scss';
import React, { useEffect, useRef } from 'react';
import { useDispatch, useSelector } from "react-redux";
import { createHandleScroll } from "../../utils/utils";
import { loadPostsInMain } from "../../redux-toolkit/main/thunks";
import {addNewPost, resetPostsInMainState} from "../../redux-toolkit/main/slice";
import PostProfile from "../../components/PostProfile/PostProfile";
import RepostProfile from "../../components/RepostProfile/RepostProfile";
import ModalAddRepost from "../../components/ModalAddRepost/ModalAddRepost";
import Loader from '../../components/Loader/Loader';
import {setNewPost} from "../../redux-toolkit/ws/slice";

export default function MainPage() {
  const scrollContainerRef = useRef(null);
  const dispatch = useDispatch();
  const {
    status,
    obj: {
      content,
      totalPages,
      pageable: {
        pageNumber
      }
    }
  } = useSelector(state => state.postsInMain.posts);
  const newPost = useSelector(state => state.webSocket.newPost);
  useEffect(() => {
    dispatch(resetPostsInMainState());
    dispatch(loadPostsInMain({ page: 0 }));
  }, []);

  const getMorePosts = () => {
    if (status !== 'pending' && pageNumber < totalPages) {
      dispatch(loadPostsInMain({ page: pageNumber + 1 }));
    }
  };

  const handleScroll = createHandleScroll({
    scrollRef: scrollContainerRef,
    status: status,
    fetchMore: getMorePosts,
  });
  useEffect(() => {
    if(newPost){
      dispatch(addNewPost(newPost));
      dispatch(setNewPost(null));
    }
  }, [newPost]);
  return (
    <>
      <ModalAddRepost />
      <div className={styles.container} onScroll={handleScroll} ref={scrollContainerRef}>
        {status === 'pending' && content.length === 0 && <Loader/>}
        <ul className={styles.container_cards_bloc}>
          {content.map((post) => (
            <li key={post.postId}>
              {post.type === "REPOST" ?
                <RepostProfile el={post} /> :
                <PostProfile el={post} />}
            </li>
          ))}
        </ul>
        {status === 'pending' && content.length > 0 && <Loader/>}
        {pageNumber === totalPages && status !== 'pending' && <h4 className={styles.container_allCard}>That`s all for now!</h4>}
      </div>
    </>

  );
}