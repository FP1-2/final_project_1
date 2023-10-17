import React from "react";
import style from "./LikedPageProfile.module.scss";
// import PropTypes from "prop-types";
import LikeCardProfile from "../LikeCardProfile/LikeCardProfile";

const LikedPageProfile = () => {
  return (
    <div className={style.profileBodyWrapper}>
      <div className={style.profileBody}>
        <div className={style.profileLikedWrapper}>
          <h2 className={style.profileLikedHeader}>Liked</h2>
          <div className={style.profileLikes}>
            <LikeCardProfile/>
            <LikeCardProfile/>
            <LikeCardProfile/>
            <LikeCardProfile/>
            <LikeCardProfile/>
          </div>
        </div>
      </div>
    </div>
  );
};
export default LikedPageProfile;