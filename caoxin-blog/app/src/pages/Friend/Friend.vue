<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">友情链接</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container">
				<h2>本站信息</h2>
				<div class="block">
					<p>名称：Doit的个人博客</p>
					<p>简介：掐指一算 逢考必过</p>
					<p>头像：http://rulf5wkef.hd-bkt.clouddn.com/head/2023/05/23/9acf03e5946d4e1384ac3274f19f0b5e.jpg</p>
				</div>
				<h2>申请方法</h2>
				<div class="welcome">需要交换友链的可登录申请 (｡･∀･)ﾉﾞ</div>
				<div class="block"><p>出于信息需要,格式要包含：网站名称、网站链接、头像链接、网站介绍</p></div>
				<h2>小伙伴们</h2>
				<div class="friends">
					<div class="friend-item" v-for="item in friendList">
						<a :href="item.address" target="_blank">
							<img class="image" :src="item.logo" alt="" />
						</a>
						<div class="info">
							<a class="name" :href="item.address" target="_blank">{{item.name}}</a>
							<p class="desc">{{item.description}}</p>
						</div>
					</div>
				</div>
        <Comment :commentList="commentList" :getFriendCommentList="getFriendCommentList"></Comment>
      </div>
		</div>
	</div>
</template>

<script>
import Comment from '@/components/Comment/Comment.vue'
export default {
	name: 'Friend',
  components: {
    Comment
  },
	data() {
		return {
      friendList: [],
      commentList: [],
    };
	},
  mounted() {
    this.getFriendList()
    this.getFriendCommentList()
  },
  methods: {
    // 查询所有友链信息
    async getFriendList() {
      let res = await this.$API.friend.getFriendList()
      console.log('查询所有友链信息',res);
      if(res.code==200) {
        this.friendList = res.data
      }
    },
    // 查询所有友链评论信息
    async getFriendCommentList() {
      const params = {
        pageNum: 1,
        pageSize: 1000
      }
      let res = await this.$API.friend.getFriendCommentList(params)
      console.log('查询所有友链评论信息',res);
      if(res.code==200) {
        res.data.rows.forEach(item => {
          item.reply = false
          item.showChildrenNum = 3
        })
        this.commentList = res.data.rows
      }
    }
  }
};
</script>

<style lang="less" scoped>
.main {
	width: 100%;
}
.imgs {
	position: fixed;
	left: 0;
	top: 0;
	height: 65vh;
	width: 100%;
	background: url(@/assets/images/bg_xue.jpg) no-repeat 50% 50%/cover;
	z-index: -10;
}
.brand-container {
	position: relative;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	height: 65vh;
	width: 100%;
	min-height: 180px;
}
.brand-container .brand {
	position: fixed;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	z-index: -1;
	height: 50vh;
	width: 100%;
}
.brand .art {
	font-size: 36px;
	line-height: 1.2;
	color: #fff;
	padding-bottom: 30px;
}
.main-container {
	background: var(--bgc-gradient);
	overflow: hidden;
}
.page-container {
	width: calc(100% - 24px);
	min-height: 160px;
	margin: 0 auto;
	padding: 28px 36px;
	border-radius: 12px;
	box-shadow: 0 0 16px rgba(0, 0, 0, 0.1);
}
@media screen and (min-width: 1200px) {
	.page-container {
		width: 1000px;
	}
}
@media screen and (max-width: 767px) {
	.page-container {
		width: calc(100% - 10px);
		padding: 16px 14px;
	}
}
h2 {
	font-weight: 700;
	font-size: 25px;
	line-height: 1.5;
	margin: 20px 0 15px;
}
.block {
	line-height: 2;
	margin: 0 16px;
	font-size: 15px;
	border-left: 3px solid #9d5b8b;
	padding: 12px 16px;
	color: #999;
	background: #ffe6fa;
	border-radius: 4px;
	word-wrap: break-word;
}
.welcome {
	position: relative;
	margin: 0 40px 8px;
	font-size: 16px;
	line-height: 2;
}
.welcome:before {
	content: '';
	position: absolute;
	width: 0.4em;
	height: 0.4em;
	background: #e9546b;
	border-radius: 50%;
	top: 0.85em;
	left: -1em;
}
.friends {
	display: flex;
	flex-wrap: wrap;
	font-size: 15px;
}
.friend-item {
	display: flex;
	align-items: center;
	width: calc(50% - 32px);
	margin: 16px;
	padding: 8px 16px;
	line-height: 1.5;
	border-radius: 8px;
	border: 1px solid #f7f7f7;
	box-shadow: 0 10px 22px -15px rgba(0, 0, 0, .1);
  transition: all .2s ease-in-out;
}
.friend-item:hover {
  box-shadow: 0 0 32px rgba(0, 0, 0, .1);
}
@media screen and (max-width: 767px) {
	.friend-item {
		width: calc(100% - 16px);
	}
}
.friend-item .image {
	display: block;
	width: 64px;
	height: 64px;
	border-radius: 15px;
}
.friend-item .info {
	padding-left: 16px;
} 
.friend-item .name {
	font-weight: 700;
	font-size: 14px;
	color: rgb(0, 191, 255);
}
.friend-item .desc {
	font-size: .75em;
	margin: 8px 0;
}
</style>
