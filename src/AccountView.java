
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class AccountView extends AbstractView{

	// Account UI elements
	private MainView mainView;
	private JPanel panel;
	private DefaultTableModel model;
	private JLabel accLabel;
	private JLabel bankLabel;
	private JLabel nicknameLabel;
	private JLabel balanceLabel;
	private JButton addButton;
	private JButton updateButton;
	private JButton deleteButton;
	private JButton clearButton;
	private JTextField bankTextfield;
	private JTextField nicknameTextfield;
	private JTextField balanceTextfield;
	private JTable table;
	private JScrollPane scrollPane;
	
	public AccountView() { 
		createAccPanel();
	}
	
	// Getters and setters
	public JPanel getPanel() {return panel;}
	public void setPanel(JPanel accPanel) {this.panel = accPanel;}
	
	public DefaultTableModel getTableModel() {return model;}
	public void setTableModel(DefaultTableModel accModel) {this.model = accModel;}
	
	public JLabel getAccLabel() {return accLabel;}
	public void setAccLabel(JLabel accLabel) {this.accLabel = accLabel;}
	
	public JLabel getBankLabel () {return bankLabel;}
	public void setBankLabel(JLabel bankLabel) {this.bankLabel = bankLabel;}
	
	public JLabel getNicknameLabel() {return nicknameLabel;}
	public void setNickname(JLabel nicknameLabel) {this.nicknameLabel = nicknameLabel;}
	
	public JLabel getBalanceLabel() {return balanceLabel;}
	public void setBalanceLabel(JLabel BalanceLabel) {this.balanceLabel = BalanceLabel;}
	
	public JTextField getBankTextfield() {return bankTextfield;}
	public void setBankTextfield(JTextField bankTextfield) {this.bankTextfield = bankTextfield;}
	
	public JTextField getNicknameTextfield() {return nicknameTextfield;}
	public void setNicknameTextfield(JTextField nicknameTextfield) {this.nicknameTextfield = nicknameTextfield;}
	
	public JTextField getBalanceTextfield() {return balanceTextfield;}
	public void setBalanceTextfield(JTextField balanceTextfield) {this.balanceTextfield = balanceTextfield;}

	public JButton getAddButton() {return addButton;}
	public void setAddButton(JButton addButton) {this.addButton = addButton;}

	public JButton getUpdateButton() {return updateButton;}
	public void setUpdateButton(JButton updateButton) {this.updateButton = updateButton;}

	public JButton getDeleteButton() {return deleteButton;}
	public void setDeleteButton(JButton deleteButton) {this.deleteButton = deleteButton;}
	
	public JButton getClearButton() {return clearButton;}
	public void setClearButton(JButton clearButton) {this.clearButton = clearButton;}


	public JTable getTable() {return table;}
	public void setTable(JTable table) {this.table = table;}
	
	public JScrollPane getScrollPane() {return scrollPane;}
	public void setScrollPane(JScrollPane scrollPane) {this.scrollPane = scrollPane;}
	
	public void update() {
		// Updates JTable
		model.fireTableDataChanged();
	}
	
	private void createAccPanel() {
		// Create Account UI elements
		panel = new JPanel();
		accLabel = new JLabel("Accounts");
		bankLabel = new JLabel("Bank");
		nicknameLabel = new JLabel("Nickname");
		balanceLabel = new JLabel("Balance");
		addButton = new JButton("Add");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		clearButton = new JButton("Clear");
		bankTextfield = new JTextField(15);
		nicknameTextfield = new JTextField(15);
		balanceTextfield = new JTextField(15);
		table = new JTable();
		model = new DefaultTableModel() {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		scrollPane = new JScrollPane(table);
		
		// Settings labels to textfields
		bankLabel.setLabelFor(bankTextfield);
		nicknameLabel.setLabelFor(nicknameTextfield);
		balanceLabel.setLabelFor(balanceTextfield);
		
		// Loading JTable
		Object[] columns = {"Bank", "Nickname", "Balance"};

		model.setColumnIdentifiers(columns);
		table.setModel(model);
		table.setPreferredScrollableViewportSize(new Dimension(300, 80));
		table.setFillsViewportHeight(true);
		
		setLayout();
	}
	
	private void setLayout() {
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);	
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(accLabel)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(bankLabel)
									.addComponent(nicknameLabel)
									.addComponent(balanceLabel))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)								
									.addComponent(bankTextfield)
									.addComponent(nicknameTextfield)
									.addComponent(balanceTextfield)))
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(addButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)									
									.addComponent(updateButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(deleteButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(clearButton))))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(scrollPane))
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(accLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(scrollPane, 50, 80, 105)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)	
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(bankLabel)
									.addComponent(bankTextfield))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(nicknameLabel)
									.addComponent(nicknameTextfield))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(balanceLabel)
									.addComponent(balanceTextfield))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(addButton)
									.addComponent(updateButton)
									.addComponent(deleteButton)	
									.addComponent(clearButton)))))
		);
		
		layout.linkSize(SwingConstants.HORIZONTAL, bankLabel, nicknameLabel, balanceLabel);
		layout.linkSize(SwingConstants.HORIZONTAL, addButton, updateButton, deleteButton, clearButton);	
	}
}
