import React from "react";
import PropTypes from "prop-types";
import {
  Modal,
  Typography,
  Button,
  Box,
  List,
  ListItem,
  Stack,
} from "@mui/material";

const OrderDetailModal = ({ open, onClose, onConfirm, email }) => {
  const orderItems = [
    {
      title: "[10일, 수] 수작도시락 구매상품",
      price: 9000,
      quantity: 3,
    },
    {
      title: "든든도시락(언양불고기덮밥)",
      price: 8000,
      quantity: 3,
    },
  ];

  const totalAmount = orderItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  const onDeleteOrders = () => {
    // 주문 처리 로직 구현
    console.log("주문 내역 삭제");
    onConfirm();
  };

  return (
    <Modal open={open} onClose={onClose} aria-labelledby="order-modal">
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          width: 360,
          bgcolor: "background.paper",
          boxShadow: 24,
          p: 3,
          borderRadius: "8px",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Typography variant="h6" fontWeight="bold" sx={{ mb: 2 }}>
          주문 내역 확인
        </Typography>

        <List sx={{ width: "100%", maxHeight: 200, overflowY: "auto" }}>
          {orderItems.map((item, index) => (
            <ListItem
              key={index}
              sx={{ display: "flex", justifyContent: "space-between" }}
            >
              <Typography variant="body2">
                {item.title} x {item.quantity}
              </Typography>
              <Typography variant="body2">
                {(item.price * item.quantity).toLocaleString()}원
              </Typography>
            </ListItem>
          ))}
        </List>

        <Typography variant="subtitle1" fontWeight="bold" sx={{ mt: 2 }}>
          총 주문금액: {totalAmount.toLocaleString()}원
        </Typography>

        {/* 주문 내역 삭제 및 닫기 버튼 */}
        <Stack direction="row" spacing={2} sx={{ mt: 2 }}>
          <Button variant="outlined" color="error" onClick={onDeleteOrders}>
            주문 내역 삭제
          </Button>
          <Button variant="contained" onClick={onClose}>
            닫기
          </Button>
        </Stack>
      </Box>
    </Modal>
  );
};

// PropTypes 검증
OrderDetailModal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  orderItems: PropTypes.arrayOf(
    PropTypes.shape({
      title: PropTypes.string.isRequired,
      quantity: PropTypes.number.isRequired,
      price: PropTypes.number.isRequired,
    })
  ).isRequired,
  onDeleteOrders: PropTypes.func.isRequired, // 주문 내역 삭제 함수
};

export default OrderDetailModal;
