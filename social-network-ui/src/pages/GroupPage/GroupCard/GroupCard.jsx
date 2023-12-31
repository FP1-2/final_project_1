import React from 'react';
import style from './GroupCard.module.scss';
import PropTypes from "prop-types";
import GroupAvatar from "../GroupAvatar/GroupAvatar";

export default function GroupCard({ pathImage, groupName, memberCount, isPublic }) {
  return (
    <div className={style.groupCard}>
      <GroupAvatar pathImage={pathImage}/>
      <div className={style.info}>
        <h2 className={style.groupName}>{groupName}</h2>
        <p className={style.details}>
          <span className={style.iconGlobe}></span>
          {isPublic ? 'Public Group' : 'Private Group'}
        </p>
        <p className={style.memberCount}>{memberCount} members</p>
      </div>
    </div>
  );
}

GroupCard.propTypes = {
  pathImage: PropTypes.string,
  groupName: PropTypes.string,
  memberCount: PropTypes.number,
  isPublic: PropTypes.bool,
};
