import Axios from 'axios';
import { API_URL } from 'src/config';
import storage from 'src/utils/storage';

export const axiosClient = Axios.create({
  baseURL: API_URL,
});

axiosClient.interceptors.request.use((config) => {
  config.headers!.Accept = 'application/json';
  const token = storage.getToken();
  if (token) {
    config.headers!.Authorization = `Bearer ${token};`;
  }
  return config;
});

axiosClient.interceptors.response.use(
  (response) => response.data,
  (error) => Promise.reject(error)
);
