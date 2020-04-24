package com.example.demo.service.impl;

import com.example.demo.dao.SongListMapper;
import com.example.demo.domain.Collect;
import com.example.demo.domain.SongList;
import com.example.demo.service.SongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SongListServiceImpl implements SongListService {

    @Autowired
    private SongListMapper songListMapper;

    @Autowired
    private RankServiceImpl rankService;

    public boolean selectByPrimaryKey(Integer id){
        return songListMapper.selectByPrimaryKey(id)!=null ?true:false;
    }
    @Override
    public boolean updateSongListMsg(SongList songList) {
        return songListMapper.updateSongListMsg(songList) >0 ?true:false;
    }

    @Override
    public boolean deleteSongList(Integer id) {
        return songListMapper.deleteSongList(id) >0 ?true:false;
    }

    @Override
    public List<SongList> allSongList()
    {
        List<SongList> list=songListMapper.allSongList();
        Collections.shuffle(list);
        return list;
    }

    public Set<SongList> allSongListByConsumer(Integer csmId){
        Set<SongList> songListR=new HashSet<SongList>();
        List<Integer> songListId=rankService.getSongListsByCsmid(csmId);
        for(Integer item:songListId)
            songListR.add(songListMapper.selectByPrimaryKey(item));
        if(songListR.size()>=10)
            return songListR;
        else {
            int b=(int)(Math.random()*25);
            List<SongList> a=songListMapper.songListBylimt(b,10);
            songListR.addAll(a);
            return songListR;
        }
    }

    @Override
    public List<SongList> likeTitle(String title)
    {
        return songListMapper.likeTitle(title);
    }

    @Override
    public List<SongList> likeStyle(String style)
    {
        return songListMapper.likeStyle(style);
    }

    @Override
    public List<SongList> songListOfTitle(String title)
    {
        return songListMapper.songListOfTitle(title);
    }

    @Override
    public boolean addSongList(SongList songList)
    {
        return songListMapper.insertSelective(songList) > 0?true:false;
    }

    @Override
    public boolean updateSongListImg(SongList songList) {

        return songListMapper.updateSongListImg(songList) >0 ?true:false;
    }
}
