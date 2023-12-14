import {NavLink, useParams} from "react-router-dom";
import style from './GroupPage.module.scss';
import React, {useEffect, useRef, useState} from "react";
import BlueButton from "../../components/BlueButton/BlueButton";
import Vibrant from 'node-vibrant';
import TripleMenu from "../../components/TripleMenu/TripleMenu";
import Tick from "../../components/TripleMenu/Tick";
import GroupCard from "../../components/GroupCard/GroupCard";
import {groupTest as group} from "./obj_test_group";
import {useSelector} from "react-redux";
import Search from "../../components/Icons/Search";

export default function GroupPage() {
  const { id } = useParams();
  const {
    profileUser: {
      obj,
      // status,
      // error
    }
  } = useSelector(state => state.profile);

  const ownerId = group.admins[0].user.userId;
  const adm = obj.id === ownerId;

  const [activeTab, setActiveTab] = useState('Posts');
  const [tab, setTab] = useState('');
  const [hideAdm, setHideAdm] = useState(false);
  const [search, setSearch] = useState(false);
  const [hideAdmMenu, setHideAdmMenu] = useState(false);
  const POSTS = 'Posts', MY_POSTS = 'My Posts', 
    MORE = 'More', DRAFT = 'Draft', ARCHIVED = 'Archived',
    REJECTED = 'Rejected';

  const join =()=>{
    //
  };

  const getPosts =()=>{
    handleTabClick(POSTS);
    //
  };
  
  const getMyPosts =()=>{
    handleTabClick(MY_POSTS);
    //
  };

  const getDraft = () => {
    if (!adm){
      setTab(DRAFT);
      setActiveTab(MORE);
    } else handleTabClick(DRAFT);
    //
  };

  const getArchived = () => {
    if (!adm){
      setTab(ARCHIVED);
      setActiveTab(MORE);
    } else handleTabClick(ARCHIVED);
    //
  };

  const getRejected = () => {
    if (!adm){
      setTab(REJECTED);
      setActiveTab(MORE);
    } else handleTabClick(REJECTED);
    //
  };

  /** Селектор параметрів меню:
   * впливає локальними змінними на параметри стилів 'TripleMenu'. */
  const getActiveTab =tab=> {
    switch (tab) {
    case DRAFT:
      return 'tabOne';
    case ARCHIVED:
      return 'tabTwo';
    case REJECTED:
      return 'tabThree';
    default:
      return '';
    }
  };

  /** Перемикач стану 'TripleMenu'. */
  const toggleAdmMenu =()=> setHideAdmMenu(state => !state);

  /** Функція табів */
  const handleTabClick = (tabName) => {
    setTab('');
    setHideAdmMenu(false);
    if (tabName !== MORE) {
      setActiveTab(tabName);
    }
  };

  /** Прибирає 'TripleMenu' при кліку в довільну область */
  useEffect(() => {
    const handleClickOutside = event => {
      !event.target.closest(`.${style.tab}`) && setHideAdmMenu(false);
    };
    if (hideAdmMenu) document.addEventListener('click', handleClickOutside);
    return () => document.removeEventListener('click', handleClickOutside);
  }, [hideAdmMenu]);

  /** Управління стилями табов за шириною в'юпорту. */
  useEffect(() => {
    const mediaQuery = window.matchMedia('(max-width: 800px)');
    const handleMediaChange = event => {
      const matches = event.matches;
      setSearch(matches);
      if (adm) {
        setHideAdm(matches);
        // Desktop.
        if (!matches) {
          if (activeTab === MORE) setActiveTab(tab);
          setHideAdmMenu(false);
          // Mobile.
        } else if ([DRAFT, ARCHIVED, REJECTED].includes(activeTab)) {
          setTab(activeTab);
          setActiveTab(MORE);
        }
      }
    };
    handleMediaChange(mediaQuery);
    mediaQuery.addEventListener('change', handleMediaChange);
    return () => mediaQuery.removeEventListener('change', handleMediaChange);
  }, [adm, activeTab, tab]);

  /** динамічний градієнт хедера */

  const imgRef = useRef(null);
  const headerRef = useRef(null);

  useEffect(() => {
    const img = imgRef.current;
    if (img && img.complete) {
      applyDynamicGradient(img);
    } else if (img) {
      img.onload = () => applyDynamicGradient(img);
    }
  }, []);

  const applyDynamicGradient = img => {
    Vibrant.from(img.src).getPalette()
      .then((palette) => {
        const rgbColor = palette.LightMuted ? palette.LightMuted.rgb : [255, 255, 255];
        const gradientStart = `rgba(${rgbColor.join(',')}, 0.5)`;
        const gradientEnd = 'rgb(255, 255, 255)';
        headerRef.current.style.background = `linear-gradient(to bottom, ${gradientStart}, ${gradientEnd})`;
      });
  };

  return (
    <div className={style.groupWrapper}>
      <aside className={style.sidebarLeft}>
        <GroupCard
          pathImage={group.imageUrl}
          groupName={group.name}
          memberCount={group.memberCount}
          isPublic={group.isPublic}
        />
      </aside>
      <div className={style.main}>
        <div ref={headerRef} className={style.header}>
          <div className={style.imageContainer}>
            <div className={style.image}>
              <img
                ref={imgRef}
                src={group.imageUrl}
                alt="Group image"
              />
              <div className={style.strip}>
                <span>Group profile
                  <NavLink to={`/profile/${ownerId}`} className={style.stripLink}>
                    {`${group.admins[0].user.name} ${group.admins[0].user.surname}`}
                  </NavLink>
                </span>
              </div>
            </div>
          </div>
          <div className={style.tabsContainer}>
            <div className={style.upperBlock}>
              <h2>{group.name}</h2>
              <div className={style.wrpBlueButton}>
                <BlueButton
                  onClick={join}
                  text={"join the group"}
                  className={style.customBlueButton}
                />
                {search && <span className={style.search}><Search/></span>}
              </div>
            </div>
            <div className={style.horizontalLine}></div>
            <div className={style.lowerBlock}>
              <div className={style.tabs}>
                <div
                  className={`${style.tab} ${activeTab === POSTS ? style.active : ''}`}
                  onClick={getPosts}
                >Posts</div>
                <div
                  className={`${style.tab} ${activeTab === MY_POSTS ? style.active : ''}`}
                  onClick={getMyPosts}
                >My Posts</div>
                {hideAdm ? <div
                  className={`${style.tab} ${activeTab === MORE ? style.active : ''}`}
                  onClick={toggleAdmMenu}
                > More<Tick/>
                </div> : <div>
                  {adm && <div className={style.adm}>
                    <div
                      className={`${style.tab} ${activeTab === DRAFT ? style.active : ''}`}
                      onClick={getDraft}
                    >Draft</div>
                    <div
                      className={`${style.tab} ${activeTab === ARCHIVED ? style.active : ''}`}
                      onClick={getArchived}
                    >Archived</div>
                    <div
                      className={`${style.tab} ${activeTab === REJECTED ? style.active : ''}`}
                      onClick={getRejected}
                    >Rejected</div>
                  </div>} </div>
                }
              </div>
              {!search && <span className={style.search}><Search/></span>}
            </div>
          </div>
        </div>
        <div className={style.contentContainer}>
          {hideAdmMenu && <TripleMenu
            className={style.tripleMenu}
            one={DRAFT}
            two={ARCHIVED}
            three={REJECTED}
            onOne={getDraft}
            onTwo={getArchived}
            onThree={getRejected}
            activeTab={getActiveTab(tab)}
          />}
          <div className={style.content}>
            <aside className={style.sidebarRight}>{id}</aside>
          </div>
        </div>
      </div>
    </div>
  );
}