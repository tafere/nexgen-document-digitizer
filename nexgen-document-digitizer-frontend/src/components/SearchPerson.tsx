import React, { useState } from "react";
import axios from "axios";
import {
  TextField,
  Paper,
  Container,
  Grid,
  InputAdornment,
  Button,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from "@mui/material";
import { Search } from "@mui/icons-material";
import ShowPersons from "./ShowPersons";
import AddPerson from "./AddPerson"; 

const SearchPerson: React.FC<{ onPersonSelect: (person: any) => void }> = ({ onPersonSelect }) => {
  const [searchTerm, setSearchTerm] = useState("");
  const [persons, setPersons] = useState<any[]>([]);
  const [openAddPersonModal, setOpenAddPersonModal] = useState(false);
  

  const handleSearch = async () => {
    try {
      const response = await axios.get(`http://localhost:8081/users/search?search=${searchTerm}`);
      setPersons(response.data);
    } catch (error) {
      console.error("Error searching persons:", error);
    }
  };

  return (
    <Container sx={{ marginTop: 4, width: "100%" }}>
      <Paper sx={{ padding: 2, boxShadow: 3, width: "100%" }}>
        <Grid container justifyContent="space-between" alignItems="center">
          <Grid item xs={12} sm={6}>
            <TextField
              label="Search Person"
              variant="outlined"
              fullWidth
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              sx={{ marginBottom: 2 }}
              InputProps={{
                endAdornment: (
                  <InputAdornment position="end">
                    <Search onClick={handleSearch} style={{ cursor: "pointer" }} />
                  </InputAdornment>
                ),
              }}
            />
          </Grid>
          <Grid item>
            <Button
              variant="contained"
              color="primary"
              onClick={() => setOpenAddPersonModal(true)}
              sx={{ marginBottom: 2 }}
            >
              + Add New Person
            </Button>
          </Grid>
        </Grid>
      </Paper>

      {persons.length > 0 && (
        <Box sx={{ width: "100%", marginTop: 2 }}>
          <ShowPersons persons={persons} onPersonSelect={onPersonSelect} setPersons={setPersons} />
        </Box>
      )}

      {/* Add Person Modal */}
      <Dialog open={openAddPersonModal} onClose={() => setOpenAddPersonModal(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Add New Person</DialogTitle>
        <DialogContent>
          <AddPerson />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenAddPersonModal(false)} color="secondary">
            Cancel
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default SearchPerson;




