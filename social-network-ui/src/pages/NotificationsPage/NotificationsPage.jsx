import React, {useEffect, useRef} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {loadNotifications} from '../../redux-toolkit/notification/thunks';
import {resetNotificationsState} from '../../redux-toolkit/notification/slice';
import Notification from '../../components/Notification/Notification';
import styles from './NotificationsPage.module.scss';
import {createHandleScroll} from "../../utils/utils";
import Loader from '../../components/Loader/Loader';

export default function NotificationsPage() {
  const scrollContainerRef = useRef(null);
  const dispatch = useDispatch();

  const {
    status,
    obj: { 
      content,
      totalPages, 
      pageable: { 
        pageNumber 
      }
    } 
  } = useSelector(state => state.notifications.notifications);

  useEffect(() => {
    dispatch(resetNotificationsState());
    dispatch(loadNotifications({ page: 0 }));
  }, []);

  const getMoreNotifications = () => {
    if (status !== 'pending' && pageNumber < totalPages) {
      dispatch(loadNotifications({ page: pageNumber + 1 }));
    }
  };

  const handleScroll = createHandleScroll({
    scrollRef: scrollContainerRef,
    status: status,
    fetchMore:  getMoreNotifications,
  });

  return (
    <div className={styles.container} onScroll={handleScroll} ref={scrollContainerRef}>
      {status === 'pending' && content.length === 0 && <Loader/>}
      <ul>
        {content.map((notification) => (
          <li key={notification.id}>
            <Notification  notification={notification} />
          </li>
        ))}
      </ul>
      {status === 'pending' && content.length > 0 && <Loader/>}
      {pageNumber === totalPages && status !== 'pending' && <h4 className={styles.container_allCard}>That`s all for now!</h4>}
    </div>
  );
}
