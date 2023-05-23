import requests from '../requests.js'

// 查询菜单栏文章分类展示
export const getHeadCategoryList = ()=>{
	return requests({
		url: '/category/getCategoryList',
		method: 'get',
	})
}

// 查询博主基本信息
export const getHostInfo = ()=>{
	return requests({
		url: '/user/userOverview',
		method: 'get',
	})
}

// 查询文章
export const getArticleList = (params)=>{
	return requests({
		url: '/article/articleList',
		method: 'get',
		params
	})
}

// 查询热门文章
export const getHotArticleList = ()=>{
	return requests({
		url: '/article/hotArticleList',
		method: 'get',
	})
}

// 查询文章通过id
export const getArticleById = (id)=>{
	return requests({
		url: `/article/${id}`,
		method: 'get'
	})
}