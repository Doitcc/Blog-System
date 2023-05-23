<template>
  <div id="app">
    <Header></Header>
    <span v-show="percent!='0%'" @click="back" class="backtop">
      <i class="iconfont icon-xiangshang"></i>
      <span>{{percent}}</span>
    </span>
		<keep-alive :exclude="['Message','ApplyFriend','Home']">
      <router-view></router-view>
		</keep-alive>
		<Footer v-show="$route.meta.show"></Footer>
  </div>
</template>

<script>
import Header from './components/Header/Header.vue'
import Footer from './components/Footer/Footer.vue'

export default {
  name: 'App',
  components: {
    Header,
		Footer
  },
  mounted() {
    window.addEventListener('scroll',this.backTop,false)
  },
  data() {
    return {
      scrollTop: 0,
      percent: '0%',
    }
  },
  watch: {
    // $route(to,from) {
    //   this.backTop()
    // }
  },
  methods: {
    backTop() {
      let scrollTop = document.documentElement.scrollTop
      let scrollHeight = document.documentElement.scrollHeight
      let height = document.documentElement.clientHeight
      let value = scrollHeight-height
      if(value>0) {
        this.percent = parseInt(Math.round((scrollTop/value)*100)) + '%'
      } else {
        this.percent = '0%'
      }
    },
    back() {
      document.documentElement.scrollTop = 0
    },
  }
}
</script>

<style>
/* #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
} */

html {
  scroll-behavior: smooth;
}
span.backtop {
  position: fixed;
  display: flex;
  flex-direction: column;
  justify-content: center;
  bottom: 20px;
  right: 20px;
  height: 50px;
  width: 50px;
  /* line-height: 50px; */
  border-radius: 30px;
  font-size: 14px;
  text-align: center;
  background-color: var(--common-color);
  box-shadow: 0 0 25px 10px #ccc;
  cursor: pointer;
  z-index: 11;
  color: var(--pink);
}
</style>
