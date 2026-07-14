import { useEffect, useState } from "react";
import { getProblems } from "../../services/problemService";
import Navbar from "../../components/Navbar";
import { Link } from "react-router-dom";

function Problems() {

    const [problems, setProblems] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadProblems();
    }, []);

    const loadProblems = async () => {

        try {

            const response = await getProblems(0, 10);

            setProblems(response.data.content);

        } catch (error) {

            console.error(error);

        } finally {

            setLoading(false);

        }

    };

    if (loading) {

        return (
            <div className="min-h-screen flex justify-center items-center">
                Loading...
            </div>
        );

    }

    return (

        <div className="min-h-screen bg-slate-900 p-8">

            <Navbar/>

            <div className="flex justify-between items-center mb-6">

                <h1 className="text-4xl font-bold text-white">
                    Problems
                </h1>

                <Link
                    to="/problems/add"
                    className="bg-cyan-500 hover:bg-cyan-600 text-white px-5 py-3 rounded-lg font-semibold"
                >
                    + Add Problem
                </Link>

            </div>

            <div className="overflow-x-auto">

                <table className="w-full table-auto bg-slate-800 rounded-xl overflow-hidden">

                    <thead className="bg-slate-700">

                        <tr>
                            <th className="w-24 p-4 text-left text-white">LeetCode ID</th>
                            <th className="w-64 p-4 text-left text-white">Title</th>
                            <th className="w-32 p-4 text-left text-white">Difficulty</th>
                            <th className="w-48 p-4 text-left text-white">Topic</th>
                            <th className="w-40 p-4 text-left text-white">Solved Date</th>
                            <th className="w-24 p-4 text-center text-white">Favorite</th>
                            <th className="w-32 p-4 text-center text-white">Actions</th>
                        </tr>

                    </thead>

                    <tbody>

                    {problems.map(problem => (

                        <tr
                            key={problem.id}
                            className="border-b border-slate-700 hover:bg-slate-700 transition"
                        >

                            <td className="p-4 text-white">{problem.leetcodeId}</td>

                            <td className="p-4 text-white">
                                {problem.title}
                            </td>

                            <td className="p-4">

                                <span
                                    className={`px-3 py-1 rounded-full text-white text-sm
                                    
                                    ${problem.difficulty === "Easy"
                                        ? "bg-green-500"
                                        : problem.difficulty === "Medium"
                                            ? "bg-yellow-500"
                                            : "bg-red-500"
                                    }`}
                                >

                                    {problem.difficulty}

                                </span>

                            </td>

                            <td className="p-4 text-white">
                                {problem.topic}
                            </td>

                            <td className="p-4 text-white">
                                {problem.solvedDate}
                            </td>

                            <td className="p-4 text-center text-yellow-400 text-xl">

                                {problem.favorite ? "⭐" : "☆"}

                            </td>

                            <td className="p-4 text-center">

                                <Link
                                    to={`/problems/edit/${problem.id}`}
                                    className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg"
                                >
                                    Edit
                                </Link>

                            </td>

                        </tr>

                    ))}

                    </tbody>

                </table>

            </div>

        </div>

    );

}

export default Problems;