import { Route, Routes } from "react-router-dom";
import HomePage from "./Components/HomePage";
import StatusCard from "./Components/StatusCard/StatusCard";
import StatusViewer from "./Components/StatusCard/StatusViewer";
import Signin from "./Components/Register/Signin";
import Signup from "./Components/Register/Signup";
import Authenticate from "./Components/Register/Authenticate";
function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<HomePage/>}> </Route>
        <Route path="/status" element={<StatusCard/>}> </Route>
        <Route path="/status/:userId" element={<StatusViewer/>}> </Route>
        <Route path="/signin" element={<Signin/>}> </Route>
        <Route path="/signup" element={<Signup/>}> </Route>
        <Route path="/authenticate" element={<Authenticate/>}> </Route>
      </Routes>
    </div>
  );
}

export default App;
