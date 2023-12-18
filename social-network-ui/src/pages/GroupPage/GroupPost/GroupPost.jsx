import style from './GroupPost.module.scss';
import { ReactComponent as LikePostBtn } from "../../../img/likePostBtn.svg";
import { ReactComponent as CommentPostBtn } from "../../../img/commentPostBtn.svg";
import { ReactComponent as SharePostBtn } from "../../../img/sharePostBtn.svg";
import GroupAvatar from "../GroupAvatar/GroupAvatar";
import TripleMenu from "../../../components/TripleMenu/TripleMenu";
import {useEffect, useState} from "react";
import PropTypes from "prop-types";
import FLikes from "../../../components/Icons/FLikes";

export default function GroupPost({ adm, post }) {
  const [fullPost, setFullPost] = useState(true);
  const togglePost =()=> setFullPost(state => !state);

  const [fullOriginal, setFullOriginal] = useState(true);
  const toggleOriginal =()=> setFullOriginal(state => !state);

  const [fullComments, setFullComments] = useState(true);
  const toggleComments =()=> setFullComments(state => !state);

  const REPOST = post.type === "REPOST";
  const likes = true;
  const DRAFT = 'Draft', ARCHIVED = 'Archived',
    REJECTED = 'Rejected';
  
  const [hideAdmMenu, setHideAdmMenu] = useState(false);
  const [tab, setTab] = useState('');
  const toggleAdmMenu = () => setHideAdmMenu(state => !state);

  const setDraft = () => {
    setTab(DRAFT);
    //
    //console.log(DRAFT);
  };

  const setArchived = () => {
    setTab(ARCHIVED);
    //
    //console.log(ARCHIVED);
  };

  const setRejected = () => {
    setTab(REJECTED);
    //
    //console.log(REJECTED);
  };

  const getActiveTab = tab => {
    switch (tab) {
    case DRAFT:
      return 'tabOne';
    case ARCHIVED:
      return 'tabTwo';
    case REJECTED:
      return 'tabThree';
    default:
      return '';
    }
  };

  useEffect(() => {
    const handleClickOutside = event => {
      !event.target.closest(`.${style.spriteButton}`) && setHideAdmMenu(false);
    };
    if (hideAdmMenu) document.addEventListener('click', handleClickOutside);
    return () => document.removeEventListener('click', handleClickOutside);
  }, [hideAdmMenu]);
    
  return (
    <li className={style.postWrapper}>
      <div className={style.header}>
        <div className={style.rightSection}>
          <div className={style.avatarSection}>
            <GroupAvatar pathImage={post.groupImageUrl} />
            <div className={style.userAvatar}>
              <GroupAvatar pathImage={post.authorMember.user.avatar}
                circle={true} />
            </div>
          </div>
          <div className={style.nameSection}>
            <span className={style.groupName}>{post.groupName}</span>
            <span className={style.memberName}>{post.authorMember.user.name}</span>
          </div>
        </div>
        {adm && <button onClick={toggleAdmMenu} className={style.spriteButton}></button>}
        {hideAdmMenu && <TripleMenu className={style.tripleMenu}
          one={DRAFT}
          two={ARCHIVED}
          three={REJECTED}
          onOne={setDraft}
          onTwo={setArchived}
          onThree={setRejected}
          activeTab={getActiveTab(tab)}
        />}
      </div>
      <div className={`${style.contentPosts} ${fullPost && style.full}`}>
        {post.body}</div>
      <span className={style.btnFull} onClick={togglePost}>{fullPost ? "...more" : "hide"}</span>

      {REPOST && <div className={style.original}>Original post:</div>}
      {REPOST && <div className={style.originalPost}>
        <GroupAvatar pathImage={post.originalPost.authorMember.user.avatar}
          circle={true}/>
        <div className={style.originalNameSection}>
          <span>{post.originalPost.authorMember.user.name}</span>
        </div>
      </div>}
      {REPOST && <div className={`${style.originalContent} ${fullOriginal && style.full}`}>
        {post.originalPost.body}</div>}
      {REPOST && <span className={style.btnFull} 
        onClick={toggleOriginal}>{fullOriginal ? "...more" : "hide"}</span>}


      <div className={style.postImage}>
        <img src={post.postImageUrl} alt="Post"/>
      </div>

      <div className={style.likeCommentInfo}>
        <div>
          <FLikes className={style.likeIcon}/>
          {likes && <span>you and </span>}
          <span>{post.likes}</span>
        </div>
        <span>Comment: {post.comments}</span>
      </div>

      <div className={style.wrapLine}>
        <hr className={style.horizontalLine}/>
      </div>

      <div className={style.actionButtons}>
        <div><LikePostBtn className={style.postBtnImg}/><span>Like</span></div>
        <div><CommentPostBtn className={style.postBtnImg}/><span>Comment</span></div>
        <div><SharePostBtn className={style.postBtnImg}/><span>Share</span></div>
      </div>

      <div className={style.wrapLine}>
        <hr className={style.horizontalLine}/>
      </div>

      <div className={`${style.commentsSection} ${fullComments && style.fullComments}`}>
        <span onClick={toggleComments} className={style.moreComments}>Comments</span>
      </div>
    </li>
  );
}

GroupPost.propTypes = {
  adm: PropTypes.bool,
  post: PropTypes.object,
};