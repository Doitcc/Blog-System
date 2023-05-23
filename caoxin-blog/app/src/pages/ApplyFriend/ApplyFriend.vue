<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">申请友链</h1>
			</div>
		</div>
		<div class="main-container mt">
      <div v-show="!requestOk" class="page-container article-loading">
        <i class="iconfont icon-jiazaizhong"></i>
        <span>正在加载中</span>
      </div>
			<div v-show="requestOk" class="page-container">
        <div class="title" v-show="!applyStatusObj.status">
          您目前还未申请友链，点击下方申请按钮填写信息即可申请！
        </div>
        <div v-if="isApply">
          <div class="friend-box">
            <div class="friend-status" :class="applyStatusObj.color">
              <span>{{applyStatusObj.text}}</span>
            </div>
            <div class="friend-info">
              <div class="friend-logo">
                <img :src="friendInfo.logo" alt="">
              </div>
              <div class="friend-detail">
                <div class="friend-name">{{friendInfo.name}}</div>
                <div class="friend-description">{{friendInfo.description}}</div>
                <div class="friend-createtime">申请时间：{{friendInfo.createTime}}</div>
              </div>
              <div class="friend-choose">
                <el-button v-show="friendInfo.status=='1'" @click="showUpdateDialog" type="primary">申请修改</el-button>
                <el-button v-show="friendInfo.status=='-1'" @click="showApplyDialog" type="primary">重新申请</el-button>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="info-container">
          <div class="introduction-body">
            <div class="info-item">
              <el-button @click="showApplyDialog" class="apply-btn">申请友链</el-button>
            </div>
          </div>
        </div>
			</div>
      
      <!-- 申请友链信息 -->
      <el-dialog title="修改友链信息" :lock-scroll="false" :visible.sync="dialogApplyVisible">
        <el-form :model="friendForm">
          <el-form-item label="友链名称" :label-width="formLabelWidth">
            <el-input v-model="friendForm.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="友链头像地址" :label-width="formLabelWidth">
            <el-input v-model="friendForm.logo" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="友链描述" :label-width="formLabelWidth">
            <el-input v-model="friendForm.description" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="友链地址" :label-width="formLabelWidth">
            <el-input v-model="friendForm.address" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="applyFriend">确 定</el-button>
        </div>
      </el-dialog>
      
      <!-- 修改友链信息 -->
      <el-dialog title="填写友链信息" :lock-scroll="false" :visible.sync="dialogUpdateVisible">
        <el-form :model="updateFriendForm">
          <el-form-item label="友链名称" :label-width="formLabelWidth">
            <el-input v-model="updateFriendForm.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="友链头像地址" :label-width="formLabelWidth">
            <el-input v-model="updateFriendForm.logo" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="友链描述" :label-width="formLabelWidth">
            <el-input v-model="updateFriendForm.description" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="友链地址" :label-width="formLabelWidth">
            <el-input v-model="updateFriendForm.address" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="updateFriend">确 定 修 改</el-button>
        </div>
      </el-dialog>
		</div>
  </div>
</template>

