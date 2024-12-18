import React from "react";
import { AppBar, Toolbar, Button, Container } from "@mui/material";

const MenuBar = () => (
  <AppBar
    position="static"
    color="default"
    elevation={0}
    sx={{ backgroundColor: "white", borderBottom: 1, borderColor: "grey.200" }}
  >
    <Container>
      <Toolbar sx={{ justifyContent: "center", gap: 2, flexWrap: "wrap" }}>
        {[
          "도시락몰",
          "오늘의특가",
          "든든도시락",
          "참치 덮밥",
          "집밥 국밥상",
          "컵밥 도시락",
          "제계 도시락",
          "프리미엄 도시락",
        ].map((item) => (
          <Button
            key={item}
            sx={{
              color: "text.primary",
              "&:hover": { color: "primary.main" },
            }}
          >
            {item}
          </Button>
        ))}
      </Toolbar>
    </Container>
  </AppBar>
);

export default MenuBar;
