import React, { useState } from "react";
import { AppBar, Toolbar, Button, Container, Box, Paper } from "@mui/material";

const MenuBar = () => {
  const [selectedItem, setSelectedItem] = useState("도시락몰");
  const [hoveredItem, setHoveredItem] = useState(null);

  // 메뉴 항목 + 하위 카테고리 추가
  const menuItems = [
    { name: "도시락몰", subItems: ["서브1", "서브2", "서브3", "서브4"] },
    { name: "오늘의특가", subItems: [] },
    { name: "든든도시락", subItems: ["고기 도시락", "채식 도시락"] },
    { name: "참치 덮밥", subItems: [] },
    { name: "집밥 국밥상", subItems: ["한식", "양식", "일식"] },
    { name: "컵밥 도시락", subItems: ["소고기 컵밥", "닭고기 컵밥"] },
    { name: "제계 도시락", subItems: [] },
    { name: "프리미엄 도시락", subItems: ["특선 도시락", "고급 도시락"] },
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
        position: "relative",
      }}
    >
      <Container>
        <Toolbar sx={{ justifyContent: "center", gap: 4, flexWrap: "wrap" }}>
          {menuItems.map((item) => (
            <Box
              key={item.name}
              onMouseEnter={() => setHoveredItem(item.name)}
              onMouseLeave={() => setHoveredItem(null)}
              sx={{ position: "relative" }}
            >
              <Button
                onClick={() => setSelectedItem(item.name)}
                sx={{
                  color: selectedItem === item.name ? "#000000" : "#757575",
                  fontSize: "16px",
                  fontWeight: selectedItem === item.name ? "bold" : "normal",
                  "&:hover": { color: "#000000" },
                }}
              >
                {item.name}
              </Button>

              {/* 하위 메뉴 표시 */}
              {hoveredItem === item.name && item.subItems.length > 0 && (
                <Paper
                  sx={{
                    position: "absolute",
                    top: "40px",
                    left: 0,
                    minWidth: "120px",
                    backgroundColor: "white",
                    boxShadow: 3,
                    zIndex: 10,
                    borderRadius: "5px",
                    overflow: "hidden",
                  }}
                >
                  {item.subItems.map((subItem) => (
                    <Button
                      key={subItem}
                      sx={{
                        display: "block",
                        width: "100%",
                        textAlign: "left",
                        padding: "8px 16px",
                        color: "#757575",
                        fontSize: "14px",
                        "&:hover": {
                          backgroundColor: "#f5f5f5",
                          color: "#000",
                        },
                      }}
                    >
                      {subItem}
                    </Button>
                  ))}
                </Paper>
              )}
            </Box>
          ))}
        </Toolbar>
      </Container>
    </AppBar>
  );
};

export default MenuBar;
