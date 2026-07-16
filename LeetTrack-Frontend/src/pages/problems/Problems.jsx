import { useEffect, useState } from "react";
import Navbar from "../../components/Navbar";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import {
    getProblems,
    deleteProblem,
    toggleFavorite,
    searchProblems,
    filterByDifficulty
} from "../../services/problemService";
import {
    exportCsv,
    exportPdf
} from "../../services/problemService";

function Problems() {

    const [problems, setProblems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [selectedProblem, setSelectedProblem] = useState(null);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [search, setSearch] = useState("");
    const [difficulty, setDifficulty] = useState("");
    const [sortBy, setSortBy] = useState("solvedDate");
    const [direction, setDirection] = useState("desc");

    useEffect(() => {
        loadProblems();
    }, [search, difficulty, direction]);

    const openDeleteModal = (problem) => {

        setSelectedProblem(problem);
        setShowDeleteModal(true);

    };

    const handleSort = (column) => {

        if (sortBy === column) {

            setDirection(direction === "asc" ? "desc" : "asc");

        } else {

            setSortBy(column);
            setDirection("asc");

        }

    };

    const handleDifficulty = async (value) => {

        setDifficulty(value);

        try {

            if (value === "All") {
                loadProblems();
                return;
            }

            const response = await filterByDifficulty(value);

            setProblems(response.data);

        } catch (error) {

            toast.error("Failed to filter.");
        }

    };

    const handleSearch = async (value) => {

        setSearch(value);

        try {

            if (value.trim() === "") {

                loadProblems();
                return;

            }

            const response = await searchProblems(value);

            setProblems(response.data);

        } catch (error) {

            console.error(error);

            toast.error("Search failed");

        }

    };

    const handleFavorite = async (id) => {

        try {

            const response = await toggleFavorite(id);

            const updatedFavorite = response.data;

            setProblems(prev =>
                prev.map(problem =>
                    problem.id === id
                        ? { ...problem, favorite: updatedFavorite }
                        : problem
                )
            );

        } catch (error) {

            toast.error("Couldn't update favorite.");

        }

    };

    const handleDelete = async () => {

        try {

            await deleteProblem(selectedProblem.id);

            toast.success("Problem deleted successfully!");

            setProblems((prevProblems) =>
                prevProblems.filter(
                    (problem) => problem.id !== selectedProblem.id
                )
            );

            setShowDeleteModal(false);
            setSelectedProblem(null);

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                "Failed to delete problem."
            );

        }

    };

    const handleExport = async () => {

        try {

            const response = await exportCsv();

            const url = window.URL.createObjectURL(
                new Blob([response.data])
            );

            const link = document.createElement("a");

            link.href = url;
            link.setAttribute(
                "download",
                "leetcode-problems.csv"
            );

            document.body.appendChild(link);

            link.click();

            link.remove();

        } catch (error) {

            console.error(error);

        }

    };

    const handleExportPdf = async () => {

        try {

            const response = await exportPdf();

            const url = window.URL.createObjectURL(
                new Blob([response.data], { type: "application/pdf" })
            );

            const link = document.createElement("a");

            link.href = url;

            link.download = "LeetTrack_Report.pdf";

            document.body.appendChild(link);

            link.click();

            link.remove();

            window.URL.revokeObjectURL(url);

        } catch (error) {

            console.error(error);

        }

    };

    const loadProblems = async () => {

        try {

            const response = await getProblems(
                page,
                10,
                sortBy,
                direction
            );

            setProblems(response.data.content);
            setTotalPages(response.data.totalPages);

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

            <div className="flex justify-between items-center mb-6 gap-4">

                <h1 className="text-4xl font-bold text-white">
                    Problems
                </h1>

                <input
                    type="text"
                    placeholder="🔍 Search by title..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className="w-96 mt-3 bg-slate-800 text-white px-4 py-3 rounded-lg border border-slate-600 focus:outline-none focus:border-cyan-500"
                />

                <select
                    value={difficulty}
                    onChange={(e) => setDifficulty(e.target.value)}
                    className="mt-3 bg-slate-800 text-white px-4 py-3 rounded-lg border border-slate-600"
                >
                    <option>All</option>
                    <option>Easy</option>
                    <option>Medium</option>
                    <option>Hard</option>
                </select>

                <button
                    onClick={handleExport}
                    className="mt-3 bg-green-500 hover:bg-green-600 text-white px-5 py-3 rounded-xl font-semibold transition duration-200 shadow-md"
                >
                    📄 Export CSV
                </button>

                <button
                    onClick={handleExportPdf}
                    className="mt-3 bg-red-500 hover:bg-red-600 text-white px-5 py-3 rounded-xl font-semibold transition duration-200 shadow-md"
                >
                    📕 Export PDF
                </button>

                <Link
                    to="/problems/add"
                    className="mt-2 bg-cyan-500 hover:bg-cyan-600 text-white px-5 py-3 rounded-lg font-semibold"
                >
                    + Add Problem
                </Link>

            </div>

            <div className="overflow-x-auto">

                <table className="w-full table-auto bg-slate-800 rounded-xl overflow-hidden">

                    <thead className="bg-slate-700">

                        <tr>
                            <th
                                onClick={() => handleSort("leetcodeId")}
                                className="w-24 p-4 text-left text-white cursor-pointer hover:text-cyan-400"
                            >
                                LeetCode ID {sortBy === "leetcodeId" && (direction === "asc" ? "↑" : "↓")}
                            </th>
                            <th
                                onClick={() => handleSort("title")}
                                className="w-64 p-4 text-left text-white cursor-pointer hover:text-cyan-400"
                            >
                                Title {sortBy === "title" && (direction === "asc" ? "↑" : "↓")}
                            </th>
                            <th className="w-32 p-4 text-left text-white">Difficulty</th>
                            <th className="w-48 p-4 text-left text-white">Topic</th>
                            <th
                                onClick={() => handleSort("solvedDate")}
                                className="w-40 p-4 text-left text-white cursor-pointer hover:text-cyan-400"
                            >
                                Solved Date {sortBy === "solvedDate" && (direction === "asc" ? "↑" : "↓")}
                            </th>
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

                            <td className="p-4">
                                <Link
                                    to={`/problems/${problem.id}`}
                                    className="text-cyan-400 hover:underline"
                                >
                                    {problem.title}
                                </Link>
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

                            <td className="p-4 text-center">

                                <button
                                    onClick={() => handleFavorite(problem.id)}
                                    className="text-2xl hover:scale-125 transition"
                                >
                                    {problem.favorite ? "⭐" : "☆"}
                                </button>

                            </td>

                            <td className="p-4 text-center">

                                <div className="flex justify-center gap-2">

                                    <Link
                                        to={`/problems/edit/${problem.id}`}
                                        className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg"
                                    >
                                        Edit
                                    </Link>

                                    <button
                                        onClick={() => openDeleteModal(problem)}
                                        className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg"
                                    >
                                        Delete
                                    </button>

                                </div>

                            </td>

                        </tr>

                    ))}

                    </tbody>

                </table>

                <div className="flex justify-center items-center gap-4 mt-6">

                    <button
                        disabled={page === 0}
                        onClick={() => setPage(page - 1)}
                        className="bg-slate-700 text-white px-4 py-2 rounded disabled:opacity-40"
                    >
                        Previous
                    </button>

                    <span className="text-white">
                         Page {page + 1} of {totalPages}
                    </span>

                    <button
                        disabled={page === totalPages - 1}
                        onClick={() => setPage(page + 1)}
                        className="bg-cyan-500 text-white px-4 py-2 rounded disabled:opacity-40"
                    >
                        Next
                    </button>

                </div>

            </div>

            {showDeleteModal && (

                <div className="fixed inset-0 bg-black/60 flex justify-center items-center z-50">

                    <div className="bg-slate-800 rounded-xl p-8 w-96 shadow-2xl">

                        <h2 className="text-2xl font-bold text-white mb-4">
                            ⚠ Delete Problem
                        </h2>

                        <p className="text-slate-300 mb-6">
                            Are you sure you want to delete
                            <span className="font-semibold text-white">
                    {" "}{selectedProblem?.title}
                </span>
                            ?
                        </p>

                        <div className="flex justify-end gap-3">

                            <button
                                onClick={() => {
                                    setShowDeleteModal(false);
                                    setSelectedProblem(null);
                                }}
                                className="bg-slate-600 hover:bg-slate-500 text-white px-4 py-2 rounded-lg"
                            >
                                Cancel
                            </button>

                            <button
                                onClick={handleDelete}
                                className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg"
                            >
                                Delete
                            </button>

                        </div>

                    </div>

                </div>

            )}

        </div>

    );

}

export default Problems;