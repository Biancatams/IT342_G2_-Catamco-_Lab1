import axios from "axios";
import {useState} from "react";

function Login(){
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const login = async() => {
    setMessage("");

    if (!email || !password) {
      setMessage("All fields are required.");
      return;
    }

    try {
      // Send LoginDTO to backend (matches sequence diagram)
      const res = await axios.post("http://localhost:8080/api/auth/login", {
        email: email,
        password: password
      });
      
      if (!res.data) {
        setMessage("Invalid credentials.");
        return;
      }
      
      // Store JWT token
      localStorage.setItem("token", res.data);
      window.location = "/dashboard";
    } catch(err) { 
      if (err.response && err.response.status === 401) {
        setMessage("Invalid credentials.");
      } else {
        setMessage("Login failed. Please try again.");
      }
    }
  };

  return(
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-left">
          <h1>Welcome to Mini App</h1>
          <p>Login to access your account and manage your profile.</p>
        </div>
        <div className="auth-right">
          <div className="auth-box">
            <h2>Login</h2>
            <p className="subtitle">Enter your email and password to login</p>
            
            {message && <p style={{color: '#dc143c', marginBottom: '15px', fontSize: '14px'}}>{message}</p>}
            
            <label>Email</label>
            <input 
              type="email"
              placeholder="name@example.com" 
              value={email}
              onChange={e => setEmail(e.target.value)}
            />
            
            <label>Password</label>
            <input 
              type="password" 
              placeholder="Enter your password" 
              value={password}
              onChange={e => setPassword(e.target.value)}
            />
            
            <button onClick={login}>Login</button>
            
            <div className="auth-footer">
              Don't have an account? <span onClick={() => window.location="/register"}>Register</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
} 

export default Login;