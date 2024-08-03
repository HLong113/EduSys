/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.edusys.ui;

import com.edusys.dao.NguoiHocDAO;
import com.edusys.entify.NguoiHoc;
import com.edusys.utils.Auth;
import com.edusys.utils.MsgBox;
import com.edusys.utils.XDate;
import com.edusys.utils.XImage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class NguoiHocJDialog extends javax.swing.JDialog {
  NguoiHocDAO dao = new NguoiHocDAO();
    int row = 0;

    /**
     * Creates new form NguoiHocJDialog
     */
    public NguoiHocJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    void init() {
        setIconImage(XImage.getAppIcon());
        setLocationRelativeTo(null);
        setTitle("Edusys - Quản lý Người Học");
        fillTable();
        updateStatus();
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tbl_Danhsach.getModel();
        model.setRowCount(0);
        try {
            String keyword = txt_timkiem.getText();
            List<NguoiHoc> list = dao.selectByKeyWord2(keyword);
            for (NguoiHoc nh : list) {
                Object[] row = {
                    nh.getMaNH(),
                    nh.getHoTen(),
                    nh.isGioiTinh() ? "Nam" : "Nữ",
                    XDate.toString(nh.getNgaySinh(), "dd-MM-yyyy"),
                    nh.getDienThoai(),
                    nh.getEmaill(),
                    nh.getMaNV(),
                    XDate.toString(nh.getNgayDK(), "dd-MM-yyyy")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            MsgBox.alter(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void updateStatus() {
        boolean edit = this.row >= 0;
        boolean first = this.row == 0;
        boolean last = this.row == tbl_Danhsach.getRowCount() - 1;
        txt_maNHoc.setEditable(!edit);
        // khi insert thì không update , delete
        btn_them.setEnabled(!edit);
        btn_sua.setEnabled(edit);
        btn_xoa.setEnabled(edit);

        btn_first.setEnabled(edit && !first);
        btn_prev.setEnabled(edit && !first);
        btn_next.setEnabled(edit && !last);
        btn_last.setEnabled(edit && !last);
    }

    void setForm(NguoiHoc model) {
        txt_maNHoc.setText(model.getMaNH());
        txt_HoTen.setText(model.getHoTen());
        rdoNam.setSelected(model.isGioiTinh());
        rdoNu.setSelected(!model.isGioiTinh());
        txt_ngaysinh.setText(XDate.toString(model.getNgaySinh(), "dd-MM-yyyy"));
        txt_dienthoai.setText("0" + model.getDienThoai());
        txt_email.setText(model.getEmaill());
        txt_ghichu.setText(model.getGhiChu());

    }
    Date now = new Date();

    NguoiHoc getForm() {
        Date date1 = XDate.toDate(txt_ngaysinh.getText(), "dd-MM-yyyy");
        String date2 = XDate.toString(date1, "yyyy-MM-dd");

        NguoiHoc model = new NguoiHoc();
        model.setMaNH(txt_maNHoc.getText());
        model.setHoTen(txt_HoTen.getText());
        model.setGioiTinh(rdoNam.isSelected());
        model.setNgaySinh(XDate.toDate(date2, "yyyy-MM-dd"));
        model.setDienThoai(txt_dienthoai.getText());
        model.setEmaill(txt_email.getText());

        model.setGhiChu(txt_ghichu.getText());
        model.setMaNV(Auth.user.getMaNV());
        model.setNgayDK(XDate.addDays(now, 0));
        return model;
    }

    void edit() {
        try {
            String maNH = (String) tbl_Danhsach.getValueAt(this.row, 0);
            NguoiHoc model = dao.selectById(maNH);
            if (model != null) {
                setForm(model);
                updateStatus();
                tabs.setSelectedIndex(0);
            }
        } catch (Exception e) {
            MsgBox.alter(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void clearForm() {
        txt_HoTen.setText("");
        txt_dienthoai.setText("");
        txt_email.setText("");
        txt_maNHoc.setText("");
        txt_ngaysinh.setText("");
        txt_ghichu.setText("");
        this.updateStatus();
        row = -1;
        updateStatus();
    }

    private void timKiem() {
        this.fillTable();
        this.clearForm();
        this.row = -1;
        updateStatus();
    }

    void insert() {
        if (validateForm()) {
            NguoiHoc model = getForm();
            try {
                dao.insert(model);
                this.fillTable();
                this.clearForm();
                MsgBox.alter(this, "Thêm mới thành công!");
            } catch (Exception e) {
                MsgBox.alter(this, "Thêm mới thất bại!");
                throw new RuntimeException(e);
            }
        }
    }

    void update() {
        if (validateForm()) {
            NguoiHoc model = getForm();
            try {
                dao.update(model);
                this.fillTable();
                this.clearForm();
                MsgBox.alter(this, "Cập nhật thành công!");
            } catch (Exception e) {
                MsgBox.alter(this, "Cập nhật thất bại!");
                throw new RuntimeException(e);
            }
        }
    }

    void delete() {
        if (!Auth.isManager()) {
            MsgBox.alter(this, "Bạn không có quyền xoá chuyên đề");
        } else {
            if (MsgBox.confirm(this, "Bạn thực sự muốn xoá chuyên đề này?")) {
                String maNH = txt_maNHoc.getText();
                try {
                    dao.delete(maNH);
                    this.fillTable();
                    this.clearForm();
                    MsgBox.alter(this, "Xoá thành công!");
                } catch (Exception e) {
                    MsgBox.alter(this, "Xoá thất bại!");
                    throw new RuntimeException(e);
                }
            }
        }
    }

//    private boolean validateForm() {
//        List<String> errors = new ArrayList<>();
//
//        if (txt_maNHoc.getText().isEmpty()) {
//            errors.add("Vui lòng nhập mã người học!");
//        }
//
//        if (txt_HoTen.getText().isEmpty()) {
//            errors.add("Vui lòng nhập họ tên người học!");
//    }
//    
//    if (txt_ngaysinh.getText().isEmpty()) {
//            errors.add("Vui lòng nhập ngày sinh người học!");
//        } else {
//            String ngaySinh = convertToValidFormat(txt_ngaysinh.getText());
//            if (!isValidDate(ngaySinh, "dd-MM-yyyy")) {
//                errors.add("Vui lòng nhập đúng định dạng ngày sinh (dd/MM/yyyy)!");
//            }
//            
//            if (txt_dienthoai.getText().isEmpty()) {
//         errors.add("Vui lòng nhập số điện thoại.");
//        return false;
//    }
//
//    // Kiểm tra trường email
//    String email = txt_email.getText();
//    if (email.isEmpty()) {
//        errors.add( "Vui lòng nhập email.");
//    }
//
//    // Kiểm tra email có chứa ký tự "@"
//    if (!email.contains("@")) {
//        errors.add("Email không hợp lệ.");
//                }
//    
//        }
//
//        // Các kiểm tra khác ở đây...
//        if (!errors.isEmpty()) {
//            String errorMsg = String.join("\n", errors);
//            MsgBox.alter(this, errorMsg);
//            return false;
//        }
//
//        return true;
//    }

    private boolean validateForm() {
    List<String> errors = new ArrayList<>();

    if (txt_maNHoc.getText().isEmpty()) {
        errors.add("Vui lòng nhập mã người học!");
    }

    if (txt_HoTen.getText().isEmpty()) {
        errors.add("Vui lòng nhập họ tên người học!");
    }

    if (txt_ngaysinh.getText().isEmpty()) {
        errors.add("Vui lòng nhập ngày sinh người học!");
    } else {
        String ngaySinh = convertToValidFormat(txt_ngaysinh.getText());
        if (!isValidDate(ngaySinh, "dd-MM-yyyy")) {
            errors.add("Vui lòng nhập đúng định dạng ngày sinh (dd/MM/yyyy)!");
        }
    }

    if (txt_dienthoai.getText().isEmpty()) {
        errors.add("Vui lòng nhập số điện thoại.");
    }

    // Kiểm tra trường email
    String email = txt_email.getText();
    if (email.isEmpty()) {
        errors.add("Vui lòng nhập email.");
    } else if (!email.contains("@")) {
        errors.add("Email không hợp lệ.");
    }

    // Các kiểm tra khác ở đây...
    if (!errors.isEmpty()) {
        String errorMsg = String.join("\n", errors);
        MsgBox.alter(this, errorMsg);
        return false;
    }

    return true;
}

    
    private String convertToValidFormat(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = dateFormat.parse(date);
            dateFormat.applyPattern("dd-MM-yyyy");
            return dateFormat.format(parsedDate);
        } catch (ParseException e) {
            return date;
        }
    }

    private boolean isValidDate(String date, String pattern) {
        try {
            XDate.toDate(date, pattern);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    void first() {
        row = 0;
        edit();
    }

    void prev() {
        if (row > 0) {
            row--;
            edit();
        }
    }

    void next() {
        if (row < tbl_Danhsach.getRowCount() - 1) {
            row++;
            edit();
        }
    }

    void last() {
        row = tbl_Danhsach.getRowCount() - 1;
        edit();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_maNHoc = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_HoTen = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_ngaysinh = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_dienthoai = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_ghichu = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        btn_moi = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btn_first = new javax.swing.JButton();
        btn_prev = new javax.swing.JButton();
        btn_next = new javax.swing.JButton();
        btn_last = new javax.swing.JButton();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txt_timkiem = new javax.swing.JTextField();
        btn_tim = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Danhsach = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 204));
        jLabel1.setText("QUẢN LÝ NGƯỜI HỌC");

        jLabel2.setText("Mã Người Học");

        txt_maNHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_maNHocActionPerformed(evt);
            }
        });

        jLabel3.setText("Họ Và Tên");

        jLabel4.setText("Giới Tính");

        jLabel5.setText("Ngày Sinh (dd-MM-yyyy)");

        jLabel6.setText("Điện Thoại");

        jLabel7.setText("ĐỊa Chỉ Email");

        jLabel8.setText("Ghi Chú");

        txt_ghichu.setColumns(20);
        txt_ghichu.setRows(5);
        jScrollPane2.setViewportView(txt_ghichu);

        jPanel6.setLayout(new java.awt.GridLayout(1, 4, 5, 0));

        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });
        jPanel6.add(btn_them);

        btn_sua.setText("Sửa");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });
        jPanel6.add(btn_sua);

        btn_xoa.setText("Xóa");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });
        jPanel6.add(btn_xoa);

        btn_moi.setText("Mới");
        btn_moi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_moiActionPerformed(evt);
            }
        });
        jPanel6.add(btn_moi);

        jPanel5.setLayout(new java.awt.GridLayout(1, 4, 5, 5));

        btn_first.setText("|<");
        btn_first.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_firstActionPerformed(evt);
            }
        });
        jPanel5.add(btn_first);

        btn_prev.setText("<<");
        btn_prev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_prevActionPerformed(evt);
            }
        });
        jPanel5.add(btn_prev);

        btn_next.setText(">>");
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });
        jPanel5.add(btn_next);

        btn_last.setText(">|");
        btn_last.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_lastActionPerformed(evt);
            }
        });
        jPanel5.add(btn_last);

        rdoNam.setText("Nam");

        rdoNu.setText("Nữ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(txt_HoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(247, 247, 247)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txt_dienthoai, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_maNHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rdoNam, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoNu, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(115, 115, 115)
                                .addComponent(txt_ngaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(262, 262, 262)
                                .addComponent(jLabel5)))))
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addGap(6, 6, 6)
                .addComponent(txt_maNHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addGap(6, 6, 6)
                .addComponent(txt_HoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel4))
                    .addComponent(jLabel5))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_ngaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoNam)
                    .addComponent(rdoNu))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel7)))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_dienthoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jLabel8)
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        tabs.addTab("Cập Nhật", jPanel1);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btn_tim.setText("Tìm");
        btn_tim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_timActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_timkiem)
                .addGap(18, 18, 18)
                .addComponent(btn_tim)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_tim))
                .addContainerGap())
        );

        tbl_Danhsach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã NH", "Họ Tên", "Giới Tính", "Ngày Sinh", "Điện Thoại", "Email", "Mã NV", "Ngày DK"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_Danhsach.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_DanhsachMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbl_DanhsachMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_Danhsach);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("Danh Sách", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_maNHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_maNHocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_maNHocActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        insert();
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        update();
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        delete();
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_moiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_moiActionPerformed
        clearForm();
    }//GEN-LAST:event_btn_moiActionPerformed

    private void btn_firstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_firstActionPerformed
        first();
    }//GEN-LAST:event_btn_firstActionPerformed

    private void btn_prevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_prevActionPerformed
        prev();
    }//GEN-LAST:event_btn_prevActionPerformed

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        next();
    }//GEN-LAST:event_btn_nextActionPerformed

    private void btn_lastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_lastActionPerformed
        last();
    }//GEN-LAST:event_btn_lastActionPerformed

    private void btn_timActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_timActionPerformed
        timKiem();
    }//GEN-LAST:event_btn_timActionPerformed

    private void tbl_DanhsachMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhsachMouseClicked
        //       if(evt.getClickCount()==2){
            //            this.row = tbl_Danhsach.getSelectedRow();
            //            this.edit();
            //        }
    }//GEN-LAST:event_tbl_DanhsachMouseClicked

    private void tbl_DanhsachMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_DanhsachMousePressed
        if (evt.getClickCount() == 2) {
            this.row = tbl_Danhsach.getSelectedRow();
            this.edit();
        }
    }//GEN-LAST:event_tbl_DanhsachMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NguoiHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NguoiHocJDialog dialog = new NguoiHocJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_first;
    private javax.swing.JButton btn_last;
    private javax.swing.JButton btn_moi;
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_prev;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_tim;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tbl_Danhsach;
    private javax.swing.JTextField txt_HoTen;
    private javax.swing.JTextField txt_dienthoai;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextArea txt_ghichu;
    private javax.swing.JTextField txt_maNHoc;
    private javax.swing.JTextField txt_ngaysinh;
    private javax.swing.JTextField txt_timkiem;
    // End of variables declaration//GEN-END:variables
}
