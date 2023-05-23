<template>
	<div class="main">
		<div class="imgs">
			<ul>
				<li class="item"></li>
				<!-- <li class="item" style="background: url(@/assets/images/男头像.png);"></li> -->
			</ul>
		</div>
		<div class="brand-container">
			<div class="brand">
				<p class="art">Just Doit</p>
				<div class="title">任你去山高水远，你永远在我的心间...<span class="sansuo"></span></div>
			</div>
		</div>
		<div class="main-container mt">
			<div class="content">
				<div class="left-content">
					<a class="talk-swiper">
						<div class="swiper-content">
              <el-carousel height="20px" direction="vertical" :interval="3000" indicator-position="none">
                <el-carousel-item v-for="(item,index) in talkList" :key="index">
                  <h3 class="medium">{{ item }}</h3>
                </el-carousel-item>
              </el-carousel>
            </div>
					</a>
					<el-carousel :interval="5000" height="270px" class="swiper swiper-container">
						<el-carousel-item v-for="item in topArticleList" :key="item.id" style="text-align: center;">
							<img class="swiper-img" :src="item.thumbnail" alt="">
						</el-carousel-item>
					</el-carousel>
					<div class="article-item" v-for="(item,index) in articleList" :key="item.id" @click="goArticleById(item.id)">
						<div class="article-img">
              <img :src="item.thumbnail" alt="">
            </div>
						<div class="article-info">
							<div class="article-meta">
                <span v-show="item.isTop=='1'" style="color: #ec7c86;" class="meta-item">
                  <i class="iconfont icon-chuangti-zhiding"></i>
                  置顶
                </span>
                <span class="meta-item">
                  <i class="iconfont icon-24gl-tags"></i>
                  {{item.tagName[0]}}
                </span>
                <span class="meta-item">
                  <i class="iconfont icon-riqi_o" style="margin-right: 2px;"></i>
                  <span>{{item.createTime.split(' ')[0]}}</span>
                </span>
              </div>
							<h3 class="article-title">{{item.title}}</h3>
							<div class="article-content">{{item.summary}}</div>
							<div class="article-category" @click="goCategoryItemById(item.categoryId)">
                <span style="color: #ec7c86;">
                  <i class="iconfont" :class="iconObj[item.categoryId]"></i>
                  {{cateName[item.categoryId]}}
                </span>
                <div class="article-view">{{item.viewCount}}人浏览</div>
              </div>
							<div class="article-more">更多...</div>
						</div>
					</div>
				</div>
				<div class="right-content">
					<div class="side-card">
            <div class="host-avatar" @click="$router.push('/about')">
              <img :src="hostInfo.avatar" alt="">
            </div>
            <div class="host-nickName">{{hostInfo.nickName}}</div>
            <div class="host-explanation">{{hostInfo.explanation}}</div>
            <div class="host-info">
              <div class="host-info-item" @click="goArchive">
                <span class="info-item-num1">{{hostInfo.countArticles}}</span>
                <span class="info-item-content">文章</span>
              </div>
              <div class="host-info-item" @click="goCategory">
                <span class="info-item-num2">{{hostInfo.countCategorys}}</span>
                <span class="info-item-content">分类</span>
              </div>
              <div class="host-info-item" @click="goTag">
                <span class="info-item-num3">{{hostInfo.countTags}}</span>
                <span class="info-item-content">标签</span>
              </div>
            </div>
          </div>
					<div class="side-card">
            <div style="font-weight: bold;margin-bottom: 20px;color: #ec7c86;">
              <i class="iconfont icon-hot"></i>
              <span>热门文章</span>
            </div>
            <div class="hot-item" v-for="(item,index) in hotArticleList" :key="item.id" @click="goArticleById(item.id)">
              <div class="hot-title">{{index+1}}. {{item.title}}</div>
              <div class="hot-view">———— 访问量：<span class="hot-viewCount">{{item.viewCount}}</span></div>
            </div>
            <div></div>
          </div>
					<div class="side-card">
            <div>项目运行时间：{{runningTime}}</div>
          </div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import {mapState,mapGetters} from 'vuex'
