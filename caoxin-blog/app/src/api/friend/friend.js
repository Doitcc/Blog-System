import requests from '../requests.js'

// 用户获取所有友链信息
export const getFriendList = ()=>{
	return requests({
		url: '/link/getLinkOrdinary',
		method: 'get'
	})
}

// 获取所有友链评论
export const getFriendCommentList = (params)=>{
	return requests({
		url: '/comment/linkCommentList',
		method: 'get',
    params
	})
}

// 添加友链评论
export const addFriendComment = (data) => {
  return requests({
  	url: '/comment/addLinkComment',
  	method: 'post',
    data
  })
}

// 查看友链申请状态
export const getApplyFriendStatus = () => {
  return requests({
  	url: '/link/queryStatusSelf',
  	method: 'get'
  })
}

// 申请友链
export const applyFriend = (data) => {
  return requests({
  	url: '/link/applyLink',
  	method: 'post',
    data
  })
}

// 修改友链
export const updateFriend = (data) => {
  return requests({
  	url: '/link/modifyLink',
  	method: 'put',
    data
  })
}

// 管理员获取所有友链信息
export const getAdminFriendList = ()=>{
	return requests({
		url: '/link/getLinkAdmin',
		method: 'get'
	})
}

// 管理员删除友链信息根据id
export const deleteFriendById = (params)=>{
	return requests({
		url: '/link/deleteLink',
		method: 'delete',
    params
	})
}

// 管理员删除友链信息根据id
export const auditFriendById = (params)=>{
	return requests({
		url: '/link/editLink',
		method: 'put',
    params
	})
}