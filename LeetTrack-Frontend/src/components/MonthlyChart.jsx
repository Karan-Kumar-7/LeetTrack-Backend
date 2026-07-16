import {
    ResponsiveContainer,
    BarChart,
    Bar,
    XAxis,
    YAxis,
    Tooltip,
    CartesianGrid
} from "recharts";

function MonthlyChart({ data }) {

    const months = [
        "Jan","Feb","Mar","Apr","May","Jun",
        "Jul","Aug","Sep","Oct","Nov","Dec"
    ];

    const chartData = data.map(item => ({
        month: months[item.month - 1],
        solved: item.count
    }));

    return (

        <div className="bg-slate-800 rounded-2xl p-6 shadow-lg h-[420px]">

            <h2 className="text-2xl font-bold text-white text-center mb-6">
                Monthly Activity
            </h2>

            <div className="h-[320px]">

                <ResponsiveContainer width="100%" height="100%">

                    <BarChart
                        data={chartData}
                        margin={{
                            top: 10,
                            right: 20,
                            left: 0,
                            bottom: 10
                        }}
                    >

                        <CartesianGrid
                            strokeDasharray="3 3"
                            stroke="#334155"
                        />

                        <XAxis
                            dataKey="month"
                            stroke="#cbd5e1"
                        />

                        <YAxis
                            stroke="#cbd5e1"
                            allowDecimals={false}
                        />

                        <Tooltip />

                        <Bar
                            dataKey="solved"
                            fill="#06b6d4"
                            radius={[8,8,0,0]}
                            maxBarSize={55}
                        />

                    </BarChart>

                </ResponsiveContainer>

            </div>

        </div>

    );

}

export default MonthlyChart;