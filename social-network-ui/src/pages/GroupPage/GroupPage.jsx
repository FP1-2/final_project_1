import {NavLink, useParams} from "react-router-dom";
import style from './GroupPage.module.scss';
import React, {useEffect, useRef, useState} from "react";
import BlueButton from "../../components/BlueButton/BlueButton";
import Search from "../../components/Icons/Search";
import Vibrant from 'node-vibrant';
import TripleMenu from "../../components/TripleMenu/TripleMenu";
import Tick from "../../components/TripleMenu/Tick";

export default function GroupPage() {
  const { id } = useParams();
  const adm = true;

  /** управління адмін меню */
  const [activeTab, setActiveTab] = useState('Posts');
  const [hideAdm, setHideAdm] = useState(false);
  const [hideAdmMenu, setHideAdmMenu] = useState(false);
  //const [isMoreMenuOpen, setIsMoreMenuOpen] = useState(false);

  const toggleMoreMenu = () => {
    setHideAdmMenu(prevState => !prevState);
  };

  const handleTabClick = (tabName) => {
    if (tabName !== 'More') {
      setActiveTab(tabName);
      setHideAdmMenu(false);
    } else {
      if (!hideAdmMenu) {
        setActiveTab(tabName);
      }
      toggleMoreMenu();
    }
  };


  useEffect(() => {
    const handleClickOutside = event => {
      if (!event.target.closest(`.${style.tab}`)) {
        setHideAdmMenu(false);
      }
    };

    if (hideAdmMenu) {
      document.addEventListener('click', handleClickOutside);
    }

    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, [hideAdmMenu]);

  const getDraft =()=>{};
  const getArchived =()=>{};
  const getRejected =()=>{};

  /** прибираємо адмін меню в мобільній версії */

  useEffect(() => {
    if (adm) {
      const mediaQuery = window.matchMedia('(max-width: 800px)');
      const handleMediaChange = event => {
        setHideAdm(event.matches);
        if (event.matches) {
          setHideAdmMenu(false);
        }
      };
      handleMediaChange(mediaQuery);
      mediaQuery.addEventListener('change', handleMediaChange);
      return () => {
        mediaQuery.removeEventListener('change', handleMediaChange);
      };
    }
  }, [adm]);

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

  const applyDynamicGradient = (img) => {
    Vibrant.from(img.src).getPalette()
      .then((palette) => {
        const rgbColor = palette.LightMuted ? palette.LightMuted.rgb : [255, 255, 255];
        const gradientStart = `rgba(${rgbColor.join(',')}, 0.5)`;
        const gradientEnd = 'rgb(255, 255, 255)';
        headerRef.current.style.background = `linear-gradient(to bottom, ${gradientStart}, ${gradientEnd})`;
      });
  };

  const join =()=>{};
  return (
    <div className={style.groupWrapper}>
      <aside className={style.sidebarLeft}>{id}</aside>
      <div className={style.main}>
        <div ref={headerRef} className={style.header}>
          <div className={style.imageContainer}>
            <div className={style.image}>
              <img
                ref={imgRef}
                src={"https://source.unsplash.com/random?wallpapers"}
                alt="Group image"
              />
              <div className={style.strip}>
                <span>Group profile
                  <NavLink to={`/profile`} className={style.stripLink}>
                    {"Zaporozhye View"}
                  </NavLink>
                </span>
              </div>
            </div>
          </div>
          <div className={style.tabsContainer}>
            <div className={style.upperBlock}>
              <h2>Radio market</h2>
              <BlueButton onClick={join} text={"join the group"}/>
            </div>
            <div className={style.horizontalLine}></div>
            <div className={style.lowerBlock}>
              <div className={style.tabs}>
                <div
                  className={`${style.tab} ${activeTab === 'Posts' ? style.active : ''}`}
                  onClick={() => handleTabClick('Posts')}
                >Posts</div>
                <div
                  className={`${style.tab} ${activeTab === 'My Posts' ? style.active : ''}`}
                  onClick={() => handleTabClick('My Posts')}
                >My Posts</div>
                {hideAdm ? <div
                  className={`${style.tab} ${activeTab === 'More' ? style.active : ''}`}
                  onClick={() => handleTabClick('More')}
                > More<Tick/>
                </div> : <div>
                  {adm && <div className={style.adm}>
                    <div
                      className={`${style.tab} ${activeTab === 'Draft' ? style.active : ''}`}
                      onClick={() => handleTabClick('Draft')}
                    >Draft</div>
                    <div
                      className={`${style.tab} ${activeTab === 'Archived' ? style.active : ''}`}
                      onClick={() => handleTabClick('Archived')}
                    >Archived</div>
                    <div
                      className={`${style.tab} ${activeTab === 'Rejected' ? style.active : ''}`}
                      onClick={() => handleTabClick('Rejected')}
                    >Rejected</div>
                  </div>} </div>
                }
              </div>
              <span className={style.searchIcon}>
                <Search/>
              </span>
            </div>
          </div>
        </div>
        <div className={style.contentContainer}>
          {hideAdmMenu && <TripleMenu
            className={style.tripleMenu}
            one={"Draft"}
            two={"Archived"}
            three={"Rejected"}
            onOne={getDraft}
            onTwo={getArchived}
            onThree={getRejected}
          />}
          <div className={style.content}>
            <aside className={style.sidebarRight}>{id}</aside>
          </div>
        </div>
      </div>
    </div>
  );
}