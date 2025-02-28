import React from "react";
import PropTypes from "prop-types";
import { Modal, Button, Typography, Box } from "@mui/material";

const CustomModal = ({ open, onClose, onConfirm, content, hideCancel }) => {
  return (
    <Modal open={open} onClose={onClose} aria-labelledby="custom-modal">
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          width: 320,
          bgcolor: "background.paper",
          boxShadow: 24,
          p: 3,
          borderRadius: "8px",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <Typography variant="body1" sx={{ textAlign: "center", mb: 2 }}>
          {content}
        </Typography>
        <Box sx={{ display: "flex", gap: 2 }}>
          {!hideCancel && ( // hideCancel이 true이면 취소 버튼을 숨김
            <Button variant="outlined" onClick={onClose}>
              취소
            </Button>
          )}
          <Button variant="contained" color="error" onClick={onConfirm}>
            확인
          </Button>
        </Box>
      </Box>
    </Modal>
  );
};

// Props 타입 검증
CustomModal.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onConfirm: PropTypes.func.isRequired,
  content: PropTypes.string.isRequired,
  hideCancel: PropTypes.bool, // 새로운 prop 추가 (선택 사항)
};

// 기본값 설정 (hideCancel 기본값 false)
CustomModal.defaultProps = {
  hideCancel: false,
};

export default CustomModal;
