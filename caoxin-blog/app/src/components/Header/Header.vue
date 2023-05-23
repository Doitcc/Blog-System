<template>
	<div class="header" :class="classObj">
		<div class="toggle" @click="showNavDrawer">
			<div class="lines">
				<span class="line"></span>
				<span class="line"></span>
				<span class="line"></span>
			</div>
		</div>
		<div class="menu">
			<div class="menu-item title">
				<router-link to="/">Hello World</router-link>
			</div>
			<div class="menu-item" :class="{active:$route.path==='/home'}">
				<router-link to="/home" class="menu-btn">
					<i class="iconfont icon-shouye-shouye"></i>
					首页
				</router-link>
			</div>
			<div class="menu-item dropdown">
				<div class="menu-btn drop">
					<i class="iconfont icon-yingyongzhongxin"></i>
					文章
				</div>
        <ul class="submenu">
        	<li class="subitem" :class="{active:$route.path===`/categoryitem/${item.id}`}" v-for="item in headCategoryList" :key="item.id">
        		<router-link :to="`/categoryitem/${item.id}`" class="link">
        			<i class="iconfont" :class="iconObj[item.id]"></i>
        			{{item.name}}
        		</router-link>
        	</li>
        </ul>
			</div>
			<div class="menu-item dropdown">
				<div class="menu-btn drop">
					<i class="iconfont icon-zhexiantu"></i>
					统计
				</div>
				<ul class="submenu">
					<li class="subitem" :class="{active:$route.path==='/archive'}">
						<router-link to="/archive" class="link">
							<i class="iconfont icon-guidang"></i>
							归档
						</router-link>
					</li>
					<li class="subitem" :class="{active:$route.path==='/category'}">
						<router-link to="/category" class="link">
							<i class="iconfont icon-fenlei1"></i>
							分类
						</router-link>
					</li>
					<li class="subitem" :class="{active:$route.path==='/tag'}">
						<router-link to="/tag" class="link">
							<i class="iconfont icon-24gl-tags"></i>
							标签
						</router-link>
					</li>
				</ul>
			</div>
			<div class="menu-item" :class="{active:$route.path==='/friend'}">
				<router-link to="/friend" class="menu-btn">
					<i class="iconfont icon-lianjie1"></i>
					友链
				</router-link>
			</div>
			<div class="menu-item" :class="{active:$route.path==='/message'}">
				<router-link to="/message" class="menu-btn">
					<i class="iconfont icon-liuyan"></i>
					留言板
				</router-link>
			</div>
			<div class="menu-item" :class="{active:$route.path==='/about'}">
				<router-link to="/about" class="menu-btn">
					<i class="iconfont icon-guanyu1"></i>
					关于
				</router-link>
			</div>
			<div class="menu-item dropdown" v-if="isLogin">
				<img :src="userAvatar" alt="">
				<ul class="submenu">
					<li class="subitem" :class="{active:$route.path==='/user'}">
						<router-link to="/user" class="link">
							<i class="iconfont icon-gerenzhongxin"></i>
							个人中心
						</router-link>
					</li>
          <li class="subitem" :class="{active:$route.path==='/applyfriend'}" v-show="type=='0'">
          	<router-link to="/applyfriend" class="link">
          		<i class="iconfont icon-shenqingquanxian"></i>
          		申请友链
          	</router-link>
          </li>
          <li class="subitem" :class="{active:$route.path==='/editarticle'}" v-show="type=='1'">
          	<router-link to="/editarticle" class="link">
          		<i class="iconfont icon-write"></i>
          		写文章
          	</router-link>
          </li>
          <li class="subitem" :class="{active:$route.path==='/managearticle'}" v-show="type=='1'">
          	<router-link to="/managearticle" class="link">
          		<i class="iconfont icon-yingyongguanli"></i>
          		管理文章
          	</router-link>
          </li>
          <li class="subitem" :class="{active:$route.path==='/managetag'}" v-show="type=='1'">
          	<router-link to="/managetag" class="link">
          		<i class="iconfont icon-biaoqian"></i>
          		管理标签
          	</router-link>
          </li>
          <li class="subitem" :class="{active:$route.path==='/managefriend'}" v-show="type=='1'">
          	<router-link to="/managefriend" class="link">
          		<i class="iconfont icon-shenqingquanxian"></i>
          		管理友链
          	</router-link>
          </li>
					<li class="subitem" @click="loginOut">
						<router-link to="/home" class="link">
							<i class="iconfont icon-tuichu1"></i>
							退出
						</router-link>
					</li>
				</ul>
			</div>
			<div class="menu-item" v-else>
				<div class="menu-btn" @click="showLogin">
					<i class="iconfont icon-denglu"></i>
					登录
				</div>
			</div>
		</div>
		<div class="right">
			<div class="right-item" @click="setDocumentTheme">
        <i v-if="theme=='white'" class="iconfont icon-taiyangtianqi big"></i>
        <i v-else class="iconfont icon-moonyueliang big"></i>
      </div>
			<div class="right-item" @click="showSearch"><i class="iconfont icon-sousuo1 big"></i></div>
		</div>
    
		<!-- 登录对话框 -->
		<el-dialog :visible.sync="dialogFormVisible" center :lock-scroll="false" append-to-body width="450px" class="login-dialog">
		  <el-form style="width: 100%; margin:20px auto 0;" :model="userForm">
		    <el-form-item>
		      <el-input v-model="userForm.userName" placeholder="请输入QQ邮箱号"></el-input>
		    </el-form-item>
				<el-form-item>
				  <el-input type="password" v-model="userForm.password" placeholder="请输入密码" show-password></el-input>
				</el-form-item>
		  </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="login" style="width: 100%;">登录</el-button>
				<div class="footer-body">
					<div class="footer-item1"><span @click="goRegist">立即注册</span><span @click="goForget">忘记密码?</span></div>
					<div class="footer-item2">社交账号登录</div>
					<div class="footer-item3">
            <i class="iconfont icon-qq qq"></i>
            <i class="iconfont icon-github"></i>
          </div>
				</div>
			</div>
		</el-dialog>
		
		<!-- 注册对话框 -->
		<el-dialog :visible.sync="dialogRegistVisible" center :lock-scroll="false" append-to-body width="450px" class="regist-dialog">
      <!-- 忘记密码 -->
      <el-form style="width: 100%; margin:20px auto 0;" :model="forgetForm" v-if="isForgetPwd">
        <el-form-item>
          <el-input v-model="forgetForm.userName" placeholder="请输入QQ邮箱号"></el-input>
        </el-form-item>
      	<el-form-item>
      	  <el-input v-model="forgetForm.code" placeholder="请输入验证码">
            <template slot="append">
              <el-button @click="getCode('forget')" class="getcode" :class="{disabled: isDisabled}">{{text}}</el-button>
            </template>
          </el-input>
      	</el-form-item>
      	<el-form-item>
      	  <el-input type="password" v-model="forgetForm.password" placeholder="请输入密码" show-password></el-input>
      	</el-form-item>
      </el-form>
      <!-- 注册 -->
		  <el-form style="width: 100%; margin:20px auto 0;" :model="registForm" v-else>
		    <el-form-item>
		      <el-input v-model="registForm.userName" placeholder="请输入QQ邮箱号"></el-input>
		    </el-form-item>
				<el-form-item>
				  <el-input v-model="registForm.code" placeholder="请输入验证码">
            <template slot="append">
              <el-button @click="getCode('regist')" class="getcode" :class="{disabled: isDisabled}">{{text}}</el-button>
            </template>
          </el-input>
				</el-form-item>
				<el-form-item>
				  <el-input type="password" v-model="registForm.password" placeholder="请输入密码" show-password></el-input>
				</el-form-item>
		  </el-form>
			<div slot="footer" class="dialog-footer">
				<el-button v-if="isForgetPwd" @click="forget" style="width: 100%;">
          确定
        </el-button>
        <el-button v-else @click="regist" style="width: 100%;">
          注册
        </el-button>
				<div class="footer-body">
					<div style="margin: 10px 0 20px;">已有账号？<span @click="showLogin" class="gologin">登录</span></div>
				</div>
			</div>
		</el-dialog>
		
		<!-- 搜索对话框 -->
		<el-dialog title="本地搜索" :visible.sync="dialogSearchVisible" :lock-scroll="false" append-to-body width="600px">
			<el-input v-model="searchInfo" placeholder="请输入文章标题或内容...">
				<i slot="prefix" class="el-input__icon el-icon-search"></i>
			</el-input>
			<div slot="footer" class="dialog-footer searchContent">
				<div>功能暂未完善，敬请期待...</div>
			</div>
		</el-dialog>
		
		<!-- 导航栏抽屉 -->
		<el-drawer :visible.sync="navDrawerShow" :lock-scroll="false" append-to-body size="240px" :show-close="false">
		  <div class="n-drawer-body">
        <div class="host-avatar" @click="$router.push('/about')">
          <img :src="hostInfo.avatar" alt="">
        </div>
        <div class="host-nickName">{{hostInfo.nickName}}</div>
        <div class="host-explanation">{{hostInfo.explanation}}</div>
        <div class="host-info">
          <div class="host-info-item" @click="goArchive">
            <span class="info-item-num1">{{hostInfo.countArticles}}</span>
            <span class="info-item-content">文章</span>
          </div>
          <div class="host-info-item" @click="goCategory">
            <span class="info-item-num2">{{hostInfo.countCategorys}}</span>
            <span class="info-item-content">分类</span>
          </div>
          <div class="host-info-item" @click="goTag">
            <span class="info-item-num3">{{hostInfo.countTags}}</span>
            <span class="info-item-content">标签</span>
          </div>
        </div>
				<div class="nav-menu">
					<ul>
						<li :class="{active:$route.path==='/home'}">
							<router-link to="/home">
								<i class="iconfont icon-shouye-shouye"></i>
								首页
							</router-link>
						</li>
						<li class="nav-dropdown">
							<router-link to="">
								<i class="iconfont icon-yingyongzhongxin"></i>
								文章
							</router-link>
							<ul :class="{disblock:secondDisBlock}">
								<li class="subitem" :class="{active:$route.path===`/categoryitem/${item.id}`}" v-for="item in headCategoryList" :key="item.id">
									<router-link :to="`/categoryitem/${item.id}`" class="link">
										<i class="iconfont" :class="iconObj[item.id]"></i>
										{{item.name}}
									</router-link>
								</li>
							</ul>
						</li>
						<li class="nav-dropdown">
							<router-link to="">
								<i class="iconfont icon-zhexiantu"></i>
								统计
							</router-link>
              <ul :class="{disblock:firstDisBlock}">
              	<li :class="{active:$route.path==='/archive'}">
              		<router-link to="/archive">
              			<i class="iconfont icon-guidang"></i>
              			归档
              		</router-link>
              	</li>
              	<li :class="{active:$route.path==='/category'}">
              		<router-link to="/category">
              			<i class="iconfont icon-fenlei1"></i>
              			分类
              		</router-link>
              	</li>
              	<li :class="{active:$route.path==='/tag'}">
              		<router-link to="/tag">
              			<i class="iconfont icon-24gl-tags"></i>
              			标签
              		</router-link>
              	</li>
              </ul>
						</li>
						<li :class="{active:$route.path==='/friend'}">
							<router-link to="/friend">
								<i class="iconfont icon-lianjie1"></i>
								友链
							</router-link>
						</li>
						<li :class="{active:$route.path==='/message'}">
							<router-link to="/message">
								<i class="iconfont icon-liuyan"></i>
								留言板
							</router-link>
						</li>
						<li :class="{active:$route.path==='/about'}">
							<router-link to="/about">
								<i class="iconfont icon-guanyu1"></i>
								关于
							</router-link>
						</li>
						<li @click="showLogin" v-show="!isLogin">
							<router-link to="">
								<i class="iconfont icon-denglu"></i>
								登录
							</router-link>
						</li>
            <li class="subitem" :class="{active:$route.path==='/user'}" v-show="isLogin">
            	<router-link to="/user" class="link">
            		<i class="iconfont icon-denglu"></i>
            		个人中心
            	</router-link>
            </li>
            <li class="subitem" :class="{active:$route.path==='/applyfriend'}" v-show="isLogin&&type=='0'">
            	<router-link to="/applyfriend" class="link">
            		<i class="iconfont icon-shenqingquanxian"></i>
            		申请友链
            	</router-link>
            </li>
            <li class="subitem" :class="{active:$route.path==='/editarticle'}" v-show="isLogin&&type=='1'">
            	<router-link to="/editarticle" class="link">
            		<i class="iconfont icon-write"></i>
            		写文章
            	</router-link>
            </li>
            <li class="subitem" :class="{active:$route.path==='/managearticle'}" v-show="isLogin&&type=='1'">
            	<router-link to="/managearticle" class="link">
            		<i class="iconfont icon-yingyongguanli"></i>
            		管理文章
            	</router-link>
            </li>
            <li class="subitem" :class="{active:$route.path==='/managetag'}" v-show="isLogin&&type=='1'">
            	<router-link to="/managetag" class="link">
            		<i class="iconfont icon-biaoqian"></i>
            		管理标签
            	</router-link>
            </li>
            <li class="subitem" :class="{active:$route.path==='/managefriend'}" v-show="isLogin&&type=='1'">
            	<router-link to="/managefriend" class="link">
            		<i class="iconfont icon-shenqingquanxian"></i>
            		管理友链
            	</router-link>
            </li>
            <li class="subitem" @click="loginOut"v-show="isLogin">
            	<router-link to="/home" class="link">
            		<i class="iconfont icon-tuichu1"></i>
            		退出
            	</router-link>
            </li>
					</ul>
				</div>
			</div>
		</el-drawer>
