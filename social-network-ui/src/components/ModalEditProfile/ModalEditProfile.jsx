import React, {useState, useRef} from "react";
import { Formik, Form} from 'formik';
import { object, string,number } from "yup";
import style from "./ModalEditProfile.module.scss";
import { ReactComponent as Cross } from "../../img/cross.svg";
import Input from "../Input/Input";

const maxYear = new Date().getFullYear()-5;
const minYear = new Date().getFullYear()-100;
const validationSchema = object({
  name: string().required("Name is required").min(2, "Must be more than 1 characters"),
  surname: string().required("Surname is required").min(2, "Must be more than 1 characters"),
  address:string().min(4, "Must be more than 3 characters"),
  dateOfBirth:number().max(maxYear,"Must be more than 4 years").min(minYear,"Too old")
});

const ModalEditProfile = () => {

  const [editInformBtn,setEditInformBtn]=useState(false);

  const avatar = useRef();
  const headerPhoto = useRef();

  const clickDownloadAvatar = () => {
    avatar.current.click();
  };

  const clickDownloadHeaderPhoto = () => {
    headerPhoto.current.click();
  };

  const initialValues = {
    name: "",
    surname: "",
    avatar:"",
    headerPhoto: "",
    address:"",
    dateOfBirth:"",
  };


  const onSubmit = async () => {
  };


  return (
    <Formik initialValues={initialValues} onSubmit={onSubmit} validationSchema={validationSchema} >
      {({isValid}) => (
        <div className={style.modalWrapper}>
          <Form className={style.modal}>
            <div>
              <div className={style.modalHeader}>
                <div className={style.modalHeaderTitleWrapper}>
                  <h2 className={style.modalHeaderTitle}>Create a publication</h2>
                  <button type="button" className={style.modalHeaderCloseBtn}>
                    <Cross className={style.modalHeaderCloseBtnImg} />
                  </button>
                </div>
              </div>
              <div className={style.modalMain}>
                <div className={style.modalMainAvatar}>
                  <div className={style.modalMainAvatarWrapper}>
                    <h2 className={style.modalMainAvatarTitle}>Profile photo</h2>
                    <input type="file" ref={avatar} style={{display:"none"}}/>
                    <button type="button" className={style.modalMainAvatarBtn} onClick={clickDownloadAvatar}>Edit</button>
                  </div>
                  <img src="https://risovach.ru/upload/2013/01/mem/kakoy-pacan_9771748_orig_.jpeg" alt=""  className={style.modalMainAvatarImg}/>
                </div>
                <div className={style.modalMainHeaderPhoto}>
                  <div className={style.modalMainHeaderPhotoWrapper}>
                    <h2 className={style.modalMainHeaderPhotoTitle}>Cover photo</h2>
                    <input type="file" ref={headerPhoto} style={{display:"none"}}/>
                    <button type="button" className={style.modalMainHeaderPhotoBtn} onClick={clickDownloadHeaderPhoto}>Edit</button>
                  </div>
                  <img src="https://bipbap.ru/wp-content/uploads/2017/04/0_7c779_5df17311_orig.jpg" alt=""  className={style.modalMainHeaderPhotoImg}/>
                </div>
                <button type="button" className={style.modalMainBtnAddInform} onClick={()=>{setEditInformBtn((prev)=>!prev);}}>Add/Edit information about yourself</button>
                <div className={editInformBtn?style.modalMainInputsWrapper:style.displayNone}>
                  <Input name="name" type="text" placeholder="Name" modal="editProfile"/>
                  <Input name="surname" type="text" placeholder="Surame" modal="editProfile"/>
                  <Input name="dateOfBirth" type="text" placeholder="Year of birth" modal="editProfile"/>
                  <Input name="address" type="text" placeholder="Address" modal="editProfile"/>
                  <button className={editInformBtn?style.modalMainSaveProfileBtn:style.displayNone} disabled={!isValid} type="submit">Save changes</button>
                </div>
              </div>
            </div>
          </Form>
        </div>)}
    </Formik>
  );
};
export default ModalEditProfile;