<template>
	<div class="main">
		<div class="imgs">
			<ul>
				<li class="item"></li>
				<!-- <li class="item" style="background-image: url(@/assets/images/男头像.png);"></li> -->
			</ul>
		</div>
		<div class="brand-container">
			<div class="brand" v-show="article.id">
				<p class="art">{{article.title}}</p>
				<div class="title">
          <span><i class="iconfont icon-shijian"></i> 发表于 {{article.createTime}}</span>
          <span><i class="iconfont icon-gengxin"></i> 更新于 {{article.updateTime}}</span>
        </div>
        <div class="title">
          <span><i class="iconfont" :class="iconObj[article.categoryId]"></i> {{cateName[article.categoryId]}}</span>
          <span>
            <span class="title-tag" v-for="(tag,index) in article.tagName"><i class="iconfont icon-24gl-tags" v-show="index<3"></i> {{tag}}</span>
          </span>
          <span><i class="iconfont icon-icon-"></i> 阅读量：{{article.viewCount}}</span>
        </div>
			</div>
		</div>
		<div class="main-container mt">
			<div class="content">
				<div class="left-content">
					<div v-show="article.id" class="article-box">
            <MdPreview :text="article.content"></MdPreview>
            <div class="line"><span>感谢阅读</span></div>
            <div class="tags">
              <i class="iconfont icon-shengqian"></i>
              <span class="tag-item" v-for="(tag,index) in article.tagName" :key="index">{{tag}}</span>
            </div>
          </div>
          <div v-show="article.id&&article.isComment=='1'" class="comment-box">
            <div class="comment-width">
              <Comment :articleId="id" :commentList="commentList" :getArticleCommentList="getArticleCommentList"></Comment>
            </div>
          </div>
          <div v-show="!article.id" class="article-loading">
            <i class="iconfont icon-jiazaizhong"></i>
            <span>正在加载中</span>
          </div>
        </div>
			</div>
		</div>
	</div>
</template>

