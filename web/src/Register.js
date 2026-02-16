import React, { useState } from "react";
import axios from "axios";

function Register() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    username: "",
    emailAddress: "",
    password: "",
    confirmPassword: ""
  });
  const [message, setMessage] = useState("");

  const submit = async () => {
    setMessage("");

    // Frontend validation
    if (!form.firstName || !form.lastName || !form.username || 
        !form.emailAddress || !form.password || !form.confirmPassword) {
      setMessage("All fields are required.");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.emailAddress)) {
      setMessage("Invalid email format.");
      return;
    }

    if (form.password.length < 6) {
      setMessage("Password must be at least 6 characters.");
      return;
    }

    if (form.password !== form.confirmPassword) {
      setMessage("Passwords do not match.");
      return;
    }

    try {
      // Send RegisterDTO to backend (matches sequence diagram)
      await axios.post("http://localhost:8080/api/auth/register", {
        firstName: form.firstName,
        lastName: form.lastName,
        username: form.username,
        emailAddress: form.emailAddress,
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
              value={form.firstName}
              onChange={(e) => setForm({ ...form, firstName: e.target.value })} 
            />
            
            <label>Last Name</label>
            <input 
              placeholder="Doe" 
              value={form.lastName}
              onChange={(e) => setForm({ ...form, lastName: e.target.value })} 
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
              value={form.emailAddress}
              onChange={(e) => setForm({ ...form, emailAddress: e.target.value })} 
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
              value={form.confirmPassword}
              onChange={(e) => setForm({ ...form, confirmPassword: e.target.value })} 
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