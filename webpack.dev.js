const path = require("path");
const webpack = require('webpack');
const merge = require('webpack-merge');
const base = require('./webpack.base.js');

module.exports = merge(base, {
    plugins: [
        new webpack.HotModuleReplacementPlugin()
    ],
    devServer: {
        contentBase: path.join(__dirname, "/dist"),
        historyApiFallback: true,
        publicPath: '/dist/',
        open: true,
        hot: true,
        port: 8080,
        overlay: {
            errors: true,
            warnings: false
        },
        proxy: {
            '**': {
                target: 'http://127.0.0.1:9003',
                secure: false,
                changeOrigin: true,
                headers: {
                    Connection: 'keep-alive'
                },
                logLevel: 'info',
                methods: ["POST", "GET", "PATCH", "PUT"],
                bypass: function (req, res, proxyOptions) {
                    // Don't proxy hot reload requests.
                    if (/hot-update\.json$/.test(req.url)) {
                        return true;
                    }
                }
            }
        }
    },
    devtool: 'source-map',
    module: {
        rules: [{
            enforce: 'pre',
            test: /\.jsx?$/,
            exclude: /node_modules/,
            loader: 'eslint-loader',
            options: {
                cache: true,
                fix: true,
                emitWarning: true,
                formatter: require('eslint-friendly-formatter')
            }
        }]
    }
});
