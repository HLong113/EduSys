/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.dao;

import com.edusys.entify.NguoiHoc;
import com.edusys.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pc
 */
public class NguoiHocDAO extends EduSysDAO<NguoiHoc, String> {

    final String INSERT_SQL = "insert into NguoiHoc (MaNH, HoTen, NgaySinh, GioiTinh, DienThoai, Email, GhiChu, MaNV, NgayDK) values (?,?,?,?,?,?,?,?,?) ";
    final String UPDATE_SQL = " update NguoiHoc set HoTen = ?, NgaySinh = ?, GioiTinh =?, DienThoai=?, Email =?, GhiChu =?, MaNV =?, NgayDK= ? where MaNH = ?";
    final String DELETE_SQL = "DELETE from  NguoiHoc where MaNH =?";
    final String SELECT_ALL_SQL = "select * from NguoiHoc";
    final String SELECT_BY_ID_SQL = "select* from NguoiHoc where MaNH = ?";
    final String SELECT_NOT_IN_COURSE_SQL = "select* from NguoiHoc where HoTen like? and MaNH not in (Select MaNH from HocVien where MaKH =?)";
//    final String SELECT_BY_KEYWORD = "select* from NguoiHoc where HoTen like ? and MaNH NOT IN (Select  MaNH from HocVien where MaKH = ?)";
    final String SELECT_BY_KEYWORD = "select* from NguoiHoc where HoTen like ? ";
    final String SELECT_NOT_IN_COURSE = "select * from NguoiHoc where MaNH NOT IN (Select MaNH from HocVien where MaKH = ?)";

    String SELECT_NOT_IN_COURSE2 = "SELECT * FROM NGUOIHOC WHERE HoTen LIKE ? AND "
            + "MaNH NOT IN (SELECT MaNH FROM HOCVIEN WHERE MaKH = ?)";

    @Override
    public void insert(NguoiHoc entity) {
        JdbcHelper.update(INSERT_SQL, entity.getMaNH(), entity.getHoTen(), entity.getNgaySinh(), entity.isGioiTinh(), entity.getDienThoai(), entity.getEmaill(), entity.getGhiChu(), entity.getMaNV(), entity.getNgayDK());
    }

    @Override
    public void update(NguoiHoc entity) {
        JdbcHelper.update(UPDATE_SQL, entity.getHoTen(), entity.getNgaySinh(), entity.isGioiTinh(), entity.getDienThoai(), entity.getEmaill(), entity.getGhiChu(), entity.getMaNV(), entity.getNgayDK(), entity.getMaNH());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public List<NguoiHoc> selectAll() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public NguoiHoc selectById(String id) {
        List<NguoiHoc> list = selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NguoiHoc> selectBySql(String sql, Object... args) {
        List<NguoiHoc> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                NguoiHoc entity = new NguoiHoc();
                entity.setMaNH(rs.getString("MaNH"));
                entity.setHoTen(rs.getString("HoTen"));
                entity.setNgaySinh(rs.getDate("NgaySinh"));
                entity.setGioiTinh(rs.getBoolean("GioiTinh"));
                entity.setDienThoai(rs.getString("DienThoai"));
                entity.setEmaill(rs.getString("Email"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setMaNV(rs.getString("MaNV"));
                entity.setNgayDK(rs.getDate("NgayDK"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
//    public List<NguoiHoc> selectNotInCourse(int MaKH, String keyword) {
//        String sql = "SELECT * FROM NguoiHoc"
//                + "Where HoTen LIKE? AND"
//                + "MaNH NOT IN (SELECT MaNH FROM HocVien WHERE MaKH=?)";
//        return this.selectBySql(sql, "%" + keyword + "%", MaKH);
//    }

public List<NguoiHoc> selectNotInCourse(int maKH) {
    String sql = "SELECT * FROM NguoiHoc WHERE MaNH NOT IN (Select MaNH FROM HocVien WHERE MaKH = ?)";
		return this.selectBySql(sql, maKH);
	}
    public List<NguoiHoc> selectByKeyWord(int MaKH, String keyword) {
        return this.selectBySql(SELECT_BY_KEYWORD, "%" + keyword + "%", MaKH);

    }

    public List<NguoiHoc> selectByKeyWord2(String keyword) {
        return this.selectBySql(SELECT_BY_KEYWORD, "%" + keyword + "%");

    }

    public List<NguoiHoc> selectNotlnCourse2(int makh, String keywork) {
        return this.selectBySql(SELECT_NOT_IN_COURSE2, "%" + keywork + "%", makh);
    }

//    public List <NguoiHoc> selectNotInCourse2 (int maKH, String keyword){
//        String sql = "SELECT * from NguoiHoc" 
//                + "Where HoTen LIKE ? AND"
//                +"MaNH NOT IN (Select MaNH FROM HocVien WHERE MaKH= ?)";
//        return this.selectBySql(sql, "%"+keyword+"%", maKH);
//    }
}
