<template>
	<div class="main">
		<div class="imgs">
			<ul>
				<li class="item"></li>
				<!-- <li class="item" style="background-image: url(@/assets/images/男头像.png);"></li> -->
			</ul>
		</div>
		<div class="brand-container">
			<div class="brand">
				<p class="art">管理文章</p>
			</div>
		</div>
		<div class="main-container mt">
			<div class="content">
				<div class="left-content">
					<div v-show="!isHidden" class="title-box">
            <div class="release" @click="goShowArticle('0')">
              <span class="release-title">已发布</span>
            </div>
            <div class="draft" @click="goShowArticle('1')">
              <span class="draft-title">草稿箱</span>
            </div>
          </div>
          <div v-show="isHidden&&articleList.length>0" class="article">
            <div class="back" @click="goBack">
              <i class="iconfont icon-fanhui"></i>返回
            </div>
            <div class="title">目前共计{{articleList.length}}篇文章，继续加油！</div>
            <div class="block">
              <el-timeline>
                <el-timeline-item v-for="item in articleList" :key="item.id" :timestamp="item.createTime" placement="top" color="#ec7c86">
                  <el-card class="card">
                    <div class="article-box">
                      <div class="operations">
                        <span @click="goArticleById(item.id)" v-show="status=='0'">预览</span>
                        <span @click="goWriteArticle(item.id)">编辑</span>
                        <span @click="deleteArticleById(item.id)">删除</span>
                      </div>
                      <div class="article-img">
                        <img :src="item.thumbnail" alt="">
                      </div>
                      <div class="article-info">
                        <h2 class="article-title">{{item.title}}</h2>
                        <p>{{item.summary}}</p>
                        <div class="article-category">
                          <span>
                            <i class="iconfont" :class="iconObj[item.categoryId]"></i>{{cateName[item.categoryId]}}
                          </span>
                          <div class="article-view">
                          <i class="iconfont icon-icon-"></i>{{item.viewCount}}人浏览</div>
                        </div>
                      </div>
                    </div>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
          <div v-show="isHidden&&articleList.length==0">
            <div class="back" @click="goBack">
              <i class="iconfont icon-fanhui"></i>返回
            </div>
            <Empty></Empty>
          </div>
        </div>
			</div>
		</div>
	</div>
</template>

