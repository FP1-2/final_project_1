import RegistrationForm from "./components/RegistrationForm/RegistrationForm"
import {Route,Routes} from "react-router-dom";

function App() {
  return (
    <Routes>
      <Route path='/registration' element={<RegistrationForm/>}/>
        <Route path='/' element={
            <div style={{border: "1px solid #ccc", padding: "20px", width: "200px"}}>
                <h2>Тест</h2>
                <p>Тестуємо головний роут</p>
            </div>
        } />
    </Routes>
  );
}

export default App;
