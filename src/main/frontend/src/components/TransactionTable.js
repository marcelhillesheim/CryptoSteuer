import { useState, useEffect } from 'react';
import http from '../http-common';
import { DataGrid } from '@mui/x-data-grid';


export default function TransactionTable() {

  const [transactions, setTransactions] = useState("");

  useEffect(() => {
    http.get("/api/v1/transactions")
    .then(res => setTransactions(res.data))
    .catch(err => console.log(err));
  }, []);

  const columns = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'tradingPlatform', headerName: 'Trading Platform', width: 130 },
    { field: 'currencyA', headerName: 'Currency A', width: 130 },
    { field: 'amountA', headerName: 'Amount A', type: 'number', width: 130 },
    { field: 'currencyB', headerName: 'Currency B', width: 130 },
    { field: 'amountB', headerName: 'Amount B', type: 'number', width: 130 },
    { field: 'executionDate', headerName: 'date', width: 130 },
  ];

  //TODO rows per page rowsPerPageOptions={[5,10,50,100]}

  return (
    <>
      <DataGrid
        autoHeight={true}
        rows={transactions}
        columns={columns}
        pageSize={10}
        checkboxSelection
      />
    </>
  );
}