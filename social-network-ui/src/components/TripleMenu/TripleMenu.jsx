import React from 'react';
import style from './TripleMenu.module.scss';
import PropTypes from "prop-types";

export default function TripleMenu({className, onOne, onTwo, onThree, one, two, three}) {
  return (
    <div className={`${style.tripleMenu} ${className}`}>
      <div className={style.menuItem} onClick={onOne}>{one}</div>
      <div className={style.menuItem} onClick={onTwo}>{two}</div>
      <div className={style.menuItem} onClick={onThree}>{three}</div>
    </div>
  );
}

TripleMenu.propTypes = {
  className: PropTypes.string,
  onOne: PropTypes.func,
  onTwo: PropTypes.func,
  onThree: PropTypes.func,
  one: PropTypes.string,
  two: PropTypes.string,
  three: PropTypes.string,
};