import {countDown} from '@/utils/tool.js'
import {cateName,iconObj,talkList} from '@/utils/constData.js'
export default {
	name: 'Home',
	data() {
		return {
      pageNum: 1,
      // pageSize: 3,
      pageSize: 1000,
      articleList: [],
      hotArticleList: [],
      talkList: talkList,
      cateName: cateName,
      iconObj: iconObj,
      runningTime: ''
    };
	},
  mounted() {
    this.getArticleList()
    this.getHotArticleList()
    this.getRunningTime()
  },
  computed: {
    ...mapGetters({
      hostInfo: 'hostInfoNow'
    }),
    topArticleList() {
      return this.articleList.filter(item => item.isTop=='1')
    }
  },
  methods: {
    // 查询首页展示文章
    async getArticleList() {
      const {pageNum,pageSize} = this
      var params = {
        pageNum,
        pageSize,
        status: '0'
      }
      const res = await this.$API.home.getArticleList(params)
      console.log('查询首页展示文章',res);
      if(res.code==200) {
        this.articleList = res.data.rows
        // this.articleList = [...this.articleList,...res.data.rows]
      }
    },
    // 查询热门文章
    async getHotArticleList() {
      const res = await this.$API.home.getHotArticleList()
      console.log('查询热门文章',res);
      if(res.code==200) {
        this.hotArticleList = res.data
        // this.hotArticleList = [...this.hotArticleList,...res.data.rows]
      }
    },
    // 跳转文章通过id
    goArticleById(id) {
      this.$router.push({name: 'article',params: {id}})
    },
    // 去归档页
    goArchive() {
      this.$router.push('/archive')
    },
    // 去分类页
    goCategory() {
      this.$router.push('/category')
    },
    // 去标签页
    goTag() {
      this.$router.push('/tag')
    },
    // 计算项目运行时间
    getRunningTime() {
      this.runningTime = countDown('2023-5-20 17:20:00',false)
      setInterval(()=>{
        this.runningTime = countDown('2023-5-20 17:20:00',false)
      },1000)
    }
  },
};
</script>

<style lang="less" scoped>
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
	background: url('@/assets/images/bg_xue.jpg') no-repeat 50% 50%/cover;
	// opacity: 0;
}
.kenburns-bottom {
	-webkit-animation: kenburns-bottom 7s ease-out both;
	animation: kenburns-bottom 7s ease-out both;
}
/**
 * ----------------------------------------
 * animation kenburns-bottom
 * ----------------------------------------
 */
