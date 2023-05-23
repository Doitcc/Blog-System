<template>
  <div class="empty">
    <i class="iconfont icon-meiyoushuju"></i>
    <span>空空如也</span>
  </div>
</template>

<script>
export default {
  name: 'Empty',
}
</script>

<style lang="less" scoped>
.empty {
  height: 300px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.empty .iconfont {
  font-size: 100px;
}
</style>