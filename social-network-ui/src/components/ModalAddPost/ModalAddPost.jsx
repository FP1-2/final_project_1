import React, { useRef, useState, useEffect } from "react";
import { Formik, Form } from 'formik';
import { object, string } from "yup";
import Textarea from "../Textarea/Textarea";
import PreviewImage from "../PreviewImage/PreviewImage";
import style from "./ModalAddPost.module.scss";
import { ReactComponent as AddPhoto } from "../../img/addPhoto.svg";
import { ReactComponent as Cross } from "../../img/cross.svg";
import { useDispatch, useSelector } from "react-redux";
import { modalAddPostState } from "../../redux-toolkit/post/slice";
import { getPhotoURL } from "../../redux-toolkit/registration/thunks";
import { addPost } from "../../redux-toolkit/post/thunks";

const validationSchema = object({
  text: string().required("Text is required").min(3, "Must be more than 2 characters"),
  // name: string().required("Name is required").min(2, "Must be more than 1 characters"),
});

const ModalAddPost = () => {

  const [scroll, setScroll] = useState(null);
  const [errorValidation, setErrorValidation] = useState(false);
  const img = useRef();
  const modalAddPost = useSelector((state) => state.post.modalAddPost);
  const userObject = useSelector(state => state.profile.profileUser.obj);

  const handleScroll = () => {
    setScroll(Math.round(window.scrollY));
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);




  const clickDownloadImg = () => {
    img.current.click();
  };

  const initialValues = {
    text: "",
    img: ""
  };

  const onSubmit = async (value) => {
    if (value.text === "" && value.img === "") {
      setErrorValidation(true);
    } else if (value.img === "") {
      dispatch(addPost({ imageUrl: "", body: value.text, title: "add new post" }));
      setErrorValidation(false);
      dispatch(modalAddPostState(false));
    } else {
      const photo = (await getPhotoURL(value.img));
      dispatch(addPost({ imageUrl: photo, body: value.text, title: "add new post" }));
      setErrorValidation(false);
      dispatch(modalAddPostState(false));
    }
  };

  const dispatch = useDispatch();



  const modalAddPostClose = () => {
    dispatch(modalAddPostState(false));
  };



  return (
    <Formik initialValues={initialValues} onSubmit={onSubmit} validationSchema={validationSchema} >
      {({ setFieldValue, values }) => (
        <div className={modalAddPost ? style.modalWrapper : style.displayNone} style={{ top: `${scroll - 492}px` }}>
          <Form className={style.modal}>
            <div>
              <div className={style.modalHeader}>
                <div className={style.modalHeaderTitleWrapper}>
                  <h2 className={style.modalHeaderTitle}>Create a publication</h2>
                  <button className={style.modalHeaderCloseBtn} onClick={modalAddPostClose}>
                    <Cross className={style.modalHeaderCloseBtnImg} />
                  </button>
                </div>
                <div className={style.modalHeaderInfo}>
                  <img src={userObject.avatar ? userObject.avatar : "https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="" className={style.modalHeaderInfoImg} />
                  <h3  className={style.modalHeaderInfoTitle}>
                    {`${userObject.name} ${userObject.surname}`}
                  </h3>
                </div>
              </div>
              <div className={style.modalMain}>
                <Textarea type="text" name="text" placeholder="Anything new?" />
                {values.img && <button type="button" className={style.modalMainBtn} onClick={() => { values.img = "" }}>Clear photo</button>}
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
export default ModalAddPost;