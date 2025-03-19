import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper,
  Typography, Dialog, DialogTitle, DialogContent, IconButton, Box, Button
} from "@mui/material";
import { Visibility, Download, Delete } from "@mui/icons-material";

interface Attachment {
  id: number;
  fileName: string;
  fileType: string;
  fileSize: number;
  uploadedDate: string;
}

interface ShowAttachmentsProps {
  personId: number | null;
  onClose: () => void;
}

const ShowAttachments: React.FC<ShowAttachmentsProps> = ({ personId, onClose }) => {
  const [attachments, setAttachments] = useState<Attachment[]>([]);
  const [loading, setLoading] = useState(false);
  const [viewingFileId, setViewingFileId] = useState<number | null>(null); // Stores selected document ID for viewing

  useEffect(() => {
    if (personId) {
      setLoading(true);
      axios.get(`http://localhost:8081/documents?personId=${personId}`)
        .then((response) => setAttachments(response.data))
        .catch((error) => console.error("Error fetching attachments:", error))
        .finally(() => setLoading(false));
    }
  }, [personId]);

  const handleDelete = async (documentId: number) => {
    if (!window.confirm("Are you sure you want to delete this document?")) return;
    
    try {
      await axios.delete(`http://localhost:8081/documents/${documentId}`);
      setAttachments(attachments.filter((attachment) => attachment.id !== documentId));
    } catch (error) {
      console.error("Error deleting document:", error);
    }
  };

  return (
    <Dialog
      open={!!personId}
      onClose={onClose}
      maxWidth="lg"
      fullWidth
      PaperProps={{ style: { width: "90vw", height: "80vh", maxWidth: "none" } }}
    >
      <DialogTitle>
        {viewingFileId ? "Document Preview" : `Documents for Person ${personId}`}
      </DialogTitle>

      <DialogContent dividers style={{ maxHeight: "70vh", overflowY: "auto" }}>
        {loading ? (
          <Typography>Loading...</Typography>
        ) : viewingFileId ? (
          <Box display="flex" flexDirection="column" alignItems="center" width="100%">
  <Box width="100%" height="95vh" overflow="auto">
    <iframe
      src={`http://localhost:8081/documents/view/${viewingFileId}`}
      width="100%"
      height="100%"
      style={{
        border: "1px solid #ccc",
        zoom: .8, // Ensures no automatic scaling
        transform: "none", // Prevents unwanted zoom effects
      }}
    />
  </Box>
  <Button onClick={() => setViewingFileId(null)} variant="contained" color="primary" sx={{ mt: 2 }}>
    Back to Document List
  </Button>
</Box>

        ) : attachments.length > 0 ? (
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell><b>File Name</b></TableCell>
                  <TableCell><b>File Type</b></TableCell>
                  <TableCell><b>File Size (KB)</b></TableCell>
                  <TableCell><b>Uploaded Date</b></TableCell>
                  <TableCell><b>Actions</b></TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {attachments.map((attachment) => (
                  <TableRow key={attachment.id}>
                    <TableCell>{attachment.fileName}</TableCell>
                    <TableCell>{attachment.fileType}</TableCell>
                    <TableCell>{(attachment.fileSize / 1024).toFixed(2)}</TableCell>
                    <TableCell>{attachment.uploadedDate}</TableCell>
                    <TableCell>
                      {/* 🔹 View File */}
                      <IconButton onClick={() => setViewingFileId(attachment.id)} color="primary">
                        <Visibility />
                      </IconButton>

                      {/* 🔹 Download File */}
                      <IconButton
                        color="secondary"
                        component="a"
                        href={`http://localhost:8081/documents/download/${attachment.id}`}
                        download
                      >
                        <Download />
                      </IconButton>

                      {/* 🔹 Delete File */}
                      <IconButton color="error" onClick={() => handleDelete(attachment.id)}>
                        <Delete />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        ) : (
          <Typography>No attachments found.</Typography>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default ShowAttachments;


