import $ from 'jquery'
import { decode } from 'jsonwebtoken'

const tokenStorageKey = 'token';

const isLoggedIn = () => {
    const token = localStorage.getItem(tokenStorageKey);
    if (token == null) {
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

const login = (userName, password) => {
    return $.ajax('/auth/login', buildRequest({userName, password}, "POST"))
        .then(({ token }) => storeToken(token));
};

const logout = () => {
    localStorage.removeItem(tokenStorageKey);
};

const register = (userName, password) => $.ajax('auth/register', buildRequest({ userName, password }, "POST"));

export default {
    isLoggedIn,
    login,
    register,
    logout
}