@-webkit-keyframes kenburns-bottom {
  0% {
    -webkit-transform: scale(1) translateY(0);
    transform: scale(1) translateY(0);
    -webkit-transform-origin: 50% 84%;
    transform-origin: 50% 84%;
  }
  100% {
    -webkit-transform: scale(1.25) translateY(15px);
    transform: scale(1.25) translateY(15px);
    -webkit-transform-origin: bottom;
    transform-origin: bottom;
  }
}
@keyframes kenburns-bottom {
  0% {
    -webkit-transform: scale(1) translateY(0);
    transform: scale(1) translateY(0);
    -webkit-transform-origin: 50% 84%;
    transform-origin: 50% 84%;
  }
  100% {
    -webkit-transform: scale(1.25) translateY(15px);
    transform: scale(1.25) translateY(15px);
    -webkit-transform-origin: bottom;
    transform-origin: bottom;
  }
}
.brand-container {
	position: relative;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	height: 100vh;
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
	font-size: 56px;
	line-height: 1.5;
}
.brand .title {
  position: relative;
	font-size: 24px;
	line-height: 1.2;
	letter-spacing: 1.6px;
  padding: 0 20px 40px;
}
.brand .title .sansuo {
  position: absolute;
  right: 0;
  top: 0;
  width: 3px;
  height: 28px;
  background-color: #fff;
  animation: sansuo 1s ease infinite;
}
@keyframes sansuo {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}
@media screen and (max-width: 750px) {
	.brand .title {
		font-size: 18px;
	}
}
.main-container {
	// background-color: rgba(255, 255, 255, 1);
  // background-color: var(--bgc-white);
  min-height: 400px;
}
.content {
	display: flex;
	justify-content: center;
	align-items: flex-start;
	width: calc(100% - 10px);
	min-height: 400px;
	margin: 0 auto;
}
@media (min-width: 1200px) {
	.content {
		width: 1200px;
	}
}
.left-content {
	width: calc(100% - 300px);
  // background-color: rgba(255,255,255,.6);
}
.right-content {
	position: sticky;
  top: 50px;
	width: 300px; 
	margin-left: 13px;
  // background-color: rgba(255,255,255,.6);
}
@media screen and (max-width: 990px) {
	.left-content {
		width: 100%;
	}
	.right-content {
		display: none;
	}
}
//.left-content
.talk-swiper {
	display: flex;
	justify-content: center;
	align-items: center;
	margin: 0 8px;
	padding: 10px 25px;
	font-size: 15px;
	border-radius: 8px;
  line-height: 1.2;
	box-shadow: 0 5px 20px -0.15px #ccc;
  background-color: var(--bgc-opcity);
}
.swiper-content {
	width: 100%;
	text-align: center;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
  line-height: 20px;
}
.swiper-content .medium {
  font-size: 16px;
  color: var(--talk-color);
  font-weight: 600;
  letter-spacing: 1px;
}
.swiper {
	position: relative;
	cursor: pointer;
}
.swiper-container {
	height: 270px;
	margin: 15px 8px 22px 8px;
	border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 20px -0.15px #ccc;
}
.swiper-container::before {
	content: '推荐';
	position: absolute;
	display: flex;
	left: 10px;
	top: 0;
	justify-content: center;
	width: 65px;
	height: 30px;
	line-height: 30px;
	border-radius: 0 0 12px 12px;
	letter-spacing: 3px;
	font-size: 15px;
	background-color: #ec925a;
	color: #fff;
	z-index: 10;
}
.swiper-container .swiper-img {
  // height: 100%;
  // margin-top: -160px;
  width: 100%;
  height: 100%;
  transition: all .6s;
}
.swiper-container .swiper-img:hover {
  transform: scale(1.1);
}
/deep/.is-active .el-carousel__button {
	background-color: #49B1F5;
	width: 30px;
}
/deep/.el-carousel__indicators--horizontal {
	bottom: 20px;
}
/deep/.el-carousel__button {
	height: 10px;
	width: 10px;
	border-radius: 5px;
	background-color: #E8EFF1;
	transition: all 1s;
}
/deep/.el-carousel__arrow {
  height: 50px;
  width: 50px;
  font-size: 18px;
  color: #ec7c86;
  background-color: rgba(255,255,255,.11);
}
/deep/.el-carousel__arrow:hover {
  background-color: rgba(150,150,150,.2);
}
.article-item {
	display: flex;
	height: 224px;
	margin: 20px 8px 0;
	border-radius: 10px;
	box-shadow: var(--box-shadow);
	overflow: hidden;
  transition: all .25s;
  background-color: var(--bgc-opcity);
}
.article-item:hover {
  box-shadow: 0 0 20px 5px #ccc;
  transform: translateX(-15px);
  
}
.article-item:nth-child(even) {
	flex-direction: row-reverse;
}
.article-item .article-img,
.article-item .article-info {
	width: 50%;
	height: 224px;
}
.article-item .article-img {
	margin-right: 24px;
	clip-path: polygon(0 0, 92% 0, 100% 100%, 0 100%);
	border-radius: 10px 0 0 10px;
	background-color: yellow;
}
.article-item .article-img img{
  width: 100%;
  height: 100%;
}
.article-item:nth-child(even) .article-img {
	margin-right: auto;
	margin-left: 24px;
	-webkit-clip-path: polygon(0 0, 100% 0, 100% 100%, 8% 100%);
	clip-path: polygon(0 0, 100% 0, 100% 100%, 8% 100%);
	border-radius: 0 10px 10px 0;
}
.article-item .article-info {
	position: relative;
	width: 50%;
	padding: 16px 24px 48px 0;
}
.article-item:nth-child(even) .article-info {
	padding: 16px 0 48px 24px;
}
.article-meta {
	display: flex;
	justify-content: flex-end;
  align-items: center;
	font-size: 14px;
	color: var(--grey-8);
}
.article-meta .meta-item {
  margin: 5px;
}
.article-meta .meta-item .iconfont {
  font-size: 14px;
}
.article-title {
	text-overflow: ellipsis;
	white-space: nowrap;
	margin: 10px 0;
	color: var(--pink);
	font-weight: 700;
	font-size: 18px;
	line-height: 1.5;
	overflow: hidden;
  
}
.article-content {
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 3;
	max-height: 80px;
	font-size: 14px;
	overflow: hidden;
}
.article-category {
	position: absolute;
	display: flex;
	bottom: 8px;
	align-items: center;
	height: 26px;
	font-size: 14px;
	color: var(--grey-8);
}
.article-category .iconfont {
  font-size: 12px;
  margin-right: 3px;
}
.article-view {
	position: absolute;
	display: flex;
	bottom: 0;
  right: -80px;
	align-items: center;
	height: 26px;
	font-size: 14px;
	color: #8f8f8f;
}
.article-more {
	position: absolute;
	right: 0;
	bottom: 0;
	height: 40px;
	line-height: 30px;
	padding: 5px 16px;
	border-radius: 10px 0;
	font-size: 16px;
	color: var(--common-color);
	background-color: var(--pink);
}

