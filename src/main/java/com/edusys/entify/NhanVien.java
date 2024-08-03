/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.entify;

/**
 *
 * @author pc
 */
public class NhanVien {

    private String maNV;
    private String HoTen;
    private String MatKhau;
    private boolean VaiTro;

    public NhanVien() {
    }

    public NhanVien(String maNV, String HoTen, String MatKhau, boolean VaiTro) {
        this.maNV = maNV;
        this.HoTen = HoTen;
        this.MatKhau = MatKhau;
        this.VaiTro = VaiTro;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String HoTen) {
        this.HoTen = HoTen;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String MatKhau) {
        this.MatKhau = MatKhau;
    }

    public boolean isVaiTro() {
        return VaiTro;
    }

    public void setVaiTro(boolean VaiTro) {
        this.VaiTro = VaiTro;
    }

    @Override
    public String toString() {
        return this.HoTen;
    }

}
