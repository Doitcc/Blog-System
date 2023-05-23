import Vue from 'vue'
import Vuex from 'vuex'
//需要使用插件一次
Vue.use(Vuex);
//引入各个小仓库
import user from './userStore'
import article from './articleStore'
//对外暴露 store 类的一个实例
export default new Vuex.Store({
	//实现 vuex 仓库模块式开发存储数据
	modules:{
		user,
    article
	}
})