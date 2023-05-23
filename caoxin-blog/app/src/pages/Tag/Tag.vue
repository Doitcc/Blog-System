<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">标签</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container" v-if="tagList.length>0">
				<h2 class="tag-title">标签 - {{tagList.length}}</h2>
        <div class="tags">
          <span class="tag-item" v-for="tag in tagList" :key="tag.id" @click="goArticleByTagId(tag.id,tag.name)">{{tag.name}}</span>
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
export default {
	name: 'Tag',
	data() {
		return {
      tagList: []
    };
	},
  mounted() {
    this.getTagList()
  },
  methods: {
    // 获取所有文章标签列表数据
    async getTagList() {
      const params = {
        pageNum:1,
        pageSize: 1000,
      }
      const res = await this.$API.article.getTagList(params)
      console.log('获取所有文章标签列表数据',res);
      if(res.code == 200) {
        this.tagList = res.data.rows
      }
    },
    // 去该标签的文章通过标签id
    goArticleByTagId(tagId,tagName) {
      this.$router.push({name: 'tagitem',params: {tagId},query:{tagName}})
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
.page-container .tag-title {
  text-align: center;
  font-size: 36px;
  margin-top: 20px;
  margin-bottom: 20px;
  font-weight: normal;
}
.page-container .tags {
  padding: 20px;
  text-align: center;
}
.tag-item {
  margin-right: 30px;
  margin-bottom: 30px;
  font-size: 20px;
  transition: all .6s;
  display: inline-block;
  color: #03a9f4;
}
.tag-item:hover {
  color: orange;
  transform: scale(1.2);
}
</style>
