import {mapState,mapGetters} from 'vuex'
const getHostIndoMixin = {
  data(){
    return {
      
    }
  },
  methods: {
    // 获取博主信息
    async getHostInfo() {
      const msg = await this.$store.dispatch('getHostInfo')
      // 发请求获取信息后，读取本地存储的信息
      console.log('第一次获取博主信息结果',msg)
      if(msg=='ok') {
        // this.hostInfo = JSON.parse(localStorage.getItem('hostInfo'))
        console.log('第一次存储博主信息',this.hostInfo)
      } else {
        console.log('第一次获取博主信息失败',msg)
      }
    },
  },
  mounted() {
    var hostInfo = JSON.parse(localStorage.getItem('hostInfo'))
    // 查询博主信息
    if(hostInfo) {
      // 如果不为空，即已经存储过了博主信息，不做处理
      console.log('已经存储过博主信息',hostInfo)
      // this.hostInfo = hostInfo
    } else {
      // 如果为 null，即第一次请求该数据，派发action
      this.getHostInfo()
    }
  },
  computed: {
    ...mapGetters({
      hostInfo: 'hostInfoNow'
    })
  }
}

export default getHostIndoMixin