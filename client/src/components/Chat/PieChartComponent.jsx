import React from "react";
import { Chart } from "react-google-charts";
import { ESentiment } from "../../utils";

function PieChartComponent({ sentiment }) {
    const data1 = sentiment
        ? sentiment?.sentimentDetails?.map((detail) => ([
              ESentiment.getViFromName(detail.sentiment),
              detail.total,
        ]))
        : [
              [ ESentiment.NEGATIVE.vi, 0 ],
              [ ESentiment.NEUTRAL.vi, 0 ],
              [ ESentiment.POSITIVE.vi, 0 ],
          ];
          console.log(sentiment?.sentimentDetails)
    const data = [["Sentiment", "Total"], ...data1];
// const data = [
//         ["Task", "Hours per Day"],
//         ["Work", 11],
//         ["Eat", 2],
//         ["Commute", 2],
//         ["Watch TV", 2],
//         ["Sleep", 7],
//       ];
      
const options = {
        title: "Phân tích quan điểm (Analysis sentiment)",
        is3D: true,
      };
    return (
        // <Chart data={data} chartType="PieChart">
        //     <Legend />

        //     <PieSeries valueField="percent" argumentField="sentiment" />
        //     <Title text="Analysis sentiment" />
        //     <Animation />
        // </Chart>
        <Chart
        chartType="PieChart"
        data={data}
        options={options}
        // width={"100%"}
        // height={"400px"}
      />
    );
}

export default PieChartComponent;
