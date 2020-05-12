const webpack = require('webpack');
const merge = require('webpack-merge');
const base = require('./webpack.base.js');

const appPort = process.env['application.port'] | 9000;
console.log('sssss', appPort);

module.exports = merge(base, {
    plugins: [
        new webpack.HotModuleReplacementPlugin()
    ],
    devServer: {
        publicPath: '/dist/',
        overlay: {
            errors: true,
            warnings: false
        },
        port: 8080,
        hot: true,
        proxy: {
            "/": {
                target: 'http://localhost:' + appPort,
                bypass: function (req, res, proxyOptions) {
                    // Don't proxy hot reload requests.
                    if (/hot-update\.json$/.test(req.url)) {
                        return true;
                    }
                }
            }
        }
    }
});