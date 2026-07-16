import { getProblem } from "../../services/problemService";
import Navbar from "../../components/Navbar";
import { Link, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { ExternalLink } from "lucide-react";

function ProblemDetails() {

    const { id } = useParams();
    const [problem, setProblem] = useState(null);

    useEffect(() => {

        loadProblem();

    }, []);

    const loadProblem = async () => {

        try {

            console.log("Problem ID:", id);

            const response = await getProblem(id);

            console.log(response.data);

            setProblem(response.data);

        } catch (error) {

            console.error(error);

        }

    };

    if (!problem) {

        return (
            <div className="min-h-screen bg-slate-900 flex items-center justify-center text-white">
                Loading...
            </div>
        );

    }

    return (
        <div className="min-h-screen bg-slate-900">

            <Navbar />

            <div className="max-w-6xl mx-auto p-8">

                <div className="bg-slate-800 rounded-2xl shadow-xl p-8">

                    <div className="flex justify-between items-start">

                        <div>

                            <h1 className="text-4xl font-bold text-white">
                                {problem.title}
                            </h1>

                            <p className="text-slate-400 mt-2">
                                LeetCode #{problem.leetcodeId}
                            </p>

                            {problem.problemLink && (
                                <a
                                    href={problem.problemLink}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="inline-flex items-center gap-2 mt-4 bg-yellow-500 hover:bg-yellow-600 text-black font-semibold px-5 py-3 rounded-xl transition shadow-lg"
                                >
                                    <ExternalLink size={18} />
                                    Open on LeetCode
                                </a>
                            )}

                            <p className="text-white">{problem.problemLink}</p>

                        </div>

                        <Link
                            to={`/problems/edit/${problem.id}`}
                            className="bg-cyan-500 hover:bg-cyan-600 px-5 py-3 rounded-xl text-white font-semibold transition"
                        >
                            Edit Problem
                        </Link>

                    </div>

                    <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6 mt-10">

                        <div className="bg-slate-700 rounded-xl p-5">
                            <p className="text-slate-400 mb-2">Difficulty</p>

                            <span
                                className={`px-4 py-2 rounded-full text-white
                            ${
                                    problem.difficulty === "Easy"
                                        ? "bg-green-500"
                                        : problem.difficulty === "Medium"
                                            ? "bg-yellow-500"
                                            : "bg-red-500"
                                }`}
                            >
                            {problem.difficulty}
                             </span>
                        </div>

                        <div className="bg-slate-700 rounded-xl p-5">
                            <p className="text-slate-400 mb-2">Topic</p>

                            <p className="text-white font-semibold">
                                {problem.topic}
                            </p>
                        </div>

                        <div className="bg-slate-700 rounded-xl p-5">
                            <p className="text-slate-400 mb-2">Solved Date</p>

                            <p className="text-white font-semibold">
                                {problem.solvedDate}
                            </p>
                        </div>

                        <div className="bg-slate-700 rounded-xl p-5">
                            <p className="text-slate-400 mb-2">Favorite</p>

                            <p className="text-3xl">
                                {problem.favorite ? "⭐" : "☆"}
                            </p>
                        </div>

                    </div>

                    <div className="grid lg:grid-cols-2 gap-8 mt-10">

                        <div className="bg-slate-700 rounded-xl p-6">

                            <h2 className="text-2xl font-bold text-white mb-4">
                                📝 Notes
                            </h2>

                            <p className="text-slate-300 whitespace-pre-wrap">
                                {problem.notes || "No notes added yet."}
                            </p>

                        </div>

                        <div className="space-y-6">

                            <div className="bg-slate-700 rounded-xl p-6">

                                <h2 className="text-xl font-bold text-white mb-3">
                                    ⏱ Time Complexity
                                </h2>

                                <p className="text-cyan-400 text-lg font-semibold">
                                    {problem.timeComplexity || "-"}
                                </p>

                            </div>

                            <div className="bg-slate-700 rounded-xl p-6">

                                <h2 className="text-xl font-bold text-white mb-3">
                                    💾 Space Complexity
                                </h2>

                                <p className="text-cyan-400 text-lg font-semibold">
                                    {problem.spaceComplexity || "-"}
                                </p>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </div>
    );
}

export default ProblemDetails;