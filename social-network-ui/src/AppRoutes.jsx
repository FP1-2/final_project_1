import React from 'react';
import {Route, Routes} from 'react-router-dom';
import LoginPage from "./pages/LoginPage/LoginPage";
import FavoritsPage from './pages/FavoritsPage/FavoritsPage';
import MessagesPage from './pages/MessagesPage/MessagesPage';
import NotificationsPage from './pages/NotificationsPage/NotificationsPage';
import PostsPage from './pages/PostsPage/PostsPage';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import RegistrationForm from "./components/RegistrationForm/RegistrationForm";
import ConfirmRegistration from "./components/ConfirmRegistration/ConfirmRegistration";
import ResetPassword from './pages/ChangePasswordForm/ResetPassword';
import UpdatePass from './pages/ChangePasswordForm/UpdatePassword';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage/>} />
      <Route path="/" element={<PostsPage/>} />
      <Route path="/messages" element={<MessagesPage/>} />
      <Route path="/favorites" element={<FavoritsPage/>}/>
      <Route path="/notifications" element={<NotificationsPage/>}/>
      <Route path="/profile" element={<ProfilePage/>} />
      <Route path='/registration' element={<RegistrationForm />} />
      <Route path='/registration/confirm' element={<ConfirmRegistration/>}/>
      <Route path='/reset-password' element={<ResetPassword/>}/>
      <Route path='/change_password/:token' element={<UpdatePass/>}/>
    </Routes>
  );
}

export default AppRoutes;