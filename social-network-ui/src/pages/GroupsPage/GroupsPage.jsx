import React, {useEffect, useState} from 'react';
import Loader from '../../components/Loader/Loader';
import style from './GroupsPage.module.scss';

export default function GroupsPage() {

  const [status, setStatus] = useState('pending');
  const [groups, setGroups] = useState([]);

  useEffect(() => {
    const timer = setTimeout(() => {
      setStatus('');
      setGroups( [{id: 1, g: 'group1'}, {id: 2, g: 'group2'}, {id: 3, g: 'group3'}]);
    }, 2000);

    return () => clearTimeout(timer);
  }, [groups]);

  return (
    <div className={style.wrapper}>
      {status === 'pending' && groups.length === 0 && <Loader/>}
      <ul>
        {groups.map((group) => (
          <li key={group.id}>
            <div>{group.g}</div>
          </li>
        ))}
      </ul>
    </div>
  );
}