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

      {props.type === "date" ?
        <label className={style.dateOfBirth}>
          {props.text}:
          <input className={style.input} {...field} {...props} />
          <p className={error && touched ? style.error : style.errorDisplayNone} >{error}</p>
        </label>
        : props.type === "password" ?
          <div className={style.passworslInputWrapper}>

            <input className={style.passwordInput} {...field} {...props} type={inputType} />
            <button onClick={handleClickOpenEyeBtn} type="button" className={style.btnEye}>
              <OpenEye className={openEyeState ? style.btnCloseEye : style.btnOpenEye} />
            </button>

            <button onClick={handleClickCloseEyeBtn} type="button" className={style.btnEye}>
              <CloseEye className={closeEyeState ? style.btnCloseEye : style.btnOpenEye} />
            </button>
            <p className={error && touched ? style.errorPassword : style.errorDisplayNone} >{error}</p>
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
  text: PropTypes.string,
  type: PropTypes.string.isRequired
};

Input.defaultProps = {
  text: "",
};

export default memo(Input);