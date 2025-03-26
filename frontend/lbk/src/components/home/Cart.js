import React, { useState } from "react";
import {
  Paper,
  Box,
  Typography,
  Button,
  List,
  ListItem,
  IconButton,
  Stack,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";
import CustomModal from "../common/CustomModal";

const Cart = ({ cartItems, onRemoveItem, onOrder, onUpdateQuantity }) => {
  const [modalOpen, setModalOpen] = useState(false);
  const [modalAction, setModalAction] = useState(""); // "cancel" 또는 "order"

  const handleModalOpen = (actionType) => {
    setModalAction(actionType);
    setModalOpen(true);
  };

  const handleConfirm = (status) => {
    if (status) {
      if (modalAction === "cancel") {
        onRemoveItem();
      } else if (modalAction === "order") {
        onOrder();
      }
    }
    setModalOpen(false); // 모달 닫기
  };

  const totalAmount = cartItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  if (cartItems.length === 0) return null;

  return (
    <>
      <Paper
        sx={{
          position: "fixed",
          top: 64, // Topbar 높이만큼 여백
          right: 0,
          width: "300px",
          height: "calc(100vh - 64px)", // Topbar 높이를 제외한 전체 높이
          zIndex: 1000,
          backgroundColor: "#f5f5f5",
          display: "flex",
          flexDirection: "column",
        }}
      >
        <Box
          sx={{
            p: 2,
            borderBottom: "1px solid rgba(0, 0, 0, 0.12)",
            backgroundColor: "white",
          }}
        >
          <Typography variant="h6" component="h2" fontWeight="bold">
            장바구니
          </Typography>
        </Box>

        <Box
          sx={{
            flexGrow: 1,
            overflowY: "auto",
            backgroundColor: "white",
          }}
        >
          <List disablePadding>
            {cartItems.map((item, index) => (
              <ListItem
                key={index}
                sx={{
                  py: 1,
                  borderBottom: "1px solid rgba(0, 0, 0, 0.06)",
                  flexDirection: "column",
                  alignItems: "stretch",
                }}
              >
                <Typography variant="body2" noWrap>
                  {item.name}
                </Typography>

                <Stack
                  direction="row"
                  spacing={1}
                  alignItems="center"
                  justifyContent="space-between"
                  sx={{ mt: 1 }}
                >
                  <Box sx={{ display: "flex", alignItems: "center" }}>
                    <IconButton
                      size="small"
                      onClick={() => onUpdateQuantity(index, -1)}
                      disabled={item.quantity <= 1}
                    >
                      <RemoveCircleOutlineIcon fontSize="small" />
                    </IconButton>
                    <Typography
                      sx={{ mx: 1, minWidth: 20, textAlign: "center" }}
                    >
                      {item.quantity}
                    </Typography>
                    <IconButton
                      size="small"
                      onClick={() => onUpdateQuantity(index, 1)}
                    >
                      <AddCircleOutlineIcon fontSize="small" />
                    </IconButton>
                  </Box>

                  <Box sx={{ display: "flex", alignItems: "center" }}>
                    <Typography variant="body2" sx={{ mr: 1 }}>
                      {(item.price * item.quantity).toLocaleString()}원
                    </Typography>
                    <IconButton
                      size="small"
                      onClick={() => onRemoveItem(index)}
                    >
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </Box>
                </Stack>
              </ListItem>
            ))}
          </List>
        </Box>

        <Box
          sx={{
            p: 2,
            borderTop: "1px solid rgba(0, 0, 0, 0.12)",
            backgroundColor: "white",
          }}
        >
          <Typography variant="subtitle1" sx={{ mb: 2, fontWeight: "bold" }}>
            총 주문금액: {totalAmount.toLocaleString()}원
          </Typography>
          <Stack direction="row" spacing={1}>
            <Button
              variant="outlined"
              fullWidth
              size="large"
              onClick={() => handleModalOpen("cancel")}
              sx={{
                borderColor: "#e0e0e0",
                color: "#757575",
                "&:hover": {
                  borderColor: "#bdbdbd",
                  backgroundColor: "#f5f5f5",
                },
              }}
            >
              취소
            </Button>
            <Button
              variant="contained"
              fullWidth
              size="large"
              onClick={() => handleModalOpen("order")}
              sx={{
                backgroundColor: "#e51937",
                "&:hover": {
                  backgroundColor: "#d4112f",
                },
              }}
            >
              주문
            </Button>
          </Stack>
        </Box>
      </Paper>

      {/* CustomModal 컴포넌트 */}
      <CustomModal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        onConfirm={handleConfirm}
        content={
          modalAction === "cancel"
            ? "주문을 취소 하시겠습니까?"
            : "주문을 진행 하시겠습니까?"
        }
      />
    </>
  );
};

export default Cart;
