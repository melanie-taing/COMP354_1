
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Implementation of the IAccountView interface. Requires a model that implements the IModelView
 * interface.
 */
public class AccountView extends AbstractView<Account> implements IAccountView, IViewGUI {

	// Account UI elements
	private JPanel panel;
	private DefaultTableModel tableModel;
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

    /**
     * Constructor.
     * @param model model
     */
	public AccountView(IModelView model)
	{
        super(model);
        items = new ArrayList<>();
        model.attachObserver(this);
		createAccPanel();
		setLayout();
        // The clear button is handled directly by the view, no need for the controller here
        clearButton.addActionListener(e->handleClear());
	}

    public void update() {
        items = model.getAllAccounts();

        // Also checks to see if previous selection is still there
        fillTable();

        highlightCurrentSelection();
    }

    @Override
    public JPanel getPanel()            {return panel;}
    @Override
    public String getBankInput() {return bankTextfield.getText();}
    @Override
    public String getNicknameInput() {return nicknameTextfield.getText();}

    @Override
    protected void handleBudgetSelectionChange() {
        // do nothing
        return;
    }

    @Override
    public Integer getBalanceInput() {
        String amount = balanceTextfield.getText();
        if (!amount.matches("\\d+") || amount.isEmpty())
        {
            return null;
        }
        else
        {
            return Integer.parseInt(amount);
        }
    }
    @Override
    public Integer getSelectedAccountId() {return getCurrentAccountSelection();}
    @Override
    public void setSelection(Integer id) {
        setCurrentAccountSelection(id);
    }

    @Override
    protected void handleAccountSelectionChange() {
        Account currentSelection = findItem(getCurrentAccountSelection());
        fillFields(currentSelection);
        highlightCurrentSelection();
    }

    @Override
    protected void handleTransactionSelectionChange() {
        // do nothing
    }

    @Override
    public void registerAddActionCallback(ActionListener listener, String actionCommand) {
        addButton.setActionCommand(actionCommand);
        addButton.addActionListener(listener);
    }

    @Override
    public void registerUpdateActionCallback(ActionListener listener, String actionCommand) {
        updateButton.setActionCommand(actionCommand);
        updateButton.addActionListener(listener);
    }

    @Override
    public void registerDeleteActionCallback(ActionListener listener, String actionCommand) {
        deleteButton.setActionCommand(actionCommand);
        deleteButton.addActionListener(listener);
    }

    protected void highlightCurrentSelection() {
        if (getCurrentAccountSelection() == 0)
        {
            fillFields(null);
            return;
        }
        Account item;
        for (int i = 0; i < items.size(); i++)
        {
            item = items.get(i);
            if (item.getId().equals(getCurrentAccountSelection()))
            {
                table.setRowSelectionInterval(i, i);
                fillFields(item);
                return;
            }
        }
    }

    protected void handleClear()
    {
        setCurrentAccountSelection(0);
        update();
    }


    protected void fillFields(Account account)
    {
        if (account == null)
        {
            bankTextfield.setText("");
            nicknameTextfield.setText("");
            balanceTextfield.setText("");
        }
        else
        {
            bankTextfield.setText(account.getBankName());
            nicknameTextfield.setText(account.getNickname());
            balanceTextfield.setText(account.getBalance().toString());
        }

    }

    protected void fillTable()
    {
        // Clear table
        tableModel.setRowCount(0);

        // Fetch transactions associated with account and display
        boolean validSelectionFound = false;
        if (!items.isEmpty()) {
            //add rows to table
            for (Account item : items)
            {
                tableModel.addRow(new Object[] {item.getBankName(), item.getNickname(), item.getBalance()});
                if (!validSelectionFound && item.getId().equals(getCurrentAccountSelection()))
                {
                    validSelectionFound = true;
                }
            }
        }
        if (!validSelectionFound)
            setCurrentAccountSelection(0);
    }

    /**
     * Create GUI elements
     */
    private void createAccPanel() {
		// Create Account UI elements
		panel = new JPanel();
		accLabel = new JLabel("Accounts");
		bankLabel = new JLabel("Bank");
		nicknameLabel = new JLabel("Nickname");
		balanceLabel = new JLabel("Balance (cents)");
		addButton = new JButton("Add");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		clearButton = new JButton("Clear");
		bankTextfield = new JTextField(15);
		nicknameTextfield = new JTextField(15);
		balanceTextfield = new JTextField(15);
		table = new JTable();
		tableModel = new DefaultTableModel() {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(table);

		// Loading JTable
		Object[] columns = {"Bank", "Nickname", "Balance (cents)"};

		tableModel.setColumnIdentifiers(columns);
		table.setModel(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(300, 80));
		table.setFillsViewportHeight(true);

		// Set mouse click to update values in field with currently selected row
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// i = the index of the selected row
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
                    Account selectedItem = items.get(selectedRow);
                    setCurrentAccountSelection(selectedItem.getId());
				}
			}
		});
	}

    /**
     * Set layout for GUI elements
     */
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
									.addComponent(bankTextfield, 200, 200, 250)
									.addComponent(nicknameTextfield, 200, 200, 250)
									.addComponent(balanceTextfield, 200, 200, 250)))
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(addButton))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)									
									.addComponent(updateButton))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(deleteButton)).
                                addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(clearButton))

								))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(scrollPane, 650, 650, 700))
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
									.addComponent(clearButton)
									.addComponent(updateButton)
									.addComponent(deleteButton)	
									))))
		);
		
		layout.linkSize(SwingConstants.HORIZONTAL, bankLabel, nicknameLabel, balanceLabel);
		layout.linkSize(SwingConstants.HORIZONTAL, addButton, updateButton, deleteButton, clearButton);	
	}
}
