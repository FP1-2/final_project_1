import styles from './MainPage.module.scss';
import React, {useEffect, useRef} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {createHandleScroll} from "../../utils/utils";
import {loadPostsInMain} from "../../redux-toolkit/main/thunks";
import {resetPostsInMainState} from "../../redux-toolkit/main/slice";
import PostProfile from "../../components/PostProfile/PostProfile";
import RepostProfile from "../../components/RepostProfile/RepostProfile";

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
  } = useSelector(state => state.posts_in_main.posts);

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
    fetchMore:  getMorePosts,
  });
  
  return (
    <div className={styles.container} onScroll={handleScroll} ref={scrollContainerRef}>
      <div className={styles.container_cards_bloc}>
        {content.map((post) => (
          <React.Fragment key={post.postId}>
            {post.type === "REPOST" ?
              <RepostProfile  el={post} /> : 
              <PostProfile el={post} />}
          </React.Fragment>
        ))}
      </div>
      {pageNumber === totalPages && <h4 className={styles.container_allCard}>That`s all for now!</h4>}
    </div>
  );
}