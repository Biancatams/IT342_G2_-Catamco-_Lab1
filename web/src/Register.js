import React, { useState } from "react";
import axios from "axios";

function Register() {
  const [form, setForm] = useState({
    first_name: "",
    last_name: "",
    username: "",
    email_address: "",
    password: ""
  });

  const submit = async () => {
    try {
      await axios.post("http://localhost:8080/api/auth/register", form);
      alert("Registration successful! Now redirecting to login.");
      window.location = "/";
    } catch (err) {
      alert("Registration failed. Check if email is already used.");
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-left">
          <h1>Welcome to Mini App</h1>
          <p>Join us today and experience a seamless authentication system.</p>
        </div>
        <div className="auth-right">
          <div className="auth-box">
            <h2>Register</h2>
            <p className="subtitle">Fill in your details to get started</p>
            
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
              placeholder="Create a password" 
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })} 
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