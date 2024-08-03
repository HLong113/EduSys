/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.dao;

import com.edusys.entify.HocVien;
import com.edusys.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dino
 */
public class HocVienDAO extends EduSysDAO<HocVien, Integer> {

    final String INSERT_SQL = "INSERT INTO HocVien (MaHV, MaKH, MaNH, Diem) values (?,?,?,?) ";
    final String UPDATE_SQL = "UPDATE HocVien SET MaKH = ?, MaNH = ?, Diem = ? WHERE MaHV = ?";
    final String DELETE_SQL = "delete from HocVien where MaHV=?";
    final String SELECT_ALL_SQL = "select * from HocVien";
    final String SELECT_BY_ID_SQL = "select* from HocVien where MaHV = ?";
    final String SELECT_BY_KHOA_HOC_SQL = "select* from HocVien where MaKH = ?";

    @Override
    public void insert(HocVien entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaHV(),entity.getMaKH(), entity.getMaNH(), entity.getDiem());
    }

    @Override
    public void update(HocVien entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getMaKH(), entity.getMaNH(), entity.getDiem(), entity.getMaHV());
    }

    @Override
    public void delete(Integer id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<HocVien> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public HocVien selectById(Integer id) {
        List<HocVien> list = selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HocVien> selectBySql(String sql, Object... args) {
        List<HocVien> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                HocVien entity = new HocVien();
                entity.setMaKH(rs.getInt("MaKH"));
                entity.setMaHV(rs.getInt("MaHV"));
                entity.setMaNH(rs.getString("MaNH"));
                entity.setDiem(rs.getDouble("Diem"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<HocVien> selectByKhoaHoc(int MaKH) {
        String sql = "SELECT * FROM HocVien WHERE MaKH=?";
        return this.selectBySql(sql, MaKH);
    }
}
