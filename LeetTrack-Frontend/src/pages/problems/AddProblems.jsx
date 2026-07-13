import { useState } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

import Navbar from "../../components/Navbar";
import ProblemForm from "../../components/problems/ProblemForm";
import { addProblem } from "../../services/problemService";

function AddProblem() {

    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const onSubmit = async (data) => {

        try {

            setLoading(true);

            await addProblem(data);

            toast.success("Problem added successfully!");

            navigate("/problems");

        } catch (error) {

            toast.error(
                error.response?.data?.message || "Failed to add problem."
            );

        } finally {

            setLoading(false);

        }

    };

    return (

        <div className="min-h-screen bg-slate-900">

            <Navbar />

            <div className="p-8">

                <h1 className="text-4xl font-bold text-white text-center mb-8">
                    Add Problem
                </h1>

                <ProblemForm
                    onSubmit={onSubmit}
                    loading={loading}
                />

            </div>

        </div>

    );

}

export default AddProblem;