import { useForm } from "react-hook-form";

function ProblemForm({ onSubmit, loading }) {

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm({
        defaultValues: {
            solvedDate: new Date().toISOString().split("T")[0],
            favorite: false,
        },
    });

    return (

        <form
            onSubmit={handleSubmit(onSubmit)}
            className="bg-slate-800 rounded-xl p-8 max-w-2xl mx-auto space-y-6"
        >

            <div>

                <label className="text-white block mb-2">
                    LeetCode ID
                </label>

                <input
                    type="number"
                    {...register("leetcodeId", {
                        required: "LeetCode ID is required",
                    })}
                    className="w-full rounded-lg p-3 bg-slate-700 text-white"
                />

                <p className="text-red-400 text-sm mt-1">
                    {errors.leetcodeId?.message}
                </p>

            </div>

            <div>

                <label className="text-white block mb-2">
                    Title
                </label>

                <input
                    {...register("title", {
                        required: "Title is required",
                    })}
                    className="w-full rounded-lg p-3 bg-slate-700 text-white"
                />

                <p className="text-red-400 text-sm mt-1">
                    {errors.title?.message}
                </p>

            </div>

            <div className="grid grid-cols-2 gap-6">

                <div>

                    <label className="text-white block mb-2">
                        Difficulty
                    </label>

                    <select
                        {...register("difficulty")}
                        className="w-full rounded-lg p-3 bg-slate-700 text-white"
                    >
                        <option>Easy</option>
                        <option>Medium</option>
                        <option>Hard</option>
                    </select>

                </div>

                <div>

                    <label className="text-white block mb-2">
                        Topic
                    </label>

                    <input
                        {...register("topic", {
                            required: "Topic is required",
                        })}
                        className="w-full rounded-lg p-3 bg-slate-700 text-white"
                    />

                </div>

            </div>

            <div>

                <label className="text-white block mb-2">
                    Solved Date
                </label>

                <input
                    type="date"
                    {...register("solvedDate")}
                    className="w-full rounded-lg p-3 bg-slate-700 text-white"
                />

            </div>

            <div className="flex items-center gap-3">

                <input
                    type="checkbox"
                    {...register("favorite")}
                />

                <label className="text-white">
                    Mark as Favorite
                </label>

            </div>

            <button
                disabled={loading}
                className="w-full bg-cyan-500 hover:bg-cyan-600 text-white py-3 rounded-lg font-semibold"
            >
                {loading ? "Saving..." : "Save Problem"}
            </button>

        </form>

    );

}

export default ProblemForm;