import React, { useState, useEffect } from "react";
import {
  Box,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Typography,
} from "@mui/material";
import Api from "../../hooks/api";

const SideBar = ({ onSelect }) => {
  const [menuItems, setMenuItems] = useState([]);
  const [selectedItem, setSelectedItem] = useState(null);

  useEffect(() => {
    getBrandList();
  }, []);

  const getBrandList = async () => {
    try {
      const response = await Api.get("/api/brand");
      const data = await response.brands;
      setMenuItems(data);
      if (data.length > 0) {
        setSelectedItem(data[0].id);
        onSelect(data[0].id); // 초기 상태 전달
      }
    } catch (error) {
      console.error("Error getting brandList:", error);
    }
  };

  const handleItemClick = (id) => {
    setSelectedItem(id);
    onSelect(id);
  };

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
          {menuItems.map((item) => (
            <ListItem key={item.id} disablePadding>
              <ListItemButton
                onClick={() => handleItemClick(item.id)}
                sx={{
                  py: 2,
                  backgroundColor:
                    selectedItem === item.id
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
                        fontWeight:
                          selectedItem === item.id ? "bold" : "normal",
                        color: selectedItem === item.id ? "#ffffff" : "#9e9e9e",
                      }}
                    >
                      {item.name}
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
