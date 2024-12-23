import React from "react";
import { AppBar, Toolbar, Typography } from "@mui/material";

const Topbar = () => {
  return (
    <AppBar
      position="fixed"
      sx={{
        zIndex: (theme) => theme.zIndex.drawer + 1,
        backgroundColor: "#0747a6",
        boxShadow: "none",
      }}
    >
      <Toolbar>
        <Typography
          variant="h6"
          sx={{
            color: "#ffffff",
            fontWeight: "bold",
            letterSpacing: 1,
          }}
        >
          LBK
        </Typography>
      </Toolbar>
    </AppBar>
  );
};

export default Topbar;
