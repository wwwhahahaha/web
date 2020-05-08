package com.example.demo.service.impl;

import com.example.demo.controller.SongController;
import com.example.demo.dao.SingerMapper;
import com.example.demo.dao.SongListMapper;
import com.example.demo.dao.SongMapper;
import com.example.demo.domain.Song;
import com.example.demo.domain.SongList;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
class SpiderImplTest {
    @Autowired
    SpiderImpl test;

    @Autowired
    SongMapper songMapper;

    @Autowired
    SingerMapper singerMapper;

    @Autowired
    SongListServiceImpl songListService;

    @Autowired
    SongListMapper songListMapper;
    @Test
    void getSongsTest() throws IOException {
        //test.getData("https://music.163.com/artist/album?id=10559");
        //test.getDocument("https://music.163.com/discover/playlist");
        test.getAlbum("https://music.163.com/discover/playlist");
        //SongController songController=new SongController();
        //test.getSongs(new SongList(),"https://music.163.com/playlist?id=4882598073",882598073);
        //getSong("https://music.163.com/song?id=1428668442");
        //test.getSinger("https://music.163.com/artist/desc?id=3090");
        //songMapper.selectByPrimaryKey(1);
        //songListMapper.selectByPrimaryKey(1);
        //test.getSongs(new SongList(),"https://music.163.com/playlist?id=740666719",94646186);
        //Set<SongList> a=songListService.allSongListByConsumer(3);
        //System.out.println(1);
    }

//    @Test
//    void test(){
//        List<Song>allSongs=songMapper.allSong();
//        for(Song a:allSongs){
//            String b=a.getName();
//            String c=singerMapper.selectByPrimaryKey(a.getSingerId()).getName();
//            if(b.contains("-"))
//                b=b.split("-")[0];
//            String d=b+"-"+c;
//            if(d.length()>45)
//                d=d.substring(0,45);
//            a.setName(d);
//            System.out.println(a.getName());
//            songMapper.updateSongMsg(a);
//        }
//        //songMapper.deleteAllSong();
//
//    }
}