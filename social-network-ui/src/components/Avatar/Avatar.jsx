import styles from "./Avatar.module.scss";
import PropTypes from "prop-types";

export default function Avatar({name, additionalClass, photo, style, styleWrap}) {
  
  return (
    <div style={styleWrap} className={`${styles.avatarWrapper} ${additionalClass ? additionalClass : ""}`}>
      <img style={style} src={photo !== null ? photo : "/img/default-avatar.jpg"} alt={name}/>
    </div>
  );
}

Avatar.propTypes = {
  photo: PropTypes.oneOfType([PropTypes.string, PropTypes.oneOf([null])]),
  name: PropTypes.string.isRequired,
  additionalClass: PropTypes.string,
  style: PropTypes.object,
  styleWrap: PropTypes.object,
};