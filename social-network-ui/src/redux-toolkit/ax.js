import axios from 'axios';
import { getTokenFromLocalStorage } from '../utils/localStorageHelper';


export const basicAx = axios.create({
  baseURL: 'http://13.53.82.220:9000/',
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
