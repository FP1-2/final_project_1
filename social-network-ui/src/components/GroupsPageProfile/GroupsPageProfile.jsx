import React, {useEffect, useRef} from "react";
import { useDispatch } from "react-redux";
import style from "./GroupsPageProfile.module.scss";
import { useSelector } from "react-redux";
import ErrorPage from "../ErrorPage/ErrorPage";
// import { useParams } from "react-router";
import Loader from "../Loader/Loader";
import {NavLink, useParams} from "react-router-dom";
import {getUserGroups} from "../../redux-toolkit/groups/thunks";
import GroupCardProfile from "../GroupCardProfile/GroupCardProfile";
import {postsUser} from "../../redux-toolkit/post/thunks";
import {createHandleScroll} from "../../utils/utils";

const GroupsProfile = () => {
  const dispatch = useDispatch();
  const { id } = useParams();
  const {
    userGroups: { obj:{content, totalPages, pageable: {
      pageNumber,
    }}, status, error },
  } = useSelector(state => state.groups);
  useEffect(() => {
    dispatch(getUserGroups({id: id, page: 0}));
  }, []);
  const getMoreGroups = () => {
    if (status !== 'pending' && pageNumber < totalPages) {
      dispatch(postsUser({ page: pageNumber + 1, id: id }));
    }
  };
  const scrollContainerRef = useRef(null);
  const handleScroll = createHandleScroll({
    scrollRef: scrollContainerRef,
    status: status,
    fetchMore: getMoreGroups,
  });


  return (
    <>
      {status === "rejected" ? (
        <ErrorPage
          message={error.message ? error.message : "Oops something went wrong!"}
        />
      ) : (
        <div className={style.profileBodyWrapper}>
          <div className={style.profileBody}>
            <div className={style.profileGroupsWrapper}  onScroll={() => handleScroll()} ref={scrollContainerRef}>
              <div className={style.profileGroupsHeader}>
                <NavLink className={style.profileGroupsHeaderLink}  to={"/groups"}>
                  Groups
                </NavLink>
              </div>
              {status === "pending" ?
                <Loader />
                :
                (
                  content.length ? (
                    <ul className={style.profileGroups}>
                      {content.map(el => (
                        <li key={el.id}>
                          <GroupCardProfile el ={el}/>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p>You are not a member of any groups</p>
                  )
                )
              }

            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default GroupsProfile;
