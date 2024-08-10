import React, { useState, useEffect } from "react";
import { Button, Snackbar, Alert, Divider, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { green } from "@mui/material/colors";
import { useDispatch, useSelector } from "react-redux";
import { login } from "../../Redux/Auth/Action";
import { currentUser } from "../../Redux/Auth/Action";
import { FcGoogle } from "react-icons/fc";
import { OAuthConfig } from "../../Config/Oauth2";
const Signin = () => {
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [inputData, setInputData] = useState({ email: "", password: "" });
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { auth } = useSelector((store) => store);
  const token = localStorage.getItem("token");
  const handleSubmit = (e) => {
    e.preventDefault();
    setOpenSnackbar(true);
    dispatch(login(inputData));
  };
  const handleChange = (e) => {
    const { name, value } = e.target;
    setInputData((values) => ({ ...values, [name]: value }));
  };
  const handleContinueWithGoogle = () => {
    const callbackUrl = OAuthConfig.redirectUri;
    const authUrl = OAuthConfig.authUri;
    const googleClientId = OAuthConfig.clientId;

    const targetUrl = `${authUrl}?redirect_uri=${encodeURIComponent(
      callbackUrl
    )}&response_type=code&client_id=${googleClientId}&scope=openid%20email%20profile`;

    console.log(targetUrl);

    window.location.href = targetUrl;
  };
  const handleSnackbarClose = () => {
    setOpenSnackbar(false);
  };
  useEffect(() => {
    if (token) dispatch(currentUser(token));
  }, [token]);

  useEffect(() => {
    if (auth.reqUser?.result?.full_name) {
      navigate("/");
    }
  }, [auth.reqUser]);
  return (
    <div>
      
      <div className="flex justify-center h-screen items-center">
      
        <div className="w-[30%] p-10 shadow-md bg-white">
        <Typography variant="h5" component="h1" gutterBottom>
              ViChat
            </Typography>
          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <p className="mb-2">Email</p>
              <input
                placeholder="Enter your Email"
                type="text"
                className="py-2 outline outline-green-600 w-full rounded-md border"
                onChange={handleChange}
                value={inputData.email.trim()}
                name="email"
              />
            </div>
            <div>
              <p className="mb-2">Password</p>
              <input
                placeholder="Enter your Password"
                type="password"
                className="py-2 outline outline-green-600 w-full rounded-md border"
                onChange={handleChange}
                value={inputData.password.trim()}
                name="password"
              />
            </div>
            <div>
              <Button
                type="submit"
                sx={{ bgcolor: green[700], padding: ".5rem 0rem" }}
                className="w-full bg-green-600"
                variant="contained"
              >
                Sign In
              </Button>
            </div>
          </form>
          <div>
            <Button
              type="submit"
              sx={{ bgcolor: green[700], padding: ".5rem 0rem", marginTop: 2 }}
              className="w-full bg-green-600"
              variant="contained"
              onClick={handleContinueWithGoogle}
            >
              <FcGoogle />
              Continue with Google
            </Button>
            <Divider></Divider>
          </div>

          <div className="flex space-x-3 items-center mt-5">
            <p className="m-0">Create New Account</p>
            <Button variant="text" onClick={() => navigate("/signup")}>
              signup
            </Button>
          </div>
        </div>
      </div>
      <Snackbar
        open={openSnackbar}
        autoHideDuration={6000}
        onClose={handleSnackbarClose}
      >
        <Alert
          onClose={handleSnackbarClose}
          severity="success"
          sx={{ width: "100%" }}
        >
          Login successfull!
        </Alert>
      </Snackbar>
    </div>
  );
};

export default Signin;
