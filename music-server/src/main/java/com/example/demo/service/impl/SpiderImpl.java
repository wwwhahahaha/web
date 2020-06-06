package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.ListSong;
import com.example.demo.domain.Singer;
import com.example.demo.domain.Song;
import com.example.demo.domain.SongList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.TreeMap;


@Service
public class SpiderImpl {
    @Autowired
    private SongListServiceImpl songListService;

    @Autowired
    private SongServiceImpl songService;

    @Autowired
    private SingerServiceImpl singerService;

    @Autowired
    public ListSongServiceImpl listSongService;

    //HttpClient爬取
//    public void getDocument(String url) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpResponse response = null;
//        HttpGet request = new HttpGet("https://music.163.com/discover/playlist");
//
//        List<Header> headerList = new ArrayList<>();
//        headerList.add(new BasicHeader("Host", "music.163.com"));
//        headerList.add(new BasicHeader("Referer", "https://music.163.com/"));
//        headerList.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"));
//        Header[] header=headerList.toArray(new Header[headerList.size()]);
//        request.setHeaders(header);
//
//        try {
//            response = httpClient.execute(request);
//
//            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                //5.获取响应内容
//                HttpEntity httpEntity = response.getEntity();
//                String html = EntityUtils.toString(httpEntity, "utf-8");
//
//                File a = new File("abc.html");
//                if (!a.exists())
//                    a.createNewFile();
//                BufferedWriter bfWrite = new BufferedWriter(new FileWriter(a));
//                bfWrite.write(html);
//            } else {
//                System.out.println("返回状态不是200");
//                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            //6.关闭
//            HttpClientUtils.closeQuietly(response);
//            HttpClientUtils.closeQuietly(httpClient);
//        }
//    }


    @Scheduled(cron = "* * * */1 * ?")
    public void getSpider() throws IOException {
        getAlbum("https://music.163.com/discover/playlist");
    }
    public void getAlbum(String url) throws IOException {

        Document doc1 = null;

        doc1 = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Cookie", "PSTM=1585406681; BAIDUID=00A0D3A4B32701F5EAB781C9F1005BF9:FG=1; BIDUPSID=711F8227AD3E8C231C3348346A47ADA5")
                .header("Referer", "https://music.163.com/discover/artist/cat?id=1001&initial=65")
                .header("Upgrade-Insecure-Requests", "1")
                .method(Connection.Method.GET)
                .timeout(200000).get(); // 设置请求头等信息，模拟人工访问，超时时间可自行设置

        File a = new File("songList.html");
        if (!a.exists())
            a.createNewFile();
        BufferedWriter bfWrite = new BufferedWriter(new FileWriter(a));
        bfWrite.write(String.valueOf(doc1));

        //Elements names = doc1.select("#song-list-pre-data"); // 歌名
        //Elements singer = doc1.select(".intr:eq(1) span a"); // 歌手
        //Elements album = doc1.select(".hd f-cb .f-ff2"); // 专辑名

        Elements songLists=doc1.select("#m-pl-container li");

        for(Element element:songLists){
            SongList sLia=new SongList();

            String ab=element.select(".icon-play.f-fr").attr("data-res-id");

            String idDoc=element.select(".icon-play.f-fr").attr("data-res-id");
            String oId= idDoc;
            Integer nId=Integer.valueOf(idDoc.substring(1));
            if(songListService.selectByPrimaryKey(nId)) continue;
            String title=element.select(".msk").attr("title");
            String pic=element.select("img").attr("src").split("\\?")[0];
            sLia.setId(nId);sLia.setTitle(title);sLia.setPic(pic);
            getSongs(sLia,"https://music.163.com/playlist?id="+oId,nId);
        }
//        JSONArray jsonArray = JSONArray.parseArray(names.text());
//
//        for (int i = 0; i < jsonArray.size(); i++) {
//            String songName = jsonArray.getJSONObject(i).getString("name").replace("\"", "").replace("\\", "");
//            String songId = jsonArray.getJSONObject(i).getString("id");
//            String songStatus = jsonArray.getJSONObject(i).getString("status");
//            String mess = "{" +
//                    "\"songName\":\"" + songName + "\"," +
//                    "\"songId\":\"" + songId + "\"," +
//                    "\"songStatus\":\"" + songStatus + "\"," +
//                    "\"album\":\"" + album.text().replace("\"", "").replace("\\", "") + "\"," +
//                    "\"albumId\":\"" + 85953546 + "\"" +
//                    "}";
//            System.out.println(mess);
//        }
    }

