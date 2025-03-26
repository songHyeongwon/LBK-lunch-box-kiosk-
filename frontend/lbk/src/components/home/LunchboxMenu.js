import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Grid, Container, Box } from "@mui/material";
import MenuItem from "./MenuItem";
import MenuBar from "./MenuBar";
import SideBar from "./SideBar";
import TopBar from "./TopBar";
import Cart from "./Cart";
import CustomModal from "../common/CustomModal";
import Api from "../../hooks/api";

const LunchboxMenu = () => {
  const [cartItems, setCartItems] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalAction, setModalAction] = useState("");
  const [selectedBrand, setSelectedBrand] = useState(null);
  const [selectedMenu, setSelectedMenu] = useState(null);
  const [menuItems, setMenuItems] = useState([]);
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email;

  useEffect(() => {
    if (email === "" || email === null || email === undefined) {
      navigate("/");
      return;
    }
  });

  useEffect(() => {
    getMenuItemList();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedMenu]);

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

  const handleSelectBrand = (brandId) => {
    setSelectedBrand(brandId);
    console.log("Selected Brand:", brandId);
  };

  const handleSelectMenu = (menuId) => {
    setSelectedMenu(menuId);
    console.log("Selected Menu:", menuId);
  };

  const getMenuItemList = async () => {
    try {
      if (selectedBrand === null || selectedMenu === null) return;

      const response = await Api.get(
        `/api/menu/${selectedBrand}/${selectedMenu}?page=1&size=100`
      );
      const data = response.menus;
      setMenuItems(data);
    } catch (error) {
      console.error("Error getting category list:", error);
    }
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
          <SideBar onSelectBrand={handleSelectBrand} />
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
              <MenuBar
                brandId={selectedBrand}
                onSelectMenu={handleSelectMenu}
              />
              <Grid container spacing={5}>
                {menuItems.map((item, index) => (
                  <Grid item xs={12} sm={6} md={3} key={index}>
                    <MenuItem
                      {...item.menu}
                      onAddToCart={() => handleAddToCart(item.menu)}
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
