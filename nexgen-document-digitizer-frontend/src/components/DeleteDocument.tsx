import React, { useState } from 'react';
import axios from 'axios';

const DeleteDocument: React.FC = () => {
  const [documentId, setDocumentId] = useState<number>(1); // Example Document ID

  const handleDelete = async () => {
    try {
      await axios.delete(`http://localhost:8081/documents/${documentId}`);
      console.log('Document deleted successfully');
    } catch (error) {
      console.error('Error deleting document:', error);
    }
  };

  return (
    <div>
      <h2>Delete Document</h2>
      <input type="number" value={documentId} onChange={(e) => setDocumentId(Number(e.target.value))} placeholder="Enter Document ID" />
      <button onClick={handleDelete}>Delete</button>
    </div>
  );
};

export default DeleteDocument;
