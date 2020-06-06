export const mixin = {
  methods: {
    // 提示信息
    notify (title, type) {
      this.$notify({
        title: title,
        type: type
      })
    },
    // 获取图片信息
    attachImageUrl (srcUrl) {
      // eslint-disable-next-line eqeqeq
      if (!srcUrl) return '../assets/img/user.jpg' || ''
      if (String(srcUrl).indexOf('http') !== -1) { } else { srcUrl = this.$store.state.configure.HOST + srcUrl || '../assets/img/user.jpg' }
      console.log(srcUrl)
      return srcUrl
    },
    attachBirth (val) {
      let birth = String(val).match(/[0-9-]+(?=\s)/)
      return Array.isArray(birth) ? birth[0] : birth
    },
    // 得到名字后部分
    replaceFName (str) {
      let arr = str.split('-')
      return arr[1]
    },
    // 得到名字前部分
    replaceLName (str) {
      let arr = str.split('-')
      return arr[0]
    },
    // 播放
    toplay: function (id, url, pic, index, name, lyric) {
      this.$store.commit('setId', id)
      this.$store.commit('setListIndex', index)
      this.$store.commit('setUrl', url)
      this.$store.commit('setpicUrl', pic)
      this.$store.commit('setTitle', this.replaceFName(name))
      this.$store.commit('setArtist', this.replaceLName(name))
      this.$store.commit('setLyric', this.parseLyric(lyric))
    },
    // 解析歌词
    parseLyric (text) {
      let lines = text.split('\n')
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g //  正则表达式，pattern.text("aaa")，可以用来检验aaa是不是符合“[00:10.080]”这种时间轴的格式
      let result = []

      // 对于歌词格式不对的特殊处理
      if (!(/\[.+\]/.test(text))) {
        return [[0, text]]
      }
      //  如果第一行是歌名或歌手名没有时间轴，就去掉该行
      while (!pattern.test(lines[0])) {
        lines = lines.slice(1)
      }

      lines[lines.length - 1].length === 0 && lines.pop() //  若最后一行为空，则去掉该行
      for (let item of lines) {
        let time = item.match(pattern) // 存前面的时间段 item是一行的歌词，pattern是上面定义的时间轴的正则表达式，item.match(pattern)就是返回这一行里面匹配到的时间轴
        let value = item.replace(pattern, '') // 存时间轴后面的歌词，将一行前面的时间轴变成空白，这一行就是我们要的纯歌词了
        // console.log(time) // 时间
        // console.log(value) // 歌词数据
        for (let item1 of time) {
          var t = item1.slice(1, -1).split(':') //  “{00:10.080]->“0:10.080”去掉方块号
          if (value !== '') {
            result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value])//  parseInt(t[0], 10) * 60 + parseFloat(t[1]) 把时间轴转化成秒数，如上t[0]表示分钟，t[1]表示秒，用t[0]*60+t[1]把分钟成秒，之后和value(上面定义的纯歌词变量),作为键值对存到result
          }
        }
      }
      result.sort(function (a, b) { //  按照每一行歌词的时间大小把每一行由小到大排序
        return a[0] - b[0]
      })
      return result
    },
    // 搜索音乐
    getSong () {
      if (!this.$route.query.keywords) {
        this.$store.commit('setListOfSongs', [])
        this.notify('您输入内容为空', 'warning')
      } else {
        this.$api.songAPI.getSongOfSingerName(this.$route.query.keywords)
          .then(res => {
            if (!res.data.length) {
              this.$store.commit('setListOfSongs', [])
              this.notify('系统暂无该歌曲', 'warning')
            } else {
              this.$store.commit('setListOfSongs', res.data)
            }
          })
          .catch(err => {
            console.log(err)
          })
      }
    }
  }
}
