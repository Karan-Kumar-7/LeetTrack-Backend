import { useEffect, useState } from "react";
import { FaBookOpen, FaFire, FaTrophy } from "react-icons/fa";
import { MdCheckCircle } from "react-icons/md";
import { BsBarChartFill } from "react-icons/bs";
import Navbar from "../../components/Navbar";
import { getDashboard } from "../../services/dashboardService";

function Dashboard() {

    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        const fetchDashboard = async () => {

            try {

                const response = await getDashboard();

                setDashboard(response.data);

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);

            }

        };

        fetchDashboard();

    }, []);

    if (loading) {
        return (
            <div className="min-h-screen bg-slate-900 flex items-center justify-center text-white">
                Loading...
            </div>
        );
    }

    if (!dashboard) {
        return (
            <div className="min-h-screen bg-slate-900 flex items-center justify-center text-red-400">
                Failed to load dashboard.
            </div>
        );
    }

    return (

        <div className="min-h-screen bg-slate-900">

            <Navbar/>

            <div className="max-w-7xl mx-auto p-8">

                <h1 className="text-4xl font-bold text-white mb-8">
                    Dashboard
                </h1>

                <div className="grid md:grid-cols-3 lg:grid-cols-6 gap-6">

                    <StatCard
                        title="Solved"
                        value={dashboard.totalSolved}
                        icon={<FaBookOpen />}
                        color="bg-cyan-500"
                    />

                    <StatCard
                        title="Easy"
                        value={dashboard.difficultyDistribution.easy}
                        icon={<MdCheckCircle />}
                        color="bg-green-500"
                    />

                    <StatCard
                        title="Medium"
                        value={dashboard.difficultyDistribution.medium}
                        icon={<BsBarChartFill />}
                        color="bg-yellow-500"
                    />

                    <StatCard
                        title="Hard"
                        value={dashboard.difficultyDistribution.hard}
                        icon={<FaBookOpen />}
                        color="bg-red-500"
                    />

                    <StatCard
                        title="Current"
                        value={dashboard.streak.currentStreak}
                        icon={<FaFire />}
                        color="bg-orange-500"
                    />

                    <StatCard
                        title="Longest"
                        value={dashboard.streak.longestStreak}
                        icon={<FaTrophy />}
                        color="bg-purple-500"
                    />

                </div>

            </div>

        </div>

    );

}

function StatCard({ title, value, icon, color }) {

    return (

        <div className="bg-slate-800 rounded-2xl p-6 shadow-xl">

            <div className="flex justify-between items-center">

                <div>

                    <p className="text-slate-400">
                        {title}
                    </p>

                    <h2 className="text-3xl font-bold text-white mt-2">
                        {value}
                    </h2>

                </div>

                <div className={`${color} p-4 rounded-xl text-white text-2xl`}>

                    {icon}

                </div>

            </div>

        </div>

    );

}

export default Dashboard;