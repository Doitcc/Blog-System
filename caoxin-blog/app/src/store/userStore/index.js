import {getUserInfo,login,register,loginOut} from '@/api/user/user.js'
import {getHostInfo} from '@/api/home/home.js'

const state = {
  userInfo: JSON.parse(localStorage.getItem('userInfo')),
  loginInfo: {
    userInfo: {},
    type: '0',
    avatar: '',
    isLogin: false
  },
  hostInfo: null
}
const mutations = {
  // 登录后存储信息
  LOGIN(state,data) {
    state.userInfo = data.userInfo
    state.loginInfo.userInfo = data.userInfo
    state.loginInfo.type = data.userInfo.type
    state.loginInfo.avatar = data.userInfo.avatar
    state.loginInfo.isLogin = true
    localStorage.setItem('loginInfo',JSON.stringify(state.loginInfo))
    localStorage.setItem('token',data.token)
    localStorage.setItem('userInfo',JSON.stringify(state.userInfo))
  },
  // 退出登录后清空信息
  LOGINOUT(state) {
    state.userInfo = {}
    state.loginInfo.userInfo = {}
    state.loginInfo.type = '0'
    state.loginInfo.avatar =''
    state.loginInfo.isLogin = false
    localStorage.setItem('loginInfo',JSON.stringify(state.loginInfo))
    // localStorage.setItem('loginInfo',null)
    localStorage.setItem('token','')
    localStorage.setItem('userInfo',null)
  },
  // 存储用户信息
  GETUSERINFO(state,userInfo) {
    state.userInfo = userInfo
    state.loginInfo.userInfo = userInfo
    state.loginInfo.avatar = userInfo.avatar
    state.loginInfo.isLogin = true
    state.loginInfo.type = JSON.parse(localStorage.getItem('loginInfo')).type
    localStorage.setItem('userInfo',JSON.stringify(state.userInfo))
    localStorage.setItem('loginInfo',JSON.stringify(state.loginInfo))
  },
  // 存储博主基本信息
  GETHOSTINFO(state,hostInfo) {
    state.hostInfo = hostInfo
    localStorage.setItem('hostInfo',JSON.stringify(hostInfo))
  }
}
const actions = {
  // 登录
  async login({commit},userForm) {
  	let res = await login(userForm)
  	console.log('vuex登录',res)
  	if(res.code == 200) {
  		commit('LOGIN',res.data)
      // localStorage.setItem('type',res.data.userInfo.type)
      // localStorage.setItem('avatar',res.data.avatar)
  		return 'ok'
  	} else {
      // console.log(res);
      return 'login failed'
  		// return Promise.reject(new Error('login failed'))
  	}
  },
  // 退出登录
  async loginOut({commit}) {
  	let res = await loginOut()
  	console.log('vuex退出登录',res)
  	if(res.code == 200) {
  		commit('LOGINOUT')
  		return 'ok'
  	} else if(res.code == 401) {
      console.log('token过期',res);
      commit('LOGINOUT')
      return 'token is failure,please login again'
    } else if(res.code == 402) {
      console.log('强制下线',res);
      commit('LOGINOUT')
      return '402'
    } else {
      return 'loginOut failed'
  		// return Promise.reject(new Error('loginOut failed'))
  	}
  },
  // 注册
  async regist({commit},registForm) {
  	let res = await register(registForm)
  	console.log('vuex注册',res)
  	if(res.code == 200) {
  		return 'ok'
  	} else {
      return 'regist failed'
  		// return Promise.reject(new Error('regist failed'))
  	}
  },
  //获取用户信息
  async getUserInfo({commit}) {
  	let res = await getUserInfo()
  	console.log('vuex获取用户信息',res)
  	if(res.code == 200) {
  		commit('GETUSERINFO',res.data)
  		return 'ok'
  	} else if(res.code == 401){
      console.log('token过期',res);
      // commit('LOGINOUT')
      return 'token is failure,please login again'
  	} else if(res.code == 402) {
      console.log('强制下线',res);
      // commit('LOGINOUT')
      return '402'
    } else {
      // console.log(res);
      return 'getUserInfo failed'
  		// return Promise.reject(new Error('getUserInfo failed'))
  	}
  },
  //获取博主基本信息
  async getHostInfo({commit}) {
  	let res = await getHostInfo()
  	console.log('vuex获取博主基本信息',res)
  	if(res.code == 200) {
  		commit('GETHOSTINFO',res.data)
  		return 'ok'
  	} else {
      return 'getHostInfo failed'
      // return Promise.reject(new Error('getHostInfo failed'))
    }
  },
}
const getters = {
  userAvatar: state => {
    if(!JSON.parse(localStorage.getItem('loginInfo'))) {
      console.log('userAvatar1',state.loginInfo.avatar);
      var userAvatar = state.loginInfo.avatar
    } else {
      userAvatar = state.loginInfo.avatar ? state.loginInfo.avatar : JSON.parse(localStorage.getItem('loginInfo')).avatar
      console.log('userAvatar2',userAvatar);
    }
    return userAvatar
  },
  hostInfoNow: state => {
    var hostInfoNow = state.hostInfo ? state.hostInfo : JSON.parse(localStorage.getItem('hostInfo'))
    if(!JSON.parse(localStorage.getItem('hostInfo'))) {
      hostInfoNow = {}
    }
    return hostInfoNow
  }
}

export default {
  state,
  mutations,
  actions,
  getters
}