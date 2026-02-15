import React, { useState } from "react";
import axios from "axios";

function Register() {
  const [form, setForm] = useState({
    first_name: "",
    last_name: "",
    username: "",
    email_address: "",
    password: "",
    confirm_password: ""
  });
  const [message, setMessage] = useState("");

  const submit = async () => {
    setMessage("");

    if (!form.first_name || !form.last_name || !form.username || 
        !form.email_address || !form.password || !form.confirm_password) {
      setMessage("All fields are required.");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email_address)) {
      setMessage("Invalid email format.");
      return;
    }

    if (form.password.length < 6) {
      setMessage("Password must be at least 6 characters.");
      return;
    }

    if (form.password !== form.confirm_password) {
      setMessage("Passwords do not match.");
      return;
    }

    try {
      await axios.post("http://localhost:8080/api/auth/register", {
        first_name: form.first_name,
        last_name: form.last_name,
        username: form.username,
        email_address: form.email_address,
        password: form.password
      });
      
      setMessage("Registration successful!");
      setTimeout(() => window.location = "/", 1500);
    } catch (err) {
      if (err.response && err.response.status === 409) {
        setMessage("Email already registered.");
      } else {
        setMessage("Registration failed.");
      }
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-left">
          <h1>Welcome to Mini App</h1>
          <p>Create an account to access the user authentication system.</p>
        </div>
        <div className="auth-right">
          <div className="auth-box">
            <h2>Register</h2>
            <p className="subtitle">Fill in your details to get started</p>
            
            {message && <p style={{color: '#dc143c', marginBottom: '15px', fontSize: '14px'}}>{message}</p>}
            
            <label>First Name</label>
            <input 
              placeholder="John" 
              value={form.first_name}
              onChange={(e) => setForm({ ...form, first_name: e.target.value })} 
            />
            
            <label>Last Name</label>
            <input 
              placeholder="Doe" 
              value={form.last_name}
              onChange={(e) => setForm({ ...form, last_name: e.target.value })} 
            />
            
            <label>Username</label>
            <input 
              placeholder="johndoe" 
              value={form.username}
              onChange={(e) => setForm({ ...form, username: e.target.value })} 
            />
            
            <label>Email</label>
            <input 
              type="email"
              placeholder="name@example.com" 
              value={form.email_address}
              onChange={(e) => setForm({ ...form, email_address: e.target.value })} 
            />
            
            <label>Password</label>
            <input 
              type="password" 
              placeholder="At least 6 characters" 
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })} 
            />
            
            <label>Confirm Password</label>
            <input 
              type="password" 
              placeholder="Re-enter your password" 
              value={form.confirm_password}
              onChange={(e) => setForm({ ...form, confirm_password: e.target.value })} 
            />
            
            <button onClick={submit}>Register</button>
            
            <div className="auth-footer">
              Already have an account? <span onClick={() => window.location="/"}>Login</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Register;