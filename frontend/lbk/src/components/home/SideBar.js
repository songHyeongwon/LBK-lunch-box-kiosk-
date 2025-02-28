import React, { useState } from "react";
import {
  Box,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Typography,
} from "@mui/material";

const SideBar = () => {
  const [selectedItem, setSelectedItem] = useState("본도시락");

  const menuItems = ["본도시락", "추가예정..."];

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: 240,
        flexShrink: 0,
        "& .MuiDrawer-paper": {
          width: 240,
          boxSizing: "border-box",
          backgroundColor: "#424242",
          borderRight: "1px solid #e0e0e0",
        },
      }}
    >
      <Box sx={{ overflow: "auto", mt: 8 }}>
        <List>
          {menuItems.map((text) => (
            <ListItem key={text} disablePadding>
              <ListItemButton
                onClick={() => setSelectedItem(text)}
                sx={{
                  py: 2,
                  backgroundColor:
                    selectedItem === text
                      ? "rgba(255, 255, 255, 0.08)"
                      : "transparent",
                  "&:hover": {
                    backgroundColor: "rgba(255, 255, 255, 0.12)",
                  },
                }}
              >
                <ListItemText
                  primary={
                    <Typography
                      sx={{
                        fontWeight: selectedItem === text ? "bold" : "normal",
                        color: selectedItem === text ? "#ffffff" : "#9e9e9e",
                      }}
                    >
                      {text}
                    </Typography>
                  }
                />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Box>
    </Drawer>
  );
};

export default SideBar;
