package edu.hitsz.dao.impl;

import edu.hitsz.dao.RankDao;
import edu.hitsz.dao.pojo.Rank;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * dao实现类
 */
public class RankDaoImpl implements RankDao {

    private List<Rank> ranks; // 用于存储排行榜数据
    private String filePath = "players.txt";

    public RankDaoImpl() {
        ranks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    // 从文件中读取数据并创建 Rank 对象
                    String name = data[0].trim();
                    int score = Integer.parseInt(data[1].trim());
                    String time = data[2].trim();
                    Rank rank = new Rank(name, score, time);
                    ranks.add(rank);
                } else {
                    System.err.println("行数据格式错误: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将排行榜数据转换为二维字符串数组
     * @return 包含排行榜数据的二维字符串数组
     */
    @Override
    public String[][] toArray() {
        String[][] rankData = new String[ranks.size()][];
        int i = 0;
        for (Rank rank : ranks) {
            rankData[i] = new String[]{String.valueOf(i + 1), rank.getName(), String.valueOf(rank.getScore()), rank.getTime()};
            i++;
        }
        return rankData;
    }

    /**
     * 按分数降序对排行榜进行排序
     */
    @Override
    public List<Rank> select() {
        ranks.sort(Comparator.comparing(Rank::getScore).reversed());
        return ranks;
    }

    /**
     * 删除一条排行记录
     * @param rank 要删除的排行记录
     */
    @Override
    public void delete(Rank rank) {
        ranks.remove(rank);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,false))) {
            for (Rank rank1 : ranks) {
                String line = rank1.getName() + "," + rank1.getScore()+","+rank1.getTime();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条新的排行记录
     * @param rank 要添加的排行记录
     */
    @Override
    public void add(Rank rank) {
        ranks.add(rank);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {
            String line = rank.getName() + "," + rank.getScore() + "," + rank.getTime();
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据ID查询排行记录
     * @param id 要查询的记录的ID
     * @return 对应的排行记录
     */
    @Override
    public Rank selectById(int id) {
        return ranks.get(id);
    }
}