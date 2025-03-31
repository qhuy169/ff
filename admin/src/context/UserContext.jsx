import axiosInstance from "./../api";
import WithAxios from "../helpers/WithAxios";
import React, { createContext, useContext, useEffect, useState } from "react";
import authService from "../services/auth.service";

const UserContext = createContext();

const UserProvider = ({ children }) => {
  const [userData, setUserData] = useState(null);
  const [authData, setAuthData] = useState({
    token: "",
  });
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    if (isLoggedIn) {
      //authService.getCurrentUser().then((res) => setUserData(res?.data));
    }
  }, [isLoggedIn]);

  useEffect(() => {
    if (localStorage.getItem("token")) {
      setIsLoggedIn(true);
      setAuthData(JSON.parse(localStorage.getItem("token")));
    } else {
      setIsLoggedIn(false);
    }
  }, []);

  const updateUserData = async ({
    fullname,
    email,
    username,
    address,
    city,
    state,
    country,
  }) => {
    const res = await axiosInstance.put(`/users/${userData.user_id}`, {
      fullname,
      email,
      username,
      address,
      city,
      state,
      country,
    });
    setUserData(res.data);
  };

  const setUserInfo = (data) => {
    const { accessToken } = data;
    //const { user, token } = data;
    setIsLoggedIn(true);
    //setUserData(user);
    setAuthData({
      accessToken
    });
    localStorage.setItem("token", JSON.stringify(accessToken));
  };

  const logout = () => {
    setUserData(null);
    setAuthData(null);
    setIsLoggedIn(false);
    authService.logout();
  };

  return (
    <UserContext.Provider
      value={{
        userData,
        setUserData,
        setUserState: (data) => setUserInfo(data),
        logout,
        isLoggedIn,
        setIsLoggedIn,
        authData,
        setAuthData,
        updateUserData,
      }}
    >
      <WithAxios>{children}</WithAxios>
    </UserContext.Provider>
  );
};

const useUser = () => {
  const context = useContext(UserContext);

  if (context === undefined) {
    throw new Error("useUser must be used within UserProvider");
  }
  return context;
};

export { UserProvider, useUser };
