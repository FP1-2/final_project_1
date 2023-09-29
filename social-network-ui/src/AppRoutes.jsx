import React from 'react';
import {Route, Routes} from 'react-router-dom';
import FavoritsPage from './pages/FavoritsPage/FavoritsPage';
 import MessagesPage from './pages/MessagesPage/MessagesPage';
 import NotificationsPage from './pages/NotificationsPage/NotificationsPage';
 import PostsPage from './pages/PostsPage/PostsPage';
 import ProfilePage from './pages/ProfilePage/ProfilePage';

function AppRoutes() {
  return (
    <Routes>
        <Route path="/" element={<PostsPage/>} />
        <Route path="/messages" element={<MessagesPage/>} />
        <Route path="/favorits" element={<FavoritsPage/>}/>
        <Route path="/notifications" element={<NotificationsPage/>}/>
        <Route path="/profile" element={<ProfilePage/>} />
      </Routes>
  )
}

export default AppRoutes