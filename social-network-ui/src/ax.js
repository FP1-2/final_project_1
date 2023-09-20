import axios from 'axios';

export const basicAx = axios.create({
    baseURL: 'http://localhost:9000/',
});



export const getTokenFromState = (state) => {
    const {
        registration: {
            token: {
                obj: {
                    token: actualToken
                }
            }
        }
    } = state;
    return actualToken || null;
}


export const workAx = async (method, url, data, state) => {
    const token = getTokenFromState(state);
    return basicAx({
        method: method,
        url: url,
        data: data,
        headers: token ? {Authorization: `Bearer ${token}`} : {}
    });
}