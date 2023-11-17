import React, { useState, useRef } from "react";
import { Formik, Form } from 'formik';
import { object, string, number } from "yup";
import style from "./ModalEditProfile.module.scss";
import { ReactComponent as Cross } from "../../img/cross.svg";
import Input from "../Input/Input";
import { useDispatch, useSelector } from "react-redux";
import { modalEditProfileState } from "../../redux-toolkit/profile/slice";
import { editUser } from "../../redux-toolkit/profile/thunks";
import { getPhotoURL } from "../../utils/thunks";

const validationSchema = object({
  name: string().min(2, "Must be more than 2 characters"),
  surname: string().min(2, "Must be more than 1 characters"),
  address: string().min(4, "Must be more than 3 characters"),
  dateOfBirth: number().max(100, "Too old").min(6, "Must be more than 5 years"),
  email: string().email(),
  username: string().min(2, "Must be more than 1 characters"),
});

const ModalEditProfile = () => {

  const dispatch = useDispatch();

  const avatar = useRef();
  const headerPhoto = useRef();

  const [nameInput, setNameInput] = useState(false);
  const [surnameInput, setSurnameInput] = useState(false);
  const [dateOfBirthInput, setDateOfBirthInput] = useState(false);
  const [addressInput, setAddressInput] = useState(false);
  const [emailInput, setEmailInput] = useState(false);
  const [usernameInput, setUsernameInput] = useState(false);

  const modalEditProfile = useSelector((state) => state.profile.modalEditProfile.state);
  const userObject = useSelector(state => state.profile.profileUser.obj);


  const downloadInputAvatarPicture = async (e) => {
    const file = e.target.files[0];
    const photo = (await getPhotoURL(file)).data.url;
    dispatch(editUser({ avatar: photo }));
  };
  const downloadInputHeaderPicture = async (e) => {
    const file = e.target.files[0];
    const photo = (await getPhotoURL(file)).data.url;
    dispatch(editUser({ headerPhoto: photo }));
  };

  const modalEditProfileClose = () => {
    dispatch(modalEditProfileState(false));
  };

  const clickDownloadAvatar = () => {
    avatar.current.click();
  };

  const clickDownloadHeaderPhoto = () => {
    headerPhoto.current.click();
  };

  const initialValues = {
    name: "",
    surname: "",
    avatar: "",
    headerPhoto: "",
    address: "",
    dateOfBirth: "",
  };

  
  const onSubmit = async (value) => {
    const obj={};
    for (const key in value) {
      if (value[key] !== "") {
        obj[key] = value[key];
      }
    }
    dispatch(editUser(obj));
    dispatch(modalEditProfileState(false));
  };


  return (
    <Formik initialValues={initialValues} onSubmit={onSubmit} validationSchema={validationSchema} >
      {({ isValid }) => (
        <div className={modalEditProfile ? style.modalWrapper : style.displayNone} >
          <Form className={style.modal}>
            <div>
              <div className={style.modalHeader}>
                <div className={style.modalHeaderTitleWrapper}>
                  <h2 className={style.modalHeaderTitle}>Edit profile</h2>
                  <button type="button" className={style.modalHeaderCloseBtn} onClick={modalEditProfileClose}>
                    <Cross className={style.modalHeaderCloseBtnImg} />
                  </button>
                </div>
              </div>
              <div className={style.modalMain}>
                <div className={style.modalMainAvatar}>
                  <div className={style.modalMainAvatarWrapper}>
                    <h2 className={style.modalMainAvatarTitle}>Profile photo</h2>
                    <input type="file" ref={avatar} style={{ display: "none" }} onChange={(e) => downloadInputAvatarPicture(e)}/>
                    <button type="button" className={style.modalMainAvatarBtn} onClick={clickDownloadAvatar}>Edit</button>
                  </div>
                  <img src={userObject.avatar ? userObject.avatar : "https://senfil.net/uploads/posts/2015-10/1444553580_10.jpg"} alt="" className={style.modalMainAvatarImg} />
                </div>
                <div className={style.modalMainHeaderPhoto}>
                  <div className={style.modalMainHeaderPhotoWrapper}>
                    <h2 className={style.modalMainHeaderPhotoTitle}>Cover photo</h2>
                    <input type="file" ref={headerPhoto} style={{ display: "none" }} onChange={(e) => downloadInputHeaderPicture(e)}/>
                    <button type="button" className={style.modalMainHeaderPhotoBtn} onClick={clickDownloadHeaderPhoto}>Edit</button>
                  </div>
                  <img src={userObject.headerPhoto ? userObject.headerPhoto : "https://www.colorbook.io/imagecreator.php?hex=f0f2f5&width=1080&height=1920&text=%201080x1920"} alt="" className={style.modalMainHeaderPhotoImg} />
                </div>
                <ul className={style.modalMainInfoList}>
                  <li className={style.modalMainInfoElem}>
                    <div className={style.modalMainInfoElemText}>
                      <h3 className={style.modalMainInfoElemTitle}>Name: {userObject.name}</h3>
                      <button type="button" className={style.modalMainInfoElemBtn} onClick={() => setNameInput((val) => !val)}>Edit</button>
                    </div>
                    {nameInput ? <Input name="name" type="text" placeholder="Name" modal="editProfile" /> : null}
                  </li>
                  <li className={style.modalMainInfoElem}>
                    <div className={style.modalMainInfoElemText}>
                      <h3 className={style.modalMainInfoElemTitle}>Surname: {userObject.surname}</h3>
                      <button type="button" className={style.modalMainInfoElemBtn} onClick={() => setSurnameInput((val) => !val)}>Edit</button>
                    </div>
                    {surnameInput ? <Input name="surname" type="text" placeholder="Surame" modal="editProfile" /> : null}
                  </li>
                  <li className={style.modalMainInfoElem}>
                    <div className={style.modalMainInfoElemText}>
                      <h3 className={style.modalMainInfoElemTitle}>Age: {userObject.dateOfBirth ? userObject.dateOfBirth : null}</h3>
                      <button type="button" className={style.modalMainInfoElemBtn} onClick={() => setDateOfBirthInput((val) => !val)}>Edit</button>
                    </div>
                    {dateOfBirthInput ? <Input name="dateOfBirth" type="text" placeholder="Age" modal="editProfile" /> : null}
                  </li>
                  <li className={style.modalMainInfoElem}>
                    <div className={style.modalMainInfoElemText}>
                      <h3 className={style.modalMainInfoElemTitle}>Address: {userObject.address ? userObject.address : null}</h3>
                      <button type="button" className={style.modalMainInfoElemBtn} onClick={() => setAddressInput((val) => !val)}>Edit</button>
                    </div>
                    {addressInput ? <Input name="address" type="text" placeholder="Address" modal="editProfile" /> : null}
                  </li>
                  <li className={style.modalMainInfoElem}>
                    <div className={style.modalMainInfoElemText}>
                      <h3 className={style.modalMainInfoElemTitle}>E-mail: {userObject.email}</h3>
                      <button type="button" className={style.modalMainInfoElemBtn} onClick={() => setEmailInput((val) => !val)}>Edit</button>
                    </div>
                    {emailInput ? <Input name="email" type="text" placeholder="E-mail" modal="editProfile" /> : null}
                  </li>
                  <li className={style.modalMainInfoElem}>
                    <div className={style.modalMainInfoElemText}>
                      <h3 className={style.modalMainInfoElemTitle}>Username: {userObject.username}</h3>
                      <button type="button" className={style.modalMainInfoElemBtn} onClick={() => setUsernameInput((val) => !val)}>Edit</button>
                    </div>
                    {usernameInput ? <Input name="username" type="text" placeholder="Username" modal="editProfile" /> : null}
                  </li>
                </ul>
                <button className={style.modalMainSaveProfileBtn} disabled={!isValid} type="submit">Save changes</button>
              </div>
            </div>
          </Form>
        </div>)}
    </Formik>
  );
};
export default ModalEditProfile;