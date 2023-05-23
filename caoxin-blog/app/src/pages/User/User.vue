<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">个人中心</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container">
        <div class="title">
          基本信息
        </div>
        <div class="info-container">
          <div class="avatar-box" @click="showUpload">
          	<img :src="userInfo.avatar" class="author-avatar" alt="">
          </div>
          <div class="introduction-body">
            <div class="info-item">
              <span>昵称</span>
              <el-input v-model="userInfo.nickName" placeholder="请输入昵称,长度必须在0~20之间"></el-input>
            </div>
            <div class="info-item">
              <span>性别</span>
              <el-select v-model="userInfo.sex" class="selectSex" placeholder="请选择性别">
                <el-option label="男" value="0"></el-option>
                <el-option label="女" value="1"></el-option>
              </el-select>
            </div>
            <div class="info-item">
              <span>手机号</span>
              <el-input v-model="userInfo.phonenumber" placeholder="请输入手机号"></el-input>
            </div>
            <div class="info-item">
              <span>个人网站</span>
              <el-input v-model="userInfo.website" placeholder="请输入个人网站地址"></el-input>
            </div>
            <div class="info-item">
              <span>简介</span>
              <el-input v-model="userInfo.explanation" placeholder="请输入简介,长度不能大于30"></el-input>
            </div>
            <div class="info-item">
              <el-button @click="updataUserInfo">修改</el-button>
            </div>
          </div>
        </div>
			</div>
		</div>
    <el-dialog
      title="上传头像"
      :visible.sync="dialogVisible"
      :lock-scroll="false"
      append-to-body
      :show-close="false">
      <el-upload
        class="avatar-uploader"
        action=""
        :show-file-list="false"
        :before-upload="beforeAvatarUpload">
        <img v-if="imageUrl" :src="imageUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        <div slot="tip" class="el-upload__tip">只能上传jpeg/jpg/png文件，且不超过2MB</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="updataAvatar">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {mapState,mapMutations} from 'vuex'
export default {
	name: 'User',
	data() {
		return {
      dialogVisible: false,
      imageUrl: '',
      formData: {}
    };
	},
  mounted() {
    this.getUserInfo()
  },
  methods: {
    ...mapMutations(['LOGINOUT']),
    // 获取用户信息
    async getUserInfo() {
      let res = await this.$store.dispatch('getUserInfo')
      console.log('获取用户信息',res);
      if(res == 'ok') {
        console.log('获取用户信息成功');
      } else if(res == 'token is failure,please login again' || res=='402') {
        console.log('token过期 或 强制下线',res);
        this.$bus.$emit('loginOut')
      }
    },
    // 显示上传头像dialg
    showUpload() {
      this.dialogVisible = true
    },
    // 上传头像
    async beforeAvatarUpload(file) {
      // const isJPG = file.type === 'image/jpeg';
      const isJPG = true;
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        // this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      if(isJPG && isLt2M) {
        this.formData = new FormData()
        this.formData.append("img",file)
        const res = await this.$API.user.updataAvator(this.formData)
        // console.log(res);
        if(res.code==200) {
          this.imageUrl = res.data;
        }
      }
      return isJPG && isLt2M;
    },
    // 取消修改头像
    cancel() {
      this.imageUrl = ''
      this.formData = {}
      this.dialogVisible = false
    },
    // 确定修改头像
    updataAvatar() {
      if(this.imageUrl) {
        this.userInfo.avatar = this.imageUrl
        this.updataUserInfo()
        this.cancel()
      } else {
        this.$message({
          type: 'warning',
          message: '请先上传头像图片',
          showClose: true
        })
      }
    },
    // 正则表达式验证
    isRulesOk() {
      var {nickName,phonenumber,website,explanation} = this.userInfo
      if(nickName!=null) {
        nickName = nickName.trim()
        this.userInfo.nickName = nickName
        if(nickName.length<=0||nickName.length>20) {
          this.$message({
            type: 'warning',
            message: '昵称长度必须在0~20之间',
            showClose: true
          })
          return false
        }
      }
      if(phonenumber!=null&&phonenumber!='') {
        phonenumber = phonenumber.trim()
        this.userInfo.phonenumber = phonenumber
        var phoneRule = /^1[3|4|5|6|7|8][0-9]{9}$/
        if(!phoneRule.test(phonenumber)) {
          this.$message({
            type: 'warning',
            message: '请填写正确的手机号格式',
            showClose: true
          })
          return false
        }
      }
      if(website!=null&&website!='') {
        website = website.trim()
        this.userInfo.website = website
        var websiteRule = /^[a-zA-z]+:\/\/[^\s]*/
        if(!websiteRule.test(website)) {
          this.$message({
            type: 'warning',
            message: '请填写正确的网址地址格式',
            showClose: true
          })
          return false
        }
      }
      if(explanation!=null&&explanation!='') {
        explanation = explanation.trim()
        this.userInfo.explanation = explanation
        if(explanation.length>30) {
          this.$message({
            type: 'warning',
            message: '简介长度必须在0~30之间',
            showClose: true
          })
          return false
        }
      }
      return true
    },
    // 修改信息
    async updataUserInfo() {
      console.log('userInfo',this.userInfo);
      if(!this.isRulesOk()) {
        return 
      }
      const res = await this.$API.user.updataUserInfo(this.userInfo)
      console.log('修改信息',res)
      if(res.code == 200) {
        this.getUserInfo()
        this.$message({
          type: 'success',
          showClose: true,
          message: '修改信息成功'
        })
        if(this.userInfo.type=='1') {
          // 如果是博主修改信息，则重新获取博主信息
          this.$store.dispatch('getHostInfo')
        }
      } else if(res.code==401||res.code==402) {
        console.log('token过期 或 强制下线',res);
        this.$bus.$emit('loginOut')
      } else {
        this.$message({
          type: 'error',
          showClose: true,
          message: '修改信息失败'
        })
      }
    }
  },
  computed: {
    ...mapState({
    	userInfo: state => state.user.userInfo||{}
    })
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
}
.page-container {
	width: calc(100% - 24px);
	min-height: 160px;
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
  .brand-container {
    height: 50vh;
  }
}
.page-container .title {
  font-size: 22px;
  font-weight: bold;
}
.page-container .info-container {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  padding: 0 40px;
}
.avatar-box {
  width: 260px;
  padding-right: 40px;
}
.author-avatar {
	display: block;
	width: 140px;
	height: 140px;
	border-radius: 50%;
  margin: 0 auto;
}
@media screen and (max-width: 850px) {
  .avatar-box {
    padding-right: 0px;
  }
}
/deep/.el-dialog {
  width: 450px;
}
@media screen and (max-width: 1000px) {
  /deep/.el-dialog {
    width: 450px;
  }
}
@media screen and (max-width: 767px) {
	/deep/.el-dialog {
	  width: 300px;
	}
}
/deep/.avatar-uploader {
  text-align: center;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
/deep/.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
/deep/.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.introduction-body {
  width: 520px;
  font-size: 16px;
	// padding: 32px 40px;
}
.introduction-body .info-item {
  margin: 20px 0;
}
.introduction-body .info-item .selectSex  {
  width: 100%;
}
.introduction-body .info-item span {
  display: block;
  margin-bottom: 10px;
}
</style>