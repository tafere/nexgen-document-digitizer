import React, { useState } from "react";
import axios from "axios";
import { TextField, Button, Box, Typography } from "@mui/material";
import { LocalizationProvider, DatePicker } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";

const AddPerson: React.FC = () => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    dateOfBirth: null as string | null,
    address: {
      address1: "",
      address2: "",
      city: "",
      state: "",
      country: "",
    },
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    if (name.startsWith("address.")) {
      setFormData((prevData) => ({
        ...prevData,
        address: {
          ...prevData.address,
          [name.split(".")[1]]: value,
        },
      }));
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleDateChange = (date: dayjs.Dayjs | null) => {
    setFormData((prevData) => ({
      ...prevData,
      dateOfBirth: date ? date.format("YYYY-MM-DD") : null,
    }));
  };
  const handleSubmit = async () => {
    console.log("Sending form data:", formData); // Debugging
  
    try {
      await axios.post("http://localhost:8081/users", formData, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      alert("Person added successfully!");
    } catch (error) {
      console.error("Error adding person:", error);
    }
  };
  
  
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Box sx={{ display: "flex", flexDirection: "column", gap: 2, padding: 2 }}>
        <Typography variant="h6" align="center" sx={{ fontWeight: "bold" }}>
          Add New Person
        </Typography>

        <TextField label="First Name" name="firstName" onChange={handleChange} fullWidth variant="outlined" size="medium" />
        <TextField label="Last Name" name="lastName" onChange={handleChange} fullWidth variant="outlined" size="medium" />
        <DatePicker
          label="Date of Birth"
          value={formData.dateOfBirth ? dayjs(formData.dateOfBirth) : null}
          onChange={handleDateChange}
          slotProps={{ textField: { fullWidth: true, variant: "outlined" } }}
        />
        <TextField label="Email" name="email" onChange={handleChange} fullWidth variant="outlined" size="medium" />
        <TextField label="Phone" name="phone" onChange={handleChange} fullWidth variant="outlined" size="medium" />

        <Typography variant="subtitle1" sx={{ fontWeight: "bold", marginTop: 2 }}>
          Address
        </Typography>
        <TextField label="Address 1" name="address.address1" onChange={handleChange} fullWidth variant="outlined" size="medium" />
        <TextField label="Address 2" name="address.address2" onChange={handleChange} fullWidth variant="outlined" size="medium" />
        <TextField label="City" name="address.city" onChange={handleChange} fullWidth variant="outlined" size="medium" />
        <TextField label="State" name="address.state" onChange={handleChange} fullWidth variant="outlined" size="medium" />
        <TextField label="Country" name="address.country" onChange={handleChange} fullWidth variant="outlined" size="medium" />

        <Button
          variant="contained"
          color="primary"
          onClick={handleSubmit}
          sx={{
            padding: "12px 24px",
            fontWeight: 600,
            borderRadius: 2,
            marginTop: 2,
            width: "100%",
          }}
        >
          Add Person
        </Button>
      </Box>
    </LocalizationProvider>
  );
};

export default AddPerson;


