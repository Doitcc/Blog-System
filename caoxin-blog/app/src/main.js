import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from '@/router';
import { vueBaberrage } from 'vue-baberrage'
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
import createAlignPlugin from '@kangc/v-md-editor/lib/plugins/align';
import createTipPlugin from '@kangc/v-md-editor/lib/plugins/tip/index';
import '@kangc/v-md-editor/lib/plugins/tip/tip.css';

VMdEditor.use(createTipPlugin());
VMdEditor.use(createAlignPlugin());
VMdEditor.use(createCopyCodePlugin());
// highlightjs
import hljs from 'highlight.js';

VMdEditor.use(githubTheme, {
  Hljs: hljs,
  config: {
    toc: {
      includeLevel: [1,2, 3],
    },
  },
  codeHighlightExtensionMap: {
    vue: 'xml',
  },
});

Vue.use(VMdEditor);


Vue.use(vueBaberrage);

Vue.use(ElementUI);

Vue.config.productionTip = false

//统一引入api接口文件里所有的请求函数
import API from '@/api'
//引入仓库
import store from '@/store'

import Empty from '@/components/Empty/Empty.vue'

Vue.component(Empty.name,Empty)

new Vue({
  render: h => h(App),
	//生命周期函数
	beforeCreate() {
		//全局事件总线的配置
		Vue.prototype.$bus = this,
		Vue.prototype.$API = API
	},
	router,
  //注册仓库信息：组件身上拥有 $store属性
  store
}).$mount('#app')
