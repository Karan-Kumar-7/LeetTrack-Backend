import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import toast from "react-hot-toast";

import Navbar from "../../components/Navbar";
import ProblemForm from "../../components/problems/ProblemForm";
import { getProblem, updateProblem } from "../../services/problemService";

function EditProblem() {

    const { id } = useParams();
    const navigate = useNavigate();

    const [problem, setProblem] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchProblem();
    }, []);

    async function fetchProblem() {

        try {

            const response = await getProblem(id);

            setProblem(response.data);

        } catch (error) {

            toast.error("Problem not found");

            navigate("/problems");

        } finally {

            setLoading(false);

        }

    }

    async function onSubmit(data) {

        try {

            await updateProblem(id, data);

            toast.success("Problem updated successfully!");

            navigate("/problems");

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                "Failed to update problem"
            );

        }

    }

    if (loading) {

        return (
            <div className="min-h-screen flex justify-center items-center">
                Loading...
            </div>
        );

    }

    return (

        <div className="min-h-screen bg-slate-900">

            <Navbar />

            <div className="p-8">

                <h1 className="text-4xl text-white font-bold mb-8">
                    Edit Problem
                </h1>

                <ProblemForm
                    defaultValues={problem}
                    onSubmit={onSubmit}
                />

            </div>

        </div>

    );

}

export default EditProblem;