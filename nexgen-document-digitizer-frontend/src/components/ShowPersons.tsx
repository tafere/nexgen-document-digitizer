import React, { useState } from "react";
import {
  Table,
  Typography,
  Paper,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  TableContainer,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
} from "@mui/material";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import VisibilityIcon from "@mui/icons-material/Visibility";
import UploadDocument from "./UploadDocument";
import ShowAttachments from "./ShowAttachments";

interface Address {
  address1?: string;
  address2?: string;
  city?: string;
  state?: string;
  country?: string;
}

interface Person {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  address: string | Address;
  dateOfBirth: string;
  phone: string;
}

const formatAddress = (address: string | Address): string => {
  if (typeof address === "string") return address;
  return `${address.address1 || ""} ${address.address2 || ""}, ${address.city || ""}, ${address.state || ""}, ${address.country || ""}`.trim();
};

interface ShowPersonsProps {
  persons: Person[];
  onPersonSelect: (person: Person) => void;
  setPersons: React.Dispatch<React.SetStateAction<Person[]>>;
}

const ShowPersons: React.FC<ShowPersonsProps> = ({ persons, onPersonSelect, setPersons }) => {
  const [selectedPersonId, setSelectedPersonId] = useState<number | null>(null);
  const [selectedForDocs, setSelectedForDocs] = useState<number | null>(null);

  return (
    <Paper sx={{ padding: 2, boxShadow: 3, width: "100%" }}>
      <Typography variant="h4" gutterBottom align="center">
        Registered Persons
      </Typography>
      <TableContainer sx={{ width: "100%" }}>
        <Table sx={{ width: "100%" }} aria-label="persons table">
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>First Name</TableCell>
              <TableCell>Last Name</TableCell>
              <TableCell>Date of Birth</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Phone</TableCell>
              <TableCell>Address</TableCell>
              <TableCell>Upload</TableCell>
              <TableCell>View Documents</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {persons.map((person) => (
              <TableRow key={person.id} onClick={() => onPersonSelect(person)} sx={{ cursor: "pointer", "&:hover": { backgroundColor: "#f5f5f5" } }}>
                <TableCell>{person.id}</TableCell>
                <TableCell>{person.firstName}</TableCell>
                <TableCell>{person.lastName}</TableCell>
                <TableCell>{person.dateOfBirth}</TableCell>
                <TableCell>{person.email}</TableCell>
                <TableCell>{person.phone}</TableCell>
                <TableCell>{formatAddress(person.address) || "N/A"}</TableCell>
                <TableCell>
                  <IconButton
                    color="primary"
                    onClick={(e) => {
                      e.stopPropagation();
                      setSelectedPersonId(person.id);
                    }}
                  >
                    <CloudUploadIcon />
                  </IconButton>
                </TableCell>
                <TableCell>
                  <IconButton
                    color="secondary"
                    onClick={(e) => {
                      e.stopPropagation();
                      setSelectedForDocs(person.id);
                    }}
                  >
                    <VisibilityIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Upload Dialog */}
      <Dialog open={selectedPersonId !== null} onClose={() => setSelectedPersonId(null)}>
        <DialogTitle>Upload Document</DialogTitle>
        <DialogContent>
          {selectedPersonId && <UploadDocument personId={String(selectedPersonId)} />}
        </DialogContent>
      </Dialog>

      {/* Attachments Dialog */}
      <ShowAttachments personId={selectedForDocs} onClose={() => setSelectedForDocs(null)} />
    </Paper>
  );
};

export default ShowPersons;