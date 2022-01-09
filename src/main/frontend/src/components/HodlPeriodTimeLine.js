import Chart from "react-apexcharts";
import { useState, useEffect } from 'react';
import http from '../http-common';
import PanToolIcon from '@mui/icons-material/PanTool';

export default function HodlPeriodTimeLine() {

    const [hodlPeriods, setHodlPeriods] = useState("");
  
    useEffect(() => {
      //TODO use user input for currency
      http.get("/api/v1/hodl/ETHEREUM")
      .then(res => setHodlPeriods(res.data))
      .catch(err => console.log(err));
    }, [])

    console.log(hodlPeriods);

    let series = [];

    for(let i = 0; i < hodlPeriods.length; i++) {
        let period = {
            data: [{
                x: '',
                y: [
                new Date(hodlPeriods[i]["startDate"]).getTime(),
                //TODO current date, if there is no endDate -> still holding 
                new Date(hodlPeriods[i]["endDate"]).getTime(),
                ],
                hodlPeriod: hodlPeriods[i],
                //TODO change color based on tax relevant or not -> longer than 1 year, shorter than 1 year and still holding
                fillColor: '#1976d2',
                //TODO change height based on transaction amount
            }]
        };
        series.push(period);
        console.log(hodlPeriods[0]);
    }

    console.log(series);

    const options = {
        chart: {
            id: "hodl-bar-graph",
            toolbar:{
                tools: {
                    zoomin: true,
                    zoomout: true,
                    selection: true,
                    pan: true,
                    reset: true,
                    download: true,
                    //TODO add refresh 
                    //TODO change icons to MUI icons
                }
            }
        },
        plotOptions: {
            bar: {
              horizontal: true
            }
        },
        xaxis: {
            type: 'datetime'
        },

        tooltip: {
            custom: function({series, seriesIndex, dataPointIndex, w}) {
            const hodlPeriod = w.config.series[seriesIndex].data[dataPointIndex].hodlPeriod;
            return '<div class="arrow_box">' +
                '<span>' +  hodlPeriod.startDate + ' - ' +  hodlPeriod.endDate +
                '</span>' +
                '</div>'
            }
        },

        legend: {
            show: false
        },

    };
    
    return (
        <>
        <Chart
            options={options}
            series={series}
            type="rangeBar"
            width="100%"
        />
        </>
    );
}