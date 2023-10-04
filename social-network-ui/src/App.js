import RegistrationForm from "./components/RegistrationForm/RegistrationForm"
import { Route, Routes } from "react-router-dom";
import ConfirmRegistration from "./components/ConfirmRegistration/ConfirmRegistration";
import Messenger from "./pages/Messanger/Messenger";


function App() {
  return (
    <Routes>
      <Route path='/registration' element={<RegistrationForm />} />
      <Route path='/' element={
        <div style={{ border: "1px solid #ccc", padding: "20px", width: "200px" }}>
          <h2>Тест</h2>
          <p>Тестуємо головний роут</p>
        </div>
      } />
      <Route path='/registration/confirm' element={<ConfirmRegistration/>}/>
      <Route path='/messages' element={<Messenger/>}/>
    </Routes>
  );
}

export default App;
