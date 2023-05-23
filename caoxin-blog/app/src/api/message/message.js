import requests from '../requests.js'

// 获取所有留言数据
export const getBarrageList = ()=>{
	return requests({
		url: '/message/getMessage',
		method: 'get'
	})
}

// 添加游客留言
export const addTouristMsg = (params)=>{
	return requests({
		url: '/message/notLoggedIn',
		method: 'post',
		params
	})
}

// 添加用户留言
export const addUserMsg = (params)=>{
	return requests({
		url: '/message/loggedIn',
		method: 'post',
		params
	})
}