package com.shaoqin.fruit.dao.impl;

import com.shaoqin.fruit.dao.FruitDAO;
import com.shaoqin.fruit.dao.base.BaseDAO;
import com.shaoqin.fruit.pojo.Fruit;

import java.util.List;

/**
 * ClassName: FruitDao
 * Package: com.shaoqin.dao.impl
 * Description:
 * Author Shaoqin
 * Create 6/15/23 12:57 AM
 * Version 1.0
 */
public class FruitDaoImpl extends BaseDAO implements FruitDAO {

    @Override
    public List<Fruit> getFruitList() {
        return super.queryForList(Fruit.class, "select * from fruit");
    }

    @Override
    public List<Fruit> getFruitList(Integer pageNo, Integer pageSize) {
        return super.queryForList(Fruit.class, "select * from fruit limit ?, ?", pageSize * (pageNo - 1), pageSize);
    }

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo, Integer pageSize) {
        String sql = "select * from fruit where name like ? or remark like ? limit ?, ?";
        return super.queryForList(Fruit.class, sql, "%" + keyword + "%", "%" + keyword + "%", pageSize * (pageNo - 1), pageSize);
    }

    @Override
    public Fruit getFruitById(Integer id) {
        return (Fruit) super.queryForOne(Fruit.class, "select * from fruit where id = ?", id);
    }

    @Override
    public void updateFruit(Fruit fruit) {
        String sql = "update fruit set name = ?, price = ?, count = ?, remark = ? where id = ?";
        super.update(sql, fruit.getName(),fruit.getPrice(), fruit.getCount(), fruit.getRemark(), fruit.getId());
    }

    @Override
    public void deleteFruit(Integer id) {
        super.update("delete from fruit where id = ?", id);
    }

    @Override
    public void addFruit(Fruit fruit) {
        String sql = "insert into fruit (name, price, count, remark) values(?,?,?,?)";
        super.update(sql, fruit.getName(), fruit.getPrice(), fruit.getCount(), fruit.getRemark());
    }

    @Override
    public Integer getFruitCount() {
        return ((Long) super.queryForSingleValue("select count(*) from fruit")).intValue();
    }

    @Override
    public Integer getFruitCount(String keyword) {
        String sql = "select count(*) from fruit where name like ? or remark like ?";
        return ((Long) super.queryForSingleValue(sql, "%" + keyword + "%", "%" + keyword + "%")).intValue();
    }

}
