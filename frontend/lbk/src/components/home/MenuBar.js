import React, { useState } from "react";
import { AppBar, Toolbar, Button, Container } from "@mui/material";

const MenuBar = () => {
  const [selectedItem, setSelectedItem] = useState("도시락몰");

  const menuItems = [
    "도시락몰",
    "오늘의특가",
    "든든도시락",
    "참치 덮밥",
    "집밥 국밥상",
    "컵밥 도시락",
    "제계 도시락",
    "프리미엄 도시락",
  ];

  return (
    <AppBar
      position="static"
      color="default"
      elevation={0}
      sx={{
        backgroundColor: "white",
        borderBottom: 1,
        borderColor: "grey.200",
      }}
    >
      <Container>
        <Toolbar sx={{ justifyContent: "center", gap: 4, flexWrap: "wrap" }}>
          {menuItems.map((item) => (
            <Button
              key={item}
              onClick={() => setSelectedItem(item)}
              sx={{
                color: selectedItem === item ? "#000000" : "#757575",
                fontSize: "16px",
                fontWeight: selectedItem === item ? "bold" : "normal",
                "&:hover": { color: "#000000" },
              }}
            >
              {item}
            </Button>
          ))}
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default MenuBar;
