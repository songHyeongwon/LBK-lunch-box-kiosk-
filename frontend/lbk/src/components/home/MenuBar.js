import React, { useState, useEffect } from "react";
import { AppBar, Toolbar, Button, Container, Box, Paper } from "@mui/material";
import Api from "../../hooks/api";

const MenuBar = ({ brandId, onSelectMenu }) => {
  const [menuItems, setMenuItems] = useState([]);
  const [selectedMenu, setSelectedMenu] = useState(null);
  const [hoveredMenu, setHoveredMenu] = useState(null);

  useEffect(() => {
    if (brandId) {
      getMenuList(brandId);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [brandId]);

  const getMenuList = async (brandId) => {
    try {
      const response = await Api.get(`/api/category?brand_id=${brandId}`);
      const data = response.categories;
      setMenuItems(data);

      if (data.length > 0) {
        setSelectedMenu(data[0].id);
        if (data[0].childs.length > 0) onSelectMenu(data[0].childs[0].id);
      }
    } catch (error) {
      console.error("Error getting category list:", error);
    }
  };

  const handleItemClick = (mainId, subId) => {
    setSelectedMenu(mainId);
    if (!(subId === null || subId === undefined || subId === "")) {
      onSelectMenu(subId);
    }
  };

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
              key={item.id}
              onMouseEnter={() => setHoveredMenu(item.id)}
              onMouseLeave={() => setHoveredMenu(null)}
              sx={{ position: "relative" }}
            >
              {/* 상위 메뉴 버튼 */}
              <Button
                disableRipple
                sx={{
                  cursor: "default", // 손가락 커서 없애기
                  pointerEvents: "none", // 클릭 이벤트 제거
                  color: selectedMenu === item.id ? "#000000" : "#757575",
                  fontSize: "16px",
                  fontWeight: selectedMenu === item.id ? "bold" : "normal",
                  backgroundColor: "transparent", // 클릭 배경 제거
                  "&:hover": {
                    color: selectedMenu === item.id ? "#000000" : "#757575", // hover 시 변화 없음
                    backgroundColor: "transparent",
                  },
                }}
              >
                {item.name}
              </Button>

              {/* 하위 메뉴 표시 */}
              {hoveredMenu === item.id && item.childs.length > 0 && (
                <Paper
                  sx={{
                    position: "absolute",
                    top: "40px",
                    left: 0,
                    minWidth: "150px",
                    backgroundColor: "white",
                    boxShadow: 3,
                    zIndex: 10,
                    borderRadius: "5px",
                    overflow: "hidden",
                  }}
                >
                  {item.childs.map((subItem) => (
                    <Button
                      key={subItem.id}
                      onClick={() => handleItemClick(item.id, subItem.id)}
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
                      {subItem.name}
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
