import {NavLink, useParams} from "react-router-dom";
import style from './GroupPage.module.scss';
import React from "react";
import BlueButton from "../../components/BlueButton/BlueButton";
export default function GroupPage() {
  const { id } = useParams();
  const join =()=>{};
  return (
    <div className={style.groupWrapper}>
      <aside className={style.sidebarLeft}>{id}</aside>
      <div className={style.main}>
        <div className={style.header}>
          <div className={style.imageContainer}>
            <div className={style.image}>
              <img
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
                <div className={style.tab}>My</div>
                <div className={style.tab}>Draft</div>
                <div className={style.tab}>Archived</div>
                <div className={style.tab}>Rejected</div>
              </div>
              <div className={style.searchIcon}>🔍</div>
            </div>
          </div>
        </div>
        <div className={style.contentContainer}>
          <div className={style.content}>
            <aside className={style.sidebarRight}></aside>
          </div>
        </div>
      </div>
    </div>
  );
}