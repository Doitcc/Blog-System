<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">管理标签</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container">
				<el-tag
				  :key="tag.id"
				  v-for="tag in tagList"
				  closable
				  :disable-transitions="false"
				  @close="handleClose(tag)">
				  {{tag.name}}
				</el-tag>
				<el-input
				  class="input-new-tag"
				  v-if="inputVisible"
				  v-model="inputValue"
				  ref="saveTagInput"
				  size="small"
				  @keyup.enter.native="$event.target.blur()"
				  @blur="handleInputConfirm"
				>
				</el-input>
				<el-button v-else class="button-new-tag" size="small" @click="showInput">+ 新增标签</el-button>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	name: 'ManageTag',
	data() {
		return {
      tagList: [],
      inputVisible: false,
      inputValue: ''
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
    // 删除标签
    async handleClose(tag) {
      const res = await this.$API.manage.deleteTagById(tag.id)
      console.log('删除标签操作',res)
      if(res.code==200) {
        this.$message({
          message: '删除标签成功',
          type: 'success',
          showClose: true
        })
        this.tagList.splice(this.tagList.indexOf(tag), 1);
        this.$store.dispatch('getHostInfo')
        // this.getTagList()
      } else if(res.code==401) {
        this.$bus.$emit('loginOut')
        this.$router.push('/home')
      } else if(res.code==403) {
        this.$message({
          message: '您无权限进行该操作',
          type: 'error',
          showClose: true
        })
      } else if(res.code==401||res.code==402) {
        console.log('token过期 或 强制下线',res);
        this.$bus.$emit('loginOut')
      } else {
        this.$message({
          message: '删除标签失败',
          type: 'error',
          showClose: true
        })
      }
    },
    // 展示新增标签输入框
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    // 确定新增标签
    async handleInputConfirm() {
      let inputValue = this.inputValue;
      let flag = this.tagList.every(item =>{
        if(item.name != this.inputValue) {
          return true
        }
      })
      console.log(flag);
      if (inputValue&&flag) {
        const params = {name: inputValue}
        // console.log(params);
        const res = await this.$API.manage.addTag(params)
        if(res.code==200) {
          this.$message({
            message: '新增标签成功',
            type: 'success',
            showClose: true
          })
          this.getTagList()
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
            message: '新增标签失败',
            type: 'error',
            showClose: true
          })
        }
      } else if(inputValue&&!flag) {
        this.$message({
          type: 'warning',
          message: '该标签已存在',
          showClose: true
        })
      }
      this.inputVisible = false;
      this.inputValue = '';
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
}
.page-container {
	width: calc(100% - 24px);
	min-height: 360px;
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
.el-tag {
  margin-right: 15px;
  margin-bottom: 15px;
}
.button-new-tag {
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
}
</style>
