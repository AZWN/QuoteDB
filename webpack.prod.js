const merge = require('webpack-merge');
const base = require('./webpack.base.js');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');

module.exports = merge(base, {
    plugins: [
        new UglifyJSPlugin()
    ],
    module: {
        rules: [{
            enforce: 'pre',
            test: /\.jsx?$/,
            exclude: /node_modules/,
            loader: 'eslint-loader',
            options: {
                cache: true,
                formatter: require('eslint-friendly-formatter')
            }
        }]
    }
});
