import {getArticleById} from '@/api/home/home.js'
const state = {
  article: {}
}
const mutations = {
  GETARTICLEBYID(state,article) {
    state.article = article
  },
  CLEARARTICLE(state) {
    state.article = {}
  }
}
const actions = {
  // 根据id查询文章
  async getArticleById({commit},id) {
    const res = await getArticleById(id)
  	console.log('vuex根据id查询文章',res)
  	if(res.code == 200) {
      commit('GETARTICLEBYID',res.data)
  		return 'ok'
  	} else {
      return 'getArticleById failed'
  		// return Promise.reject(new Error('regist failed'))
  	}
  },
}
const getters = {}

export default {
  state,
  mutations,
  actions,
  getters
}