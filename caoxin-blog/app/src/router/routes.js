// 注册路由
import Home from '@/pages/Home/Home.vue'
import CategoryItem from '@/pages/CategoryItem/CategoryItem.vue'
import Category from '@/pages/Category/Category.vue'
import Tag from '@/pages/Tag/Tag.vue'
import TagItem from '@/pages/TagItem/TagItem.vue'
import Archive from '@/pages/Archive/Archive.vue'
import Friend from '@/pages/Friend/Friend.vue'
import ApplyFriend from '@/pages/ApplyFriend/ApplyFriend.vue'
import Message from '@/pages/Message/Message.vue'
import About from '@/pages/About/About.vue'
import User from '@/pages/User/User.vue'
import Article from '@/pages/Article/Article.vue'
import EditArticle from '@/pages/EditArticle/EditArticle.vue'
import ManageArticle from '@/pages/ManageArticle/ManageArticle.vue'
import ManageTag from '@/pages/ManageTag/ManageTag.vue'
import ManageFriend from '@/pages/ManageFriend/ManageFriend.vue'

export default [
	//路由重定向，'/'，'*'
	{
		path: '*',
		redirect: '/home',
		meta: {
			show: true
		},
	},
	{
		path: '/home',
		component: Home,
		meta: {
			show: true
		},
    name: 'home'
	},
  {
  	path: '/managetag',
  	component: ManageTag,
  	meta: {
  		show: true
  	},
    name: 'managetag'
  },
  {
  	path: '/managefriend',
  	component: ManageFriend,
  	meta: {
  		show: true
  	},
    name: 'managefriend'
  },
  {
  	path: '/article/:id',
  	component: Article,
  	meta: {
  		show: true
  	},
    name: 'article'
  },
	{
		path: '/categoryitem/:categoryId',
		component: CategoryItem,
		meta: {
			show: true
		},
    name: 'categoryitem'
	},
  {
  	path: '/archive',
  	component: Archive,
  	meta: {
  		show: true
  	},
    name: 'archive'
  },
	{
		path: '/category',
		component: Category,
		meta: {
			show: true
		},
    name: 'category'
	},
  {
  	path: '/editarticle',
  	component: EditArticle,
  	meta: {
  		show: true
  	},
    name: 'editarticle'
  },
  {
  	path: '/managearticle',
  	component: ManageArticle,
  	meta: {
  		show: true
  	},
    name: 'managearticle'
  },
	{
		path: '/tag',
		component: Tag,
		meta: {
			show: true
		},
    name: 'tag'
	},
  {
  	path: '/tagitem/:tagId',
  	component: TagItem,
  	meta: {
  		show: true
  	},
    name: 'tagitem'
  },
	{
		path: '/friend',
		component: Friend,
		meta: {
			show: true
		},
    name: 'friend'
	},
  {
  	path: '/applyfriend',
  	component: ApplyFriend,
  	meta: {
  		show: true
  	},
    name: 'applyfriend'
  },
	{
		path: '/message',
		component: Message,
		meta: {
			show: false
		},
    name: 'message'
	},
	{
		path: '/about',
		component: About,
		meta: {
			show: true
		},
    name: 'about'
	},
	{
		path: '/user',
		component: User,
		meta: {
			show: true
		},
    name: 'user'
	},
]
