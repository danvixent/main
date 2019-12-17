/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.mysql.jdbc.PacketTooBigException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DANIEL
 */
public class MainWindow extends javax.swing.JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int _id = 0;
    private String _name = "";
    private float _price = 0;
    private Date _addDate = null;
    private SimpleDateFormat dteFmt = new SimpleDateFormat("yyyy-MM-dd");
    private Connection con = getConnection();

    public MainWindow() {
        initComponents();
        showProducts();
    }

    /*
     *Connect to the Database
     */
    public Connection getConnection() {
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/CA6WLzGRoj", "CA6WLzGRoj", "T1cTUvhvce");
            JOptionPane.showMessageDialog(null, "Connected.");
            return con;
        } catch (final CommunicationsException ce) {
            JOptionPane.showMessageDialog(null, "Unable to connect: Check Your Server");
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ce);
            return null;
        }catch (final SQLException e) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    String imgPath = "";
    int pos = -1;

    /*
     * Resizes the Image chosen by the user
     * @param impPath the Image File path
     */
    ImageIcon resizeImg(String impPath, byte[] pic) {
        ImageIcon myimg = null;

        if (impPath != null) {
            myimg = new ImageIcon(impPath);
            imgPath = impPath;
        } else {
            myimg = new ImageIcon(pic);
        }

        Image img = myimg.getImage();
        Image img2 = img.getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH);// error
                                                                                                            // here
        ImageIcon image = new ImageIcon(img2);
        return image;
    }

    // Display Data in JTable
    // 1 - Fill ArrayList With the Data
    private ArrayList<Product> getProductList() {
        try {
            ArrayList<Product> prodList = new ArrayList<>();

            String qry = "SELECT * FROM products";

            Statement stm = null;
            ResultSet rst = null;
            Product prod = null;

            stm = con.createStatement();
            rst = stm.executeQuery(qry);

            while (rst.next()) {
                prod = new Product(rst.getInt("ID"), rst.getString("Name"), rst.getFloat("Price"),
                        rst.getDate("DateAdded"), rst.getBytes("Image"));
                prodList.add(prod);

            }
            return prodList;

        } catch (SQLException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
    }

    // 2 - Populate the JTable
    void showProducts() {
        ArrayList<Product> list = getProductList();
        DefaultTableModel model = (DefaultTableModel) Prod_Table.getModel();
        // clear table Content
        model.setRowCount(0);
        Object[] row = new Object[4];
        for (int i = 0; i < list.size(); i++) {

            row[0] = list.get(i).getId();
            row[1] = list.get(i).getName();
            row[2] = list.get(i).getPrice();
            row[3] = list.get(i).getDateAdded().toString(); // err possible

            model.addRow(row);
        }
        Prod_Table.setModel(model);
    }

    /*
     * Translates the row Information to all fileds
     * @param index the position of the row
     */
    void showItemInFields(int index) {
        idField.setText(Integer.toString(getProductList().get(index).getId()));
        nameField.setText(getProductList().get(index).getName());
        priceField.setText(Float.toString(getProductList().get(index).getPrice()));
        addDateField.setDate(getProductList().get(index).getDateAdded());
        lbl_image.setIcon(resizeImg(null, getProductList().get(index).getPicture()));
    }

    /*
     * Clears all fields and refreshes variables
     */
    void clearFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        addDateField.setDate(null);
        lbl_image.setIcon(null);
        refreshAllVariables();
    }

    /*
     * After any selection refresh all Variables
     */
    private void refreshAllVariables() {
        try {
            _id = Integer.parseInt(idField.getText());
            _name = nameField.getText();
            _price = Float.parseFloat(priceField.getText());
            _addDate = addDateField.getDate();
        } catch (NumberFormatException e) {
        }
    }

    /*
     * Check input without image...for updating data
     */
    boolean inputCheck() {
        return !("".equals(_name) || _price == 0 || _addDate == null);
    }

    /*
     * Check input image...for inserting data
     */
    boolean inputWithImgCheck() {
        return !("".equals(_name) || _price == 0 || _addDate == null || "".equals(imgPath));
    }

    boolean idCheck() {
        return !(_id == 0);
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        priceField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbl_image = new javax.swing.JLabel();
        addDateField = new com.toedter.calendar.JDateChooser();
        Btn_Choose_Image = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Prod_Table = new javax.swing.JTable();
        insertButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        firstButton = new javax.swing.JButton();
        lstButton = new javax.swing.JButton();
        prevButton = new javax.swing.JButton();
        nxtButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Product App");
        setBackground(java.awt.Color.lightGray);

        jPanel1.setBackground(java.awt.Color.white);
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Hack", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("ID:");

        idField.setFont(new java.awt.Font("Hack", 0, 14)); // NOI18N
        idField.setToolTipText("");
        idField.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        idField.setEnabled(false);
        idField.setInputVerifier(new InputVerifier() {
            public boolean verify(JComponent input) {
                final JTextComponent source = (JTextComponent) input;
                String text = source.getText();
                if (!text.equals("")) {
                    try {
                        Integer.parseInt(text);
                        return true;
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(source, "Enter Numeric Input", "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                } // Possible Error
                else {
                    return true;
                }
            }
        });
        idField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                idFieldFocusLost(evt);
            }
        });
        idField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idFieldActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Hack", 0, 18)); // NOI18N
        jLabel2.setText("Name:");

        nameField.setFont(new java.awt.Font("Hack", 0, 14)); // NOI18N
        nameField.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFieldFocusLost(evt);
            }
        });
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Hack", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Product Details.");

        jLabel4.setFont(new java.awt.Font("Hack", 0, 16)); // NOI18N
        jLabel4.setText("Price:");

        priceField.setFont(new java.awt.Font("Hack", 0, 14)); // NOI18N
        priceField.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        priceField.setInputVerifier(new InputVerifier() {
            public boolean verify(JComponent input) {
                final JTextComponent source = (JTextComponent) input;
                String text = source.getText();
                if (!text.equals("")) {
                    try {
                        Float.parseFloat(text);
                        return true;
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(source, "Enter Numeric Input", "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                } // Possible Error
                else {
                    return true;
                }
            }
        });
        priceField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                priceFieldFocusLost(evt);
            }
        });
        priceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceFieldActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Hack", 0, 16)); // NOI18N
        jLabel5.setText("Date Added:");

        jLabel6.setFont(new java.awt.Font("Hack", 0, 16)); // NOI18N
        jLabel6.setText("Image:");

        lbl_image.setBackground(new java.awt.Color(232, 241, 243));
        lbl_image.setOpaque(true);

        addDateField.setDateFormatString("yyyy-MM-dd");
        addDateField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                addDateFieldFocusLost(evt);
            }
        });

        Btn_Choose_Image.setText("Choose Image");
        Btn_Choose_Image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Choose_ImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(
                                jPanel1Layout.createSequentialGroup().addGap(40, 40, 40).addGroup(jPanel1Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 176,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(60, 60, 60)
                                                .addGroup(jPanel1Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(nameField).addComponent(priceField)))))
                                .addGroup(jPanel1Layout.createSequentialGroup().addGap(39, 39, 39)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 70,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48,
                                                Short.MAX_VALUE)
                                        .addComponent(lbl_image, javax.swing.GroupLayout.PREFERRED_SIZE, 179,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 33, 33))
                .addGroup(jPanel1Layout.createSequentialGroup().addGap(113, 113, 113).addComponent(jLabel3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                        jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Btn_Choose_Image).addGap(66, 66, 66))
                .addGroup(jPanel1Layout.createSequentialGroup().addGap(20, 20, 20).addComponent(jLabel5)
                        .addGap(30, 30, 30).addComponent(addDateField, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(27, 27, 27)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 30,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(priceField, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(addDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl_image, javax.swing.GroupLayout.PREFERRED_SIZE, 159,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Btn_Choose_Image).addContainerGap(35, Short.MAX_VALUE)));

        Prod_Table.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

        }, new String[] { "ID", "Name", "Price", "Date Added" }));
        Prod_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Prod_TableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Prod_Table);

        insertButton.setText("Insert");
        insertButton.setBorder(new javax.swing.border.MatteBorder(null));
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        firstButton.setText("First");
        firstButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstButtonActionPerformed(evt);
            }
        });

        lstButton.setText("Last");
        lstButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lstButtonActionPerformed(evt);
            }
        });

        prevButton.setText("Previous");
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });

        nxtButton.setText("Next");
        nxtButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nxtButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(deleteButton)
                                        .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(insertButton, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(clearButton))
                        .addGap(46, 46, 46)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 167, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup().addComponent(firstButton).addGap(67, 67, 67)
                                        .addComponent(prevButton).addGap(85, 85, 85).addComponent(nxtButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lstButton))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(125, 125, 125)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
                .createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup().addGap(66, 66, 66)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(
                                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(firstButton).addComponent(lstButton)
                                                .addComponent(prevButton).addComponent(nxtButton)))
                        .addGroup(layout.createSequentialGroup().addGap(48, 48, 48).addGroup(layout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(insertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30).addComponent(updateButton).addGap(41, 41, 41)
                                        .addComponent(deleteButton).addGap(93, 93, 93).addComponent(clearButton)))))
                .addContainerGap(97, Short.MAX_VALUE)));

        pack();
    }
    //Empty Action for ID, Name, and Price Fields
    private void idFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void priceFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    //Insert Button
    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (inputWithImgCheck()) {
            try {
                PreparedStatement ps = con
                        .prepareStatement("INSERT INTO products(Name,Price,DateAdded,Image)" + "values(?,?,?,?)");

                ps.setString(1, _name);
                ps.setFloat(2, _price);

                ps.setString(3, dteFmt.format(_addDate));

                try {
                    InputStream img = new FileInputStream(new File(imgPath));
                    ps.setBlob(4, img);
                    ps.executeUpdate();
                    // idField.setText("");
                    // showItemInFields(index);
                    showItemInFields(getProductList().size() - 1);
                    showProducts();
                    JOptionPane.showMessageDialog(null, "Data Inserted");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, "The File Doesn't exist");
                }

            } catch (PacketTooBigException pcte) {
                JOptionPane.showMessageDialog(null, "Image Too Large");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Couldn't Insert");
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            JOptionPane.showMessageDialog(null, "One or more Fields are empty.");
        }
    }

    //Update Button
    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        refreshAllVariables();
        if (inputCheck() & idCheck()) {
            String upteQry = "";
            PreparedStatement ps = null;

            if ("".equals(imgPath)) {
                upteQry = "UPDATE products SET name = ?, price = ?" + ",DateAdded = ? WHERE id = ?";
                try {
                    ps = con.prepareStatement(upteQry);

                    ps.setString(1, _name);
                    ps.setFloat(2, _price);

                    ps.setString(3, dteFmt.format(_addDate));

                    ps.setInt(4, _id);
                    ps.executeUpdate();
                    showProducts();
                    JOptionPane.showMessageDialog(null, "Update Successful");
                } catch (SQLException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            } // update with image
            else {
                InputStream img = null;
                try {
                    img = new FileInputStream(new File(imgPath));
                    upteQry = "UPDATE products SET name = ?, price = ?" + ", DateAdded = ?, Image = ? WHERE id = ?";

                    try {
                        ps = con.prepareStatement(upteQry);
                        ps.setString(1, _name);
                        ps.setFloat(2, _price);
                        ps.setString(3, dteFmt.format(_addDate));
                        ps.setBlob(4, img);
                        ps.setInt(5, _id);
                        ps.executeUpdate();
                        showProducts();
                        JOptionPane.showMessageDialog(null, "Update Successful.");
                    } catch (SQLException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                } finally {
                    try {
                        img.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, "One or more Fields are empty.");
        }
    }

    //Delete Button
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (idCheck()) {
            try {
                PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE id=?");
                int id = Integer.parseInt(idField.getText());
                ps.setInt(1, id);
                ps.executeUpdate();
                showProducts();
                JOptionPane.showMessageDialog(null, "Delete Successful");

            } catch (SQLException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Delete Unsuccessful");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Delete Unsuccessful : Enter the Product ID.");

        }

    }

    //first Button
    private void firstButtonActionPerformed(java.awt.event.ActionEvent evt) {
        pos = 0;
        showItemInFields(pos);
    }

    //Last Button
    private void lstButtonActionPerformed(java.awt.event.ActionEvent evt) {
        pos = getProductList().size() - 1;
        showItemInFields(pos);
    }

    //Previous Button
    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {
        pos--;
        if (pos < 0) {
            pos = 0;
        }
        showItemInFields(pos);
    }

    //Next Button
    private void nxtButtonActionPerformed(java.awt.event.ActionEvent evt) {

        pos++;
        if (pos >= getProductList().size()) {
            pos = getProductList().size() - 1;
        }
        showItemInFields(pos);

    }

    //Choose Image Button
    private void Btn_Choose_ImageActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.image", "jpg", "png");
        file.addChoosableFileFilter(filter);
        int result = file.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = file.getSelectedFile();
            imgPath = selectedFile.getAbsolutePath();
            lbl_image.setIcon(resizeImg(imgPath, null));
            _addDate = addDateField.getDate();

        } else {
            System.out.println("No File Selected");
        }

    }

    //Focus Listeners for  Fields
    private void idFieldFocusLost(java.awt.event.FocusEvent evt) {

        try {
            _id = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
        }
    }

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {
        clearFields();
    }

    private void nameFieldFocusLost(java.awt.event.FocusEvent evt) {
        _name = nameField.getText();
    }

    private void priceFieldFocusLost(java.awt.event.FocusEvent evt) {
        try {
            _price = Float.parseFloat(priceField.getText());
        } catch (NumberFormatException e) {
        }
    }

    private void addDateFieldFocusLost(java.awt.event.FocusEvent evt) {
    }

    //Translate Row Data into Fields
    private void Prod_TableMouseClicked(java.awt.event.MouseEvent evt) {
        int index = Prod_Table.getSelectedRow();
        pos = index;
        showItemInFields(index);
        refreshAllVariables();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton Btn_Choose_Image;
    private javax.swing.JTable Prod_Table;
    private com.toedter.calendar.JDateChooser addDateField;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton firstButton;
    private javax.swing.JTextField idField;
    private javax.swing.JButton insertButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_image;
    private javax.swing.JButton lstButton;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton nxtButton;
    private javax.swing.JButton prevButton;
    private javax.swing.JTextField priceField;
    private javax.swing.JButton updateButton;
    private javax.swing.JButton clearButton;
    // End of variables declaration

}
