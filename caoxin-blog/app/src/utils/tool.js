/**
 * 封装一个格式化当前日期的函数
 * @returns 返回格式化当前日期
 * 格式如：2023-5-20 17:14:12 星期六
 */
export const getTime = function() {
  var date = new Date()
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var dates = date.getDate()
  var h = date.getHours()
  h = h < 10 ? '0' + h : h
  var mm = date.getMinutes()
  mm = mm < 10 ? '0' + mm : mm
  var s = date.getSeconds()
  s = s < 10 ? '0' + s : s
  var day = date.getDay()
  var arr = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  return year + '-' + month + '-' + dates + ' ' + h + ':' + mm + ':' + s + ' ' + arr[day]
}

/**
 * @param {String} time 传入一个时间字符串
 * 格式：2023-5-17 0:0:0、2022/10/29 17:44:44均可
 * @param {Boolean} flag 传入一个布尔值
 * flag值为 true：表示是参数时间减去当前时间，值为 false：表示是当前时间减去参数时间
 * @returns 返回当前时间与参数时间之差
 * 格式如：03天17时15分06秒
 */
export const countDown = function(time, flag) {
  var nowTime = +new Date()  // 当前时间总毫秒数
  var inputTime = +new Date(time)  // 输入时间总毫秒数
  if(flag) {
    var times = inputTime - nowTime  // 剩余时间总毫秒数
  } else {
    var times = nowTime - inputTime  // 剩余时间总毫秒数
  }
  times = times / 1000  // 剩余时间总 秒数
  var d = parseInt(times / 60 / 60 / 24)  // 计算天数
  d = d < 10 ? '0' + d : d
  var h = parseInt(times / 60 / 60 % 24)  // 计算小时
  h = h < 10 ? '0' + h : h
  var m = parseInt(times / 60 % 60)  //计算分数
  m = m < 10 ? '0' + m : m
  var s = parseInt(times % 60)  //计算秒数
  s = s < 10 ? '0' + s : s
  return d + '天' + h + '时' + m + '分' + s + '秒'
}