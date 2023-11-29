import style from './IncomingFriendRequests.module.scss';
import { requestsToMe } from '../../redux-toolkit/friend/thunks';
import { useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { useSelector } from 'react-redux/es/hooks/useSelector';
import Notification from '../Notification/Notification';
/* import {convertToLocalTime} from "../../utils/formatData";
 */
const IncomingFriendRequests=()=>{
  const dispatch=useDispatch();
  useEffect(()=>{
    dispatch(requestsToMe());
  },[]);
  const incomingFriendRequests=useSelector(state=>state.friends.requestsToMe.obj);
  return(
    <div className={style.s}>
      <ul>
        {incomingFriendRequests.map((el)=>(
          <li key={el.id}>
            <Notification notification={el}></Notification>
          </li>
        ))}
      </ul>
    </div>
    
  );
};
export default IncomingFriendRequests;