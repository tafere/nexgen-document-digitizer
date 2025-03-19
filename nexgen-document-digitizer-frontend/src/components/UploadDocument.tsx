import React, { useState } from "react";
import axios from "axios";
import { TextField, Button } from "@mui/material";

const UploadDocument: React.FC<{ personId: string }> = ({ personId }) => {
  const [file, setFile] = useState<File | null>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setFile(e.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!file) return alert("Select a file first!");

    const formData = new FormData();
    formData.append("personId", personId);
    formData.append("file", file);

    try {
      await axios.post("http://localhost:8081/documents/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      alert("File uploaded successfully!");
    } catch (error) {
      console.error("Upload error:", error);
    }
  };

  return (
    <div>
      <h4>Upload Document for {personId}</h4>
      <TextField type="file" onChange={handleFileChange} fullWidth />
      <Button variant="contained" color="primary" className="mt-2" onClick={handleUpload}>
        Upload
      </Button>
    </div>
  );
};

export default UploadDocument;

