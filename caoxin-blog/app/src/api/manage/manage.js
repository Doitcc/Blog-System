import requests from '../requests.js'

// 根据id删除标签
export const deleteTagById = (id)=>{
	return requests({
		url: `/content/tag/delete/${id}`,
		method: 'delete',
	})
}

// 根据标签id查询文章
export const getTagArticleListByTagId = (params)=>{
	return requests({
		url: '/article/articleTagList',
		method: 'get',
    params
	})
}

// 新增标签
export const addTag = (params)=>{
	return requests({
		url: `/content/tag/add`,
		method: 'post',
    params
	})
}

// 更新文章浏览数
export const updateViewCount = (id)=>{
	return requests({
		url: `/article/updateViewCount/${id}`,
		method: 'put',
	})
}