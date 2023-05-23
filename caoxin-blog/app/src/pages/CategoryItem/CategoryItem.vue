<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">分类--{{cateName[this.categoryId]}}</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container" v-if="articleList.length>0">
        <div class="title">{{cateName[this.categoryId]}}目前共计{{articleList.length}}篇文章，继续加油！</div>
				<div class="block">
				  <el-timeline>
				    <el-timeline-item v-for="item in articleList" :key="item.id" :timestamp="item.createTime" placement="top" color="#ec7c86">
				      <el-card class="card" @click.native="goArticleById(item.id);">
                <div class="article-box">
                  <div class="article-img">
                    <img :src="item.thumbnail" alt="">
                  </div>
                  <div class="article-info">
                    <h2 class="article-title">{{item.title}}</h2>
                    <p>{{item.summary}}</p>
                    <div class="article-category">
                      <div class="article-view">
                        <span class="article-look">
                          <i class="iconfont icon-icon-"></i>
                          {{item.viewCount}}人浏览
                        </span>
                        <span>
                          <span class="tag" v-for="(tag,index) in item.tagName" :key="index" v-show="index<4">{{tag}}</span>
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
				      </el-card>
				    </el-timeline-item>
				  </el-timeline>
				</div>
			</div>
      <div class="page-container" v-else>
        <Empty></Empty>
      </div>
    </div>
	</div>
</template>

<script>
import {mapState} from 'vuex'
import {cateName} from '@/utils/constData.js'
export default {
	name: 'CategoryItem',
	data() {
		return {
      pageNum: 1,
      pageSize: 1000,
      articleList: [],
      categoryId: this.$route.params.categoryId,
      cateName: cateName,
    };
	},
  mounted() {
    this.getArticleList()
    console.log('当前分类id',this.categoryId);
  },
  methods: {
    // 查询该分类展示文章
    async getArticleList() {
      const {pageNum,pageSize,categoryId} = this
      var params = {
        pageNum,
        pageSize,
        categoryId,
        status: '0'
      }
      const res = await this.$API.home.getArticleList(params)
      console.log(`查询该${this.cateName[categoryId]}分类展示文章`,res);
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
    }
  },
  watch:{
    $route(to,from) {
      if(to.name=='categoryitem') {
        this.categoryId = this.$route.params.categoryId
        this.getArticleList()
      }
    }
  }
};
</script>

<style lang="less" scoped>
.main {
	width: 100%;
  min-height: 100vh;
}
.imgs {
	position: fixed;
	left: 0;
	top: 0;
	height: 100vh;
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
	font-size: 42px;
	line-height: 1.2;
	color: #fff;
}
.main-container {
	background: var(--bgc-gradient);
	overflow: hidden;
}
.page-container {
	width: calc(100% - 24px);
	min-height: 300px;
	margin: 0 auto;
	padding: 28px 36px;
	border-radius: 12px;
	box-shadow: 0 0 16px rgba(0, 0, 0, 0.1);
  background-color: var(--bgc-white);
  animation: trans 1s;
}
@keyframes trans {
  0% {
    opacity: 0;
    transform: translateY(100px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
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
.page-container .title {
  font-size: 26px;
  text-align: center;
  margin: 10px 0 20px 0;
  color: deeppink;
}
/deep/.el-timeline-item__timestamp {
  font-size: 14px;
  color: #ec7c86;
}
/deep/.el-card {
  border: none;
}
.article-box {
  display: flex;
  align-items: center;
  background-color: var(--bgc-white);
}
.card {
  transition: all .5s;
  margin: 15px 0 5px 0;
  background-color: var(--bgc-white);
}
.card:hover {
  box-shadow: 5px 5px 10px 2px #ccc;
}
.article-box .article-img {
  width: 200px;
  height: 150px;
  font-size: 0;
  margin-right: 20px;
}
.article-box .article-img img {
  width: 200px;
  height: 100%;
  border-radius: 10px;
}
@media screen and (max-width:767px) {
  .article-box {
    flex-wrap: wrap;
  }
  .article-box .article-img {
    width: 100%;
    margin-right: 0px;
    margin-bottom: 10px;
  }
  .article-box .article-img img {
    width: 100%;
  }
}
.article-box .article-title {
  font-size: 24px;
  margin-bottom: 10px;
  color: var(--normal-text);
}
.article-box p{
  color: var(--normal-text-2);
}
.article-box .article-category {
	display: flex;
	align-items: center;
	line-height: 26px;
	font-size: 14px;
	color: #8f8f8f;
  margin-top: 10px;
}
.article-box .article-view {
	display: flex;
	// align-items: center;
	line-height: 26px;
	font-size: 14px;
	color: #8f8f8f;
}
.article-box .article-category .article-view .article-look {
  line-height: 26px;
  margin-right: 10px;
}
@media screen and (max-width: 767px) {
  .article-box .article-category .article-view .article-look {
    width: 100px;
    margin-right: 15px;
  }
}
.article-box .article-category .article-view .iconfont {
  font-size: 14px;
  margin-right: 3px;
  color: #ec7c86;
}
.article-box .article-view .tag {
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
