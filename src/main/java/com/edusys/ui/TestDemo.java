/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.ui;

import com.edusys.dao.NguoiHocDAO;
import com.edusys.dao.NhanVienDAO;
import com.edusys.dao.ThongKeDAO;
import com.edusys.entify.NhanVien;
import java.util.List;


/**
 *
 * @author pc
 */
public  class TestDemo {
    public static void main(String[] args) {
//        NhanVienDAO dao = new NhanVienDAO();
//        dao.insert(new NhanVien("longnv","Le Hoang Long","admin123",true));
        ThongKeDAO tkDao = new ThongKeDAO();
        List<Object[]> list = tkDao.getBangDiem(1);
        for (Object[] obj : list) {
            System.out.println("=>"+obj[0]+"-"+obj[1]+"-"+obj[2]);
        }
    }
}
