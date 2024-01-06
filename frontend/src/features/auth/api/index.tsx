import { AuthResponse, LoginRequest, RegisterRequest } from '../types';
import { axiosClient } from 'src/lib/axios';

export const registerUser = (data: RegisterRequest): Promise<AuthResponse> => {
  return axiosClient.post('auth/register', data);
};

export const loginUser = (data: LoginRequest): Promise<AuthResponse> => {
  return axiosClient.post('auth/login', data);
};
