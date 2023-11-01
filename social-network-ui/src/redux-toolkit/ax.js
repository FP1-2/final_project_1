import axios from 'axios';

export const basicAx = axios.create({
  // baseURL: 'https://yourhostel.world',
  baseURL: 'http://localhost:9000/',
});

export const workAx = async (method, url, data) => {
  const { getToken } = await import('./store');
  const token = getToken();
  return basicAx({
    method: method,
    url: url,
    data: data,
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });
};
