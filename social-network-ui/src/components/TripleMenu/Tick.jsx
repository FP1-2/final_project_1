import PropTypes from "prop-types";

export default function Tick({className, style}){
  return(
    <svg viewBox="0 0 20 20" width="16" height="16" fill="currentColor"
      className={`${className}`} style={style}>
      <path
        d="M10 14a1 1 0 0 1-.755-.349L5.329 9.182a1.367 1.367 0 0 1-.205-1.46A1.184 1.184 0 0 1 6.2 7h7.6a1.18 1.18 0 0 1 1.074.721 1.357 1.357 0 0 1-.2 1.457l-3.918 4.473A1 1 0 0 1 10 14z">
      </path>
    </svg>
  );
}

Tick.propTypes = {
  className: PropTypes.string,
  style: PropTypes.string,
};