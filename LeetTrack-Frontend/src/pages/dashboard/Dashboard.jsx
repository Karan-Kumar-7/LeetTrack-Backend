import { useEffect, useState } from "react";
import Navbar from "../../components/Navbar";
import { getDashboard } from "../../services/dashboardService";
import DifficultyChart from "../../components/DifficultyChart";
import MonthlyChart from "../../components/MonthlyChart";
import RecentProblems from "../../components/RecentProblems";
import FavoriteProblems from "../../components/FavoriteProblems";
import {
    BookOpen,
    Flame,
    Trophy,
    Star
} from "lucide-react";

import DashboardCard from "../../components/DashboardCard";
import { getHeatmap } from "../../services/problemService";
import HeatmapChart from "../../components/HeatmapChart";
import {data} from "react-router-dom";

function Dashboard() {

    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(true);
    const [heatmapData, setHeatmapData] = useState([]);

    useEffect(() => {

        const fetchDashboard = async () => {

            try {

                const response = await getDashboard();

                const heatmap = await getHeatmap();

                setHeatmapData(heatmap.data);

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

                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6 mt-8">

                    <DashboardCard
                        title="Solved Problems"
                        value={dashboard.totalSolved}
                        color="bg-cyan-500"
                        icon={<BookOpen size={30} color="white" />}
                    />

                    <DashboardCard
                        title="Current Streak"
                        value={dashboard.streak.currentStreak}
                        color="bg-orange-500"
                        icon={<Flame size={30} color="white" />}
                    />

                    <DashboardCard
                        title="Longest Streak"
                        value={dashboard.streak.longestStreak}
                        color="bg-purple-500"
                        icon={<Trophy size={30} color="white" />}
                    />

                    <DashboardCard
                        title="Favorites"
                        value={dashboard.favorites.length}
                        color="bg-yellow-500"
                        icon={<Star size={30} color="white" />}
                    />

                </div>

                <div className="flex justify-between items-center mb-6">

                    <h2 className="text-2xl font-bold text-white">
                        Solving Activity
                    </h2>

                    <p className="text-slate-400">

                        {(Array.isArray(data) ? data : []).reduce(
                            (sum, day) => sum + day.count,
                            0
                        )}
                        {" "}problems solved this year

                    </p>

                </div>

                <HeatmapChart data={heatmapData} />

                <div className="flex justify-end items-center gap-2 mt-5 text-slate-300 text-sm">

                    <span>Less</span>

                    <div className="w-4 h-4 rounded bg-slate-700"></div>

                    <div className="w-4 h-4 rounded bg-green-900"></div>

                    <div className="w-4 h-4 rounded bg-green-700"></div>

                    <div className="w-4 h-4 rounded bg-green-500"></div>

                    <div className="w-4 h-4 rounded bg-green-400"></div>

                    <span>More</span>

                </div>

                <div className="grid grid-cols-1 xl:grid-cols-2 gap-8 mt-10">

                    <DifficultyChart
                        data={dashboard.difficultyDistribution}
                    />

                    <MonthlyChart
                        data={dashboard.monthlyActivity}
                    />

                </div>

                <div className="grid grid-cols-1 xl:grid-cols-2 gap-8 mt-10">

                    <RecentProblems
                        problems={dashboard.recentFive}
                    />

                    <FavoriteProblems
                        problems={dashboard.favorites}
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