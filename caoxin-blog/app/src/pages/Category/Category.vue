<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">分类</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container" v-if="articleList.length>0">
				<h2 class="category-title">分类 - {{categoryList.length}}</h2>
        <div class="cates">
          <div class="cate-item" v-for="cate in categoryList" :key="cate.id">
            <i class="iconfont icon-tongzhi"></i>
            <span class="link" @click="$router.push(`/categoryitem/${cate.id}`)">
              <span class="cate-name">{{cate.name}}</span>
              <span class="cate-num">({{cate.num}})</span>
            </span>
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
import {mapState} from 'vuex'
import {categoryList} from '@/utils/constData.js'
export default {
	name: 'Category',
	data() {
		return {
      pageNum: 1,
      pageSize: 1000,
      articleList: [],
      categoryList: categoryList
    };
	},
  mounted() {
    this.getArticleList()
  },
  methods: {
    // 查询所有已发布文章
    async getArticleList() {
      const {pageNum,pageSize} = this
      var params = {
        pageNum,
        pageSize,
        status: '0'
      }
      const res = await this.$API.home.getArticleList(params)
      console.log('分类页查询所有已发布展示文章',res);
      if(res.code==200) {
        this.articleList = res.data.rows
        this.articleList.forEach(item => {
          this.categoryList.forEach(cate => {
            if(cate.id == item.categoryId) {
              cate.num++
            }
          })
        })
      }
    },
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
	margin-top: 10px;
}
.main-container {
	background: var(--bgc-gradient);
	overflow: hidden;
  min-height: 400px;
}
.page-container {
	width: calc(100% - 24px);
	min-height: 400px;
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
.page-container .category-title {
  text-align: center;
  font-size: 36px;
  margin-top: 20px;
  margin-bottom: 20px;
  font-weight: normal;
}
.page-container .cates {
  padding: 20px;
  // text-align: center;
}
.cate-item {
  padding: 15px 30px;
  font-size: 20px;
  transition: all .6s;
}
.cate-item:hover .iconfont {
  color: orange;
  
}
.cate-item .iconfont {
  color: #B8E2F7;
  margin-right: 20px;
  transition: all .3s;
  vertical-align: 1px;
}
.cate-item .link {
  transition: all .3s;
}
.cate-item .link:hover {
  color: #03a9f4;
}
.cate-item .cate-num {
  margin-left: 10px;
  font-size: 14px;
  opacity: .7;
}
</style>
