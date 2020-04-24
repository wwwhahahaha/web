package com.example.demo.service.impl;

import com.example.demo.dao.RankMapper;
import com.example.demo.domain.Rank;
import com.example.demo.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private RankMapper rankMapper;

    @Override
    public int rankOfSongListId(Long songListId) {
        int countId=rankMapper.selectRankNum(songListId);;
        return countId!=0?rankMapper.selectScoreSum(songListId) / countId:countId;
    }

    @Override
    public boolean addRank(Rank rank) {

        return rankMapper.insertSelective(rank) > 0 ? true:false;
    }

    public List<Integer> getSongListsByCsmid(Integer id){
        List<Integer> songs=new LinkedList<>();
        List<Integer> a=rankMapper.songListOfConsumerId(id);
        int i=0;
        for(Integer songId:a){
            List<Integer> b=rankMapper.consumerOfSongList(songId);
            int j=0;
            for(Integer cusId:b){
                List<Integer> c=rankMapper.songListOfConsumerId(cusId);
                if(++j==2)
                    break;
                songs.addAll(c);
            }
            if(++i==5)
                break;
        }
        return songs;
    }
}
