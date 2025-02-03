import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { CssBaseline, ThemeProvider } from "@mui/material";
import theme from "./theme";
import Login from "./components/login/Login"; // Login 컴포넌트 경로
import LunchboxMenu from "./components/home/LunchboxMenu"; // LunchboxMenu 컴포넌트 경로

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Routes>
          {/* 로그인 화면 */}
          <Route path="/" element={<Login />} />
          {/* LunchboxMenu 화면 */}
          <Route path="/home" element={<LunchboxMenu />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
