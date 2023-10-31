import styles from "./Avatar.module.scss";
import PropTypes from "prop-types";

export default function Avatar({name, additionalClass, photo}) {
  
  return (
    <div className={`${styles.avatarWrapper} ${additionalClass ? additionalClass : ""}`}>
      <img src={photo !== null ? photo : "http://res.cloudinary.com/ditpsafw3/image/upload/v1698702906/spl2yas0amgpe6tapva3.jpg"} alt={name}/>
    </div>
  );
}

Avatar.propTypes = {
  photo: PropTypes.string,
  name: PropTypes.string.isRequired,
  additionalClass: PropTypes.string,
};