</div>
</template>

<script>
import {mapState,mapGetters} from 'vuex'
import getHostIndoMixin from '@/mixins/getHostIndoMixin'
import {cateName,iconObj} from '@/utils/constData.js'
export default {
	name: 'Header',
  mixins: [getHostIndoMixin],
	data() {
		return {
			upClass: false,
			scrollTop: 0,
			downClass: false,
			dialogFormVisible: false,
			userForm: {
				userName: '',
				password: ''
			},
			dialogRegistVisible: false,
			registForm: {
				userName: '',
        code: '' ,
				password: ''
			},
      text: '发送',
      timer: null,
      isDisabled: false,
      isForgetPwd: true,
      forgetForm: {
      	userName: '',
        code: '' ,
      	password: ''
      },
			dialogSearchVisible: false,
			searchInfo: '',
			navDrawerShow: false,
			firstDisBlock: false,
			secondDisBlock: false,
			isLogin: false,
      type: '0',
      headCategoryList: [],
      iconObj: iconObj,
      theme: localStorage.getItem('theme')||'white'
		};
	},
	mounted() {
    console.log('theme主题',this.theme);
    this.setDocumentTheme()
		window.addEventListener('scroll',this.scrollEventFn,false)
    var loginInfo = JSON.parse(localStorage.getItem('loginInfo'))
    // const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if(loginInfo&&loginInfo.isLogin) {
      // 如果为真，即已经登录，并存储过了登录信息，直接赋值
      console.log('登录信息',loginInfo);
      this.type = loginInfo.type
      this.isLogin = loginInfo.isLogin
    } else {
      console.log('未登录',loginInfo);
    }
    this.getHeadCategoryList()
    this.$bus.$on('loginOut',(value)=>{
      this.loginOut()
    })
	},
	methods: {
    // 切换主题
    setDocumentTheme() {
      const docElm = document.documentElement
      console.log('theme',this.theme);
      if(!docElm.hasAttribute('theme')) {
        docElm.setAttribute('theme',this.theme)
      } else {
        console.log(docElm.getAttribute('theme'));
        if(docElm.getAttribute('theme')=='white') {
          this.theme = 'dark'
          docElm.setAttribute('theme',this.theme)
        } else {
          this.theme = 'white'
          docElm.setAttribute('theme',this.theme)
        }
      }
      localStorage.setItem('theme',this.theme)
    },
    // 查询菜单栏文章分类展示
    async getHeadCategoryList() {
      const res = await this.$API.home.getHeadCategoryList()
      console.log('查询菜单栏文章分类展示',res);
      if(res.code==200) {
        this.headCategoryList = res.data
        // this.hotArticleList = [...this.hotArticleList,...res.data.rows]
      }
    },
    // 监听滚动条
		scrollEventFn(e) {
			let scrollTopNow = document.body.scrollTop||document.documentElement.scrollTop
			let res = scrollTopNow - this.scrollTop
			if(scrollTopNow == 0) {
				this.upClass = false
			} else {
				this.upClass = true
			}
			if(res>0) {
				// 向下
				this.downClass = true
			} else {
				// 向上
				this.downClass = false
			}
			this.scrollTop = scrollTopNow
		},
    // 显示导航栏抽屉
		showNavDrawer() {
			this.navDrawerShow = true
		},
    // 去归档页
    goArchive() {
      this.$router.push('/archive')
    },
    // 去分类页
    goCategory() {
      this.$router.push('/category')
    },
    // 去标签页
    goTag() {
      this.$router.push('/tag')
    },
		// 显示登录dialog
		showLogin() {
			// Object.assign(this._data,this.$options.data())
			this.dialogRegistVisible= false
			this.userForm={
				userName: '',
				password: ''
			}
			this.dialogFormVisible = true
		},
    // 显示忘记密码dialog
    goForget() {
      this.forgetForm= {
      	userName: '',
        code: '',
      	password: ''
      }
      this.isForgetPwd = true
      this.dialogRegistVisible = true
      setTimeout(()=>{
      	this.dialogFormVisible= false
      },100)
    },
		// 显示注册dialog
		goRegist() {
			this.registForm= {
				userName: '',
        code: '',
				password: ''
			}
      this.isForgetPwd = false
			this.dialogRegistVisible = true
			setTimeout(()=>{
				// Object.assign(this._data,this.$options.data())
				// this.dialogRegistVisible = true
				this.dialogFormVisible= false
			},100)
		},
		// 显示搜索dialog
		showSearch() {
			this.searchInfo = ''
			this.dialogSearchVisible = true
		},
		// 登录
		async login() {
      const {userName,password} = this.userForm
      if(userName!==''&&password!=='') {
        let res = await this.$store.dispatch('login',this.userForm)
        console.log('登录',res)
        if(res == 'ok') {
          this.$message({
            message: '登录成功',
            type: 'success',
            showClose: true
          })
          var loginInfo = JSON.parse(localStorage.getItem('loginInfo'))
          this.type = loginInfo.type
          this.dialogFormVisible = false
          this.isLogin = true
        } else {
          this.$message({
            message: '账号名或密码错误，登录失败',
            type: 'error',
            showClose: true
          })
        }
      } else {
        this.$message({
          message: '账号名和密码不能为空',
          type: 'warning',
          showClose: true
        })
      }
		},
		// 获取验证码
    async getCode(type) {
      console.log('获取验证码');
      if(this.isDisabled) {
        return 
      }
      var form = this.registForm
      if(type == 'forget') {
        form = this.forgetForm
      }
      console.log('type为',type)
      var {userName} = form
      form.userName = userName
      userName = userName.trim()
      var userNameRule = /[1-9][0-9]{4,}@qq.com/
      if(userName!==''&&userNameRule.test(userName)) {
        const params = {
          emailReceiver: userName
        }
        console.log('邮箱为',params);
        const res = await this.$API.user.getCode(params)
        if(res.code==200) {
          this.$message({
            type: 'success',
            message: '发送验证码成功',
            showClose: true
          })
          this.isDisabled = true
          this.text = 59
          this.timer = setInterval(()=> {
            this.text--
            if(this.text<=0) {
              this.text = '发送'
              clearInterval(this.timer)
              this.timer = null
              this.isDisabled = false
            }
          },1000)
        }
      } else {
        this.$message({
          type:'warning',
          message: 'QQ邮箱格式不正确',
          showClose: true
        })
      }
    },
    // 正则表达式验证
    isRulesOK(form) {
      var {userName,password,code} = form
      userName = userName.trim()
      password = password.trim()
      code = code.trim()
      form.userName = userName
      form.password = password
      form.code = code
      if(userName==''||password==''||code=='') {
        this.$message({
          message: '注册信息不能为空',
          type: 'warning',
          showClose: true
        })
        return false
      }
      var userNameRule = /[1-9][0-9]{4,}@qq.com/
      if(!userNameRule.test(userName)) {
        this.$message({
          type: 'warning',
          message: '请填写正确的QQ邮箱格式',
          showClose: true
        })
        return false
      }
      var codeRule = /[0-9a-zA-Z]{6}/
      if(!codeRule.test(code)) {
        this.$message({
          type: 'warning',
          message: '请填写正确的六位验证码格式',
          showClose: true
        })
        return false
      }
      if(password.length<6||password.length>20) {
        this.$message({
          type: 'warning',
          message: '密码长度必须在6~20之间',
          showClose: true
        })
        return false
      }
      return true
    },
    // 注册
    async regist() {
      if(this.isRulesOK(this.registForm)) {
        const res = await this.$store.dispatch('regist',this.registForm)
        console.log('注册',res);
        if(res == 'ok') {
          this.$message({
            message: '注册成功',
            type: 'success',
            showClose: true
          })
          this.showLogin()
        } else {
          this.$message({
            message: '注册失败',
            type: 'error',
            showClose: true
          })
        }
      }
    },
    // 找回密码
    async forget() {
      if(this.isRulesOK(this.forgetForm)) {
        const res = await this.$API.user.forget(this.forgetForm)
        console.log('找回密码',res);
        if(res.code == 200) {
          this.$message({
            message: '设置密码成功',
            type: 'success',
            showClose: true
          })
          this.showLogin()
        } else {
          this.$message({
            message: res.msg,
            type: 'error',
            showClose: true
          })
        }
      }
    },
    // 退出登录
    async loginOut() {
      let res = await this.$store.dispatch('loginOut')
      console.log('退出登录',res)
      if(res == 'ok') {
        this.$message({
          message: '退出登录成功',
          type: 'success',
          showClose: true
        })
        this.type = ''
        this.isLogin = false
      } else if(res == 'loginOut failed') {
        this.$message({
          message: '退出登录失败',
          type: 'error',
          showClose: true
        })
      } else if(res = 'token is failure,please login again') {
        console.log('token过期',res);
        this.$message({
          type: 'warning',
          message: 'token已失效，请重新登录',
          showClose: true,
        })
        this.type = ''
        this.isLogin = false
      } else if(res == '402') {
        console.log('强制下线',res);
        this.$message({
          type: 'warning',
          message: '账号别处登录，强制下线',
          showClose: true,
        })
        this.type = ''
        this.isLogin = false
      }
      this.$router.push('/home')
    }
	},
	computed: {
		classObj() {
			return {
				down: this.downClass,
				up: this.upClass
			}
		},
    ...mapGetters(['userAvatar'])
	},
	watch: {
		$route:{
			immediate: true,
			handler(to,from){
				console.log('Head头部跳转路由',to,from)
				const firstArr = ['/archive','/category','/tag']
				const secondArr = ['/talk','/album']
				if(firstArr.indexOf(to.path) != -1) {
					this.secondDisBlock = false
					this.firstDisBlock = true
				} else if(secondArr.indexOf(to.path) != -1) {
					this.firstDisBlock = false
					this.secondDisBlock = true
				} else {
					this.firstDisBlock = false
					this.secondDisBlock = false
				}
			},
			deep:true,
		}
	}
};
</script>

