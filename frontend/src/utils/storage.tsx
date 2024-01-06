const STORAGE_PREFIX = 'expense_echo';

export default {
  getToken: () => {
    return localStorage.getItem(`${STORAGE_PREFIX}_accessToken`);
  },
  setToken: (value: string) => {
    localStorage.setItem(`${STORAGE_PREFIX}_accessToken`, value);
  },
  clearToken: () => {
    localStorage.removeItem(`${STORAGE_PREFIX}_accessToken`);
  },
};
