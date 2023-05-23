import requests from '../requests.js'

// 获取所有标签列表数据
export const getTagList = (params)=>{
	return requests({
		url: '/content/tag/list',
		method: 'get',
		params
	})
}

// 保存草稿或发布文章
export const saveOrWriteArticle = (data)=>{
	return requests({
		url: '/management/write',
		method: 'post',
		data
	})
}

// 编辑保存草稿或发布文章
export const editArticle = (data)=>{
	return requests({
		url: '/management/modify',
		method: 'put',
		data
	})
}

// 上传文章图片
export const updataArticleImg = (data)=>{
	return requests({
		url: '/uploadArticleImg',
		method: 'post',
    data
	})
}

// 根据id删除文章
export const deleteArticleById = (params)=>{
	return requests({
		url: `/management/delete`,
		method: 'delete',
    params
	})
}

// 获取文章所有评论
export const getArticleCommentList = (params) => {
  return requests({
  	url: `/comment/commentList`,
  	method: 'get',
    params
  })
}

// 添加文章评论
export const addArticleComment = (data) => {
  return requests({
  	url: '/comment/add',
  	method: 'post',
    data
  })
}