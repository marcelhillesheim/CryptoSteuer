import * as React from 'react';
import { useState, useEffect } from 'react';
import { Button, MenuItem, InputLabel, Select, FormControl }  from '@mui/material';
import http from '../http-common'
import Title from './Title';
import {useSnackbar } from 'notistack';

function preventDefault(event) {
  event.preventDefault();
}

export default function BulkUpload() {

  const { enqueueSnackbar } = useSnackbar();

  const [selectedTradingPlatform, setSelectedTradingPlatform] = useState("");
  const [tradingPlatforms, setTradingPlatforms] = useState([]);
  const [file, setFile] = useState();

  useEffect(() => {
    http.get("/api/v1/tradingPlatform")
    .then(res => setTradingPlatforms(res.data))
    .catch(err => console.log(err));
  }, [])

  const handleChangedSelect = event => {
    setSelectedTradingPlatform(event.target.value);
  };

  const handleChangedFile = event => {
    setFile(event.target.files[0]);
  };

  const uploadFile = () => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("trading_platform", selectedTradingPlatform)
    http.post("/api/v1/bulk_upload", formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        }
      } 
    )
    .then(res => {
      console.log(res);
      enqueueSnackbar("Successfully uploaded file", { variant: 'success' });
    })
    .catch(err => {
      console.log(err);
      enqueueSnackbar(err.response.data.message, { variant: 'error' });
    });
  }

  return (
    <React.Fragment>
      <Title>Import Transactions</Title>
      <FormControl fullWidth>
        <InputLabel id="tradingPlatformSelectLabel">Trading platform</InputLabel>
        <Select
          labelId="tradingPlatformSelectLabel"
          id="tradingPlatformSelect"
          value={selectedTradingPlatform}
          label="Trading platform"
          onChange={e => handleChangedSelect(e)}
        >
          {
            tradingPlatforms.map((tradingPlatform) => (
              <MenuItem value={tradingPlatform} key={tradingPlatform}>
                {tradingPlatform}
              </MenuItem>
            ))
          }
        </Select>
      <input type="file" onChange={e => handleChangedFile(e)}/>
      <Button variant="contained" component="label" onClick={() => uploadFile()}>
        Upload File
      </Button>
      </FormControl>

    </React.Fragment>
  );
}
