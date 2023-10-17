import React, { useState } from "react";
import style from "./PreviewImage.module.scss";
import PropTypes from "prop-types";

const PreviewImage = ({ file }) => {
  const [preview, setPreview] = useState({});
  if (file) {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      setPreview(reader.result);
    };
}
  return (
    <div className={style.imgWrapper}>
      <img className={style.img} src={preview} alt="" />
    </div>
  );
};


PreviewImage.propTypes = {
  file: PropTypes.object
};
PreviewImage.defaultProps = {
  file:{}
};
export default PreviewImage;