    public void getSongs(SongList songList,String url,int nID) throws IOException {

        Document doc2 = null;

        doc2 = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Cookie", "PSTM=1585406681; BAIDUID=00A0D3A4B32701F5EAB781C9F1005BF9:FG=1; BIDUPSID=711F8227AD3E8C231C3348346A47ADA5")
                .header("Referer", "https://music.163.com/discover/artist/cat?id=1001&initial=65")
                .header("Upgrade-Insecure-Requests", "1")
                .method(Connection.Method.GET)
                .timeout(200000).get(); // 设置请求头等信息，模拟人工访问，超时时间可自行设置

        File a2 = new File("songs.html");
        if (!a2.exists())
            a2.createNewFile();
        BufferedWriter bfWrite2 = new BufferedWriter(new FileWriter(a2));
        bfWrite2.write(String.valueOf(doc2));

        String type="";int i=0;
        Elements element2=doc2.select(".u-tag");
        for(Element element:element2){
            if(i!=0)type+="-";
            type+=element.text();
            i++;
            if(i==3) break;
        }
        String introduction=doc2.select("#album-desc-more").text().replaceFirst("介绍：","");
         //<p id="album-desc-more" class="intr f-brk"><b>介绍：</b> 喉咙唱的沙哑，也吻不到你啊<br /> </p>

        songList.setStyle(type);
        songList.setIntroduction(introduction);
        songListService.addSongList(songList);

        Elements elements=doc2.select(".n-songtb li a");
        int ii=0;
        for(Element element:elements){
            String href=element.attr("href");
            String id=href.substring(href.indexOf('=')+1);
            if(songService.selectByPrimaryKey(Integer.valueOf(id))) continue;
            getSong("https://music.163.com/song?id="+id);

            ListSong listSong=new ListSong();
            listSong.setSongListId(nID);
            listSong.setSongId(Integer.valueOf(id));
            listSongService.addListSong(listSong);
            ii++;
            if(ii>=15)
                break;
        }
    }
    public void getSong(String url) throws IOException {

        Document doc3 = null;

        doc3 = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Cookie", "PSTM=1585406681; BAIDUID=00A0D3A4B32701F5EAB781C9F1005BF9:FG=1; BIDUPSID=711F8227AD3E8C231C3348346A47ADA5")
                .header("Referer", "https://music.163.com/discover/artist/cat?id=1001&initial=65")
                .header("Upgrade-Insecure-Requests", "1")
                .method(Connection.Method.GET)
                .timeout(200000).get(); // 设置请求头等信息，模拟人工访问，超时时间可自行设置

        File a3 = new File("song.html");
        if (!a3.exists())
            a3.createNewFile();
        BufferedWriter bfWrite3 = new BufferedWriter(new FileWriter(a3));
        bfWrite3.write(String.valueOf(doc3));
        //https://music.163.com/#/song?id=1428668442 http://music.163.com/api/song/lyric?id=1435996552&lv=1&kv=1&tv=-1
        Integer id= Integer.valueOf(url.substring(url.indexOf('=')+1));
        String nameScr=doc3.select(".tit .f-ff2").text();
        String name=nameScr.length()>45?nameScr.substring(0,45):nameScr;
        String pic=doc3.select(".u-cover.u-cover-6.f-fl img").attr("data-src");
        String sUrl="https://music.163.com/song/media/outer/url?id="+id;

        String json="";
        URL urlObject = new URL("http://music.163.com/api/song/lyric?id="+id+"&lv=1&kv=1&tv=-1");
        URLConnection uc = urlObject.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String inputLine = null;
        while ( (inputLine = in.readLine()) != null) {
            json+=inputLine;
        }
        in.close();
        JSONObject lrc1=JSON.parseObject(json);
        String lrc="";
        if(lrc1.getJSONObject("lrc")!=null)
            lrc=lrc1.getJSONObject("lrc").getString("lyric");
        else
            lrc="[00:00.000] 暂无歌词";

        String singerDoc=doc3.select(".cnt .s-fc7").get(0).attr("href");
        if(singerDoc!="") {
            Integer singerId = Integer.valueOf(singerDoc.substring(singerDoc.indexOf('=') + 1));
            //String lyric=http://music.163.com/api/song/lyric?id=1435996552&lv=1&kv=1&tv=-1
            Song song = new Song();
            song.setName(name);
            song.setId(id);
            song.setPic(pic);
            song.setUrl(sUrl);
            song.setSingerId(singerId);
            song.setLyric(lrc);
            getSinger("https://music.163.com/artist/desc?id=" + singerId,song);
        }
    }