<script>
import {mapState,mapMutations} from 'vuex'
import MdPreview from '@/components/MdPreview/MdPreview.vue'
import Comment from '@/components/Comment/Comment.vue'
import {cateName,iconObj} from '@/utils/constData.js'
export default {
	name: 'Article',
  components: {
    MdPreview,
    Comment
  },
	data() {
		return {
      id: this.$route.params.id,
      total: 0,
      commentList: [],
      cateName: cateName,
      iconObj: iconObj
    };
	},
  mounted() {},
  computed: {
    ...mapState({
      article: state => state.article.article
    })
  },
  methods: {
    ...mapMutations(['CLEARARTICLE']),
    // 查询文章通过id
    async getArticleById() {
      const res = await this.$store.dispatch('getArticleById',this.id)
      if(res=='ok') {
        console.log('查询文章通过id成功',res);
      } else {
        this.$message({
          type: 'error',
          message: '查询文章数据失败',
          showClose: true
        })
      }
    },
    // 更新文章浏览量通过id
    async updateViewCount() {
      const res = await this.$API.manage.updateViewCount(this.id)
      console.log('更新文章浏览量通过id',res);
    },
    // 获取文章所有评论
    async getArticleCommentList() {
      const params = {
        articleId: this.id,
        pageNum: 1,
        pageSize: 1000
      }
      const res = await this.$API.article.getArticleCommentList(params)
      console.log(`获取${this.id}文章所有评论`,res);
      if(res.code==200) {
        res.data.rows.forEach(item => {
          item.reply = false
          item.showChildrenNum = 3
        })
        this.commentList = res.data.rows
        this.total = res.data.total
      }
    }
  },
  watch: {
    $route: {
      immediate: true,
      handler(to,from) {
        if(to.params.id) {
          console.log('进入文章id为',to.params.id);
          this.id = to.params.id
          this.getArticleById()
          this.updateViewCount()
          this.getArticleCommentList()
        }
        if(to.name!='article') {
          this.$store.commit('CLEARARTICLE')
          console.log('离开article页面，清空article内容',this.article);
        }
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
	height: 100vh;
	width: 100%;
	overflow: hidden;
	z-index: -10;
}
.imgs>ul {
	display: block;
	height: 100vh;
}
.item {
	position: absolute;
	left: 0;
	top: 0;
	width: 100%;
	height: 100vh;
	background: url(@/assets/images/bg_xue.jpg) no-repeat 50% 50%/cover;
	// opacity: 0;
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
	color: #fff;
}
.brand-container .brand {
	position: fixed;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	z-index: -1;
}
.brand .art {
	font-size: 36px;
	line-height: 1.5;
  font-weight: 600;
  padding-bottom: 10px;
}
.brand .title {
  margin-top: 10px;
	font-size: 16px;
  text-align: center;
  font-weight: 500;
}
.brand .title>span {
  display: inline-block;
  height: 16px;
  line-height: 16px;
  padding: 0 10px;
  border-right: 2px solid #fff;
}
.brand .title>span:last-child {
  border-right: none;
}
.brand .title span.title-tag {
  padding-right: 10px;
}
.brand .title span.title-tag:last-child {
  padding-right: 0;
}
@media screen and (max-width: 750px) {
	.brand .title {
		font-size: 18px;
	}
}
.main-container {
	background-color: #fff;
}
.content {
	display: flex;
	justify-content: center;
	align-items: flex-start;
	width: calc(100% - 10px);
	min-height: 100vh;
	margin: 0 auto;
}
@media (min-width: 1300px) {
	.content {
		width: 1200px;
	}
}
.left-content {
  width: 100%;
  min-height: 400px;
}
@media screen and (max-width: 990px) {
	.left-content {
		width: 100%;
	}
}
//.left-content
.article-box {
  min-height: 400px;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 0 16px #ccc;
  margin-bottom: 20px;
  font-size: 16px;
}
.article-box .line {
  padding: 5px 20px;
  text-align: center;
}
.article-box .line::before {
  content: '';
  width: 25%;
  height: 2px;
  background-color: #ccc;
  display: inline-block;
  vertical-align: 5px;
  margin: 0 16px;
}
.article-box .line::after {
  content: '';
  width: 25%;
  height: 2px;
  background-color: #ccc;
  display: inline-block;
  vertical-align: 5px;
  margin: 0 16px;
}
.tags {
  margin: 20px 30px;
  text-align: center;
}
.tags .iconfont {
  margin-right: 20px;
  font-size: 22px;
  vertical-align: -3px;
}
.tags .tag-item {
  font-size: 18px;
  display: inline-block;
  background-color: #eee;
  margin-right: 20px;
  border-radius: 4px;
  padding: 5px 15px;
}
@media screen and (max-width: 600px) {
  .tags {
    margin: 20px 10px;
  }
  .tags .iconfont {
    font-size: 16px;
    vertical-align: 0px;
    margin-right: 10px;
  }
  .tags .tag-item {
    font-size: 14px;
    border-radius: 4px;
    padding: 5px 10px;
  }
  .article-box .line span {
    font-size: 14px;
  }
}
@media screen and (max-width: 1000px) {
  .comment-box .comment-width {
    width: 90%;
  }
}
.comment-box {
  padding: 16px;
  box-shadow: 0 0 16px #ccc;
}
.comment-box .comment-width {
  width: 70%;
  margin: 0 auto;
  transition: all .5s;
}
@media screen and (max-width: 1000px) {
  .comment-box .comment-width {
    width: 90%;
  }
}
@media screen and (max-width: 400px) {
  .comment-box .comment-width {
    width: 100%;
  }
  .tags {
    margin: 20px 0;
  }
  .tags .iconfont {
    font-size: 14px;
  }
  .tags .tag-item {
    font-size: 12px;
    border-radius: 2px;
    padding: 2px 5px;
    margin-right: 10px;
  }
  .article-box .line span {
    font-size: 12px;
  }
}
.article-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  padding-top: 50px;
}
.article-loading .iconfont {
  font-size: 50px;
  margin-bottom: 10px;
}
.article-loading span {
  font-size: 30px;
}
</style>
