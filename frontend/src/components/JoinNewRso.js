import React, { useEffect } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import config from "../Config";

const url = config.url;

function JoinNewRso() {
  const navigate = useNavigate();
  const { rsoId } = useParams();

  useEffect(() => {
    const token = localStorage.getItem("token");
    axios
      .get(`${url}/api/rso/join/${rsoId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then(() => {
        navigate("/home");
      })
      .catch((error) => {
        console.log(error);
      });
  }, [navigate]);

  return null;
}

export default JoinNewRso;
