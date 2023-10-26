import React from 'react';
import {Outlet} from "react-router";

function MessagesPage() {
  return (
      <div >
        MessagePage
        <Outlet />
      </div>
  );
}

export default MessagesPage;