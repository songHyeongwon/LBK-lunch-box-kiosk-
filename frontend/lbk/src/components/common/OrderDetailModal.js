import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import {
  Modal,
  Typography,
  Button,
  Box,
  List,
  ListItem,
  Stack,
  Divider,
} from "@mui/material";
import Api from "../../hooks/api";

const OrderDetailModal = ({ open, onClose, onConfirm, email }) => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    if (open && email) {
      getOrder(email);
    }
  }, [open, email]);

  const getOrder = async (email) => {
    try {
      const response = await Api.get(`/api/order?email=${email}`);
      const data = response.order_details; // assuming the data is returned directly
      setOrders(data);
    } catch (error) {
      console.error("Error getting order list:", error);
    }
  };

  const onDeleteOrders = (orderId) => {
    console.log(orderId);
    onConfirm(orderId);
  };

  return (
    <Modal open={open} onClose={onClose} aria-labelledby="order-modal">
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          width: 400,
          bgcolor: "background.paper",
          boxShadow: 24,
          p: 3,
          borderRadius: "8px",
          maxHeight: "80vh",
          overflowY: "auto",
        }}
      >
        <Typography variant="h6" fontWeight="bold" sx={{ mb: 2 }}>
          주문 내역 확인
        </Typography>

        {orders.length === 0 ? (
          <Typography>주문 내역이 없습니다.</Typography>
        ) : (
          orders.map((orderBlock, orderIndex) => {
            const { order, menus } = orderBlock;

            // id -> quantity 매핑
            const quantityMap = {};
            order.menus.forEach((m) => {
              quantityMap[m.id] = m.quantity;
            });

            return (
              <Box key={order.id} sx={{ mb: 3 }}>
                <Typography variant="subtitle2" sx={{ mb: 1 }}>
                  주문일시: {new Date(order.created_at).toLocaleString()}
                </Typography>
                <List sx={{ width: "100%" }}>
                  {menus.map((menuData, idx) => {
                    const menu = menuData.menu;
                    const quantity = quantityMap[menu.id] || 1;
                    return (
                      <ListItem
                        key={menu.id}
                        sx={{
                          display: "flex",
                          justifyContent: "space-between",
                          py: 0.5,
                        }}
                      >
                        <Typography variant="body2">
                          {menu.name} x {quantity}
                        </Typography>
                        <Typography variant="body2">
                          {(menu.price * quantity).toLocaleString()}원
                        </Typography>
                      </ListItem>
                    );
                  })}
                </List>
                <Typography
                  variant="body1"
                  fontWeight="bold"
                  sx={{ mt: 1, textAlign: "right" }}
                >
                  총 주문금액: {order.total_price.toLocaleString()}원
                </Typography>
                <Button
                  variant="text"
                  color="error"
                  size="small"
                  onClick={() => onDeleteOrders(order.id)}
                  sx={{ mt: 1 }}
                >
                  이 주문 삭제
                </Button>
                {orderIndex < orders.length - 1 && <Divider sx={{ my: 2 }} />}
              </Box>
            );
          })
        )}

        <Stack
          direction="row"
          spacing={2}
          sx={{ mt: 2, justifyContent: "center" }}
        >
          <Button variant="contained" onClick={onClose}>
            닫기
          </Button>
        </Stack>
      </Box>
    </Modal>
  );
};

OrderDetailModal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onConfirm: PropTypes.func.isRequired,
  email: PropTypes.string.isRequired,
};

export default OrderDetailModal;