    public void getSinger(String url,Song song) throws IOException {
        Integer id= Integer.valueOf(url.substring(url.indexOf('=')+1));
        if(singerService.selectByPrimaryKey(id)) return;

        Document doc4 = null;

        doc4 = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Cookie", "PSTM=1585406681; BAIDUID=00A0D3A4B32701F5EAB781C9F1005BF9:FG=1; BIDUPSID=711F8227AD3E8C231C3348346A47ADA5")
                .header("Referer", "https://music.163.com/discover/artist/cat?id=1001&initial=65")
                .header("Upgrade-Insecure-Requests", "1")
                .method(Connection.Method.GET)
                .timeout(200000).get(); // 设置请求头等信息，模拟人工访问，超时时间可自行设置

        File a4 = new File("songer.html");
        if (!a4.exists())
            a4.createNewFile();
        BufferedWriter bfWrite4 = new BufferedWriter(new FileWriter(a4));
        bfWrite4.write(String.valueOf(doc4));

        String name=doc4.select("#artist-name").text();
        String pic=doc4.select(".n-artist.f-cb img").attr("src").split("\\?")[0]+"?param=520y520";

        song.setName(song.getName()+'-'+name);
        songService.addSong(song);
        Elements indrDoc=doc4.select(".n-artdesc");
        String indro=null;
        if(indrDoc.select("p").size()!=0) {
            indro = indrDoc.select("p").get(0).text();
        }
        else
            indro="暂无介绍";


        int sex=0;
        int indexF=0,indexM=0;
        indexM=indro.indexOf("女");
        indexF=indro.indexOf("男");
        if(indexF!=-1&&indexM!=-1){
            if(indexM<indexF)
                sex=0;
            else
                sex=1;
        }
        else if(indexF==-1&&indexM==-1)
            sex=3;
        else if(indexF==-1)
            sex=0;
        else if(indexM==-1)
            sex=1;
        String country="";
        indro = indro.length() > 255 ? indro.substring(0, 255) : indro;
        TreeMap<Integer,String> map=new TreeMap<Integer,String>();
        if(indro.contains("大陆"))
            map.put(indro.indexOf("大陆"),"大陆 ");
        if(indro.contains("韩国"))
        map.put(indro.indexOf("韩国"),"韩国");
        if(indro.contains("台湾"))
            map.put(indro.indexOf("台湾"),"台湾");
        if(indro.contains("香港"))
            map.put(indro.indexOf("香港"),"香港");
        if(indro.contains("美国"))
            map.put(indro.indexOf("美国"),"美国");
        if(indro.contains("马来西亚"))
            map.put(indro.indexOf("马来西亚"),"马来西亚");
        if(indro.contains("西班牙"))
            map.put(indro.indexOf("西班牙"),"西班牙");
        if(indro.contains("日本"))
            map.put(indro.indexOf("日本"),"日本");
        if(map.size()>1)
            country=map.firstEntry().getValue();
        Singer singer=new Singer();
        singer.setPic(pic);singer.setId(id);singer.setName(name);singer.setSex((byte) sex);singer.setIntroduction(indro);
        singerService.addSinger(singer);
    }
}