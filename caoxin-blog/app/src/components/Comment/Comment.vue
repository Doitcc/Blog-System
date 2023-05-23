<template>
  <div>
    <div class="reply-wrap">
      <div class="reply-title">
        <i class="iconfont icon-pinglun"></i>
        <span>评论</span>
      </div>
      <div class="reply-box">
        <div class="box-normal">
          <div class="reply-box-avatar">
            <img class="tourist-avatar" :src="avatar" alt="">
          </div>
          <div class="reply-box-input">
            <textarea class="reply-box-textarea" v-model="rootText" placeholder="发一条友善的评论" cols="30" rows="10"></textarea>
          </div>
          <div class="reply-box-send" @click="reply()">评论</div>
        </div>
        <div class="box-expand"></div>
      </div>
      <div v-if="commentList.length>0" class="comment-list">
        <!-- 根评论 -->
        <div v-for="(comment,index) in commentList" :key="comment.id" class="comment-item" v-show="index<showRootNum">
          <div class="comment-avatar">
            <img :src="comment.avatar" alt="">
          </div>
          <div class="comment-container">
            <div class="comment-username">{{comment.nickName}} <i v-show="comment.createBy=='1'" class="iconfont icon-renzheng"></i></div>
            <div class="comment-content">{{comment.content}}</div>
            <div class="comment-time">
              <span>{{comment.createTime}}</span>
              <span class="comment-reply" @click="showReplay(commentList,comment,comment)">回复</span>
            </div>
            <!-- 回复评论 -->
            <div class="comment-children">
              <div v-for="(comment1,index) in comment.children" :key="comment1.id" class="comment-item" v-show="index<comment.showChildrenNum">
                <div class="comment-avatar">
                  <img :src="comment1.avatar" alt="">
                </div>
                <div class="comment-container">
                  <div class="comment-username">
                  {{comment1.nickName}} <i v-show="comment1.createBy=='1'" class="iconfont icon-renzheng"></i>回复 
                  <span style="color: deepskyblue;">@{{comment1.toCommentNickName}}</span>
                  ：{{comment1.content}}</div>
                  <div class="comment-time">
                    <span>{{comment1.createTime}}</span>
                    <span class="comment-reply" @click="showReplay(commentList,comment,comment1)">回复</span>
                  </div>
                </div>
              </div>
            </div>
            <div v-show="comment.children.length>comment.showChildrenNum" class="more-chidren">
              <span>共{{comment.children.length}}条评论，</span>
              <span class="look-more" @click="showMoreChildren(comment)">点击查看</span>
            </div>
            <div v-show="comment.children.length>3&&comment.children.length<=comment.showChildrenNum" class="more-chidren">
              <span class="look-more" @click="putAwayChildren(comment)">收起</span>
            </div>
            <div v-if="comment.reply" class="reply-box">
              <div class="box-normal">
                <div class="reply-box-avatar">
                  <img class="tourist-avatar" :src="avatar" alt="">
                </div>
                <div class="reply-box-input">
                  <textarea class="reply-box-textarea" v-model="text" :placeholder="`回复@${replyObj.nickName}`" cols="30" rows="10"></textarea>
                </div>
                <div class="reply-box-send" @click="reply(comment)">评论</div>
              </div>
              <div class="box-expand"></div>
            </div>
          </div>
        </div>
        <div v-show="commentList.length>showRootNum" class="loading-more">
          <span @click="loadingMore">加载更多...</span>
        </div>
      </div>
      <div v-else class="reply-desc">来发评论吧~</div>
    </div>
  </div>
</template>

