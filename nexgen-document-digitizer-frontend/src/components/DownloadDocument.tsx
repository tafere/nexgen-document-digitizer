import React, { useState } from 'react';
import axios from 'axios';

const DownloadDocument: React.FC = () => {
  const [documentId, setDocumentId] = useState<number>(1); // Example Document ID

  const handleDownload = async () => {
    try {
      const response = await axios.get(`http://localhost:8081/documents/download/${documentId}`, {
        responseType: 'blob',
      });

      // Extract content type from response headers
      const contentType = response.headers['content-type'];
      const blob = new Blob([response.data], { type: contentType });

      // Determine file extension based on content type
      const extensionMap: { [key: string]: string } = {
        'image/png': 'png',
        'image/jpeg': 'jpg',
        'application/pdf': 'pdf',
        'text/csv': 'csv',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': 'xlsx',
      };

      const fileExtension = extensionMap[contentType] || 'bin'; // Default to 'bin' if unknown

      // Create download link
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = `document_${documentId}.${fileExtension}`;
      document.body.appendChild(link); // Append link to body (best practice)
      link.click();
      document.body.removeChild(link); // Remove link after download

    } catch (error) {
      console.error('Error downloading document:', error);
    }
  };

  return (
    <div>
      <h2>Download Document</h2>
      <input
        type="number"
        value={documentId}
        onChange={(e) => setDocumentId(Number(e.target.value))}
        placeholder="Enter Document ID"
      />
      <button onClick={handleDownload}>Download</button>
    </div>
  );
};

export default DownloadDocument;
