import React from "react";
import LunchboxMenu from "./components/home/LunchboxMenu";
import { CssBaseline, ThemeProvider } from "@mui/material";
import theme from "./theme";

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <LunchboxMenu />
    </ThemeProvider>
  );
}

export default App;
