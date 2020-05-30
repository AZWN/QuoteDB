const path = require('path');
const { HashedModuleIdsPlugin } = require('webpack');
const HtmlWebpackHarddiskPlugin = require('html-webpack-harddisk-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ChunksWebpackPlugin = require("chunks-webpack-plugin");
const publicDir = function (file) {
    return file ? path.resolve(__dirname, 'public', file) : path.resolve(__dirname, 'public');
};
const distDir = publicDir('dist');

module.exports = {
    context: publicDir(),
    entry:  publicDir('js/App.jsx'),
    output: {
        filename: '[name].[hash].js',
        path: distDir,
        publicPath: '/dist/'
    },
    plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin({
            template: 'index.html',
            alwaysWriteToDisk: true
        }),
        new HtmlWebpackHarddiskPlugin(),
        new ChunksWebpackPlugin(),
        new HashedModuleIdsPlugin()
    ],
    module: {
        rules: [{
            test: /\.css$/,
            use: ['style-loader', 'css-loader']
        }, {
            test: /\.jsx?$/,
            exclude: /node_modules/,
            use: {
                loader: "babel-loader"
            }
        }]
    },
    resolve: {
        extensions: [".js", ".jsx"]
    },
    optimization: {
        runtimeChunk: 'single',
        splitChunks: {
            chunks: 'all',
            maxInitialRequests: Infinity,
            minSize: 0,
            cacheGroups: {
                vendor: {
                    test: /[\\/]node_modules[\\/]/,
                    name(module) {
                        // get the name. E.g. node_modules/packageName/not/this/part.js
                        // or node_modules/packageName
                        const packageName = module.context.match(/[\\/]node_modules[\\/](.*?)([\\/]|$)/)[1];

                        // npm package names are URL-safe, but some servers don't like @ symbols
                        return `npm.${packageName.replace('@', '')}`;
                    },
                },
            }
        }
    }
};
