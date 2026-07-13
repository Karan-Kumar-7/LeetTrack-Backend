import api from "../api/axios";

export const getProblems = (page = 0, size = 10) => {
    return api.get(`/problems?page=${page}&size=${size}`);
};

export const addProblem = (problem) => {
    return api.post("/problems", problem);
};