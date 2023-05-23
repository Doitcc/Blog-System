<template>
  <div>
    <!-- markdown编辑器组件 -->
    <v-md-editor v-model="text" height="1000px" :left-toolbar="leftToobar" :disabled-menus="[]" @upload-image="handleUploadImage" @copy-code-success="handleCopyCodeSuccess" :default-show-toc="true" />
    
    <!-- 这也是编辑器组件，不过使用了预览模式，就可以预览markdown -->
    <!-- <v-md-editor v-model="text" height="400px" mode="preview" /> -->

    <!-- 预览html的组件 -->
    <!-- <v-md-preview-html :html="html" preview-class="vuepress-markdown-body"></v-md-preview-html> -->
  </div>
</template>

<script>
// xss是将markdown转为html的库
import VMdEditor, { xss } from '@kangc/v-md-editor'

export default {
  name: 'MarkDownEdit',
  components: {},
  props: ['clearText'],
  data() {
    return {
      // 左侧工具栏的配置，配什么，显示什么
      leftToobar: 'undo redo clear | h bold italic strikethrough quote tip | ul ol table hr | link image emoji code | save',
      text: '', // markdown 文本
      html: '' ,// 转换后，用于显示的html,
      formData: {},
    }
  },
  mounted() {
    this.$bus.$on('initValue',(value)=>{
      console.log('初始化富文本编辑器内容')
      this.text = value
    })
  },
  watch: {
    text(value) {
      // 将markdown转为html
      this.html = xss.process(VMdEditor.themeConfig.markdownParser.render(value))
      this.$emit('gettext',value)
    },
    // 清空富文本编辑器内容
    clearText(value) {
      console.log('value值变化了',value);
      if(value) {
        console.log('清空富文本编辑器内容',value);
        this.text = ''
      }
    }
  },
  methods: {
    handleCopyCodeSuccess(code) {
      this.$message('复制成功')
      // console.log('复制成功')
    },
    async handleUploadImage(event, insertImage, files) {
      // 1、拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
      var file = files[0]
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
        const res = await this.$API.article.updataArticleImg(this.formData)
        console.log(res);
        if(res.code==200) {
          insertImage({
            url: res.data,
            width: '1000',
            height: 'auto',
          })
        }
      }
    }
  }
}
</script>