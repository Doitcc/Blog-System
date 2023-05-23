<template>
  <div ref="aContent">
    <!-- 这也是编辑器组件，不过使用了预览模式，就可以预览markdown -->
    <v-md-editor v-model="text" mode="preview" @copy-code-success="handleCopyCodeSuccess" :include-level="[1,2, 3]" :default-show-toc="true" />
  </div>
</template>

<script>
export default {
  name: 'MarkDownEdit',
  // markdown 文本
  props: ['text'],
  data() {
    return {
    }
  },
  mounted() {
    // this.makeToc()
  },
  watch:{
    text(value) {
      if(value) {
        this.makeToc()
      }
    }
  },
  methods: {
    handleCopyCodeSuccess(code) {
      this.$message('复制成功')
    },
    // 显示目录
    makeToc(){
      console.log('显示目录')
      const leftDom = document.querySelector('.v-md-editor__left-area')
      leftDom.style.display = 'block'
      leftDom.style.width = '300px'
      leftDom.style.fontWeight = 'bold'
      leftDom.children[0].style.height = '60px'
      leftDom.children[0].style.lineHeight = '60px'
      leftDom.children[0].style.fontSize = '30px'
    },
  }
}
</script>

<style lang="less" scoped>
@media screen and (max-width: 1100px) {
  /deep/.v-md-editor__left-area {
    display: none!important;
  }
}
/deep/.v-md-editor__toc-nav-title {
  font-size: 18px;
}
/deep/.v-md-editor__toc-nav-item {
  height: 42px;
}
/deep/.v-md-editor__toc-nav-item span {
  line-height: 20px;
}
</style>