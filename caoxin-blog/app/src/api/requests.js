//对于axios进行二次封装
import axios from 'axios'
import nprogress from 'nprogress'
import 'nprogress/nprogress.css'
import needTokenUrl from './needTokenUrl.js'

const requests = axios.create({
	baseURL: '/api',
  // baseURL: 'http://172.30.192.219:7777/api',
  // baseURL: 'http://192.168.241.202:7777',
	timeout: 5000
})

//请求拦截器：在发请求之前，请求拦截器可以检测到，可以在请求发出之前去做一些事情
requests.interceptors.request.use((config)=>{
	nprogress.start();
  //给请求头添加一个字段token：用户登录身份
  var token = localStorage.getItem("token")
  // let flag = needTokenUrl.includes(config.url)
  let flag = needTokenUrl.includes(config.url)
  if(config.url.indexOf('/content/tag/delete')!==-1) {
    flag = true
  }
  // console.log(config.url,needTokenUrl.includes(config.url));
  if(token&&flag) {
    console.log(config.url,'该请求加了token');
    config.headers.token = token
  }
  // console.log(localStorage.getItem("token"));
	// console.log(11)
	return config;
})

//响应拦截器
requests.interceptors.response.use((res)=>{
	//成功的回调函数：服务器响应数据回来后，响应拦截器可以检测到，可以去做一些事情
	//进度条结束
	nprogress.done();
  // console.log('请求成功',res);
  console.log(res.data);
  // if(res.data.code==200) {
  //   console.log('router',router);
  // }
	return res.data
}, (err)=>{
	//失败的回调函数
	// console.log(22)
  nprogress.done();
  console.log('请求失败',err);
	// return Promise.reject(new Error('failed'))
  return err
})

export default requests;