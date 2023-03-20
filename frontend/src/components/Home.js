import React, { useState, useEffect } from "react";

const url = "http://localhost:8080";

const HomePage = () => {
    const [users, setUsers] = useState([]);
    const url = "http://localhost:8080";
  
    useEffect(() => {
      const fetchData = async () => {
        try {
          const token = localStorage.getItem("token");
          const response = await fetch(`${url}/api/user/all`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });
          const data = await response.json();
          setUsers(data);
        } catch (error) {
          console.error("Protected resource error:", error);
          // Handle the protected resource error here
        }
      };
  
      fetchData();
    }, [url]);

  return (
    <div>
      <h1>All Users</h1>
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Username</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.name}</td>
              <td>{user.email}</td>
              <td>{user.username}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default HomePage;