<style lang="less" scoped>
.header {
	position: fixed;
	display: flex;
	align-items: center;
	flex-wrap: nowrap;
	justify-content: space-between;
	height: 50px;
	width: 100%;
	padding: 0 16px;
	color: #fff;
	z-index: 99;
	transition: transform 0.6s;
}
.up {
	color: #000;
	// background-color: #e4fdf5;
	background: linear-gradient(90deg,#E3FDF5,#FCE8F9);
}
.up .toggle .line {
	background-color: #000;
}
.up a {
	color: #000;
}
.up .menu-item:not(.title) .menu-btn:before {
	background-color: #000;
}
.up .menu-item.dropdown .menu-btn:after {
	border-top-color: #000;
}
.up .menu-item.dropdown .submenu {
	color: #000;
	background-color: #fff;
}
.down {
	transform: translateY(-100%);
}
i {
	font-size: 14px;
}
i.big {
	font-size: 20px;
}
.toggle {
	display: none;
	width: 40px;
	height: 50px;
	font-size: 17px;
	cursor: pointer;
}
.lines {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}
.line {
	height: 3px;
	width: 24px;
	background-color: #fff;
	border-radius: 2px;
}
.line:not(:first-child) {
	margin-top: 3px;
}
@media screen and (max-width: 990px) {
	.toggle {
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
	}
}
/deep/.el-drawer__header {
	display: none;
}
.n-drawer-body {
	width: 100%;
	height: 100%;
	padding: 20px;
}
.n-drawer-body  .host-avatar {
  text-align: center;
  width: 150px;
  height: 150px;
  margin: 20px auto;
  border-radius: 50%;
  overflow: hidden;
}
.n-drawer-body .host-avatar img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  transition: all .5s;
}
.n-drawer-body .host-avatar img:hover {
  transform: scale(1.1);
}
.n-drawer-body .host-nickName {
  text-align: center;
  padding-bottom: 10px;
}
.n-drawer-body .host-explanation  {
  text-align: center;
  color: #999;
  padding-top: 10px;
}
.n-drawer-body .host-info {
  display: flex;
  justify-content: center;
}
.n-drawer-body .host-info .host-info-item {
  display: flex;
  flex-direction: column;
  text-align: center;
  border-right: 1px solid #ccc;
  padding: 5px 10px 0 10px;
  margin: 10px 0;
}
.n-drawer-body .host-info .host-info-item .info-item-num1,
.n-drawer-body .host-info .host-info-item .info-item-num2,
.n-drawer-body .host-info .host-info-item .info-item-num3 {
  font-weight: bold;
  font-size: 18px;
}
.n-drawer-body .host-info .host-info-item .info-item-num1{
  color: #ec7c86;
}
.n-drawer-body .host-info .host-info-item .info-item-num2 {
  color: #49B1F5;
}
.nav-menu {
	padding: 16px 16px 20px;
}
.nav-menu a {
	color: #999;
}
.nav-menu ul li {
	font-size: 18px;
	line-height: 3;
	text-align: center;
	margin-bottom: 10px;
	border-radius: 15px;
	transition: all 0.5s;
}
.nav-menu>ul>li:hover {
	background-color: #eee;
}
.nav-menu ul li:hover>a {
	color: #000;
}
.nav-menu .nav-dropdown ul {
	display: none;
}
.nav-menu .nav-dropdown:hover ul {
	display: block;
}
.nav-menu .nav-dropdown ul li:hover {
	background-color: #ccc;
}
.nav-menu ul li.active {
	background: linear-gradient(
		90deg,
		#EC7298,
		#EC8870);
}
.nav-menu ul li.active a {
	color: #fff;
}
.nav-menu .nav-dropdown li.active a {
	color: #fff;
}
.nav-menu .nav-dropdown ul.disblock {
	display: block;
}

.menu {
	display: flex;
	height: 50px;
	align-items: center;
	letter-spacing: 1px;
}
.menu .menu-item {
	position: relative;
	height: 34px;
	font-size: 17px;
	text-align: center;
	line-height: 34px;
	padding: 0 10px;
	cursor: pointer;
}
@media screen and (max-width: 900px) {
	.menu-item:not(.title) {
		display: none;
	}
	.menu .menu-item.title {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 100%;
	}
}
.menu-item.active:not(.dropdown) .menu-btn:before {
	width: 70%;
}
.menu-item:not(.title) .menu-btn:before {
	content: '';
	position: absolute;
	width: 0;
	height: 3px;
	bottom: 0;
	border-radius: 2px;
	left: 50%;
	transform: translate(-50%);
	background-color: #fff;
	transition: all 0.4s ease-in-out 0s;
}
.menu-item:not(.dropdown):hover .menu-btn:before {
	width: 70%;
}
.menu-item.dropdown .menu-btn.drop {
	width: 72px;
	text-align: left;
}
.menu-item.dropdown .menu-btn:after {
	content: '';
	position: absolute;
	display: inline-block;
	top: 17px;
	right: 10px;
	vertical-align: middle;
	border-right: 5px solid transparent;
	border-left: 5px solid transparent;
	border-top: 5px solid #fff;
}
ul {
	display: block;
}
.menu-item.dropdown .submenu {
	display: none;
	position: absolute;
	top: 43px;
	left: 10px;
	width: max-content;
	padding: 0;
	background-color: rgba(0, 0, 0, .5);
	border-top-left-radius: 10px;
	border-bottom-right-radius: 10px;
	z-index: 10;
}
.menu-item.dropdown:hover .submenu {
	display: block;
}
.submenu .subitem {
	display: block;
	font-size: 16px;
  text-align: left;
}
.menu-item.dropdown .subitem .link {
	display: inline-block;
	width: 100%;
	padding: 4px 12px;
	transition: all 0.3s;
}
.submenu:before {
	position: absolute;
	top: -20px;
	left: 0;
	width: 100%;
	height: 40px;
	content: '';
}
.submenu>.subitem:first-child,
.submenu>.subitem:first-child>.link{
	border-top-left-radius: 10px;
}
.submenu>.subitem:last-child,
.submenu>.subitem:last-child>.link{
	border-bottom-right-radius: 10px;
}
.submenu li.subitem:hover .link{
	transform: translate(5px);
}
.submenu li.subitem:hover{
	color: #fff;
	background: linear-gradient(
		90deg,
		#EC7298,
		#EC8870);
}
.menu-item.dropdown .subitem.active {
	background: linear-gradient(
		90deg,
		#EC7298,
		#EC8870);
}
.user-btn {
	width: 24px;
	height: 24px;
}
.menu-item.dropdown img {
	width: 25px;
	height: 25px;
	border-radius: 50%;
	vertical-align: middle;
	margin-bottom: 2px;
}

.right {
	display: flex;
	height: 50px;
	justify-content: space-around;
	align-items: center;
	letter-spacing: 1px;
}
.right .right-item {
	cursor: pointer;
	padding: 6px 0 6px 12px;
}
/deep/.el-dialog {
	border-radius: 10px;
	max-width: calc(100vw - 32px);
}
/deep/.el-dialog--center .el-dialog__body {
	padding: 20px 25px 0;
}
.regist-dialog /deep/.el-form-item {
  margin-bottom: 32px;
}
.login-dialog /deep/.el-form-item {
  margin-bottom: 32px;
}
/deep/.el-dialog__footer {
	padding: 0 25px 20px;
}
/deep/.el-input-group__append button.el-button.getcode {
  background-color: #EC7298;
  border: 2px solid #EC7298;
  color: #fff;
  height: 100%;
  width: 70px;
  box-shadow: 3px 3px 5px -2px #999;
}
/deep/.el-input-group__append button.el-button.getcode:hover {
  opacity: .8;
}
/deep/.el-input-group__append button.el-button.getcode.disabled {
  opacity: .6;
}
/deep/.el-button {
  color: #fff;
  background-color: #EC7298;
}
/deep/.el-button:focus, .el-button:hover {
  color: #fff;
  background-color: #EC7298;
  border-color: transparent;
  opacity: .7;
}
.footer-body {
	 padding: 20px 20px 0;
}
.footer-body .gologin {
  color: orange;
}
.footer-item1 {
	display: flex;
	justify-content: space-between;
	font-size: 14px;
}
.footer-item2 {
	height: 20px;
	margin-top: 15px;
	color: #b5b5b5;
	font-size: 14px;
}
.footer-item2::before {
	content: '';
	display: inline-block;
	margin: 0 12px;
	background-color: #d8d8d8;
	width: 60px;
	height: 1px;
	vertical-align: middle;
}
.footer-item2::after {
	content: '';
	display: inline-block;
	margin: 0 12px;
	background-color: #d8d8d8;
	width: 60px;
	height: 1px;
	vertical-align: middle;
}
.footer-item3 {
	height: 70px;
	padding-top: 25px;
}
.footer-item3 .iconfont {
  margin: 0 10px;
  font-size: 34px;
}
.footer-item3 .iconfont.qq {
  color: #49B1F5;
}
.searchContent {
	height: 250px;
	text-align: left;
	margin: 0;
	padding: 0;
}
</style>
