import React, { useState } from 'react';
import axios from 'axios';

const ViewDocument: React.FC = () => {
  const [documentId, setDocumentId] = useState<number>(1); // Example Document ID
  const [document, setDocument] = useState<any>(null);

  const handleView = async () => {
    try {
      const response = await axios.get(`http://localhost:8081/documents/${documentId}`);
      setDocument(response.data);
    } catch (error) {
      console.error('Error fetching document:', error);
    }
  };

  return (
    <div>
      <h2>View Document</h2>
      <input type="number" value={documentId} onChange={(e) => setDocumentId(Number(e.target.value))} placeholder="Enter Document ID" />
      <button onClick={handleView}>View Document</button>

      {document && (
        <div>
          <h3>{document.name}</h3>
          <p>{document.contentType}</p>
          {/* Display other document details here */}
        </div>
      )}
    </div>
  );
};

export default ViewDocument;
