<template>
	<div class="main">
		<div class="imgs"></div>
		<div class="brand-container">
			<div class="brand">
				<h1 class="art">写文章</h1>
			</div>
		</div>
		<div class="main-container mt">
			<div class="page-container">
        <div class="article-info">
          <el-form ref="articleForm" class="info-form" :model="articleForm" label-width="80px">
            <div class="form-part">
              <el-form-item label="文章标题">
                <el-input v-model="articleForm.title" placeholder="请输入文章标题"></el-input>
              </el-form-item>
              <el-form-item label="文章摘要">
                <el-input v-model="articleForm.summary" placeholder="请输入文章摘要"></el-input>
              </el-form-item>
              <el-form-item label="缩略图">
                <el-upload
                class="avatar-uploader"
                action=""
                :show-file-list="false"
                :before-upload="beforeAvatarUpload">
                <img v-if="articleForm.thumbnail" :src="articleForm.thumbnail" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                <div slot="tip" class="el-upload__tip">只能上传jpeg/jpg/png文件，且不超过2MB</div>
                </el-upload>
              </el-form-item>
            </div>
            <div class="form-part">
              <el-form-item label="文章分类">
                <el-select v-model="articleForm.categoryId" placeholder="请选择文章分类" style="width: 100%;">
                  <el-option
                    v-for="item in categoryList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="文章标签" class="info-item">
                <el-select v-model="articleForm.tags" multiple placeholder="请选择标签" style="width: 100%;">
                  <el-option
                    v-for="item in options"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="允许评论">
                <el-radio-group v-model="articleForm.isComment">
                  <el-radio label="1">是</el-radio>
                  <el-radio label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="是否置顶">
                <el-radio-group v-model="articleForm.isTop">
                  <el-radio label="1">是</el-radio>
                  <el-radio label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-button style="margin: 0 10px;" @click="confirmArticle('1')">保存草稿</el-button>
              <el-button type="primary" @click="confirmArticle('0')">发布文章</el-button>
            </div>
          </el-form>
        </div>
        <Markdown @gettext="gettext" :clearText="clearText"></Markdown>
			</div>
		</div>
	</div>
</template>

<script>
import Markdown from '@/components/Markdown/Markdown.vue'
import {categoryList} from '@/utils/constData.js'
export default {
  name: '',
  components: {
    Markdown
  },
  data () {
    return {
      text: '',
      clearText: false,
      articleForm: {
        title: '',
        summary: '',
        thumbnail: '',
        isComment: '1',
        isTop: '0',
        categoryId: null,
        tags: [],
        content: '',
        viewCount: '0',
        status: '1'
      },
      formData: {},
      categoryList: categoryList,
      options: [],
    }
  },
  mounted() {
    // this.getTagList()
  },
  watch: {
    $route: {
      immediate: true,
      handler(to,from) {
        console.log('写文章跳转路由',to,from);
        // 获取所有文章标签列表数据
        this.getTagList()
        if(to.name=='editarticle'&&to.query.id) {
          console.log('进入editarticle页，clearText为',this.clearText)
          console.log('编辑文章的id',to.query.id);
          this.getArticleById(to.query.id)
          this.clearText = false
        } else if(to.name=='editarticle'){
          this.clearText = false
          console.log('进入editarticle页，clearText为',this.clearText)
        } else {
          this.initData()
          console.log('离开editarticle页，clearText为',this.clearText)
        }
      }
    }
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
        this.options = res.data.rows
      }
    },
    // 初始化数据
    initData() {
      this.articleForm= {
        title: '',
        summary: '',
        thumbnail: '',
        isComment: '1',
        isTop: '0',
        categoryId: '',
        tags: [],
        content: '',
        viewCount: '0',
        status: '1'
      },
      this.formData = {}
      this.text = ''
      this.clearText = true
      console.log('初始化写文章数据--clearText为',this.clearText)
    },
    // 获取富文本编辑器的内容
    gettext(text) {
      this.text = text
      this.articleForm.content = text
    },
    // 上传图片
    async beforeAvatarUpload(file) {
      console.log(file);
      // const isJPG = file.type === 'image/jpeg';
      const isJPG = true;
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        // this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message({
          message: '上传头像图片大小不能超过 2MB!',
          type: 'error',
          showClose: true
        });
      }
      if(isJPG && isLt2M) {
        this.formData = new FormData()
        this.formData.append("img",file)
        const res = await this.$API.article.updataArticleImg(this.formData)
        console.log(res);
        if(res.code==200) {
          this.articleForm.thumbnail = res.data;
        }
      }
      return isJPG && isLt2M;
    },
    // 表单验证
    isAllWrite() {
      var flag = true
      const {title,summary,thumbnail,categoryId,tags,content} = this.articleForm
      if(title==''||summary==''||thumbnail==''||content==''||categoryId==''||tags.length==0) {
        flag = false
      } else {
        flag = true
      }
      // console.log(flag);
      return flag
    },
    // 发布文章或保存草稿
    async confirmArticle(status) {
      if(this.isAllWrite()) {
        this.articleForm.status = status
        const flag = await this.saveOrWriteArticle()
        console.log(flag);
        if(flag=='ok') {
          this.initData()
          this.$router.push({name:'managearticle',params:{status}})
        }
      } else {
        this.$message.warning('请输入完整文章内容')
      }
    },
    // 保存草稿或发布文章发请求
    async saveOrWriteArticle() {
      const {id} = this.$route.query
      if(id) {
        this.articleForm.beforeStatus = this.$route.query.beforeStatus
        console.log('editArticle',id,this.articleForm);
        var res = await this.$API.article.editArticle(this.articleForm)
      } else {
        console.log('saveOrWriteArticle');
        var res = await this.$API.article.saveOrWriteArticle(this.articleForm)
      }
      console.log('重新获取博主信息getHostInfo');
      this.$store.dispatch('getHostInfo')
      console.log('保存草稿或发布文章发请求',res);
      if(res.code==200) {
        this.$message({
          message: res.msg,
          type: 'success',
          showClose: true
        })
        return 'ok'
      } else if(res.code==401||res.code==402) {
        console.log('token过期 或 强制下线',res);
        this.$bus.$emit('loginOut')
        return 'false'
      } else if(res.code==403) {
        this.$message({
          message: '您无权限进行该操作',
          type: 'error',
          showClose: true
        })
        return 'false'
      } else {
        return 'false'
      }
    },
    // 查询文章通过id
    async getArticleById(id) {
      const res = await this.$API.home.getArticleById(id)
      console.log('查询文章通过id',res);
      if(res.code==200) {
        res.data.tags = []
        console.log('文章信息',res.data);
        this.options.forEach(item => {
          if(res.data.tagName.includes(item.name)) {
            res.data.tags.push(item.id)
          }
        })
        console.log('标签信息',this.options);
        console.log('文章tags',res.data.tags)
        if(res.data.tags.length!==0) {
          this.articleForm = res.data
          delete this.articleForm.tagName
          this.$bus.$emit('initValue',this.articleForm.content)
        }
      }
    },
  },
}
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
		width: 1200px;
	}
}
@media screen and (max-width: 767px) {
	.page-container {
		width: calc(100% - 10px);
		padding: 16px 14px;
	}
}
.page-container .article-info {
  padding-bottom: 20px;
  padding-top: 30px;
}
.page-container .article-info .info-form {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}
.page-container .article-info .info-form .form-part {
  width: 520px;
  font-weight: bold;
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
</style>