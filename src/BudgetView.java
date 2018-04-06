import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class BudgetView extends AbstractView<Budget> implements IBudgetView, IViewGUI{
	
	//Budget UI elements	
	private JPanel panel;
	private DefaultTableModel tableModel;
	private JLabel budgetCategoryLabel;
	private JLabel budgetLabel;
	private JLabel nameLabel;
//	private JLabel durationLabel;
	private JLabel amountLabel;
	private JButton addButton;
	private JButton updateButton;
	private JButton deleteButton;
	private JButton clearButton;
//	private JComboBox durationBox;
	private JTextField nameTextfield;
	private JTextField amountTextfield;
	private JTable table;
	private JScrollPane scrollPane;
	
	public BudgetView(IModelView model) {

		super(model);
		items = new ArrayList<>();
		model.attachObserver(this);
		createBudgetPanel();
        setLayout();

		// The clear button is handled directly by the view, no need for the controller here
		clearButton.addActionListener(e->handleClear());
	}
  
	public JPanel getPanel()		{return panel;}
	public String getNameInput()	{return nameTextfield.getText();}
	public Integer getAmountInput()
	{
		String amount = amountTextfield.getText();
		if (!amount.matches("\\d+") || amount.isEmpty())
			return null;
		else
			return Integer.parseInt(amount);
	}
	
	private void createBudgetPanel() {
		panel = new JPanel();
		budgetLabel = new JLabel("Budgets");
		nameLabel = new JLabel("Name");
//		durationLabel = new JLabel("Duration");
		amountLabel = new JLabel("Amount (cents)");
		addButton = new JButton("Add");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		clearButton = new JButton("Clear");
		nameTextfield = new JTextField(15);
//		durationBox = new JComboBox(new String[]{"Weekly", "Monthly"});
		amountTextfield = new JTextField(15);
		table = new JTable();
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(table);
		
		// Loading JTable
		Object[] columns = {"Name"/*, "Duration"*/, "Budget Amount", "Recorded Amount"};
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
                    Budget selectedItem = items.get(selectedRow);
                    setCurrentBudgetSelection(selectedItem.getId());
				}
			}
		});
	}
	
	private void setLayout() {
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(budgetLabel)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(nameLabel)
//									.addComponent(durationLabel)
									.addComponent(amountLabel))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)		
									.addComponent(nameTextfield, 200, 200, 250)
//									.addComponent(durationBox, 200, 200, 250)
									.addComponent(amountTextfield, 200, 200, 250)))
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
						.addComponent(budgetLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(scrollPane, 50, 80, 105)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)	
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(nameLabel)
									.addComponent(nameTextfield))
//								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//									.addComponent(durationLabel)
//									.addComponent(durationBox))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(amountLabel)
									.addComponent(amountTextfield))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(addButton)
									.addComponent(clearButton)
									.addComponent(updateButton)
									.addComponent(deleteButton)	
									))))
		);
		
		layout.linkSize(SwingConstants.HORIZONTAL, nameLabel/*, durationLabel*/, amountLabel);
		layout.linkSize(SwingConstants.HORIZONTAL, addButton, updateButton, deleteButton, clearButton);	
	}

    @Override
    protected void handleAccountSelectionChange() {
	    // do nothing
        return;
    }

    @Override
    protected void handleTransactionSelectionChange() {
        // do nothing
	    return;
    }

    @Override
    protected void handleBudgetSelectionChange() {
        // do nothing
    }

    @Override
    protected void highlightCurrentSelection() {
        if (getCurrentBudgetSelection() == 0 || getCurrentBudgetSelection() == 1)
        {
            fillFields(null);
            return;
        }
        Budget item;
        for (int i = 0; i < items.size(); i++)
        {
            item = items.get(i);
            if (item.getId().equals(getCurrentBudgetSelection()))
            {
                table.setRowSelectionInterval(i, i);
                fillFields(item);
                return;
            }
        }
    }

    @Override
    protected void handleClear() {
        setCurrentBudgetSelection(0);
        update();
    }

    @Override
    protected void fillFields(Budget item) {
        if (item == null)
        {
            nameTextfield.setText("");
            amountTextfield.setText("");
        }
        else
        {
            nameTextfield.setText(item.getName());
            amountTextfield.setText(item.getAmount().toString());
        }
    }

    @Override
    protected void fillTable() {
        // Clear table
        tableModel.setRowCount(0);

        // Fetch transactions associated with account and display
        boolean validSelectionFound = false;
        if (!items.isEmpty()) {
            //add rows to table
            for (Budget item : items)
            {
                if (item.getId() == 1) // This is the 'none' budget
                    continue;

                tableModel.addRow(new Object[] {item.getName(), item.getAmount(), item.getBalance()});
                if (!validSelectionFound && item.getId().equals(getCurrentBudgetSelection()))
                {
                    validSelectionFound = true;
                }
            }
        }
        if (!validSelectionFound)
            setCurrentBudgetSelection(0);
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

    @Override
    public Integer getSelectedBudgetId() {
        return getCurrentBudgetSelection();
    }

    @Override
    public void setSelection(Integer id) {
        setCurrentBudgetSelection(id);
    }

    @Override
    public void update() {
        items = model.getAllBudgets();

        // Also checks to see if previous selection is still there
        fillTable();

        highlightCurrentSelection();
    }
}
