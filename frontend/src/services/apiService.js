import axios from 'axios';

const API_URL = 'http://localhost:8080';

export const registerUser = (user) => {
    return axios.post(`${API_URL}/users/register`, user);
};

export const createExpense = (expense) => {
    return axios.post(`${API_URL}/expenses`, expense);
};

export const createTransaction = (transaction) => {
    return axios.post(`${API_URL}/transactions`, transaction);
};