<script>
import { components } from 'markdown-it/lib/presets/commonmark';
import {cateName,iconObj} from '@/utils/constData.js'
export default {
	name: 'ManageArticle',
  components: {
  },
	data() {
		return {
      pageNum: 1,
      pageSize: 1000,
      status: this.$route.params.status,
      articleList: [],
      isHidden: false,
      beforeStatus: '',
      cateName: cateName,
      iconObj: iconObj
    };
	},
  mounted() {},
  methods: {
    // 查询管理展示文章
    async getArticleList() {
      const {pageNum,pageSize,status} = this
      var params = {
        pageNum,
        pageSize,
        status
      }
      const res = await this.$API.home.getArticleList(params)
      console.log('查询管理展示文章',res);
      if(res.code==200) {
        this.beforeStatus = status
        this.articleList = res.data.rows
        this.articleList.sort(function(a, b) {
          var aDate = +new Date(a.createTime)
          var bDate = +new Date(b.createTime)
          return bDate - aDate
        })
        return 200
      }
      return 201
    },
    // 跳转文章通过id
    goArticleById(id) {
      this.$router.push({name: 'article',params: {id}})
    },
    // 去编辑文章
    goWriteArticle(id) {
      this.$router.push({name: 'editarticle',params: {id},query: {id,beforeStatus: this.beforeStatus}})
    },
    // 返回
    goBack() {
      this.isHidden = false
    },
    // 展示已发布文章或展示草稿箱文章
    async goShowArticle(status) {
      this.status = status
      var code = await this.getArticleList()
      console.log('已发布文章或展示草稿箱文章',code);
      if(code==200) {
        this.isHidden = true
      }
    },
    // 根据id删除文章
    deleteArticleById(id) {
      this.$confirm('是否删除文章？','提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        lockScroll: false
      }).then(async(e)=>{
        const params = {id}
        const res = await this.$API.article.deleteArticleById(params)
        console.log('删除文章操作',res)
        if(res.code==200) {
          this.$message.success('删除文章成功')
          this.goShowArticle(this.status)
          this.$store.dispatch('getHostInfo')
        } else if(res.code==401||res.code==402) {
          console.log('token过期 或 强制下线',res);
          this.$bus.$emit('loginOut')
        } else if(res.code==403) {
          this.$message({
            message: '您无权限进行该操作',
            type: 'error',
            showClose: true
          })
        } else {
          this.$message({
            message: '删除文章失败',
            type: 'error',
            showClose: true
          })
        }
      }).catch((e)=>{
        console.log('已取消删除文章',e)
        this.$message({
          message: '已取消删除文章',
          type: 'info',
          showClose: true
        })
      })
    }
  },
  watch: {
    $route: {
      immediate: true,
      handler(to,from) {
        console.log('管理文章跳转路由',to,from);
        if(to.name=='managearticle'&&to.params.status) {
          this.goShowArticle(to.params.status)
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
	height: 60vh;
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
	font-size: 42px;
	line-height: 1.2;
  letter-spacing: 5px;
  font-weight: bold;
  margin-top: 10px;
}
.brand .title {
	font-size: 24px;
	line-height: 1.2;
	letter-spacing: 1.6px;
	padding-bottom: 40px;
}
@media screen and (max-width: 750px) {
	.brand .title {
		font-size: 18px;
	}
}
.main-container {
	// background: linear-gradient(90deg,#FFCDB8,#B8E2F7);
  background: var(--bgc-gradient);
}
.content {
	display: flex;
	justify-content: center;
	align-items: flex-start;
	width: calc(100% - 10px);
	margin: 0 auto;
  min-height: 400px;
}
@media (min-width: 1300px) {
	.content {
		width: 1100px;
	}
}
.left-content {
  position: relative;
	width: 100%;
  min-height: 400px;
  padding: 30px 20px;
  box-shadow: 0 10px 30px -18px #000;
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
.left-content .title-box {
  padding: 50px 0;
  transition: all 1s;
  animation: trans 1s;
}
.left-content .release {
  position: relative;
  width: 450px;
  height: 450px;
  background-color: #FFA181;
  font-size: 40px;
  border-radius: 50%;
  margin: 0 auto;
  clip-path: polygon(-50px -50px,-50px 500px,500px -50px);
  transition: all .5s;
}
.left-content .release:hover {
  transform: scale(1.1);
  box-shadow: 0 0 50px #FFA181;
}
.left-content .release .release-title {
  position: absolute;
  left: 100px;
  top: 120px;
  color: #B8E2F7;
}
.left-content .draft {
  position: relative;
  width: 450px;
  height: 450px;
  background-color: #B8E2F7;
  border-radius: 50%;
  font-size: 40px;
  clip-path: polygon(500px 500px,-50px 500px,500px -50px);
  margin: 0 auto;
  margin-top: -450px;
  transition: all .5s;
}
.left-content .draft:hover {
  transform: scale(1.1);
  box-shadow: 0 0 50px #B8E2F7;
}
.left-content .draft .draft-title {
  position: absolute;
  right: 100px;
  bottom: 120px;
  color: #FFA181;
}
.left-content .article {
  transition: all 1s;
  animation: trans 1s;
}
.left-content .back {
  position: absolute;
  color: #333;
  font-size: 18px;
  line-height: 34px;
  transition: transform .5s;
}
.left-content .back:hover {
  transform: scale(1.1);
}
.left-content .title {
  font-size: 26px;
  text-align: center;
  margin: 10px 0 20px 0;
  color: deeppink;
}
/deep/.el-timeline-item__timestamp {
  font-size: 14px;
  color: #ec7c86;
}
.card {
  transition: all .5s;
  margin: 15px 0 5px 0;
}
.card:hover {
  box-shadow: 5px 5px 10px 2px #ccc;
}
.article-box {
  display: flex;
  position: relative;
  align-items: center;
}
.article-box .operations {
  position: absolute;
  right: 0;
  bottom: 0;
  display: flex;
  // justify-content: space-around;
  font-size: 16px;
}
.article-box .operations span {
  margin-left: 20px;
  transition: all .5s;
}
.article-box .operations span:hover {
  color: #ec7c86;
}
.article-box .article-img {
  width: 200px;
  height: 150px;
  font-size: 0;
  margin-right: 20px;
  // border-radius: 10px;
}
.article-box .article-img img {
  width: 200px;
  height: 100%;
  border-radius: 10px;
}
@media screen and (max-width:767px) {
  .brand-container {
  	height: 50vh;
  }
  .left-content .release {
    width: 250px;
    height: 250px;
    font-size: 20px;
    clip-path: polygon(-50px -50px,-50px 300px,300px -50px);
  }
  .left-content .release .release-title {
    left: 60px;
    top: 60px;
  }
  .left-content .draft {
    width: 250px;
    height: 250px;
    font-size: 20px;
    clip-path: polygon(300px 300px,-50px 300px,300px -50px);
    margin-top: -250px;
  }
  .left-content .draft .draft-title {
    right: 60px;
    bottom: 60px;
  }
  .left-content .back {
    transition: transform 0s;
    transform: translateY(-35px);
  }
  .left-content .back:hover {
    transform: translateY(-35px);
  }
  .article-box {
    flex-wrap: wrap;
  }
  .article-box .operations {
    bottom: -10px;
    font-size: 14px;
  }
  .article-box .operations span {
    margin-left: 10px;
  }
  .article-box .article-img {
    width: 100%;
    margin-right: 0px;
    margin-bottom: 10px;
  }
  .article-box .article-img img {
    width: 100%;
  }
  .article-box .article-category {
    margin-bottom: 5px;
  }
}
.article-box .article-title {
  font-size: 24px;
  margin-bottom: 10px;
}
.article-box .article-category {
	display: flex;
	align-items: center;
	height: 26px;
	font-size: 14px;
	color: #8f8f8f;
  margin-top: 10px;
}
.article-box .article-category .iconfont {
  font-size: 14px;
  margin-right: 3px;
  color: #ec7c86;
}
.article-box .article-view {
	display: flex;
	align-items: center;
	height: 26px;
	font-size: 14px;
	color: #8f8f8f;
  margin-left: 10px;
}
</style>
