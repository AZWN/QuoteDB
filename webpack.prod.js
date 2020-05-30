const merge = require('webpack-merge');
const base = require('./webpack.base.js');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');

module.exports = merge(base, {
    plugins: [
        new UglifyJSPlugin({
            uglifyOptions: {
                ecma: 7,
                ie8: false,
                output: {
                    comments: false
                },
                cache: true,
                parallel: true,
                sourceMap: false,
                keep_fnames: true
            }
        })
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
