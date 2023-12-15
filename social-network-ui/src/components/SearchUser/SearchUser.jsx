import React, { useState } from "react";
import styles from './SearchUser.module.scss';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from "react-redux";
import { searchUser } from "../../redux-toolkit/messenger/asyncThunk";
import { resetSearchUsers } from '../../redux-toolkit/messenger/slice';
import BackIcon from '../Icons/BackIcon';
import Search from '../Icons/Search';
import Close from '../Icons/Close';
import ChatItem from '../ChatNavigation/ChatItem';
import InputSearch from "./InputSearch";
import { NavLink } from "react-router-dom";
import Loader from "../Loader/Loader";
export default function SearchUser({ handleBack, textSearch, setTextSearch}) {
  const dispatch = useDispatch();
  const { searchUsers } = useSelector(state => state.messenger);
  const [recentSearch, setRecentSearch] = useState([]);
  const handleGetSearchResult = (searchValue) => {
    dispatch(searchUser({ input: searchValue, page: 0, size: 20 }));
  };
  const handleResetSearchResult = () =>{
    dispatch(resetSearchUsers());
  };
  function closePortal(){
    handleBack();
    setTextSearch('');
  }
  function addRecentSearch (searchUser){
    setRecentSearch([searchUser, ...recentSearch.slice(0,9)]);
  }
  return (
    <div className={styles.searchUser} id="search-user-portal" >
      <div className={styles.searchUser__search}>
        <div className={styles.searchUser__search__back} onClick={closePortal}>
          <BackIcon size={"1em"} />
        </div>
        <div className={styles.searchUser__search__input}>
          <span>
            <Search />
          </span>
          <InputSearch 
            textSearch={textSearch} 
            placeholder={"Search"} 
            setTextSearch={setTextSearch} 
            handleGetSearchResult={handleGetSearchResult} 
            handleResetSearchResult={handleResetSearchResult}
            element={'search-user-portal'}
            closePortal={closePortal}
          />
          <span onClick={() => setTextSearch('')}>
            <Close style={{ cursor: "pointer" }} />
          </span>
        </div>
      </div>

      <ul className={styles.searchUser__filteredUsers}>
        {searchUsers.status === '' ? 
          (recentSearch.length === 0 ? 
            <p className={styles.searchUser__filteredUsers__text}>enter name/username for searching user</p>
            : 
            (<React.Fragment>
              <p className={styles.searchUser__filteredUsers__recetly}>Recently:</p>
              {recentSearch.map(({ id, avatar, name, surname }) => (
                <li key={id} onClick={()=> closePortal} 
                  className={styles.searchUser__filteredUsers__item}>
                  <NavLink to={`/profile/${id}`} className={styles.searchUser__filteredUsers__item__link} >
                    <ChatItem photo={avatar} name={name + ' ' + surname} additionalClass={styles.searchUser__filteredUsers__item__link__user} />
                  </NavLink>
                </li>
              ))}
            </React.Fragment>)
          )
          : (searchUsers.status === 'pending' ? <Loader/> :
            (searchUsers.obj.length === 0 ? 
              <p  className={styles.searchUser__filteredUsers__text}>no results</p>
              :
              searchUsers.obj.map(({ id, avatar, name, surname }) => (
                <li key={id} onClick={() => {
                  addRecentSearch({ id, avatar, name, surname });
                  closePortal;}} 
                className={styles.searchUser__filteredUsers__item}>
                  <NavLink to={`/profile/${id}`} className={styles.searchUser__filteredUsers__item__link} >
                    <ChatItem photo={avatar} name={name + ' ' + surname} additionalClass={styles.searchUser__filteredUsers__item__link__user} />
                  </NavLink>
                </li>
              )))
          )}
      </ul>
    </div>
  );
}
SearchUser.propTypes = {
  handleBack: PropTypes.func.isRequired,
  textSearch: PropTypes.string.isRequired,
  setTextSearch: PropTypes.func.isRequired
};
