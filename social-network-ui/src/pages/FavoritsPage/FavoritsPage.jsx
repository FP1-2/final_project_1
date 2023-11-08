import React, { useEffect } from 'react';
import { useSelector, useDispatch } from "react-redux";
import { favouritesList } from '../../redux-toolkit/favourite/thunks';
import style from "./FavoritsPage.module.scss";
import PostProfile from '../../components/PostProfile/PostProfile';
import RepostProfile from '../../components/RepostProfile/RepostProfile';
import ModalAddRepost from '../../components/ModalAddRepost/ModalAddRepost';

function FavoritsPage() {
  const dispatch = useDispatch();
  const favourites = useSelector(state => state.favourites.favouritesList.obj);

  useEffect(() => {
    dispatch(favouritesList());
  }, []);

  return (
    <>
      <ModalAddRepost />
      <div className={style.favoritsWrapper}>
        <ul className={style.favorits}>
          {favourites.map(el => <li className={style.favoritsElem} key={el.postId}>
            {el.type === "POST" ?
              <PostProfile el={el} />
              : <RepostProfile el={el} />}</li>)}
        </ul>
      </div>
    </>

  );
}

export default FavoritsPage;