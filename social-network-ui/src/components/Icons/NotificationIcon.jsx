import PropTypes from 'prop-types';

const NotificationIcon = ({className}) => {
  return (
    <svg viewBox="0 0 24 24" width="1em" height="1em" fill="currentColor"
         className={className}>
      <path
        d="M3 9.5a9 9 0 1 1 18 0v2.927c0 1.69.475 3.345 1.37 4.778a1.5 1.5 0 0 1-1.272 2.295h-4.625a4.5 4.5 0 0 1-8.946 0H2.902a1.5 1.5 0 0 1-1.272-2.295A9.01 9.01 0 0 0 3 12.43V9.5zm6.55 10a2.5 2.5 0 0 0 4.9 0h-4.9z">
      </path>
    </svg>
  )
}
export default NotificationIcon;

NotificationIcon.propTypes = {
  className: PropTypes.string
}