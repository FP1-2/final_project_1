import axios from 'axios';
import { getTokenFromLocalStorage } from '../utils/localStorageHelper';


export const basicAx = axios.create({
  baseURL: 'https://yourhostel.world',
});

export const workAx = async (method, url, data) => {
  const token = getTokenFromLocalStorage();
  return basicAx({
    method: method,
    url: url,
    data: data,
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });
};
