import axios from "axios";
import {useEffect, useState} from "react";

function Profile(){
  const [user, setUser] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        window.location = "/";
        return;
      }
      
      try {
        const res = await axios.get("http://localhost:8080/api/auth/me", {
          headers: { Authorization: `Bearer ${token}` }
        });
        setUser(res.data);
        setLoading(false);
      } catch (err) {
        localStorage.clear();
        window.location = "/";
      }
    };
    
    fetchUser();
  }, []);

  const getInitials = () => {
    if (!user.first_name || !user.last_name) return "U";
    return user.first_name[0].toUpperCase() + user.last_name[0].toUpperCase();
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return(
    <div>
      <nav className="navbar">
        <div className="navbar-brand">Mini App</div>
        <div className="navbar-menu">
          <a onClick={() => window.location="/dashboard"}>Dashboard</a>
          <a onClick={() => window.location="/profile"}>Profile</a>
          <button onClick={() => {localStorage.clear(); window.location="/";}}>Logout</button>
        </div>
      </nav>
      
      <div className="profile-page">
        <div className="profile-card">
          <div className="profile-header">
            <div className="profile-avatar">{getInitials()}</div>
            <h2>{user.first_name} {user.last_name}</h2>
            <p>@{user.username}</p>
          </div>
          
          <div className="profile-body">
            <div className="profile-section">
              <h3>Account Information</h3>
              <div className="info-row">
                <div className="info-label">Full Name</div>
                <div className="info-value">{user.first_name} {user.last_name}</div>
              </div>
              <div className="info-row">
                <div className="info-label">Username</div>
                <div className="info-value">@{user.username}</div>
              </div>
              <div className="info-row">
                <div className="info-label">Email Address</div>
                <div className="info-value">{user.email_address}</div>
              </div>
            </div>
            
            <div className="profile-actions">
              <button onClick={() => window.location="/dashboard"}>
                Back to Dashboard
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
} 

export default Profile;