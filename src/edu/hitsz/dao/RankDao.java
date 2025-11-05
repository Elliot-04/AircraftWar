package edu.hitsz.dao;

import edu.hitsz.dao.pojo.Rank;

import java.util.List;

/**
 * 数据访问对象接口
 */
public interface RankDao {

    String[][] toArray();

    List<Rank> select();

    void delete(Rank rank);

    void add(Rank rank);

    Rank selectById(int id);
}
