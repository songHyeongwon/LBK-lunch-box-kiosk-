import axios from "axios";

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL, // 환경 변수에서 기본 URL 설정
  timeout: 10000, // 요청 타임아웃 설정 (ms)
  headers: {
    "Content-Type": "application/json",
  },
});

// 요청 인터셉터
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // 예외 처리
      if (error.response.status === 401) {
        console.error("Unauthorized: Redirect to login");
      }
    }
    return Promise.reject(error);
  }
);

export default api;
