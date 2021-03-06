import { decode } from 'jsonwebtoken';

const tokenStorageKey = 'token';

export const isLoggedIn = () => {
    const token = localStorage.getItem(tokenStorageKey);
    if (!token) {
        return false;
    }
    const { exp } = decode(token);
    return (1000 * exp > Date.now());
};

const storeToken = token => {
    localStorage.setItem(tokenStorageKey, token);
};

const buildRequest = (data, method) => {
    return {
        method,
        data: JSON.stringify(data),
        contentType: 'application/json'
    };
};

export const login = (userName, password) => {
    return import('jquery')
        .then($ => $.ajax('/auth/login', buildRequest({userName, password}, "POST")))
        .then(({ token }) => storeToken(token));
};

export const logout = () => {
    localStorage.removeItem(tokenStorageKey);
};

export const register = (userName, password) => {
    return import('jquery')
        .then($ => $.ajax('auth/register', buildRequest({userName, password}, "POST")));
};

