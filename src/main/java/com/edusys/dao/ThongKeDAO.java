/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.dao;

import com.edusys.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pc
 */
public class ThongKeDAO {
    //    private List<Object[]> getListOfArray(String sql, String[] cols, Object... args) {
//        try {
//            List<Object[]> list = new ArrayList<>();
//            ResultSet rs = jdbcHelper.query(sql, args);
//            while (rs.next()) {
//                Object[] vals = new Object[cols.length];
////                for (int i = 0; i < cols.length; i++) {
////                    vals[i] = rs.getObject(cols[i]);
////                }
//                for (int i = 0; i < cols.length; i++) {
//                    vals[i] = rs.getObject(cols[i]);
//                }
//                list.add(vals);
//            }
//            rs.getStatement().getConnection().close();
//            return list;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    private List<Object[]> getListofArray(String sql, String[] cols, Object... args) {
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = JdbcHelper.query(sql, args);
            while (rs.next()) {
                Object[] vals = new Object[cols.length];
                for (int i = 0; i < cols.length; i++) {
                    vals[i] = rs.getObject(cols[i]);
                }

                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

public List<Object[]> getBangDiem(Integer MaKH) {
    String sql = "{CALL sp_BangDiem(?)}";
    String[] cols = {"MaNH", "HoTen", "Diem"};
    return getListofArray(sql, cols, MaKH); // Pass MaKH as a parameter
}

    public List<Object[]> getLuongNguoiHoc() {
        String sql = "{CALL sp_LuongNguoiHoc}";
        String[] cols = {"Nam", "SoLuong", "DauTien", "CuoiCung"};
        return getListofArray(sql, cols);
    }

    public List<Object[]> getDiemChuyenDe() {
        String sql = "{CALL sp_DiemChuyenDe}";
        String[] cols = {"ChuyenDe", "SoHV", "ThapNhat", "CaoNhat", "TrungBinh"};
        return getListofArray(sql, cols);
    }

    public List<Object[]> getDoanhThu(Integer Nam) {
        String sql = "{Call sp_DoanhThu(?)}";
        String[] cols = {"ChuyenDe", "SoKH", "SoHV", "DoanhThu", "ThapNhat", "CaoNhat", "TrungBinh"};
//        return getListOfArray(sql, cols);
        return getListofArray(sql, cols);
    }
}
