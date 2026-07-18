function FavoriteProblems({ problems }) {

    return (

        <div className="bg-slate-800 rounded-2xl shadow-lg p-6 h-full">

            <h2 className="text-2xl font-bold text-white mb-6">
                ⭐ Favorite Problems
            </h2>

            {problems.length === 0 ? (

                <p className="text-slate-400">
                    No favorite problems yet.
                </p>

            ) : (

                <div className="space-y-4">

                    {problems.map(problem => (

                        <div
                            key={problem.id}
                            className="
                                flex
                                justify-between
                                items-center
                                bg-slate-700
                                rounded-xl
                                p-4
                                hover:bg-slate-600
                                transition
                            "
                        >

                            <div>

                                <h3 className="text-white font-semibold">
                                    {problem.title}
                                </h3>

                                <p className="text-slate-400 text-sm">
                                    {problem.topic}
                                </p>

                            </div>

                            <span
                                className={`px-3 py-1 rounded-full text-white text-sm
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

                    ))}

                </div>

            )}

        </div>

    );

}

export default FavoriteProblems;