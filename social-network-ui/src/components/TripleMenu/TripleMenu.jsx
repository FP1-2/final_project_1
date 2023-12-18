import React from 'react';
import style from './TripleMenu.module.scss';
import PropTypes from "prop-types";

export default function TripleMenu({className, onOne, onTwo, onThree, one, two, three, activeTab}) {
  return (
    <div className={`${style.tripleMenu} ${className}`}>
      <div 
        className={`${style.menuItem} ${activeTab === 'tabOne' ? style.active : ''}`}
        onClick={onOne}>{one}</div>
      <div 
        className={`${style.menuItem} ${activeTab === 'tabTwo' ? style.active : ''}`}
        onClick={onTwo}>{two}</div>
      <div 
        className={`${style.menuItem} ${activeTab === 'tabThree' ? style.active : ''}`}
        onClick={onThree}>{three}</div>
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
  activeTab: PropTypes.string,
};