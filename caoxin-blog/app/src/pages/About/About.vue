<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">关于</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container">
				<div class="avatar-box">
					<img :src="hostInfo.avatar" class="author-avatar" alt="">
				</div>
				<div class="introduction">
					<div class="introduction-body">
						<p>个人简介</p>
						<p>想进大厂的小白</p>
						<p>感谢大佬们的开源精神</p>
            <p>{{hostInfo.explanation}}</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import {mapState} from 'vuex'
import getHostIndoMixin from '@/mixins/getHostIndoMixin'
export default {
	name: 'About',
  mixins: [getHostIndoMixin],
	data() {
		return {
      
    };
	},
  mounted() {
    
  },
  methods:{
    
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
.avatar-box {
  margin: 10px auto;
  width: 160px;
  height: 160px;
  border-radius: 50%;
  overflow: hidden;
}
.author-avatar {
	display: block;
	width: 160px;
	height: 160px;
	border-radius: 50%;
	// padding: 2px;
  transition: all .5s;
}
.author-avatar:hover {
  transform: scale(1.1);
}
.introduction {
	font-size: 16px;
}
.introduction-body {
	padding: 32px 40px;
}
.introduction-body>p {
	line-height: 1.7;
	margin: 0 0 14px;
}
</style>
