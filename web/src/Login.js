import axios from "axios";
import {useState} from "react";

function Login(){
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const login = async() => {
    try {
      const res = await axios.post("http://localhost:8080/api/auth/login", {email, password});
      localStorage.setItem("token", res.data);
      window.location = "/dashboard";
    } catch(err) { 
      alert("Invalid credentials. Please try again."); 
    }
  };

  return(
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-left">
          <h1>Welcome to Mini App</h1>
          <p>Securely manage your account with our platform. Access all features and get started.</p>
        </div>
        <div className="auth-right">
          <div className="auth-box">
            <h2>Login</h2>
            <p className="subtitle">Enter your email and password to login</p>
            
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