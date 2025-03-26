import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import CustomModal from "../common/CustomModal";

const Login = () => {
  const [email, setEmail] = useState("");
  const [content, setContent] = useState("");
  const [modalOpen, setModalOpen] = useState(false);
  const navigate = useNavigate();
  const regex = /^[^@]+@digicaps\.com$/;

  const handleModalOpen = (text) => {
    setContent(text);
    setModalOpen(true);
  };

  const handleLogin = () => {
    if (email === "") {
      handleModalOpen("이메일을 입력해주세요.");
      return;
    }

    if (!regex.test(email)) {
      handleModalOpen("디지캡 이메일을 입력해주세요.");
      return;
    }

    navigate("/home", { state: { email } });
  };

  return (
    <>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          marginTop: "50px",
        }}
      >
        <h1>LBK</h1>
        <input
          type="email"
          placeholder="이메일을 입력하세요"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          style={{
            padding: "10px",
            fontSize: "16px",
            width: "300px",
            marginBottom: "20px",
            borderRadius: "5px",
            border: "1px solid #ccc",
          }}
        />
        <button
          onClick={handleLogin}
          style={{
            padding: "10px 20px",
            fontSize: "16px",
            width: "300px",
            backgroundColor: "#5a00e0",
            color: "white",
            border: "none",
            borderRadius: "5px",
            cursor: "pointer",
          }}
        >
          로그인
        </button>
      </div>
      {/* confimModal 컴포넌트 */}
      <CustomModal
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        onConfirm={() => setModalOpen(false)}
        content={content}
        hideCancel={true}
      />
    </>
  );
};

export default Login;
