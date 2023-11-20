import React from 'react';
import Container from "react-bootstrap/Container";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Activities from "./Activities";
import {ActivityDetail} from "./ActivityDetail";

function App() {
  return (
    <Container className="p-3">
      <Container className="p-5 mb-4 bg-light rounded-3">
          <h1 className="header">stranalyze</h1>
          <BrowserRouter>
              <Routes>
                  <Route path="/" element={<Activities />} />
                  <Route path="/activity" element={<ActivityDetail />} />
              </Routes>
          </BrowserRouter>
      </Container>
    </Container>
  );
}

export default App;
