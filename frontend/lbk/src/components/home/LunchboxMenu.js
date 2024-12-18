import React from "react";
import { Grid, Container, Box } from "@mui/material";
import MenuItem from "./MenuItem";
import MenuBar from "./MenuBar";
import lunchbox1 from "../../assets/images/test.jpg";

const LunchboxMenu = () => {
  const menuItems = [
    {
      image: lunchbox1,
      title: "[10일, 수] 수작도시락 구매상품",
      price: 9000,
      isNew: false,
    },
    {
      image: lunchbox1,
      title: "든든도시락(언양불고기덮밥)",
      price: 8000,
      isNew: true,
    },
    {
      image: lunchbox1,
      title: "든든도시락(카츠&된장)",
      price: 8000,
      isNew: true,
    },
    {
      image: lunchbox1,
      title: "든든도시락(치킨너겟&쌀밥)",
      price: 9000,
      isNew: true,
    },
  ];

  return (
    <Box sx={{ backgroundColor: "#f5f5f5", minHeight: "100vh" }}>
      <MenuBar />
      <Container sx={{ py: 4 }}>
        <Grid container spacing={3}>
          {menuItems.map((item, index) => (
            <Grid item xs={12} sm={6} md={3} key={index}>
              <MenuItem {...item} />
            </Grid>
          ))}
        </Grid>
      </Container>
    </Box>
  );
};

export default LunchboxMenu;
