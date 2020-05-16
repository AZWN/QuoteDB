const path = require('path');
const HtmlWebpackHarddiskPlugin = require('html-webpack-harddisk-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const publicDir = function (file) {
    return file ? path.resolve(__dirname, 'public', file) : path.resolve(__dirname, 'public');
};
const distDir = publicDir('dist');

module.exports = {
    context: publicDir(),
    entry: {
        index: publicDir('js/app.jsx')
    },
    devtool: 'source-map',
    output: {
        filename: '[name].[hash].js',
        path: distDir,
        publicPath: '/dist'
    },
    plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin({
            template: 'index.html',
            alwaysWriteToDisk: true
        }),
        new HtmlWebpackHarddiskPlugin()
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
    }
};