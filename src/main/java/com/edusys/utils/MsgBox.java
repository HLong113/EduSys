/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author pc
 */
public class MsgBox {//hiển thị thông báo
     public static void alter(Component parent, String message){
        JOptionPane.showMessageDialog(parent, message, "Hệ thống quản lí đào tạo", JOptionPane.INFORMATION_MESSAGE);
    }
    public static boolean confirm(Component parent, String message){
        int result = JOptionPane.showConfirmDialog(parent, message, "Hệ thống quản lí đào tạo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    public static String prompt(Component parent, String message){//yêu cầu nhập dữ liệu
        return JOptionPane.showInputDialog(parent, message, "Hệ thống quản lí đào tạo", JOptionPane.INFORMATION_MESSAGE);
    }
}
