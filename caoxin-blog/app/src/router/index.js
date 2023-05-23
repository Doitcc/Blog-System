import Vue from 'vue'
import VueRouter from 'vue-router'
import routes from './routes.js'

Vue.use(VueRouter)

let router = new VueRouter ({
	routes,
	scrollBehavior(to, from, savedPosition) {
		if (savedPosition) {
			return savedPosition // 按下 后退/前进 按钮时，类似浏览器的原生表现 
		} else {
			return { x: 0, y: 0 } // 让页面滚动到顶部
		}
	}
});

export default router;