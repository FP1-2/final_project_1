import {NavLink, useParams} from "react-router-dom";
import style from './GroupPage.module.scss';
import React, {useEffect, useRef, useState} from "react";
import BlueButton from "../../components/BlueButton/BlueButton";

import TripleMenu from "../../components/TripleMenu/TripleMenu";
import Tick from "../../components/TripleMenu/Tick";
import GroupCard from "./GroupCard/GroupCard";
import {useDispatch, useSelector} from "react-redux";
import Search from "../../components/Icons/Search";
import GroupDetails from "./GroupDetails/GroupDetails";
import {getGroup, getPosts} from "../../redux-toolkit/groups/thunks";
import Loader from "../../components/Loader/Loader";
import GroupError from "./GroupError/GroupError";
import {clearGroup} from "../../redux-toolkit/groups/slice";
import GroupPost from "./GroupPost/GroupPost";
import {createHandleScroll} from "../../utils/utils";

export default function GroupPage() {
  const {id} = useParams();
  const dispatch = useDispatch();
  const headerRef = useRef(null);

  const  { obj: {
    content: posts,
    totalPages,
    pageable: {
      pageNumber
    }
  }, 
  status: statusPosts, 
  error: errorPosts 
  } = useSelector(state => state.groups.getPosts);

  const errPosts = errorPosts === 'rejected';
  
  useEffect(() => {
    dispatch(getPosts({page: 0, id}));
  }, []);

  const scrollContainerRef = useRef(null);

  const getMorePosts = () => {
    if (statusPosts !== 'pending' && pageNumber < totalPages) {
      dispatch(getPosts({ page: pageNumber + 1, id }));
    }
  };

  const handleScroll = createHandleScroll({
    scrollRef: scrollContainerRef,
    status: statusPosts,
    fetchMore: getMorePosts,
  });

  useEffect(() => {
    dispatch(clearGroup());
    dispatch(getPosts({page: 0, id}));
    dispatch(getGroup({id}));
  }, [dispatch]);

  const {obj: group, status, error} = useSelector(s => s.groups.getGroup);
  const {obj: user} = useSelector(state => state.auth.user);

  const ownerId = group.admins[0]?.user.userId;
  const adm = user.id === ownerId;

  const [activeTab, setActiveTab] = useState('Posts');
  const [tab, setTab] = useState('');
  const [hideAdm, setHideAdm] = useState(false);
  const [search, setSearch] = useState(false);
  const [hideAdmMenu, setHideAdmMenu] = useState(false);
  const POSTS = 'Posts', MY_POSTS = 'My Posts',
    MORE = 'More', DRAFT = 'Draft', ARCHIVED = 'Archived',
    REJECTED = 'Rejected';

  const join = () => {
    //
  };

  const receivePosts = () => {
    handleTabClick(POSTS);
    //
  };

  const getMyPosts = () => {
    handleTabClick(MY_POSTS);
    //
  };

  const getDraft = () => {
    if (!adm) {
      setTab(DRAFT);
      setActiveTab(MORE);
    } else handleTabClick(DRAFT);
    //
  };

  const getArchived = () => {
    if (!adm) {
      setTab(ARCHIVED);
      setActiveTab(MORE);
    } else handleTabClick(ARCHIVED);
    //
  };

  const getRejected = () => {
    if (!adm) {
      setTab(REJECTED);
      setActiveTab(MORE);
    } else handleTabClick(REJECTED);
    //
  };

  /** Селектор параметрів меню:
     * впливає локальними змінними на параметри стилів 'TripleMenu'. */
  const getActiveTab = tab => {
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
  const toggleAdmMenu = () => setHideAdmMenu(state => !state);

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

  useEffect(() => {
    applyRandomGradient();
  }, [group.imageUrl]);

  const applyRandomGradient = () => {
    const randomColor = () => {
      const r = Math.floor(Math.random() * 256);
      const g = Math.floor(Math.random() * 256);
      const b = Math.floor(Math.random() * 256);
      return `rgba(${r},${g},${b},0.2)`;
    };

    const color1 = randomColor();
    const gradient = `linear-gradient(to bottom, ${color1}, rgba(255,255,255,1))`;

    if (headerRef.current) headerRef.current.style.background = gradient;
  };

  switch (status) {
  case "rejected":
    return <GroupError type={error.type} message={error.message}/>;

  case "pending":
    return <Loader/>;

  default:

    return (
      <div className={style.groupWrapper} onScroll={handleScroll} ref={scrollContainerRef}>
        <aside className={style.sidebarLeft}>
          <GroupCard
            pathImage={group.imageUrl}
            groupName={group.name}
            memberCount={group.memberCount}
            isPublic={group.isPublic}
          />
        </aside>
        <div className={style.main} >
          <div ref={headerRef} className={style.header}>
            <div className={style.imageContainer}>
              <div className={style.image}>
                <img
                  src={group.imageUrl}
                  alt="Group image"
                />
                <div className={style.strip}>
                  <span>Group profile
                    <NavLink to={`/profile/${ownerId}`} className={style.stripLink}>
                      {status === "fulfilled" && `${group.admins[0].user.name} ${group.admins[0].user.surname}`}
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
                    onClick={receivePosts}
                  >Posts
                  </div>
                  <div
                    className={`${style.tab} ${activeTab === MY_POSTS ? style.active : ''}`}
                    onClick={getMyPosts}
                  >My Posts
                  </div>
                  {hideAdm ? <div
                    className={`${style.tab} ${activeTab === MORE ? style.active : ''}`}
                    onClick={toggleAdmMenu}
                  > More<Tick/>
                  </div> : <div>
                    {adm && <div className={style.adm}>
                      <div
                        className={`${style.tab} ${activeTab === DRAFT ? style.active : ''}`}
                        onClick={getDraft}
                      >Draft
                      </div>
                      <div
                        className={`${style.tab} ${activeTab === ARCHIVED ? style.active : ''}`}
                        onClick={getArchived}
                      >Archived
                      </div>
                      <div
                        className={`${style.tab} ${activeTab === REJECTED ? style.active : ''}`}
                        onClick={getRejected}
                      >Rejected
                      </div>
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

            <ul className={style.content}>
              { errPosts ? 
                <li className={style.errorPosts}>Ops! Something went wrong...</li> 
                : posts.map((post) => (
                  <GroupPost key={post.id} adm={adm} post={post} />
                ))}
              <li className={style.errorPosts}>End!</li>
            </ul>

            <div >
              <aside className={style.sidebarRight}>
                <GroupDetails group={group}/>
              </aside>
            </div>
          </div>
        </div>
      </div>
    );
  }
}