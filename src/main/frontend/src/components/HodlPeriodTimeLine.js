import Chart from "react-apexcharts";
import { useState, useEffect } from 'react';
import http from '../http-common';

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
        const startDate = new Date(hodlPeriods[i]["startDate"]).getTime();
        let endDate = new Date(hodlPeriods[i]["endDate"]).getTime();
        if (endDate == 0) {
            //TODO check timezone
            endDate = new Date().getTime();
        }

        let period = {
            data: [{
                x: '',
                y: [
                startDate,
                endDate
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
                    //import PanToolIcon from '@mui/icons-material/PanTool';
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
            let endDate = hodlPeriod.endDate;
            if (endDate == null) {
                endDate = "now";
            }

            let endCurrency = "";
            if (hodlPeriod.endTransaction != null) {
                endCurrency = " -> " +  hodlPeriod.endTransaction.currencyB;
            }
                
            return '<div class="arrow_box">' +
                hodlPeriod.startDate + ' - ' +  endDate + '<br>' + 
                hodlPeriod.startTransaction.currencyA + ' -> ' +  hodlPeriod.startTransaction.currencyB + endCurrency+ '<br>' +
                'Amount: ' +  hodlPeriod.amount + '<br>' +  
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