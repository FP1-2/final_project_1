import React from "react";
import style from "./PostsPageProfile.module.scss";
// import classnames from "classnames";
// import PropTypes from "prop-types";
import PostProfile from "../PostProfile/PostProfile";

const PostPageProfile = () => {
  // const [fixed, setFixed] = useState(false);

  // const posts = useRef();
  
  // const handleScroll = () => {
  //   const postsTop=posts.current.offsetTop;
  //   const scrollTop=Math.round(window.scrollY);

  //   if(scrollTop>postsTop){
  //     setFixed(true);
  //   }else{
  //     setFixed(false);
  //   }
  // };

  // useEffect(() => {
  //   window.addEventListener('scroll', handleScroll);
  //   return () => window.removeEventListener('scroll', handleScroll);
  // }, []);

  // const classNames=classnames(style.profileBodyWrapper,{[style.fix]:fixed });

  return (
    <div className={style.profileBodyWrapper}>
      <div className={style.profileBody}>
        <div className={style.profileInformation}>
          <h2 className={style.profileInformationTitile}>Information</h2>
          <ul className={style.profileInformationList}>
            <li className={style.profileInformationElem}>Навчалась в</li>
            <li className={style.profileInformationElem} >Мешкає в</li>
          </ul>
          <button className={style.profileInformationBtn}>
            Add information
          </button>
        </div>
        <div className={style.profileBodyPostsWrapper}>
          <div className={style.profileAddPost}>
            <img src="https://risovach.ru/upload/2013/01/mem/kakoy-pacan_9771748_orig_.jpeg" alt="" className={style.profileAddPostImg} />
            <button className={style.profileAddPostBtn} >Add post</button>
          </div>
          <ul className={style.profilePosts}>
            <li className={style.profilePost}><PostProfile /></li>
            <li className={style.profilePost}><PostProfile /></li>
            <li className={style.profilePost}><PostProfile /></li>
          </ul>
        </div>
      </div>
    </div>
  );
};
// FriendProfile.propTypes = {
// //   file: PropTypes.object
// };
// FriendProfile.defaultProps = {
// //   file: {}
// };
export default PostPageProfile;