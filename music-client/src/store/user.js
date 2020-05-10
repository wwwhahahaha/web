const user = {
  state: {
    userId: '',
    username: '',
    avator: '',
    first: true, // 判断是否第一次打开首页
    songsList: ''
  },
  getters: {
    songsList: state => {
      let songsList = state.songsList
      return songsList
    },
    first: state => {
      let first = state.first
      return first
    },
    userId: state => {
      let userId = state.userId
      if (!userId) {
        userId = JSON.parse(window.localStorage.getItem('userId') || null)
      }
      return userId
    },
    username: state => {
      let username = state.username
      if (!username) {
        username = JSON.parse(window.localStorage.getItem('username') || null)
      }
      return username
    },
    avator: state => {
      let avator = state.avator
      if (!avator) {
        avator = JSON.parse(window.localStorage.getItem('avator') || null)
      }
      return avator
    }
  },
  mutations: {
    setSongsList: (state, songsList) => {
      state.songsList = songsList
    },
    setFirst: (state, first) => {
      state.first = first
    },
    setUserId: (state, userId) => {
      state.userId = userId
      window.localStorage.setItem('userId', JSON.stringify(userId))
    },
    setUsername: (state, username) => {
      state.username = username
      window.localStorage.setItem('username', JSON.stringify(username))
    },
    setAvator: (state, avator) => {
      state.avator = avator
      window.localStorage.setItem('avator', JSON.stringify(avator))
    }
  },
  actions: {}
}

export default user
