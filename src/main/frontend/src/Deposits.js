import * as React from 'react';
import { useState } from 'react';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';
import Title from './Title';
import { Button, MenuItem, InputLabel, Select, FormControl }  from '@mui/material';

function preventDefault(event) {
  event.preventDefault();
}



export default function Deposits() {
  return (
    <React.Fragment>
      

      <Button variant="contained" component="label">
        Upload File
        <input type="file" hidden />
      </Button>

    </React.Fragment>
  );
}