<script>
import {mapGetters,mapMutations} from 'vuex'
export default {
  name: 'Comment',
  props: ['articleId','commentList','getArticleCommentList','getFriendCommentList'],
  data() {
    return {
      defaultAvatar: 'https://static.ttkwsd.top/config/0bca52afdb2b9998132355d716390c9f.png',
      replyObj: {},
      rootText: '',
      text: '',
      type: 0,
      showRootNum: 4
    }
  },
  mounted() {
    
  },
  methods: {
    ...mapMutations(['LOGINOUT']),
    // 显示回复评论输入框
    showReplay(commentList,comment,comment1) {
      console.log('commentList,comment,comment1',commentList,comment,comment1);
      commentList.forEach(item => {
        item.reply = false
      })
      comment.reply = true
      this.replyObj = comment1
    },
    // 点击查看更多回复评论
    showMoreChildren(comment) {
      comment.showChildrenNum+=3
    },
    // 收起回复评论
    putAwayChildren(comment) {
      comment.showChildrenNum=3
    },
    // 加载更多
    loadingMore() {
      this.showRootNum+=4
    },
    // 发送评论
    async reply(comment) {
      var token = localStorage.getItem('token')
      if(!token) {
        this.$message({
          type: 'warning',
          message: '请先登录后再评论',
          showClose: true
        })
        return
      }
      console.log('comment',comment);
      console.log('replyObj',this.replyObj);
      if(!this.articleId)  {
        this.type = 1
      }
      if(comment) {
        // 回复他人评论
        var data = {
          articleId: this.articleId || null,
          type: this.type,
          rootId: comment.id,
          toCommentId: this.replyObj.id,
          toCommentUserId: this.replyObj.createBy,
          content: this.text
        }
      } else {
        // 直接评论文章
        var data = {
          articleId: this.articleId || null,
          type: this.type,
          rootId: -1,
          toCommentId: -1,
          toCommentUserId: -1,
          content: this.rootText
        }
      }
      console.log(data)
      if(this.articleId) {
        var res = await this.$API.article.addArticleComment(data)
      } else {
        var res = await this.$API.friend.addFriendComment(data)
      }
      console.log('发送评论',res);
      if(res.code==200) {
        if(comment) {
          this.text = ''
        } else {
          this.rootText = ''
          this.showRootNum+=1
        }
        this.$message({
          type: 'success',
          message: '评论成功',
          showClose: true
        })
        if(this.articleId) {
          this.getArticleCommentList()
        } else {
          this.getFriendCommentList()
        }
      } else if(res.code==401||res.code==402) {
        console.log('token过期 或 强制下线',res);
        this.$bus.$emit('loginOut')
      } else {
        this.$message({
          type: 'error',
          message: '评论失败',
          showClose: true
        })
      }
    }
  },
  filters: {
    formDataTime(value) {
      return value.split(' ')[0]
    }
  },
  computed: {
    ...mapGetters(['userAvatar']),
    avatar() {
      return this.userAvatar || this.defaultAvatar
    },
  },
}
</script>

<style lang="less" scoped>
.reply-title {
  display: flex;
  align-items: center;
  margin: 22px 0;
  padding-left: 10px;
  font-size: 20px;
  font-weight: 600;
  line-height: 40px;
}
.reply-title .iconfont {
  font-size: 24px;
  margin-right: 5px;
}
.reply-title .icon-pinglun:before {
  vertical-align: -3px;
}
.reply-box {
  display: flex;
  flex-direction: column;
}
.box-normal {
  display: flex;
  height: 50px;
}
.reply-box-avatar {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 48px;
  height: 50px;
}
.tourist-avatar {
  display: block;
  width: 48px;
  height: 48px;
  border-radius: 50%;
}
.reply-box-input {
  flex: auto;
  margin-left: 10px;
}
.reply-box-textarea {
  width: 100%;
  height: 100%;
  padding: 5px 10px;
  border: 1px solid #eff2f3;
  border-radius: 6px;
  background-color: #f1f2f3;
  font-size: 14px;
  line-height: 38px;
  color: #333;
  resize: none;
  outline: none;
}
.reply-box-textarea:focus {
  line-height: normal;
  border-color: rgb(237, 110, 160);
  background-color: #fff;
}
.write {
  line-height: normal;
  border-color: rgb(237, 110, 160);
  background-color: #fff;
}
.reply-box-send {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-basis: 70px;
  margin-left: 10px;
  border-radius: 4px;
  background-color: #ed9abb;
  color: #fff;
  font-size: 16px;
  cursor: pointer;
}
.reply-desc {
  padding: 20px;
  text-align: center;
  font-size: 16px;
  line-height: 2;
}
.comment-list {
  padding: 30px 0 10px;
  font-size: 16px;
}
.comment-list .comment-item {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
.comment-item>.comment-avatar {
  margin-right: 10px;
}
.comment-item>.comment-avatar img {
  display: block;
  width: 48px;
  height: 48px;
  // margin: 0 auto;
  border-radius: 50%;
}
.comment-item .comment-container {
  flex: 1;
  font-size: 14px;
  line-height: 24px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ccc;
}
.comment-container .comment-username .iconfont {
  color: var(--pink);
  margin-right: 5px;
}
.comment-container .comment-time {
  color: #999;
  display: flex;
  align-items: center;
}
.comment-container .comment-time .comment-reply {
  margin-left: 15px;
}
.comment-container .comment-time .comment-reply:hover {
  color: #ed9abb;
}
.comment-container .reply-box {
  margin: 12px 0 5px;
}
.comment-container .comment-children{
  // padding: 10px 0;
}
.comment-container .comment-children .comment-item {
  margin-top: 10px;
}
.comment-children .comment-item .comment-container {
  border: none;
  padding-bottom: 0px;
}
.comment-children .comment-avatar {
  margin-right: 10px;
}
.comment-children .comment-avatar img {
  display: block;
  width: 26px;
  height: 26px;
  // margin: 0 auto;
  border-radius: 50%;
}
.comment-container .more-chidren {
  margin-top: 10px;
  color: #999;
}
.comment-container .more-chidren .look-more:hover {
  color: #ed9abb;
}
.loading-more {
  padding-top: 25px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: var(--common-color);
}
.loading-more span {
  line-height: 40px;
  padding: 0 15px;
  border-radius: 4px;
  background-color: #ed9abb;
  color: var(--white);
}
</style>