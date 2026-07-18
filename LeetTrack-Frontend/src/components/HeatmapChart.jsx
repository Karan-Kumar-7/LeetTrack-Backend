import CalendarHeatmap from "react-calendar-heatmap";
import "react-calendar-heatmap/dist/styles.css";
import { Tooltip } from "react-tooltip";
import { subYears } from "date-fns";

function HeatmapChart({ data }) {

    return (
        <div className="bg-slate-800 rounded-2xl p-6 mt-8">

            <h2 className="text-2xl font-bold text-white mb-6">
                Solving Activity
            </h2>

            <CalendarHeatmap
                startDate={subYears(new Date(), 1)}
                endDate={new Date()}
                values={data}
                classForValue={(value) => {

                    if (!value) return "color-empty";

                    if (value.count >= 5) return "color-github-4";
                    if (value.count >= 3) return "color-github-3";
                    if (value.count >= 2) return "color-github-2";

                    return "color-github-1";
                }}
                tooltipDataAttrs={(value) => {

                    if (!value.date) return null;

                    return {
                        "data-tooltip-id": "heatmap-tooltip",
                        "data-tooltip-content":
                            `${value.count} problem(s) solved on ${value.date}`
                    };

                }}
            />

            <Tooltip
                id="heatmap-tooltip"
                place="top"
            />

        </div>
    );
}

export default HeatmapChart;