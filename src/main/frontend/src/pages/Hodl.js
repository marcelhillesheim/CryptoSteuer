import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import BulkUpload from '../components/BulkUpload';
import Chart from "react-apexcharts";
import {useState} from "react";
import HodlPeriodTimeLine from '../components/HodlPeriodTimeLine';

export default function Hodl() {

    return (
        <><Grid container spacing={3}>
            <Grid item xs={12} md={8} lg={9}>
                <Paper
                    sx={{
                        p: 2,
                        display: 'flex',
                        flexDirection: 'column',
                        
                    }}
                >
                <HodlPeriodTimeLine/>
                </Paper>
            </Grid>
            <Grid item xs={12} md={4} lg={3}>
                <Paper
                    sx={{
                        p: 2,
                        display: 'flex',
                        flexDirection: 'column',
                        height: 240,
                    }}
                >
                    <BulkUpload />
                </Paper>
            </Grid>
        </Grid></>
    );
}