@media screen and (max-width:767px) {
	.article-item,
	.article-item:nth-child(even) {
		flex-direction: column;
		height: fit-content;
	}
	.article-item .article-img,
	.article-item .article-info {
		width: 100%;
		height: 224px;
	}
	.article-item .article-img {
		clip-path: polygon(0 0,100% 0,100% 92%,0 100%);
		border-radius: 10px 10px 0 0;
		margin: auto;
	}
	.article-item:nth-child(even)  .article-img {
		clip-path: polygon(0 0,100% 0,100% 100%,0 92%);
		border-radius: 10px 10px 0 0;
		margin: auto;
	}
	.article-item .article-info {
		padding: 12px 16px 48px; 
	}
	.article-item:nth-child(even) .article-info {
		padding: 12px 16px 48px;
	}
}

// .right-content
.side-card:not(:first-child) {
	margin-top: 20px;
}
.side-card {
	// height: 300px;
	padding: 16px;
	border-radius: 8px;
	box-shadow: 0 0 16px #ccc;
  background-color: var(--bgc-opcity);
}
.side-card .host-avatar {
  text-align: center;
  width: 150px;
  height: 150px;
  margin: 15px auto;
  border-radius: 50%;
  overflow: hidden;
}
.side-card .host-avatar img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  transition: all .5s;
}
.side-card .host-avatar img:hover {
  transform: scale(1.1);
}
.side-card .host-nickName {
  text-align: center;
  padding-bottom: 5px;
  font-size: 18px;
  color: var(--host-color);
}
.side-card .host-explanation  {
  text-align: center;
  color: #999;
  padding-top: 5px;
}
.side-card .host-info {
  display: flex;
  justify-content: center;
}
.side-card .host-info .host-info-item {
  display: flex;
  flex-direction: column;
  text-align: center;
  border-right: 1px solid #ccc;
  padding: 5px 10px 0 10px;
  margin: 7px 0;
}
.side-card .host-info .host-info-item .info-item-num1,
.side-card .host-info .host-info-item .info-item-num2,
.side-card .host-info .host-info-item .info-item-num3 {
  font-weight: bold;
  font-size: 18px;
}
.side-card .host-info .host-info-item .info-item-num1{
  color: #ec7c86;
}
.side-card .host-info .host-info-item .info-item-num2 {
  color: #49B1F5;
}
.side-card .iconfont {
  margin-right: 4px;
}
.side-card .hot-item {
  height: 71px;
  line-height: 25px;
  padding: 10px 0;
  border-top: 1px solid #ccc;
  transition: all .4s;
}
.side-card .hot-item:hover .hot-title {
  color: #ec7c86;
  // transform: scale(1.02) translateZ(0);
  transform: translateX(4px);
}
.side-card .hot-item .hot-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: all .3s;
  backface-visibility: hidden;
  transform: translate3d(0,0,0);
}
.side-card .hot-item .hot-view {
  padding-left: 90px;
  font-size: 14px;
  color: var(--grey-8);
}
.side-card .hot-item .hot-view .hot-viewCount {
  font-size: 16px;
  color: #ec7c86;
}
</style>
