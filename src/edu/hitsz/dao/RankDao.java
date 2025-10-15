package edu.hitsz.dao;

import edu.hitsz.dao.pojo.Rank;

import java.util.List;

/**
 * 数据访问对象接口
 */
public interface RankDao {

    List<Rank> select();

    void add(Rank rank);
}
