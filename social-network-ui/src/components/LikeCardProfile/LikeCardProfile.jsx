import React from "react";
import style from "./LikeCardProfile.module.scss";
import { Link} from "react-router-dom";
// import PropTypes from "prop-types";
// import { ReactComponent as Delete} from "../../img/delete.svg";

const LikeCardProfile = () => {
  return (
    <Link to="/post" className={style.likedCard}>
      <div className={style.likedCardHeaderWrapper}>
        <div className={style.likedCardAvatarLink}>
          <img src="https://risovach.ru/upload/2013/01/mem/kakoy-pacan_9771748_orig_.jpeg" alt="" className={style.likedCardAvatar} />
        </div>
        <h3 className={style.likedCardNameLink} >Ірина Сергіївна Надточий</h3>
      </div>
      <p className={style.likedCardText}>Щось там</p>
      <img src="https://imgv3.fotor.com/images/slider-image/Female-portrait-photo-enhanced-with-clarity-and-higher-quality-using-Fotors-free-online-AI-photo-enhancer.jpg" alt="" className={style.likedCardImg} />
    </Link>
  );
};
// FriendProfile.propTypes = {
// //   file: PropTypes.object
// };
// FriendProfile.defaultProps = {
// //   file: {}
// };
export default LikeCardProfile;