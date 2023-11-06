import React, { useState, memo } from "react";
import { useField } from "formik";
import style from "./Input.module.scss";
import PropTypes from "prop-types";
import { ReactComponent as OpenEye } from "../../img/open_eye.svg";
import { ReactComponent as CloseEye } from "../../img/close_eye.svg";

const Input = (props) => {

  const [inputType, setInputType] = useState("password");
  const [closeEyeState, setCloseEyeState] = useState(true);
  const [openEyeState, setOpenEyeState] = useState(false);


  const handleClickCloseEyeBtn = () => {
    setInputType("text");
    setCloseEyeState(false);
    setOpenEyeState(true);
  };


  const handleClickOpenEyeBtn = () => {
    setInputType("password");
    setCloseEyeState(true);
    setOpenEyeState(false);
  };

  const [field, meta] = useField(props.name);
  const { error, touched } = meta;


  return (
    <div className={style.inputWrapper}>

      {props.type === "text" && props.modal === "editProfile" ?
        <div className={style.textlInputWrapper}>
          <input className={style.modalMainInputEditProfile} {...field} {...props}/>
          <p className={error ? style.errorNames : style.errorDisplayNone} >{error}</p>
        </div>
        : props.type === "text" && props.modal === "" ?
          <div className={style.textlInputWrapper}>
            <input className={style.textInput} {...field} {...props} />
            <p className={error && touched ? style.errorNames : style.errorDisplayNone} >{error}</p>
          </div>
          : props.type === "password" ?
            <div className={style.passworslInputWrapper}>
              <input className={style.input} {...field} {...props} type={inputType} />
              <button onClick={handleClickOpenEyeBtn} type="button" className={style.btnEye}>
                <OpenEye className={openEyeState ? style.btnCloseEye : style.btnOpenEye} />
              </button>

              <button onClick={handleClickCloseEyeBtn} type="button" className={style.btnEye}>
                <CloseEye className={closeEyeState ? style.btnCloseEye : style.btnOpenEye} />
              </button>
              <p className={error && touched ? style.error : style.errorDisplayNone} >{error}</p>
            </div>
            : <>
              <input className={style.input} {...field} {...props} />
              <p className={error && touched ? style.error : style.errorDisplayNone} >{error}</p>
            </>}
    </div>
  );
};

Input.propTypes = {
  name: PropTypes.string.isRequired,
  type: PropTypes.string.isRequired,
  modal: PropTypes.string,
};

Input.defaultProps = {
  modal: ""
};

export default memo(Input);