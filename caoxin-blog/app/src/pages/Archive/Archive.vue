<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">归档</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container" v-if="articleList.length>0">
        <div class="article-item" v-for="item in articleList" :key="item.id">
          <div class="article-content">
            <div class="article-img" @click="goArticleById(item.id)">
              <img :src="item.thumbnail" alt="">
            </div>
            <div class="article-main">
              <div class="article-title" @click="goArticleById(item.id)">{{item.title}}</div>
              <div class="article-info">
                <span class="article-time">
                  <i class="iconfont icon-riqi_o"></i>
                  {{item.createTime.split(' ')[0]}}
                </span>
                <span class="article-category" @click="goCategoryItemById(item.categoryId)">
                  <i class="iconfont" :class="iconObj[item.categoryId]"></i>
                  {{cateName[item.categoryId]}}
                </span>
              </div>
            </div>
            <div class="article-tag">
              <span class="tag" v-for="(tag,index) in item.tagName" :key="index" v-show="index<4">{{tag}}</span>
            </div>
          </div>
        </div>
			</div>
      <div class="page-container" v-else>
        <Empty></Empty>
      </div>
		</div>
	</div>
</template>

<script>
import {cateName,iconObj} from '@/utils/constData.js'
export default {
	name: 'Archive',
	data() {
		return {
      pageNum: 1,
      pageSize: 1000,
      articleList: [],
      cateName: cateName,
      iconObj: iconObj
    };
	},
  mounted() {
    this.getArticleList()
  },
  methods: {
    // 查询归档展示文章
    async getArticleList() {
      const {pageNum,pageSize} = this
      var params = {
        pageNum,
        pageSize,
        status: 0
      }
      const res = await this.$API.home.getArticleList(params)
      console.log('查询归档展示文章',res);
      if(res.code==200) {
        this.articleList = res.data.rows
        this.articleList.sort(function(a, b) {
          var aDate = +new Date(a.createTime)
          var bDate = +new Date(b.createTime)
          return bDate - aDate
        })
      }
    },
    // 跳转文章通过id
    goArticleById(id) {
      this.$router.push({name: 'article',params: {id}})
    },
    // 跳到该分类的页面
    goCategoryItemById(categoryId) {
      this.$router.push(`categoryitem/${categoryId}`)
    }
  },
  computed:{
    
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
	height: 70vh;
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
	height: 60vh;
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
	font-size: 42px;
	line-height: 1.2;
	color: #fff;
  margin-top: 20px;
}
.main-container {
	background: var(--bgc-gradient);
	overflow: hidden;
}
.page-container {
  display: flex;
  flex-wrap: wrap;
	width: calc(100% - 20px);
	min-height: 300px;
	margin: 0 auto;
	// padding: 28px 36px;
}
@media screen and (min-width: 1300px) {
	.page-container {
		width: 1200px;
	}
}
@media screen and (max-width: 767px) {
	.page-container {
		width: calc(100% - 10px);
		padding: 16px 14px;
	}
}
.page-container .article-item {
  width: 100%;
  max-width: 100%;
  padding: 8px;
}
@media screen and (min-width: 1000px) {
  .page-container .article-item {
    max-width: 33.33333%;
  }
}
.page-container .article-item .article-content {
  background-color: #fff;
  border-radius: 12px;
  min-height: 160px;
  box-shadow: 0 4px 8px 6px rgba(7,17,27,.06);
  animation: trans 1s;
}
@keyframes trans {
  0% {
    transform: scale(.05);
  }
  100% {
    transform: scale(1);
  }
}
.page-container .article-item .article-content:hover .article-img img {
  transform: scale(1.1);
}
.article-item .article-content .article-img {
  width: 100%;
  height: 280px;
  border-radius: 12px 12px 0 0;
  overflow: hidden;
}
@media screen and (max-width: 767px) {
  .article-item .article-content .article-img {
    height: 200px;
  }
  .brand-container {
    height: 50vh;
  }
}
.article-item .article-content .article-img img {
  width: 100%;
  height: 100%;
  border-radius: 12px 12px 0 0;
  transition: all 1s;
}
.article-item .article-content .article-main {
  padding: 20px;
  border-bottom: 1px solid #ccc;
}
.article-item .article-content .article-title {
  margin-bottom: 20px;
  transition: all .4s;
}
.article-item .article-content .article-title:hover {
  color: #ec7c86;
}
.article-item .article-content .article-info {
  display: flex;
  justify-content: space-between;
  font-size: 15px;
  color: #333;
}
.article-item .article-info .article-category .iconfont {
  vertical-align: -2px;
}
.article-item .article-info .article-category {
  transition: all .4s;
}
.article-item .article-info .article-category:hover {
  color: mediumpurple;
}
.article-item .article-content .article-tag {
  padding: 15px;
}
.article-item .article-content .article-tag .tag {
  display: inline-block;
  height: 24px;
  line-height: 14px;
  border-radius: 12px;
  color: #fff;
  background: linear-gradient(90deg,#bf4643 0,#6c9d8f);
  opacity: .6;
  padding: 5px 12px;
  font-size: 12px;
  margin-right: 8px;
}
</style>
