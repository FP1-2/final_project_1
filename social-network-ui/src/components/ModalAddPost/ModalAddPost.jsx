import React, {useEffect, useRef, useState} from "react";
import { Formik, Form } from 'formik';
import { object, string } from "yup";
import Textarea from "../Textarea/Textarea";
import PreviewImage from "../PreviewImage/PreviewImage";
import style from "./ModalAddPost.module.scss";
import { ReactComponent as AddPhoto } from "../../img/addPhoto.svg";
import { ReactComponent as Cross } from "../../img/cross.svg";
import { useDispatch, useSelector } from "react-redux";
import { modalAddPostState, appendPostStart } from "../../redux-toolkit/post/slice";
import { getPhotoURL } from "../../utils/thunks";
import { addPost } from "../../redux-toolkit/post/thunks";
import {setNewPost} from "../../redux-toolkit/ws/slice";

const validationSchema = object({
  text: string().required("Text is required").min(3, "Must be more than 2 characters"),
});

const ModalAddPost = () => {

  const [errorValidation, setErrorValidation] = useState(false);
  const img = useRef();
  const modalAddPost = useSelector((state) => state.post.modalAddPost);
  const userObject = useSelector(state => state.profile.profileUser.obj);
  const newPost = useSelector(state => state.webSocket.newPost);

  const clickDownloadImg = () => {
    img.current.click();
  };

  const initialValues = {
    text: "",
    img: ""
  };

  const onSubmit = async (value, {resetForm}) => {
    if (value.text === "" && value.img === "") {
      setErrorValidation(true);
    } else if (value.img === "") {
      dispatch(addPost({ imageUrl: "", body: value.text, title: "add new post" }));
      setErrorValidation(false);
      dispatch(modalAddPostState(false));
    } else {
      const photo = (await getPhotoURL(value.img)).data.url;
      dispatch(addPost({ imageUrl: photo, body: value.text, title: "add new post" }));
      setErrorValidation(false);
      dispatch(modalAddPostState(false));
    }
    resetForm();
  };
  useEffect(() => {
    if(newPost){
      dispatch(appendPostStart(newPost));
      dispatch(setNewPost(null));
    }
  }, [newPost]);
  const dispatch = useDispatch();
  useEffect(() => {
    return ()=>{
      dispatch(modalAddPostState(false));
    };
  }, []);


  const modalAddPostClose = () => {
    dispatch(modalAddPostState(false));
  };



  return (
    <Formik initialValues={initialValues} onSubmit={onSubmit} validationSchema={validationSchema} >
      {({ setFieldValue, values }) => (
        <div className={modalAddPost ? style.modalWrapper : style.displayNone} >
          <Form className={style.modal}>
            <div>
              <div className={style.modalHeader}>
                <div className={style.modalHeaderTitleWrapper}>
                  <h2 className={style.modalHeaderTitle}>Create a publication</h2>
                  <button type="button" className={style.modalHeaderCloseBtn} onClick={modalAddPostClose}>
                    <Cross className={style.modalHeaderCloseBtnImg} />
                  </button>
                </div>
                <div className={style.modalHeaderInfo}>
                  <img src={userObject.avatar ? userObject.avatar : "https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="" className={style.modalHeaderInfoImg} />
                  <h3 className={style.modalHeaderInfoTitle}>
                    {`${userObject.name} ${userObject.surname}`}
                  </h3>
                </div>
              </div>
              <div className={style.modalMain}>
                <Textarea type="text" name="text" placeholder="Anything new?" />
                {values.img && <button type="button" className={style.modalMainBtn} onClick={() => {
                  setFieldValue("img","");}}>
                  Clear photo</button>}
                {values.img && <PreviewImage file={values.img}/>}
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