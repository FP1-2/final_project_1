import React, { useState, useRef} from "react";
import { Formik, Form } from 'formik';
import { object, string } from "yup";
import Textarea from "../Textarea/Textarea";
import PreviewImage from "../PreviewImage/PreviewImage";
import style from "./ModalEditPost.module.scss";
import { ReactComponent as AddPhoto } from "../../img/addPhoto.svg";
import { ReactComponent as Cross } from "../../img/cross.svg";
import { useDispatch, useSelector } from "react-redux";
import { modalEditPostState} from "../../redux-toolkit/post/slice";
import { getPhotoURL } from "../../utils/thunks";
import { editPost } from "../../redux-toolkit/post/thunks";

const validationSchema = object({
  text: string().required("Text is required").min(3, "Must be more than 2 characters"),
});

const ModalEditPost = () => {

  const dispatch = useDispatch();

  const [errorValidation, setErrorValidation] = useState(false);
  const img = useRef();


  const modalEditPost = useSelector((state) => state.post.modalEditPost);
  const userObject = useSelector(state => state.profile.profileUser.obj);
  const post = useSelector((state) => state.post.postObj);


  const clickDownloadImg = () => {
    img.current.click();
  };

  const initialValues = {
    text: "",
    img: ""
  };

  const onSubmit = async (value) => {
    if (value.img === "") {
      dispatch(editPost({ obj: { imageUrl: post.imageUrl, body: value.text, title: "add new post" }, id: post.postId }));
      setErrorValidation(false);
    } else {
      const photo = (await getPhotoURL(value.img));
      dispatch(editPost({ obj: { imageUrl: photo, body: value.text, title: "add new post" }, id: post.postId }));
      setErrorValidation(false);
    }
    dispatch(modalEditPostState(false));
  };

  const modalEditPostClose = () => {
    dispatch(modalEditPostState(false));
  };



  return (
    <Formik initialValues={initialValues} onSubmit={onSubmit} validationSchema={validationSchema} >
      {({ setFieldValue, values }) => (
        <div  className={modalEditPost ? style.modalWrapper : style.displayNone}>
          <Form className={style.modal}>
            <div>
              <div className={style.modalHeader}>
                <div className={style.modalHeaderTitleWrapper}>
                  <h2 className={style.modalHeaderTitle}>Edit publication</h2>
                  <button type="button" className={style.modalHeaderCloseBtn} onClick={modalEditPostClose}>
                    <Cross className={style.modalHeaderCloseBtnImg} />
                  </button>
                </div>
                <div className={style.modalHeaderInfo}>
                  <img src={userObject.avatar ? userObject.avatar : "https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="" className={style.modalHeaderInfoImg} />
                  <a href="" className={style.modalHeaderInfoLink}>
                    {`${userObject.name} ${userObject.surname}`}
                  </a>
                </div>
              </div>
              <div className={style.modalMain}>
                <Textarea type="text" name="text" placeholder="Enter text of publication" value={post.text}/>
                {values.img && <button type="button" className={style.modalMainBtn} onClick={() => { values.img = ""; }}>Clear photo</button>}
                {values.img && <PreviewImage file={values.img} />}
              </div>
            </div>
            <div>
              <p className={errorValidation ? style.error : style.errorDisplayNone}>Error: Add a photo or text!</p>
              <div className={style.modalFooter}>
                <button type="button" className={style.modalAddPhotoBtn} onClick={clickDownloadImg}>
                  <AddPhoto className={style.modalAddPhotoImg} />
                  Download photo
                </button>
                <input type="file" name="img" ref={img} style={{ display: "none" }} onChange={(e) => {
                  setFieldValue("img", e.target.files[0]);
                  setErrorValidation(false);
                }} />
                <button type="submit" className={style.modalPublish}>Publish</button>
              </div>
            </div>
          </Form>
        </div>)}
    </Formik>
  );
};
export default ModalEditPost;