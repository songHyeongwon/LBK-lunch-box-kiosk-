import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import { Grid, Container, Box } from "@mui/material";
import MenuItem from "./MenuItem";
import Menubar from "./MenuBar";
import Sidebar from "./SideBar";
import Topbar from "./TopBar";
import Cart from "./Cart";
import lunchbox1 from "../../assets/images/test.jpg";

const LunchboxMenu = () => {
  const [cartItems, setCartItems] = useState([]);
  const location = useLocation();
  const email = location.state?.email;

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

  const handleAddToCart = (item) => {
    setCartItems((prevItems) => {
      const existingItemIndex = prevItems.findIndex(
        (cartItem) => cartItem.title === item.title
      );

      if (existingItemIndex >= 0) {
        // 이미 장바구니에 있는 경우 수량만 증가
        const newItems = [...prevItems];
        newItems[existingItemIndex].quantity += 1;
        return newItems;
      } else {
        // 새로운 아이템 추가
        return [...prevItems, { ...item, quantity: 1 }];
      }
    });
  };

  const handleRemoveItem = (index) => {
    if (index === undefined) {
      // 취소 버튼 클릭 시 장바구니 전체 비우기
      setCartItems([]);
    } else {
      setCartItems((prevItems) => prevItems.filter((_, i) => i !== index));
    }
  };

  const handleUpdateQuantity = (index, change) => {
    setCartItems((prevItems) => {
      const newItems = [...prevItems];
      const newQuantity = newItems[index].quantity + change;

      if (newQuantity < 1) return prevItems;

      newItems[index].quantity = newQuantity;
      return newItems;
    });
  };

  const handleOrder = () => {
    // 주문 처리 로직 구현
    console.log(email);
    console.log("주문 처리:", cartItems);
  };

  return (
    <Box sx={{ display: "flex", height: "100vh", overflow: "hidden" }}>
      <Topbar />
      <Sidebar />
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 3,
          mt: 8,
          height: "100%",
          overflow: "auto",
          pr: cartItems.length > 0 ? "300px" : 3, // 장바구니가 있을 때 오른쪽 여백 추가
        }}
      >
        <Container>
          <Menubar />
          <Grid container spacing={5}>
            {menuItems.map((item, index) => (
              <Grid item xs={12} sm={6} md={3} key={index}>
                <MenuItem {...item} onAddToCart={() => handleAddToCart(item)} />
              </Grid>
            ))}
          </Grid>
        </Container>
        <Cart
          cartItems={cartItems}
          onRemoveItem={handleRemoveItem}
          onOrder={handleOrder}
          onUpdateQuantity={handleUpdateQuantity}
        />
      </Box>
    </Box>
  );
};

export default LunchboxMenu;
