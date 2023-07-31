const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');


module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    concertAdmin: path.resolve(__dirname, 'src', 'pages', 'concertAdmin.js'),
    ticketPurchase: path.resolve(__dirname, 'src', 'pages', 'ticketPurchase.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: true,
    port: 8000,
    open: true,
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/admin.html',
      filename: 'admin.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()   
  ]
}
