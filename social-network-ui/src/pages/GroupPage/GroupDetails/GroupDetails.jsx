import React, {useState} from 'react';
import style from './GroupDetails.module.scss';
import PropTypes from "prop-types";
import GroupAvatar from "../GroupAvatar/GroupAvatar";
import {Link} from "react-router-dom";
import {convertToLocalTime} from "../../../utils/formatData";

export default function GroupDetails({ group }) {
  const [showAll, setShowAll] = useState(false);

  const toggleShowAll = () => {
    setShowAll(!showAll);
  };

  return (
    <div className={style.groupDetails}>
      <span className={style.date}>created: {convertToLocalTime(group.createdDate)}</span>
      <span className={style.title}>Information:</span>
      <span className={style.description}>{group.description}</span>
        
      <span className={style.title}>Administrators:</span>
      <ul className={style.admins}>
        {group.admins.slice(0, showAll ? group.admins.length : 5).map(admin => (
          <li key={admin.id}>
            <Link to={`/profile/${admin.user.userId}`} title={`${admin.user.name} ${admin.user.surname}`}>
              <GroupAvatar pathImage={admin.user.avatar}/>
            </Link>
          </li>
        ))}
      </ul>
      {group.admins.length > 5 && (
        <span className={style.more} onClick={toggleShowAll}>
          {showAll ? 'hide' : '...all administrators'}
        </span>
      )}

      {group.members.length !== 0 && <span className={style.title}>
        Members who recently joined the group:
      </span>}
      <ul className={style.admins}>
        {group.members.slice(0, showAll ? group.members.length : 5).map(member => (
          <li key={member.id}>
            <Link to={`/profile/${member.user.userId}`} title={`${member.user.name} ${member.user.surname}`}>
              <GroupAvatar pathImage={member.user.avatar}/>
            </Link>
          </li>
        ))}
      </ul>
      {group.members.length > 5 && (
        <span className={style.more} onClick={toggleShowAll}>
          {showAll ? 'hide' : '...all administrators'}
        </span>
      )}   
    </div>
  );
}

GroupDetails.propTypes = {
  group: PropTypes.object,
};