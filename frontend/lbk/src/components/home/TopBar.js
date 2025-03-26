import React, { useState } from "react";
import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import OrderDetailModal from "../common/OrderDetailModal";
import CustomModal from "../common/CustomModal";
import Api from "../../hooks/api";

const TopBar = ({ email }) => {
  const [modalOpen, setModalOpen] = useState(false);
  const [confimModalOpen, setConfimModalOpen] = useState(false);
  const [resultModalOpen, setResultModalOpen] = useState(false);
  const [modalAction, setModalAction] = useState("");
  const [deleteOrderId, setDeleteOrderId] = useState(null);

  const handleModalOpen = () => {
    setModalOpen(true);
  };

  const handleConfimModalOpen = (orderId) => {
    setDeleteOrderId(orderId);
    setModalOpen(false);
    setConfimModalOpen(true);
  };

  const handleResultModalOpen = async () => {
    try {
      if (deleteOrderId === null) return;
      const data = {
        id: deleteOrderId,
        email: email,
      };
      const response = await Api.delete(`/api/order`, JSON.stringify(data));
      if (response.status === 200) {
        handleResultAction("success");
      } else {
        handleResultAction("fail");
      }
    } catch (error) {
      console.error("Error getting order list:", error);
      handleResultAction("fail");
    }
  };

  const handleResultAction = (actionType) => {
    setConfimModalOpen(false);
    setModalAction(actionType);
    setResultModalOpen(true);
  };

  return (
    <>
      <AppBar
        position="fixed"
        sx={{
          zIndex: (theme) => theme.zIndex.drawer + 1,
          backgroundColor: "#0747a6",
          boxShadow: "none",
        }}
      >
        <Toolbar
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          {/* 왼쪽 LBK 로고 */}
          <Typography
            variant="h6"
            sx={{
              color: "#ffffff",
              fontWeight: "bold",
              letterSpacing: 1,
            }}
          >
            LBK
          </Typography>

          {/* 오른쪽 이메일 및 주문내역삭제 버튼 */}
          <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
            {email && (
              <Typography
                variant="body1"
                sx={{ color: "white", fontSize: "14px" }}
              >
                {email}
              </Typography>
            )}
            <Button
              variant="contained"
              sx={{
                backgroundColor: "#8bc34a",
                color: "white",
                fontSize: "12px",
                padding: "6px 12px",
                "&:hover": { backgroundColor: "#7cb342" },
              }}
              onClick={handleModalOpen} // 모달 열기
            >
              주문내역확인
            </Button>
          </Box>
        </Toolbar>
      </AppBar>

      {/* 주문내역 컴포넌트 */}
      <OrderDetailModal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        onConfirm={handleConfimModalOpen}
        email={email}
      />
      {/* confimModal 컴포넌트 */}
      <CustomModal
        open={confimModalOpen}
        onClose={() => setConfimModalOpen(false)}
        onConfirm={() => handleResultModalOpen()}
        content={"주문내역을 삭제하시겠습니까?"}
      />
      {/* confimModal 컴포넌트 */}
      <CustomModal
        open={resultModalOpen}
        onClose={() => setResultModalOpen(false)}
        onConfirm={() => setResultModalOpen(false)}
        content={
          modalAction === "success" ? "삭제되었습니다." : "실패하였습니다."
        }
        hideCancel={true}
      />
    </>
  );
};

export default TopBar;
