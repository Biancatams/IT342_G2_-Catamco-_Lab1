function Dashboard(){
  if(!localStorage.getItem("token")) window.location = "/";

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
      
      <div className="main-content">
        <div className="welcome-section">
          <h1>Welcome Back!</h1>
          <p>You have successfully logged in to your account</p>
          <button onClick={() => window.location="/profile"}>View Profile</button>
        </div>
      </div>
    </div>
  );
} 

export default Dashboard;