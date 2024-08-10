import { BASE_API_URL } from "../../Config/api";
import {
  REGISTER,
  LOGIN,
  REQ_USER,
  SEARCH_USER,
  LOGOUT,
} from "./ActionType.js";
export const register = (data) => async (dispatch) => {
  try {
    const res = await fetch(`${BASE_API_URL}/api/auth/signup`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });
    const resData = await res.json();
    if (resData.result.access_token)
      localStorage.setItem("token", resData.result.access_token);
    console.log(resData);
    dispatch({
      type: REGISTER,
      payload: resData,
    });
  } catch (error) {
    console.log(error);
  }
};

export const login = (data) => async (dispatch) => {
  try {
    const res = await fetch(`${BASE_API_URL}/api/auth/signin`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    const resData = await res.json();

    if (resData.result && resData.result.access_token) {
      localStorage.setItem("token", resData.result.access_token);
    
    }

    dispatch({
      type: LOGIN,
      payload: resData,
    });
  } catch (error) {
    console.log(error); // Sửa lỗi: error.error có thể không tồn tại
  }
};


export const currentUser = (token) => async (dispatch) => {
  try {
    const res = await fetch(`${BASE_API_URL}/api/users/profile`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    const resData = await res.json();
    console.log("get user")
    dispatch({
      type: REQ_USER,
      payload: resData,
    });
  } catch (error) {
    console.log(error);
  }
};

export const searchUser = (data) => async (dispatch) => {
  try {
    const res = await fetch(
      `${BASE_API_URL}/api/users/search?name=${data.keyword}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${data.token}`,
        },
      }
    );
    const resData = await res.json();
    console.log("search user success");
    
    dispatch({
      type: SEARCH_USER,
      payload: resData,
    });
  } catch (error) {
    console.log(error);
  }
};

export const updateUser = (dataa) => async (dispatch) => {
  try {
    const res = await fetch(`${BASE_API_URL}/api/users/update`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${dataa.token}`,
      },
      body: JSON.stringify(dataa.data),
    });
    const resData = await res.json();
    console.log("update user success");
    dispatch({
      type: "UPDATE_USER",
      payload: resData,
    });
  } catch (error) {
    console.log(error);
  }
};

export const logoutAction = () => async (dispatch) => {
  localStorage.removeItem("token");
  dispatch({ type: LOGOUT, payload: null });
  dispatch({ type: REQ_USER, payload: null });
};

export const loginWithGoogle = (authCode) => async (dispatch) => {
  console.log("authCode:", authCode);
  try{
    await fetch(
      `http://localhost:5454/api/auth/outbound/authentication?code=${authCode}`,
      
      {
        method: "POST",
      }
      
    )
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        console.log("Continue with google", data);
        if (data.result && data.result.access_token) {
          localStorage.setItem("token", data.result.access_token);
        }
        dispatch({
          type: LOGIN,
          payload: data,
        });
      });
  }catch(error){
    console.log(error)
  }
}
