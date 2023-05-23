<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">管理友链</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container">
				<el-tabs v-model="activeName" @tab-click="handleClick">
				    <el-tab-pane label="已通过" name="first" class="first">
              <div class="friend-box" v-for="item in firstFriendList" :key="item.id" v-show="firstFriendList.length>0">
                <div class="friend-info">
                  <div class="friend-logo">
                    <img :src="item.logo" alt="">
                  </div>
                  <div class="friend-detail">
                    <div class="friend-name">
                      友链名称：{{item.name}}
                    </div>
                    <div class="friend-description">
                      <span class="description-name">友链描述：</span>
                      <span class="description-content">{{item.description}}</span>
                    </div>
                    <div class="friend-link">
                      友链地址：<a :href="item.address" target="_blank">{{item.address}}</a>
                    </div>
                    <div class="friend-createtime">申请时间：{{item.updateTime}}</div>
                  </div>
                  <div class="friend-choose">
                    <el-button type="danger" @click="deleteFriendById(item.id)">删除友链</el-button>
                  </div>
                </div>
              </div>
              <Empty v-show="firstFriendList.length<=0"></Empty>
            </el-tab-pane>
				    <el-tab-pane label="审核中" name="second" class="second">
              <div class="friend-box" v-for="item in secondFriendList" :key="item.id" v-show="secondFriendList.length>0">
                <div class="friend-info">
                  <div class="friend-logo">
                    <img :src="item.logo" alt="">
                  </div>
                  <div class="friend-detail">
                    <div class="friend-name">
                      友链名称：{{item.name}}
                    </div>
                    <div class="friend-description">
                      友链描述：{{item.description}}
                    </div>
                    <div class="friend-link">
                      友链地址：<a :href="item.address" target="_blank">{{item.address}}</a>
                    </div>
                    <div class="friend-createtime">申请时间：{{item.updateTime}}</div>
                  </div>
                  <div class="friend-choose">
                    <el-button type="success" @click="auditFriendById(item.id,'1')">通过</el-button>
                    <el-button type="danger" @click="auditFriendById(item.id,'-1')">不通过</el-button>
                  </div>
                </div>
              </div>
              <Empty v-show="secondFriendList.length<=0"></Empty>
            </el-tab-pane>
				    <el-tab-pane label="未通过" name="third" class="third">
              <div class="friend-box" v-for="item in thirdFriendList" :key="item.id" v-show="thirdFriendList.length>0">
                <div class="friend-info">
                  <div class="friend-logo">
                    <img :src="item.logo" alt="">
                  </div>
                  <div class="friend-detail">
                    <div class="friend-name">
                      友链名称：{{item.name}}
                    </div>
                    <div class="friend-description">
                      友链描述：{{item.description}}
                    </div>
                    <div class="friend-link">
                      友链地址：<a :href="item.address" target="_blank">{{item.address}}</a>
                    </div>
                    <div class="friend-createtime">申请时间：{{item.updateTime}}</div>
                  </div>
                  <div class="friend-choose">
                    <el-button type="danger" @click="deleteFriendById(item.id)">删除</el-button>
                  </div>
                </div>
              </div>
              <Empty v-show="thirdFriendList.length<=0"></Empty>
            </el-tab-pane>
				  </el-tabs>
			</div>
		</div>
	</div>
</template>

<script>
import { getAdminFriendList } from '@/api/friend/friend';
export default {
	name: 'ManageFriend',
	data() {
		return {
      activeName: 'first',
      allFriendList: [],
      firstFriendList: [],
      secondFriendList: [],
      thirdFriendList: [],
    };
	},
  mounted() {
    this.getAdminFriendList()
  },
  methods:{
    // 管理员获取所有友链信息
    async getAdminFriendList() {
      const res = await this.$API.friend.getAdminFriendList()
      console.log('管理员获取所有友链信息',res)
      if(res.code==200) {
        this.allFriendList = res.data
        this.firstFriendList = []
        this.secondFriendList = []
        this.thirdFriendList = []
        this.allFriendList.forEach(item => {
          if(item.status=='1') {
            this.firstFriendList.push(item)
          } else if(item.status=='0') {
            this.secondFriendList.push(item)
          } else if(item.status=='-1') {
            this.thirdFriendList.push(item)
          }
        })
      } else if(res.code==401||res.code==402) {
        console.log('token过期 或 强制下线',res);
        this.$bus.$emit('loginOut')
      }
    },
    // 管理员删除友链信息根据id
    async deleteFriendById(id) {
      const params = {id}
      console.log('id',id);
      const res = await this.$API.friend.deleteFriendById(params)
      console.log('管理员删除友链信息根据id',res);
      if(res.code==200) {
        this.$message({
          type: 'success',
          message: '删除友链成功',
          showClose: true
        })
        this.getAdminFriendList()
      } else if(res.code==401||res.code==402) {
        console.log('token过期 或 强制下线',res);
        this.$bus.$emit('loginOut')
      }
    },
    // 审核友链是否通过
    async auditFriendById(id, status) {
      const params = {id, status}
      const res = await this.$API.friend.auditFriendById(params)
      console.log('审核友链是否通过',res);
      if(res.code==200) {
        this.$message({
          type: 'success',
          message: '审核通过',
          showClose: true
        })
        this.getAdminFriendList()
      } else if(res.code==401||res.code==402) {
        console.log('token过期 或 强制下线',res);
        this.$bus.$emit('loginOut')
      }
    },
    // tab切换监听
    handleClick(tab, event) {
      // console.log(tab, event);
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
.page-container .friend-box {
  margin: 30px auto;
  padding: 20px;
  width: 90%;
  border-radius: 5px;
  box-shadow: 0 0 16px rgba(0, 0, 0, 0.1);
}
.friend-box .friend-status {
  font-size: 20px;
  padding-left: 20px;
  margin-bottom: 20px;
}
.friend-box .friend-status span {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 5px;
  letter-spacing: 2px;
}
.friend-box .friend-status.red span {
  color: rgb(254, 105, 108);
  background-color: rgba(254, 105, 108, .2);
}
.friend-box .friend-status.pink span {
  color: #49B1F5;
  background-color: #49B1F522;
}
.friend-box .friend-status.green span {
  color: rgb(103, 194, 58);
  background-color: rgba(103, 194, 58, .2);
}
.friend-box .friend-info {
  display: flex;
  justify-content: space-between;
  // padding-left: 20px;
}
.friend-box .friend-info .friend-logo {
  height: 120px;
  width: 120px;
  margin-right: 20px;
  border-radius: 10px;
}
.friend-box .friend-info .friend-logo img {
  height: 120px;
  width: 120px;
  border-radius: 10px;
}
.friend-box .friend-info .friend-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}
.friend-box .friend-detail .friend-name {
  color: #49B1F5;
  font-size: 18px;
}
.friend-box .friend-detail .friend-description {
  display: flex;
}
.friend-box .friend-detail .friend-description .description-name {
  display: inline-block;
  width: 80px;
}
.friend-box .friend-detail .friend-description .description-content {
  word-break: break-all;
  flex: 1;
}
.friend-box .friend-detail .friend-link a {
  color: #EC7298;
}
.friend-box .friend-detail .friend-link a:hover {
  text-decoration: underline;
}
.friend-box .friend-detail .friend-createtime {
  // color: #999;
}
.friend-box .friend-choose {
  padding: 0 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 200px;
}
.second /deep/.el-button--success {
  width: 84px;
}
.third /deep/.el-button--danger {
  width: 84px;
}
</style>
