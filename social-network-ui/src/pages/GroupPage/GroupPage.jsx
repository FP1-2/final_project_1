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

  const [hideAdmMenu, setHideAdmMenu] = useState(false);
  const toggleAdmMenu = () => {
    setHideAdmMenu(prevState => !prevState);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (event.target.closest(`.${style.tab}`)) {
        // Якщо клацнути всередині елемента з класом .tab, нічого не робимо
        return;
      }
      setHideAdmMenu(false);
    };

    document.addEventListener('click', handleClickOutside);

    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, []);

  const getDraft =()=>{};
  const getArchived =()=>{};
  const getRejected =()=>{};

  /** прибираємо адмін меню в мобільній версії */

  const [hideAdm, setHideAdm] = useState(false);

  useEffect(() => {
    if(adm) {
      const mediaQuery = window.matchMedia('(max-width: 800px)');
      const handleMediaChange = event => setHideAdm(event.matches);
      handleMediaChange(mediaQuery);
      mediaQuery.addEventListener('change', handleMediaChange);
      return () => {
        mediaQuery.removeEventListener('change', handleMediaChange);
      };
    }
  }, []);

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
                <div className={`${style.tab} ${style.active}`}>Posts</div>
                <div className={style.tab}>My Posts</div>
                {hideAdm ? <div className={style.tab} onClick={toggleAdmMenu}> More
                  <Tick className={style.tick}/>
                </div> : <div>
                  {adm && <div className={style.adm}>
                    <div className={style.tab}>Draft</div>
                    <div className={style.tab}>Archived</div>
                    <div className={style.tab}>Rejected</div>
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