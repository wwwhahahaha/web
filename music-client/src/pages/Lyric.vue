<template>
  <div class="content">
    <div class="song-lyric">
      <h2>歌词</h2>
<!--      <transition-group appear name="lyr-fade">-->
        <!--有歌词-->
        <div v-show="lyr.length" key="has-lyr"> <!--v-show判断歌词是否展示，如果lyr.length（歌词长度）为0，即v-show为false，就是没有歌词，是不展示，反之则展示,key="has-lyr"保证更新效率-->
          <ul :style="{top:lrcTop}"  class="lrc">
            <li class="lyric" v-for="(item, index) in lyr" v-bind:key="index"> <!--   lyr是页面加载时储存的歌词数组，v-for循环读取这个数组，一次一行，item是每-行的歌词-->
              {{ item[1] }} <!--一行歌词的样式是这样的“[00:10.080]你陪我步入蝉夏 越过城市喧嚣”，是用数组形式存储的，item[0]是一行前面的数字，item[1]才是后面的歌词，所以这里用item[1]展示歌词，加两个大括号是vue绑定数据时必须的格式-->
            </li>
          </ul>
        </div>
        <!--没歌词的情况-->
     <div v-show="!lyr.length" class="no-lyric" key="no-lyr"> <!--   歌词不展示的情况-->
          <span class="no-lrc">暂无歌词</span>
        </div>
<!--      </transition-group>-->
      <comment :playId="id" :type="0"></comment> <!--   歌曲评论，comment是引用自己写的评论组件，位置在componets文件夹里面，:playId="id" :type="0"这两个是往组件里传参数，
      就相当于你往自己定义的函数里传参数一样，:playid传的是歌曲的id，:type为0是指歌曲的评论，为1是指歌单评论，这里是歌曲播放页面所以当然是0-->
    </div>
  </div>
</template>

<script>
import {mixin} from '../mixins'
import { mapGetters } from 'vuex'
import Comment from '../components/Comment'

export default {
  name: 'lyric',
  components: {
    Comment
  },
  mixins: [mixin], //  这里引用函数，mixins文件夹下面的index.js文件，引用这个文件里的函数
  data () {
    return {
      lrcTop: '200px', // 歌词滑动
      showLrc: false, // 切换唱片和歌词
      lyr: [] // 当前歌曲的歌词
    }
  },
  computed: { //  computed里面的数据是随时自动更新的，只要这几个数据在其他页面被提交，computed里就会立刻更新.curTime的数据是components文件夹里的SongAudio里更新的，播放器自动调用时间更新函数
    ...mapGetters([ // 从vuex里自动获取下面的几个数据，vuex就相当于前端的数据库
      'curTime',
      'id', // 歌曲ID
      'lyric', // 歌词
      'listOfSongs', // 存放的音乐
      'listIndex' // 当前歌曲在歌曲列表的位置
    ])
  },
  watch: { //  watch表示只要watch里的数据一改变（比如，当前时间变量curTime),就会立即调用curTime后面的函数
    id: function () { //  歌曲id改变时更新歌词信息
      this.lyr = this.parseLyric(this.listOfSongs[this.listIndex].lyric) // parseLyric是上面mixins里面的函数
      console.log('lyr ===>', this.lyr[1])
    },
    // 播放时间的开始和结束
    curTime: function () { //  时间改变时滚动歌词
      // 处理歌词位置及颜色
      if (this.lyr.length !== 0) {
        for (var i = 0; i < this.lyr.length; i++) { //  遍历每行歌词
          if (this.curTime >= this.lyr[i][0]) { // this.lyr[i][0] i是指歌词的行数，后面的0是指前面的时间信息，1的话就是歌词，这一行的意思就是判断当前时间是不是大于这一行歌词里面的时间信息，如果大于就滚动到这一行歌词
          //  就是像这样的，前面是时间信息，后面的是歌词“[00:10.080]你陪我步入蝉夏 越过城市喧嚣”
            for (var j = 0; j < this.lyr.length; j++) { //  这个循环遍历所有行的歌词，把它们调成黑色和小字号，这就是时间轴没有滚动到时候的默认样式
              document.querySelectorAll('.lrc li')[j].style.color = '#000'
              document.querySelectorAll('.lrc li')[j].style.fontSize = '15px'
            }
            if (i >= 0) { //  i是指已经滚动到的歌词行，进行特殊显示
              document.querySelectorAll('.lrc li')[i].style.color = '#95d2f6' //  特殊显示，调为蓝色
              document.querySelectorAll('.lrc li')[i].style.fontSize = '25px' //  特殊显示，放大字号
            }
          }
        }
      }
    }
  },
  created () {
    this.lyr = this.lyric
  }
}
</script>

<style scoped>
.content {
  padding: 50px 0;
  flex: 1;
}
.song-lyric {
  margin: auto;
  width: 700px;
  background-color: #ffffff;
  border-radius: 12px;
  padding: 0 20px 50px 20px;
}
.lyr-fade-enter,
.lyr-fade-leave-to {
  transform: translateX(30px);
  opacity: 0;
}
.lyc-fade-enter-active,
.lyc-fade-leave-active {
  transition: all .3s ease;
}
.lrc {
  font-size: 18px;
  padding: 30px 0;
  width: 100%;
  text-align: center;
}
.lyric {
  width: 100%;
  height: 40px;
  line-height: 40px;
}
.no-lyric {
  margin: 200px 0;
  width: 100%;
  text-align: center;
}
.no-lrc {
  font-size: 30px;
  text-align: center;
}
h2 {
  text-align: center;
  height: 50px;
  line-height: 50px;
  border-bottom: 2px solid black;
}

</style>
