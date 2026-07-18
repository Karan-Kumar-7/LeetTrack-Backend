import React from "react";

function DashboardCard({ title, value, icon, color }) {

    return (

        <div
            className="
            bg-slate-800
            rounded-2xl
            p-6
            shadow-lg
            hover:shadow-cyan-500/20
            hover:-translate-y-1
            transition-all
            duration-300"
        >

            <div className="flex justify-between items-center">

                <div>

                    <p className="text-slate-400 text-sm">
                        {title}
                    </p>

                    <h2 className="text-4xl font-bold text-white mt-2">
                        {value}
                    </h2>

                </div>

                <div
                    className={`p-4 rounded-xl ${color}`}
                >

                    {icon}

                </div>

            </div>

        </div>

    );

}

export default DashboardCard;