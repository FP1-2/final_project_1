import axios from 'axios';
import {jwtDecode} from 'jwt-decode';

const baseURL = process.env.REACT_APP_BASE_URL;

export const basicAx = axios.create({
    baseURL: baseURL,
});

const isTokenValid = (token) => {
    try {
        return jwtDecode(token).exp > Date.now() / 1000;
    } catch (error) {
        return false;
    }
};

export const workAx = async (method, url, data) => {
    const {getToken, logout} = await import('./store');
    const token = getToken();

    // Якщо токен невалідний, виходимо з облікового запису і перериваємо виконання
    if (!isTokenValid(token)) {
        await logout();
        throw new Error('Token expired');
    }

    // Якщо токен валідний, продовжуємо із запитом
    return basicAx({
        method: method,
        url: url,
        data: data,
        headers: token ? {Authorization: `Bearer ${token}`} : {},
    }).catch(async (error) => {
        switch (error.response.status) {
            case 403: // 403 Forbidden.
                await logout();
                break; // Виходимо із switch, помилка оброблена.
            default:
                throw error; // Якщо статус не 401 чи 403, прокидаємо помилку далі.
        }
    });
};

