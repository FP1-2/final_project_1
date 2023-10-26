import React, { useRef, useState, useEffect} from "react";
import { Formik, Form } from 'formik';
import { object, string } from "yup";
import Textarea from "../Textarea/Textarea";
import PreviewImage from "../PreviewImage/PreviewImage";
import style from "./ModalAddPost.module.scss";
import { ReactComponent as AddPhoto } from "../../img/addPhoto.svg";
import { ReactComponent as Cross } from "../../img/cross.svg";
import { useDispatch, useSelector } from "react-redux";
import { modalAddPostState } from "../../redux-toolkit/profile/slice";

const validationSchema = object({
  text: string().min(3, "Must be more than 2 characters"),
});

const ModalAddPost = () => {

  const [scroll,setScroll]=useState(null);
  const [errorValidation, setErrorValidation]=useState(false);

  const handleScroll = () => {
    setScroll(Math.round(window.scrollY));
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);


  const img = useRef();

  const clickDownloadImg = () => {
    img.current.click();
  };

  const initialValues = {
    text: "",
    img: ""
  };

  const onSubmit = async (value) => {
    if(value.text==="" && value.img===""){
      setErrorValidation(true);
    }else{
      dispatch(modalAddPostState(false));
      setErrorValidation(false);
    }
  };

  const dispatch=useDispatch();

  const modalAddPost=useSelector((state)=>state.profile.modalAddPost.state);
  const userObject = useSelector(state => state.profile.profileUser.obj);

  const modalAddPostClose=()=>{
    dispatch(modalAddPostState(false));
  };



  return (
    <Formik initialValues={initialValues} onSubmit={onSubmit} validationSchema={validationSchema} >
      {({setFieldValue, values }) => (
        <div className={modalAddPost?style.modalWrapper:style.displayNone} style={{top:`${scroll-492}px`}}>
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
                  <a href="" className={style.modalHeaderInfoLink}>
                    {`${userObject.name} ${userObject.surname}`}
                  </a>
                </div>
              </div>
              <div className={style.modalMain}>
                <Textarea type="text" name="text" placeholder="Anything new?"/>
                {values.img && <PreviewImage file={values.img} />}
              </div>
            </div>
            <div>
              <p className={errorValidation?style.error:style.errorDisplayNone}>Error: Add a photo or text!</p>
              <div className={style.modalFooter}>
                <button type="button" className={style.modalAddPhotoBtn} onClick={clickDownloadImg}>
                  <AddPhoto className={style.modalAddPhotoImg} />
                  Download photo
                </button>
                <input type="file" name="img" ref={img} style={{ display: "none" }} onChange={(e) => {
                  setFieldValue("img", e.target.files[0]);
                  setErrorValidation(false);}} />
                <button type="submit" className={style.modalPublish}>Publish</button>
              </div>
            </div>            
          </Form>
        </div>)}
    </Formik>
  );
};
export default ModalAddPost;