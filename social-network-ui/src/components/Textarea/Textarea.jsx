import React, { memo } from "react";
import { useField } from "formik";
import style from "./Textarea.module.scss";
import PropTypes from "prop-types";

const Textarea = (props) => {

  const [field, meta] = useField(props.name);
  const { error, touched } = meta;


  return (
    <div className={style.textareaWrapper}>
      <textarea className={style.textarea} {...field} {...props} />
      <p className={error && touched ? style.errorNames : style.errorDisplayNone} >{error}</p>
    </div>
  );
};

Textarea.propTypes = {
  name: PropTypes.string.isRequired,
  text: PropTypes.string,
};

Textarea.defaultProps = {
  text: null,
};

export default memo(Textarea);