<script>
export default {
	name: 'ApplyFriend',
	data() {
		return {
      requestOk: false,
      isApply: false,
      friendForm: {
        name: '',
        logo: '',
        description: '',
        address: ''
      },
      dialogApplyVisible: false,
      formLabelWidth: '120px',
      friendInfo: {},
      applyStatusArr: [
        {
          status: '-1',
          text: '不通过',
          color: 'red'
        },
        {
          status: '0',
          text: '审核中',
          color: 'pink'
        },
        {
          status: '1',
          text: '已通过',
          color: 'green'
        }
      ],
      updateFriendForm: {},
      dialogUpdateVisible: false,
    };
	},
  mounted() {
    this.getApplyFriendStatus()
  },
  methods: {
    // 查看友链申请状态
    async getApplyFriendStatus() {
      const res = await this.$API.friend.getApplyFriendStatus()
      if(res.code==200) {
        console.log('查看友链申请状态',res)
        this.friendInfo = res.data
        this.isApply =true
        this.requestOk = true
        console.log('当前友链信息',this.friendInfo)
      } else if(res.code==601) {
        console.log('查看友链申请状态',res.msg)
        this.requestOk = true
        this.isApply = false
      } else if(res.code==401||res.code==402) {
        console.log('token过期 或 强制下线',res);
        this.$bus.$emit('loginOut')
      } else {
        console.log('查看友链申请状态失败',res)
      }
    },
    // 展示申请友链对话框
    showApplyDialog() {
      this.dialogApplyVisible = true
    },
    // 展示修改友链对话框
    showUpdateDialog() {
      const {name,logo,description,id,address} = this.friendInfo
      this.updateFriendForm = {name,logo,description,id,address}
      console.log('updateFriendForm',this.updateFriendForm);
      this.dialogUpdateVisible = true
    },
    // 校验是否有属性值为空，正则表达式校验
    isAttrNull(obj) {
      for(var k in obj) {
        if(obj.hasOwnProperty(k)&&obj[k]==''){
          console.log(k,obj[k],obj);
          this.$message({
            type: 'warning',
            message: '请填写完整的友链信息',
            showClose: true
          })
          return false
        }
      }
      var {name,logo,description,address} = obj
      name = name.trim()
      logo = logo.trim()
      description = description.trim()
      address = address.trim()
      obj.name = name
      obj.logo = logo
      obj.description = description
      obj.address = address
      if(name.length<=0||name.length>15) {
        this.$message({
          type: 'warning',
          message: '昵称长度必须在0~15之间',
          showClose: true
        })
        return false
      }
      if(logo.length<=0||logo.length>200) {
        this.$message({
          type: 'warning',
          message: '头像地址长度必须在0~200之间',
          showClose: true
        })
        return false
      }
      if(description.length<=0||description.length>15) {
        this.$message({
          type: 'warning',
          message: '友链描述长度必须在0~15之间',
          showClose: true
        })
        return false
      }
      if(address.length<=0||address.length>200) {
        this.$message({
          type: 'warning',
          message: '友链地址长度必须在0~200之间',
          showClose: true
        })
        return false
      }
      var addressRule = /^[a-zA-z]+:\/\/[^\s]*/
      if(!addressRule.test(address)) {
        this.$message({
          type: 'warning',
          message: '请填写正确的友链地址格式',
          showClose: true
        })
        return false
      }
      return true
    },
    // 申请友链
    async applyFriend() {
      if(this.isAttrNull(this.friendForm)) {
        console.log('申请友链',this.friendForm);
        const res = await this.$API.friend.applyFriend(this.friendForm)
        if(res.code==200) {
          this.$message({
            type: 'success',
            message: '发送申请成功，等待审核',
            showClose: true
          })
          this.dialogApplyVisible = false
          this.isApply = true
          this.getApplyFriendStatus()
        } else if(res.code==401||res.code==402) {
          console.log('token过期 或 强制下线',res);
          this.$bus.$emit('loginOut')
        } else {
          this.$message({
            type: 'error',
            message: '发送申请失败，请稍后再试',
            showClose: true
          })
        }
      }
    },
    // 修改友链
    async updateFriend() {
      console.log('updateFriendForm',this.updateFriendForm);
      if(this.isAttrNull(this.updateFriendForm)) {
        console.log('修改友链',this.updateFriendForm);
        const res = await this.$API.friend.updateFriend(this.updateFriendForm)
        if(res.code==200) {
          this.$message({
            type: 'success',
            message: '发送修改申请成功，等待审核',
            showClose: true
          })
          this.dialogUpdateVisible = false
          this.isApply = true
          this.getApplyFriendStatus()
        } else if(res.code==401||res.code==402) {
          console.log('token过期 或 强制下线',res);
          this.$bus.$emit('loginOut')
        } else {
          this.$message({
            type: 'error',
            message: '发送修改申请失败，请稍后再试',
            showClose: true
          })
        }
      }
    }
  },
  computed: {
    applyStatusObj() {
      var len = this.applyStatusArr.length
      for(var i=0;i<len;i++) {
        if(this.applyStatusArr[i].status==this.friendInfo.status) {
          return this.applyStatusArr[i]
        }
      }
      return {}
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
  min-height: 500px;
}
.page-container {
	width: calc(100% - 24px);
	min-height: 500px;
	margin: 0 auto;
	padding: 28px 40px;
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
.page-container .title {
  font-size: 24px;
  // font-weight: bold;
  text-align: center;
  margin-top: 20px;
}
.page-container .friend-box {
  margin: 30px 0;
  padding: 20px;
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
  padding-left: 20px;
  padding-bottom: 20px;
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
.friend-box .friend-detail .friend-createtime {
  color: #999;
}
.friend-box .friend-choose {
  padding: 0 10px;
  display: flex;
  align-items: center;
}
.page-container .info-container {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  padding: 0 40px;
}
.introduction-body {
  width: 520px;
  font-size: 16px;
  // height: 100%;
	padding: 50px 0;
}
.introduction-body .info-item {
  margin: 20px 0;
  text-align: center;
}
.introduction-body .info-item .apply-btn {
  width: 50%;
  margin-top: 10px;
  background-color: #EC7298;
  color: #fff;
  font-size: 24px;
  line-height: 40px;
}
.article-loading {
  display: flex;
  align-items: center;
  // justify-content: center;
  flex-direction: column;
  padding-top: 100px;
}
.article-loading .iconfont {
  font-size: 50px;
  margin-bottom: 10px;
}
.article-loading span {
  font-size: 30px;
}
</style>