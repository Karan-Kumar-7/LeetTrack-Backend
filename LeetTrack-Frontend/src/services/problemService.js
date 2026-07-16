import api from "../api/axios";


export const addProblem = (problem) => {
    return api.post("/problems", problem);
};

export const getProblem = (id) => {
    return api.get(`/problems/${id}`);
};

export const updateProblem = (id, problem) => {
    return api.put(`/problems/${id}`, problem);
};

export const deleteProblem = (id) => {
    return api.delete(`/problems/${id}`);
};

export const toggleFavorite = (id) => {
    return api.patch(`/problems/${id}/favorite`);
};

export const searchProblems = (title) => {
    return api.get(`/problems/search?title=${title}`);
};

export const filterByDifficulty = (difficulty) => {
    return api.get(`/problems/filter?difficulty=${difficulty}`);
};


export const getProblems = (
    page = 0,
    size = 10,
    sort = "solvedDate",
    direction = "desc"
) => {
    return api.get(
        `/problems?page=${page}&size=${size}&sort=${sort},${direction}`
    );
};

export const exportCsv = () => {
    return api.get("/problems/export", {
        responseType: "blob",
    });
};

export const exportPdf = () => {
    return api.get("/problems/export/pdf", {
        responseType: "blob",
    });
};

export const getHeatmap = () => {
    return api.get("/problems/heatmap");
};