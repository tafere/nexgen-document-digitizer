import React, { useState } from "react";
import SearchPerson from "./components/SearchPerson";
import AddPerson from "./components/AddPerson";
import { Container, Row, Col, Card } from "react-bootstrap";

const App: React.FC = () => {
  const [selectedPerson, setSelectedPerson] = useState<any>(null);

  return (
    <div>
      <h2 className="text-center mb-4">📄 Document Management System</h2>

      {/* Two-column layout: Search Person on the left, Register Person on the right */}
      <div>
          <div>
            <h3 className="text-center">Search Person</h3>
            <SearchPerson onPersonSelect={setSelectedPerson} />
          </div>
      </div>

      {/* If a person is selected, show their document management options */}
      {selectedPerson && (
        <Row className="mt-4">
          <Col md={12}>
            <h3 className="text-center mb-4">
              Manage Documents for {selectedPerson.firstName} {selectedPerson.lastName}
            </h3>
            {/* Here you can add components like Document Actions, UploadDocument, etc. */}
          </Col>
        </Row>
      )}
    </div>
  );
};

export default App;