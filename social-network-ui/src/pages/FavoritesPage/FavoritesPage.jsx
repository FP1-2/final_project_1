import React, { useEffect } from 'react';
import { useSelector, useDispatch } from "react-redux";
import { favouritesList } from '../../redux-toolkit/favourite/thunks';
import style from "./FavoritesPage.module.scss";
import PostProfile from '../../components/PostProfile/PostProfile';
import RepostProfile from '../../components/RepostProfile/RepostProfile';
import ModalAddRepost from '../../components/ModalAddRepost/ModalAddRepost';
import { setIsFavourite } from '../../redux-toolkit/favourite/slice';

function FavoritesPage() {
  const dispatch = useDispatch();
  const favourites = useSelector(state => state.favourites.favouritesList.obj.content);

  useEffect(() => {
    dispatch(favouritesList());
    dispatch(setIsFavourite(true));
  }, []);

  return (
    <>
      <ModalAddRepost />
      <div className={style.favoritsWrapper}>
        <ul className={style.favorits}>
          {favourites.map(el => <li className={style.favoritsElem} key={el.postId}>
            {el.type === "POST" ?
              <PostProfile el={el} type="favourites"/>
              : <RepostProfile el={el} type="favourites"/>}</li>)}
        </ul>
      </div>
    </>

  );
}

export default FavoritesPage;