import React from "react";
import {
  Paper,
  Box,
  Typography,
  Button,
  Divider,
  List,
  ListItem,
  IconButton,
  Stack,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";

const CartFooter = ({ cartItems, onRemoveItem, onOrder, onUpdateQuantity }) => {
  const totalAmount = cartItems.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  if (cartItems.length === 0) return null;

  return (
    <Paper
      sx={{
        position: "fixed",
        bottom: 0,
        left: 0,
        right: 0,
        zIndex: 1000,
        backgroundColor: "white",
        boxShadow: 3,
        maxHeight: "40vh",
        display: "flex",
        flexDirection: "column",
      }}
    >
      <Box sx={{ overflowY: "auto", flexGrow: 1 }}>
        <List>
          {cartItems.map((item, index) => (
            <ListItem
              key={index}
              sx={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                py: 1,
              }}
            >
              <Typography sx={{ flex: 2 }} variant="body1">
                {item.title}
              </Typography>

              <Stack
                direction="row"
                spacing={1}
                alignItems="center"
                sx={{ flex: 1, justifyContent: "flex-end" }}
              >
                <IconButton
                  size="small"
                  onClick={() => onUpdateQuantity(index, -1)}
                  disabled={item.quantity <= 1}
                >
                  <RemoveCircleOutlineIcon />
                </IconButton>
                <Typography sx={{ minWidth: 30, textAlign: "center" }}>
                  {item.quantity}
                </Typography>
                <IconButton
                  size="small"
                  onClick={() => onUpdateQuantity(index, 1)}
                >
                  <AddCircleOutlineIcon />
                </IconButton>

                <Typography sx={{ minWidth: 100, textAlign: "right" }}>
                  {(item.price * item.quantity).toLocaleString()}원
                </Typography>

                <IconButton
                  edge="end"
                  onClick={() => onRemoveItem(index)}
                  sx={{ ml: 1 }}
                >
                  <DeleteIcon />
                </IconButton>
              </Stack>
            </ListItem>
          ))}
        </List>
      </Box>

      <Divider />
      <Box
        sx={{
          p: 2,
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <Typography variant="h6">
          총 주문금액: {totalAmount.toLocaleString()}원
        </Typography>
        <Button
          variant="contained"
          size="large"
          onClick={onOrder}
          sx={{
            backgroundColor: "#8bc34a",
            "&:hover": {
              backgroundColor: "#7cb342",
            },
            minWidth: 150,
          }}
        >
          주문하기
        </Button>
      </Box>
    </Paper>
  );
};

export default CartFooter;
