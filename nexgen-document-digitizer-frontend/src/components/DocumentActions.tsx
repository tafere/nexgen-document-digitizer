import React, { useState, useEffect } from "react";
import axios from "axios";
import { Button, List, ListItem, ListItemText } from "@mui/material";

const DocumentActions: React.FC<{ personId: string }> = ({ personId }) => {
  const [documents, setDocuments] = useState<any[]>([]);

  useEffect(() => {
    fetchDocuments();
  }, [personId]);

  const fetchDocuments = async () => {
    try {
      const response = await axios.get(`http://localhost:8081/documents?personId=${personId}`);
      setDocuments(response.data);
    } catch (error) {
      console.error("Error fetching documents:", error);
    }
  };

  const handleDownload = async (documentId: number) => {
    try {
      const response = await axios.get(`http://localhost:8081/documents/download/${documentId}`, { responseType: "blob" });
      const blob = new Blob([response.data], { type: response.headers["content-type"] });
      const link = document.createElement("a");
      link.href = window.URL.createObjectURL(blob);
      link.download = `document_${documentId}`;
      link.click();
    } catch (error) {
      console.error("Error downloading document:", error);
    }
  };

  const handleDelete = async (documentId: number) => {
    try {
      await axios.delete(`http://localhost:8081/documents/${documentId}`);
      fetchDocuments();
    } catch (error) {
      console.error("Error deleting document:", error);
    }
  };

  return (
    <div>
      <h4>Documents for {personId}</h4>
      <List>
        {documents.map((doc) => (
          <ListItem key={doc.id}>
            <ListItemText primary={doc.fileName} />
            <Button onClick={() => handleDownload(doc.id)} color="primary">
              Download
            </Button>
            <Button onClick={() => handleDelete(doc.id)} color="secondary">
              Delete
            </Button>
          </ListItem>
        ))}
      </List>
    </div>
  );
};

export default DocumentActions;
