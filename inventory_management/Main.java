package LaCusinaFilipina;

import java.awt.EventQueue;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Table extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private JTextField textField;
    private JTextField textField_1;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Table frame = new Table();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Table() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 679);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 1065, 560);
        contentPane.add(scrollPane);

        // Column headers
        String[] columns = {
        	
            "ProductId","Category", "Ingredient",
            "<html>Current<br>Quantity</html>",
            "<html>Daily<br>Need (pcs)</html>",
            "<html>Weekly<br>Need (pcs)</html>",
            "<html>Monthly<br>Need (pcs)</html>",
            "<html>Yearly<br>Need (pcs)</html>",
            "<html>Days<br>Remaining</html>",
            "Status"
        };

        // Empty model — data will be loaded from database
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // makes table read-only
            }
        };

        table = new JTable(model);
        table.setBackground(new Color(192, 192, 192));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(25);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        table.getColumnModel().getColumn(7).setPreferredWidth(130);
        table.getColumnModel().getColumn(8).setPreferredWidth(90);
        table.getColumnModel().getColumn(9).setPreferredWidth(95);
        table.getTableHeader().setPreferredSize(
            new java.awt.Dimension(0, 60)
        );

    
        table.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = value != null ? value.toString() : "";
                if (status.equals("<html><font color='black'>Good</font><html>")) {
                    setBackground(new Color(0, 180, 0));
                    setForeground(Color.GREEN);
                } else if (status.equals("<html><font color='black'>Low</font><html>")) {
                    setBackground(new Color(255, 200, 0));
                    setForeground(Color.ORANGE);
                } else if (status.equals("<html><font color='black'>Critical</font><html>")) {
                    setBackground(new Color(220, 0, 0));
                    setForeground(Color.RED);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                return this;
            }
        });

        scrollPane.setViewportView(table);

        JLabel lblProductId = new JLabel("ProductId:");
        lblProductId.setBounds(10, 574, 70, 20);
        contentPane.add(lblProductId);

        textField_1 = new JTextField();
        textField_1.setBounds(80, 571, 96, 20);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        // Add Button
        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(186, 571, 84, 20);
        contentPane.add(btnAdd);
        
        btnAdd.addActionListener(e -> {
            String productIdText = textField_1.getText().trim();
            String quantityText  = textField.getText().trim();

            // Validate inputs are not empty
            if (productIdText.isEmpty() || quantityText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "Please enter both ProductId and Quantity!",
                    "Missing Input",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
            	int productId = Integer.parseInt(productIdText);
            	int quantity = Integer.parseInt(quantityText);
            	if(quantity<=0) {
            		JOptionPane.showMessageDialog(null,
                            "Quantity must be greater than 0!",
                            "Invalid input",
                            JOptionPane.WARNING_MESSAGE);
                        return;
            	}
            	
                Connection conn = database_connection.getConnection();
                String sql = "update products set currentstock = currentstock + ? where productId = ?"; 
                PreparedStatement preparedStatement = conn.prepareStatement(sql);	
                preparedStatement.setInt(1, quantity);
                preparedStatement.setInt(2, productId);
                int row = preparedStatement.executeUpdate();
                
                if (row > 0) {
					JOptionPane.showMessageDialog(null, "Successfully added stocks to database...", "Successful",JOptionPane.INFORMATION_MESSAGE);
					textField_1.setText("");
                    textField.setText("");
                    loadData();   
                    return;
				}else {
					JOptionPane.showMessageDialog(null, "ProductId not found!",
	                        "Error",
	                        JOptionPane.ERROR_MESSAGE);
				}

                preparedStatement.close();
                conn.close();

            } catch (NumberFormatException ex) {
            		JOptionPane.showMessageDialog(null,
                    "ProductId and Quantity must be numbers!",
                    "Invalid Input",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Remove Button  
        JButton btnRemove = new JButton("Remove");
        btnRemove.setBounds(280, 571, 84, 20);
        contentPane.add(btnRemove);

        btnRemove.addActionListener(e -> {
            String productIdText = textField_1.getText().trim();
            String quantityText  = textField.getText().trim();

            // Validate inputs are not empty
            if (productIdText.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "Please enter both ProductId and Quantity!",
                    "Missing Input",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int productId   = Integer.parseInt(productIdText);
                int quantity    = Integer.parseInt(quantityText);
                // Validate quantity is positive
                if (quantity <= 0) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                        "Quantity must be greater than 0!",
                        "Invalid Quantity",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Update database — REDUCT to current stock
                Connection conn = database_connection.getConnection();
                String sql = "UPDATE products SET currentstock = currentstock - ? where productId = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                
                preparedStatement.setInt(1, quantity);
                preparedStatement.setInt(2, productId);
                
                int rows = preparedStatement.executeUpdate();

                if (rows > 0) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                        "Stock reduced successfully!",
                        "Success",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    textField_1.setText("");  // clear fields
                    textField.setText("");
                    loadData();               // refresh table
                } else {
                    javax.swing.JOptionPane.showMessageDialog(null,
                        "ProductId not found!",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                }

                preparedStatement.close();
                conn.close();

            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(null,
                    "ProductId and Quantity must be numbers!",
                    "Invalid Input",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // New Quantity Label and Field (right side)
        JLabel lblNewLabel_1 = new JLabel("New Quantity:");
        lblNewLabel_1.setBounds(374, 574, 90, 20);
        contentPane.add(lblNewLabel_1);

        textField = new JTextField();
        textField.setBounds(464, 571, 96, 20);
        contentPane.add(textField);
        textField.setColumns(10);
        // Load data from database
        loadData();

        setVisible(true);
    }

    // Fetches all products from MySQL and fills the table
    private void loadData() {
        try {
            Connection conn = database_connection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

            model.setRowCount(0);

            while (rs.next()) {
            	int productId = rs.getInt("productId");
                String category    = rs.getString("category");
                String ingredient  = rs.getString("ingredient_name");
                int currentStock   = rs.getInt("currentstock");
                int dailyNeed      = rs.getInt("daily_need");
                int weeklyNeed     = rs.getInt("weekly_need");
                int monthlyNeed    = rs.getInt("monthly_need");
                int yearlyNeed     = rs.getInt("yearly_need");

                // Calculate days remaining
                double daysRemaining = (double) currentStock / dailyNeed;

                // Determine status
                String status;
                if (daysRemaining >= 7) {
                    status = "<html><font color='black'>Good</font><html>";
                } else if (daysRemaining >= 3) {
                    status = "<html><font color='black'>Low</font><html>";
                } else {
                    status = "<html><font color='black'>Critical</font><html>";
                }

                // Add row to table
                model.addRow(new Object[]{
                	productId,
                    category,
                    ingredient,
                    currentStock,
                    dailyNeed,
                    weeklyNeed,
                    monthlyNeed,
                    yearlyNeed,
                    String.format("%.1f days", daysRemaining),
                    status
                });
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
