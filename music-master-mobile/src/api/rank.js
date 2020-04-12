import jsonp from 'common/js/jsonp'
import {commonParams, options} from './config'
import axios from 'axios'

const host = 'http://localhost:8888'

export function getTopList() {
  return axios.get(`${host}/songList`)
  // const url = 'https://c.y.qq.com/v8/fcg-bin/fcg_myqq_toplist.fcg'
  //
  // const data = Object.assign({}, commonParams, {
  //   uin: 0,
  //   needNewCode: 1,
  //   platform: 'h5'
  // })
  // console.log('getTopList')
  // console.log(jsonp(url, data, options))
  // return jsonp(url, data, options)
}

export function getMusicList(topid) {
  const url = 'https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg'

  const data = Object.assign({}, commonParams, {
    topid,
    needNewCode: 1,
    uin: 0,
    tpl: 3,
    page: 'detail',
    type: 'top',
    platform: 'h5'
  })
  return jsonp(url, data, options)
}
