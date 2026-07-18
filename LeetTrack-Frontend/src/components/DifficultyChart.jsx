import {
    PieChart,
    Pie,
    Cell,
    ResponsiveContainer,
    Tooltip,
    Legend
} from "recharts";

const COLORS = [
    "#22c55e",
    "#facc15",
    "#ef4444"
];

function DifficultyChart({ data }) {

    const chartData = [
        { name: "Easy", value: data.easy },
        { name: "Medium", value: data.medium },
        { name: "Hard", value: data.hard }
    ];

    return (

        <div className="bg-slate-800 rounded-2xl p-6 shadow-lg h-[420px]">

            <h2 className="text-2xl font-bold text-white text-center mb-6">
                Difficulty Distribution
            </h2>

            <div className="h-[320px]">

                <ResponsiveContainer width="100%" height="100%">

                    <PieChart>

                        <Pie
                            data={chartData}
                            dataKey="value"
                            nameKey="name"
                            cx="50%"
                            cy="45%"
                            innerRadius={60}
                            outerRadius={110}
                            paddingAngle={4}
                            label
                        >

                            {chartData.map((entry, index) => (

                                <Cell
                                    key={index}
                                    fill={COLORS[index]}
                                />

                            ))}

                        </Pie>

                        <Tooltip />

                        <Legend
                            verticalAlign="bottom"
                            align="center"
                            iconType="circle"
                            wrapperStyle={{
                                paddingTop: "20px"
                            }}
                        />

                    </PieChart>

                </ResponsiveContainer>

            </div>

        </div>

    );

}

export default DifficultyChart;