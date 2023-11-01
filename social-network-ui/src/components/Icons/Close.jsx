import PropTypes from 'prop-types';

const Close = ({clickHandler}) => {

  return (
    <svg fill="currentColor" viewBox="0 0 12 13" width="1em" height="1em" onClick={clickHandler}>
      <g fillRule="evenodd" transform="translate(-450 -1073)">
        <g fillRule="nonzero">
          <path d="m98.095 917.155 7.75 7.75a.75.75 0 0 0 1.06-1.06l-7.75-7.75a.75.75 0 0 0-1.06 1.06z"
            transform="translate(353.5 159)">
          </path>
          <path d="m105.845 916.095-7.75 7.75a.75.75 0 1 0 1.06 1.06l7.75-7.75a.75.75 0 0 0-1.06-1.06z"
            transform="translate(353.5 159)">
          </path>
        </g>
      </g>
    </svg>
  );
};
export default Close;
Close.propTypes = {
  clickHandler: PropTypes.func
};