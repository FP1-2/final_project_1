import styles from "./Avatar.module.scss";
import PropTypes from "prop-types";

export default function Avatar({name, additionalClass, photo}) {
    return (
        <div className={`${styles.avatarWrapper} ${additionalClass ? additionalClass : ""}`}>
            <img src={photo !== null ? photo : "/img/default-avatar.jpg"} alt={name}/>
        </div>

    )
}

Avatar.propTypes = {
    photo: PropTypes.string,
    name: PropTypes.string.isRequired,
    additionalClass: PropTypes.string,
};