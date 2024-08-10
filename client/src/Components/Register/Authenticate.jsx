import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { Box, CircularProgress, Typography } from "@mui/material";
import { useDispatch, useSelector } from 'react-redux';
import { loginWithGoogle } from "../../Redux/Auth/Action";
import { currentUser } from "../../Redux/Auth/Action";
export default function Authenticate() {
    const { auth } = useSelector((store) => store);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const token = localStorage.getItem("token");
  useEffect(() => {
    console.log(window.location.href)
    const authTokenRegex = /code=([^&]+)/;
    const isMatch = window.location.href.match(authTokenRegex);

    if (isMatch) {
      const authCode = isMatch[1];
        dispatch(loginWithGoogle(authCode));
    }
  }, []);
  useEffect(() => {
    if (token) dispatch(currentUser(token));
  }, [token]);
  useEffect(() => {
    if (auth.reqUser?.result?.full_name) {
      navigate("/");
    }
  }, [auth.reqUser]);

  return (
    <>
      <Box
        sx={{
          display: "flex",
          flexDirection : "column",
          gap: "30px",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <CircularProgress></CircularProgress>
        <Typography>Authenticating...</Typography>
      </Box>
    </>
  );
}
