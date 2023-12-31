import React, { useEffect, useRef } from 'react';
import { useSelector, useDispatch } from "react-redux";
import { favouritesList } from '../../redux-toolkit/favourite/thunks';
import style from "./FavoritesPage.module.scss";
import PostProfile from '../../components/PostProfile/PostProfile';
import RepostProfile from '../../components/RepostProfile/RepostProfile';
import ModalAddRepost from '../../components/ModalAddRepost/ModalAddRepost';
import { resetFavouritesState } from '../../redux-toolkit/favourite/slice';
import { createHandleScroll } from "../../utils/utils";
import ErrorPage from "../../components/ErrorPage/ErrorPage";
import Loader from "../../components/Loader/Loader";

function FavoritesPage() {
  const dispatch = useDispatch();
  const scrollContainerRef = useRef(null);

  useEffect(() => {
    dispatch(resetFavouritesState());
    dispatch(favouritesList({ page: 0 }));
  }, []);

  const {
    status,
    error,
    obj: {
      content,
      totalPages,
      pageable: {
        pageNumber
      }
    }
  } = useSelector(state => state.favourites.favouritesList);


  const getMoreFavourites = () => {
    if (status !== 'pending' && pageNumber + 1 < totalPages) {
      dispatch(favouritesList({ page: pageNumber + 1 }));
    }
  };

  const handleScroll = createHandleScroll({
    scrollRef: scrollContainerRef,
    status: status,
    fetchMore: getMoreFavourites,
  });

  return (
    <>
      {status === "rejected" ?
        <ErrorPage message={error ? error : "Oops something went wrong!"} />
        : status === 'pending' && content.length === 0 ? 
          <Loader/>
          :
          <>
            <ModalAddRepost />
            <div className={style.favoritesWrapper} onScroll={handleScroll} ref={scrollContainerRef}>
              <ul className={style.favorites} >
                {content ? content.map(el => <li className={style.favoritesElem} key={el.postId}>
                  {el.type === "POST" ?
                    <PostProfile el={el} />
                    : <RepostProfile el={el} />}
                </li>) : null}
              </ul>
            </div>
          </>
      }
    </>
  );
}

export default FavoritesPage;