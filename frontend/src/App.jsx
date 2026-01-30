import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [messages, setMessages] = useState([
    { text: "안녕하세요! ERP AI 비서입니다. 무엇을 찾아드릴까요?", sender: "bot" }
  ]);
  const [inputText, setInputText] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const sendMessage = async () => {
    if (!inputText.trim()) return;

    // 1. 사용자 질문 표시
    const userMsg = { text: inputText, sender: "user" };
    setMessages(prev => [...prev, userMsg]);
    setInputText("");
    setIsLoading(true);

    try {
      // 2. 서버 요청
      const response = await fetch(`http://localhost:8080/api/v1/ai/query?q=${encodeURIComponent(userMsg.text)}`);

      if (!response.ok) throw new Error("서버 통신 실패");

      const data = await response.json();

      // 3. 봇 메시지 생성 (데이터가 있으면 data 속성에 담기)
      // 데이터가 배열인지 확인해서, 배열이면 표로 그리기 위해 data에 넣음
      const botMsg = {
        text: Array.isArray(data) && data.length > 0 ? "조회된 데이터입니다." : "데이터가 없거나 메시지입니다.",
        data: data, // ★ 핵심: JSON 데이터를 통째로 저장
        sender: "bot"
      };

      setMessages(prev => [...prev, botMsg]);

    } catch (error) {
      console.error("에러:", error);
      const errorMsg = { text: "죄송합니다. 처리 중 오류가 발생했습니다.", sender: "bot" };
      setMessages(prev => [...prev, errorMsg]);
    } finally {
      setIsLoading(false);
    }
  };

  // ★ 표(Table)를 그려주는 함수
  const renderTable = (data) => {
    if (!Array.isArray(data) || data.length === 0) return null;

    const headers = Object.keys(data[0]); // 첫 번째 데이터의 키값(MAKTX 등)을 헤더로 사용

    return (
      <table style={{ borderCollapse: "collapse", width: "100%", marginTop: "10px", fontSize: "14px", backgroundColor: "white" }}>
        <thead>
          <tr style={{ backgroundColor: "#f2f2f2" }}>
            {headers.map((head) => (
              <th key={head} style={{ border: "1px solid #ddd", padding: "8px", textAlign: "left", color: "#333" }}>
                {head}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, rowIndex) => (
            <tr key={rowIndex}>
              {headers.map((head) => (
                <td key={`${rowIndex}-${head}`} style={{ border: "1px solid #ddd", padding: "8px", color: "#555" }}>
                  {row[head]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    );
  };

  return (
    <div style={{ maxWidth: "800px", margin: "0 auto", padding: "20px", fontFamily: "sans-serif" }}>
      <h1 style={{ textAlign: "center", color: "#333" }}>🏭 Intelli-ERP Chat</h1>

      <div style={{
        border: "1px solid #ccc", borderRadius: "10px", height: "500px", overflowY: "auto",
        padding: "20px", backgroundColor: "#f9f9f9", display: "flex", flexDirection: "column", gap: "15px"
      }}>
        {messages.map((msg, index) => (
          <div key={index} style={{
            alignSelf: msg.sender === "user" ? "flex-end" : "flex-start",
            maxWidth: msg.sender === "user" ? "70%" : "90%" // 봇 메시지(표)는 좀 더 넓게
          }}>
            {/* 말풍선 */}
            <div style={{
              padding: "12px 18px", borderRadius: "15px",
              backgroundColor: msg.sender === "user" ? "#007bff" : "#ffffff",
              color: msg.sender === "user" ? "white" : "#333",
              boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
              border: msg.sender === "bot" ? "1px solid #eee" : "none"
            }}>
              {msg.text}
              {/* 데이터가 있으면 표 렌더링 */}
              {msg.data && renderTable(msg.data)}
            </div>
          </div>
        ))}
        {isLoading && <div style={{ color: "#999", fontSize: "12px" }}>🤖 AI가 열심히 조회 중...</div>}
      </div>

      <div style={{ display: "flex", marginTop: "15px" }}>
        <input
          type="text" value={inputText} onChange={(e) => setInputText(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && sendMessage()}
          placeholder="예: 10만원 이상인 제품 보여줘"
          style={{ flex: 1, padding: "15px", borderRadius: "5px", border: "1px solid #ccc" }}
        />
        <button onClick={sendMessage} disabled={isLoading}
          style={{ marginLeft: "10px", padding: "15px 30px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: "5px", cursor: "pointer" }}>
          전송
        </button>
      </div>
    </div>
  )
}

export default App
