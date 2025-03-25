import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Grid, Container, Box } from "@mui/material";
import MenuItem from "./MenuItem";
import MenuBar from "./MenuBar";
import SideBar from "./SideBar";
import TopBar from "./TopBar";
import Cart from "./Cart";
import lunchbox1 from "../../assets/images/test.jpg";
import CustomModal from "../common/CustomModal";

const LunchboxMenu = () => {
  const [cartItems, setCartItems] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalAction, setModalAction] = useState("");
  const [selectedItem, setSelectedItem] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();
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

  useEffect(() => {
    if (email === "" || email === null || email === undefined) {
      navigate("/");
      return;
    }
  });

  const handleModalOpen = (actionType) => {
    setModalAction(actionType);
    setModalOpen(true);
  };

  const handleConfirm = (status) => {
    if (status) {
      if (modalAction === "success") {
        //주문 성공
        setCartItems([]);
      } else if (modalAction === "failure") {
        //주문 실패
      }
    }
    setModalOpen(false); // 모달 닫기
  };

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
    handleModalOpen("success");
  };

  const handleSelectItem = (itemId) => {
    setSelectedItem(itemId);
    console.log("Selected Item in Parent:", itemId);
  };

  return (
    <>
      <Box
        sx={{
          display: "flex",
          height: "100vh",
          overflow: "auto",
          flexDirection: "column",
        }}
      >
        <TopBar email={email} />
        <Box sx={{ display: "flex", flexGrow: 1, overflow: "auto" }}>
          <SideBar onSelect={handleSelectItem} />
          <Box
            component="main"
            sx={{
              flexGrow: 1,
              p: 3,
              mt: 8,
              overflowY: "auto", // 스크롤 가능하게 수정
              pr: cartItems.length > 0 ? "300px" : 3, // 장바구니 표시 영역 고려
              height: "calc(100vh - 64px)", // TopBar 높이 제외
            }}
          >
            <Container>
              <MenuBar />
              <Grid container spacing={5}>
                {menuItems.map((item, index) => (
                  <Grid item xs={12} sm={6} md={3} key={index}>
                    <MenuItem
                      {...item}
                      onAddToCart={() => handleAddToCart(item)}
                    />
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
      </Box>

      {/* CustomModal 컴포넌트 */}
      <CustomModal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        onConfirm={handleConfirm}
        content={
          modalAction === "success"
            ? "주문되었습니다."
            : "주문에 실패하였습니다."
        }
        hideCancel={true}
      />
    </>
  );
};

export default LunchboxMenu;
