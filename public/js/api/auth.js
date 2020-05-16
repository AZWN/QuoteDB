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

const login = (userName, password, success, fail) => {
    $.post('/auth/login', { userName, password })
        .then(res => {
            debugger;
            storeToken(res);
            success(res);
        }, fail);
};

const register = (userName, password) => $.post('auth/register', { userName, password });

export default {
    isLoggedIn,
    login,
    register
}