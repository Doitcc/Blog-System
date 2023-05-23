import requests from '../requests.js'

// 登录
export const login = (userInfo)=>{
	return requests({
		url: '/login',
		method: 'post',
		data: userInfo
	})
}

// 退出登录
export const loginOut = ()=>{
	return requests({
		url: '/logout',
		method: 'post'
	})
}

// 获取验证码
export const getCode = (params)=>{
	return requests({
		url: '/user/sendEmail',
		method: 'get',
		params
	})
}

// 注册
export const register = (registForm)=>{
  const {userName,password,code} = registForm
	return requests({
		url: `/user/register?code=${code}`,
		method: 'post',
		data: {
      userName,
      password
    }
	})
}

// 找回密码
export const forget = (params)=>{
	return requests({
		url: `/user/forgetPassword`,
		method: 'post',
		params
	})
}

// 获取用户信息
export const getUserInfo = ()=>{
	return requests({
		url: '/user/userInfo',
		method: 'get'
	})
}

// 修改用户信息
export const updataUserInfo = (data)=>{
	return requests({
		url: '/user/userInfo',
		method: 'put',
    data
	})
}

// 上传头像
export const updataAvator = (data)=>{
	return requests({
		url: '/upload',
		method: 'post',
    data
	})
}