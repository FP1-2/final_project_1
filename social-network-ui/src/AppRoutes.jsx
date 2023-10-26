import React from 'react';
import { Route, Routes } from 'react-router-dom';
import LoginPage from "./pages/LoginPage/LoginPage";
import FavoritsPage from './pages/FavoritsPage/FavoritsPage';
import MessagesPage from './pages/MessagesPage/MessagesPage';
import NotificationsPage from './pages/NotificationsPage/NotificationsPage';
import PostsPage from './pages/PostsPage/PostsPage';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import RegistrationForm from "./components/RegistrationForm/RegistrationForm";
import ConfirmRegistration from "./components/ConfirmRegistration/ConfirmRegistration";
import PostsPageProfile from "./components/PostsPageProfile/PostsPageProfile";
import FriendPageProfile from "./components/FriendPageProfile/FriendPageProfile";
import LikedPageProfile from "./components/LikedPageProfile/LikedPageProfile";
import PostPage from './pages/PostPage/PostPage';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/" element={<PostsPage />} />
      <Route path="/messages" element={<MessagesPage />} />
      <Route path="/favorites" element={<FavoritsPage />} />
      <Route path="/notifications" element={<NotificationsPage />} />
      <Route path='/profile/*' element={<ProfilePage/>}>
        <Route path='' element={<PostsPageProfile />} />
        <Route path='friends' element={<FriendPageProfile />} />
        <Route path='liked' element={<LikedPageProfile />} />
      </Route>
      <Route path='/post' element={<PostPage/>} />
      <Route path='/registration' element={<RegistrationForm />} />
      <Route path='/registration/confirm' element={<ConfirmRegistration />} />
    </Routes>
  );
}

export default AppRoutes;