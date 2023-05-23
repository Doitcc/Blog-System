<template>
	<div class="main">
		<div class="message-container">
			<p class="message-title">留言板</p>
			<div class="message-input">
				<input v-model="message" class="input" type="text" placeholder="说点什么吧" name="" id="" />
				<button class="send" @click="addToList">发送</button>
			</div>
		</div>
		<div class="danmu-container">
			<div class="danmus" ref="danmus">
				<vue-baberrage
					:isShow= "barrageIsShow"
					:barrageList = "barrageList"
          :boxHeight="boxHeight"
					:loop = "barrageLoop"
					:throttleGap="1500"
          :lanesCount="lanesCount"
				>
				</vue-baberrage>
			</div>
		</div>
	</div>
</template>

<script>
import { MESSAGE_TYPE } from 'vue-baberrage'
export default {
	name: 'Message',
	data() {
		return {
			message: '',
			msg: 'Hello vue-baberrage',
			barrageIsShow: true,
      lanesCount: 13,
      boxHeight: 30,
			currentId : 20,
			barrageLoop: false,
      barrageList:[],
		};
	},
	mounted() {
		this.getBarrageList()
	},
	methods: {
    // 获取所有留言
    async getBarrageList() {
      var res = await this.$API.message.getBarrageList()
      if(res.code==200) {
        console.log('获取所有留言数据成功',res)
        res.data.forEach(item => {
          let barrageObj = {
            id: item.id,
            avatar: item.avatar,
            msg: item.content,
            time: 15,
            type: MESSAGE_TYPE.NORMAL
          }
          if(item.createBy == -1) {
            barrageObj.avatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          }
          var time = Math.ceil(Math.random() * 10 + 8)
          barrageObj.time = time
          this.barrageList.push(barrageObj)
        })
      }
    },
    // 添加留言
	  async addToList (){
      this.message = this.message.trim()
      if(this.message.length<=0||this.message.length>40) {
        this.$message({
          type: 'warning',
          message: '留言消息长度必须在0~40之间',
          showClose: true
        })
        return false
      }
      if(this.message) {
        var msgObj = {
          id: null,
          avatar: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
          msg: this.message,
          time: 15,
          type: MESSAGE_TYPE.NORMAL
        }
        let token = localStorage.getItem('token')
        console.log('当前token值为',token)
        let nickName = '游客：'
        if(token) {
          nickName = JSON.parse(localStorage.getItem('loginInfo')).userInfo.nickName + '：'
          msgObj.avatar = JSON.parse(localStorage.getItem('loginInfo')).avatar
        }
        console.log(nickName+msgObj.msg);
        msgObj.msg = nickName+msgObj.msg
        if(token) {
          let params = {
            content: msgObj.msg
          }
          var res = await this.$API.message.addUserMsg(params)
          console.log('用户留言',res)
        } else {
          let params = {
            content: msgObj.msg,
            avatar: msgObj.avatar
          }
          var res = await this.$API.message.addTouristMsg(params)
          console.log('游客留言',res)
        }
        if(res.code == 200) {
          this.barrageList.push(msgObj)
          this.$message({
            type: 'success',
            message: '留言成功',
            showClose: true
          })
          console.log('添加留言成功',this.barrageList);
        } else if(res.code==401||res.code==402) {
          console.log('token过期 或 强制下线',res);
          this.$bus.$emit('loginOut')
        } else {
          this.$message({
            type: 'error',
            message: '留言失败',
            showClose: true
          })
          console.log('添加留言失败',this.barrageList);
        }
        this.message = ''
      }
		}
	},
	computed: {}
};
</script>

<style lang="less" scoped>
.main {
	display: flex;
	flex-direction: column;
	width: 100%;
	padding: 0 0 100px;
}
.message-container {
	position: fixed;
	top: 30%;
	left: 0;
	right: 0;
	width: 360px;
	z-index: 5;
	text-align: center;
	margin: 0 auto;
	color: #fff;
}
.message-title {
	display: block;
	font-size: 32px;
	margin-block-start: 10px;
	margin-block-end: 10px;
	margin-inline-start: 0px;
	margin-inline-end: 0px;
	font-weight: 700;
	line-height: 1.5;
	margin: 20px 0 15px;
	// color: red;
}
.message-input {
	display: flex;
	justify-content: center;
	height: 40px;
	margin-top: 32px;
}
.message-input .input {
	width: 70%;
	height: 100%;
	border-radius: 20px;
	padding: 0 20px;
	outline: none;
	color: #fff;
	border: #fff 1px solid;
  background-color: rgba(255, 255, 255, .4);
  font-size: 16px;
}
.message-input .send {
	height: 100%;
	padding: 0 20px;
	color: #eee;
	border: #fff 1px solid;
	border-radius: 20px;
	outline: none;
	background-color: rgba(255, 255, 255, .4);
  margin-left: 5px;
  font-size: 16px;
}
.danmu-container {
	position: fixed;
	left: 0;
	top: 0;
	right: 0;
	bottom: 0;
	width: 100%;
	background: url(@/assets/images/bg_xue.jpg) no-repeat 50% 50%/cover;
	z-index: -10;
}
.danmus {
	position: fixed;
	top: 50px;
	width: 100%;
	height: 100%;
	color: #fff;
}
/*弹幕区域高度*/
/deep/ .baberrage-stage {
  height: 100%;
}

/*弹幕消息框*/
/deep/ .baberrage-item .normal {
  padding: 5px 15px;
  height: 40px;
  color: #000;
  background-color: rgba(255, 255, 255, .7);
}

/*头像*/
/deep/ .baberrage-item .normal .baberrage-avatar {
  width: 30px;
  height: 30px;
}
/deep/.baberrage-item .normal .baberrage-avatar img {
  width: 100%;
  height: 100%;
}

/*文字*/
/deep/ .baberrage-item .normal .baberrage-msg {
  font-size: 16px;
  // line-height: 20px;
}

@media screen and (max-width: 767px) {
	/*弹幕区域高度*/
	/deep/ .baberrage-stage {
	  height: 100%;
	}
	
	/*弹幕消息框*/
	/deep/ .baberrage-item .normal {
	  padding: 2px 10px;
	  height: 30px;
	  color: #000;
	  background-color: rgba(255, 255, 255, .7);
    display: flex;
    align-items: center;
	}
	
	/*头像*/
	/deep/ .baberrage-item .normal .baberrage-avatar {
	  width: 20px;
	  height: 20px;
	}
	
	/*文字*/
	/deep/ .baberrage-item .normal .baberrage-msg {
	  font-size: 14px;
	}
}
</style>
