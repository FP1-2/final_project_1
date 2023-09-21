import RegistrationForm from "./components/RegistrationForm/RegistrationForm"
import {Route,Routes} from "react-router-dom";

function App() {
  return (
    <Routes>
      <Route path='registration' element={<RegistrationForm/>}/>
    </Routes>
  );
}

export default App;
