import style from './IncomingFriendRequests.module.scss';
import { requestsToMe } from '../../redux-toolkit/friend/thunks';
import { useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { useSelector } from 'react-redux/es/hooks/useSelector';
import FriendRequestCard from '../FriendRequestCard/FriendRequestCard';
import EmptyMessage from '../EmptyMessage/EmptyMessage';
const IncomingFriendRequests=()=>{
  const dispatch=useDispatch();
  useEffect(()=>{
    dispatch(requestsToMe());
  },[]);
  const incomingFriendRequests=useSelector(state=>state.friends.requestsToMe.obj);
  return(
    <div className={style.s}>
      {incomingFriendRequests.length > 0 ? (
        <ul>
          {incomingFriendRequests.map((el) => (
            <li key={el.id}>
              <FriendRequestCard type='FRIEND_REQUEST' friendRequest={el} />
            </li>
          ))}
        </ul>
      ) : (
        <EmptyMessage message="No incoming friend requests." />
      )}
    </div>
    
  );
};
export default IncomingFriendRequests;