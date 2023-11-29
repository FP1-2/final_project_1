import React from "react";
import { Formik, Form } from 'formik';
import { object, string } from "yup";
import style from "./ModalAddRepost.module.scss";
import { ReactComponent as Cross } from "../../img/cross.svg";
import { useDispatch, useSelector } from "react-redux";
import { modalAddRepostState,appendPostStart} from "../../redux-toolkit/post/slice";
import { addRepost } from "../../redux-toolkit/post/thunks";

const validationSchema = object({
  text: string().min(3, "Must be more than 2 characters"),
});

const ModalAddRepost = () => {
  const dispatch = useDispatch();
  const modalAddRepost = useSelector((state) => state.post.modalAddRepost);
  const userObject = useSelector(state => state.auth.user.obj);
  const post = useSelector((state) => state.post.postObj);


  const initialValues = {
    text: "",
  };

  const onSubmit = async (value) => {
    if (post.type === "REPOST") {
      dispatch(addRepost({ imageUrl: post.originalPost.imageUrl, body: value.text, title: "add repost", originalPostId: post.originalPost.postId }));
      dispatch(appendPostStart({...post,author:{avatar:userObject.avatar, name:userObject.name,surname:userObject.surname}, imageUrl: post.originalPost.imageUrl, body: value.text, title: "add repost", originalPostId: post.originalPost.postId }));
    } else {
      dispatch(addRepost({ imageUrl: post.imageUrl, body: value.text, title: "add repost", originalPostId: post.postId }));
      dispatch(appendPostStart({author:{avatar:userObject.avatar, name:userObject.name,surname:userObject.surname}, type:"REPOST", imageUrl: post.imageUrl, body: value.text, title: "add repost", originalPostId: post.postId, originalPost:post}));
    }
    dispatch(modalAddRepostState(false));
  };


  const modalAddRepostClose = () => {
    dispatch(modalAddRepostState(false));
  };



  return (
    <Formik initialValues={initialValues} onSubmit={onSubmit} validationSchema={validationSchema} >
      {({ setFieldValue, errors, isValid }) => (
        <div className={modalAddRepost ? style.modalWrapper : style.displayNone}>
          <Form className={style.modal}>
            <div>
              <div className={style.modalHeader}>
                <div className={style.modalHeaderTitleWrapper}>
                  <h2 className={style.modalHeaderTitle}>Share a publication</h2>
                  <button type="button" className={style.modalHeaderCloseBtn} onClick={modalAddRepostClose}>
                    <Cross className={style.modalHeaderCloseBtnImg} />
                  </button>
                </div>
                <div className={style.modalHeaderInfo}>
                  <img src={userObject.avatar ? userObject.avatar : "https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="" className={style.modalHeaderInfoImg} />
                  <h3 href="" className={style.modalHeaderInfoTitle}>
                    {`${userObject.name} ${userObject.surname}`}
                  </h3>
                </div>
                <input type="text" className={style.modalNewInfo} placeholder="Add your text" onChange={(e) => setFieldValue("text", e.target.value)} />
                <div className={style.repost}>
                  <img src={post.author ? post.author.avatar
                    : "https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="" className={style.repostAvatar} />
                  <h4 className={style.repostName} >
                    {post.author?`${post.author.name} ${post.author.surname}`:null}
                  </h4>
                </div>
              </div>
              <div className={style.modalMain}>
                <p className={style.modalMainText}>{post.type === "REPOST" ? post.originalPost.body : post.body}</p>
                {(post.type === "REPOST" && post.originalPost.imageUrl) ? <img className={style.modalMainImg} src={post.originalPost.imageUrl} /> : null}
                {(post.imageUrl && post.type === "POST") ? <img className={style.modalMainImg} src={post.imageUrl} /> : null}
              </div>
            </div>
            <div>
              {errors ? <p className={style.error}>{errors.text}</p> : null}
              <div className={style.modalFooter}>
                <button type="submit" className={style.modalPublish} disabled={!isValid}>Publish</button>
              </div>
            </div>
          </Form>
        </div>)}
    </Formik>
  );
};
export default ModalAddRepost;