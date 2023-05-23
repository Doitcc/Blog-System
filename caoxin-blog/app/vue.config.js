const { defineConfig } = require('@vue/cli-service')
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin')
module.exports = defineConfig({
	configureWebpack: {
		plugins: [new NodePolyfillPlugin()],
	},
	transpileDependencies: true,
	lintOnSave: false,
	devServer: {
		host: '47.113.231.164',
		// host: 'localhost',
		port: 8080,
		https: false,
		hot: false,
		//代理服务器解决跨域问题
		proxy: {
			'/api': {
				// target: 'http://localhost:7777',
				target: 'http://47.113.231.164:7777',
				// target: 'http://172.30.192.219:7777',
				// target: 'http://192.168.241.202:7777',
				//路径重写
				// pathRewrite: { '^/api': ''},
			},
		},
	},
})
