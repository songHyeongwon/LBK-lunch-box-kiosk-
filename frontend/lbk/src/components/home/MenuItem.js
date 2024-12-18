import React from "react";
import {
  Card,
  CardMedia,
  CardContent,
  Typography,
  Box,
  Chip,
} from "@mui/material";

const MenuItem = ({ image, title, price, isNew }) => (
  <Card sx={{ height: "100%", display: "flex", flexDirection: "column" }}>
    <Box sx={{ position: "relative" }}>
      <CardMedia component="img" height="200" image={image} alt={title} />
      {isNew && (
        <Chip
          label="NEW"
          color="primary"
          sx={{
            position: "absolute",
            top: 10,
            left: 10,
            backgroundColor: "#8bc34a",
          }}
        />
      )}
    </Box>
    <CardContent>
      <Typography gutterBottom variant="h6" component="h2">
        {title}
      </Typography>
      <Typography variant="body1" color="primary" fontWeight="bold">
        {price.toLocaleString()}원
      </Typography>
    </CardContent>
  </Card>
);

export default MenuItem;
