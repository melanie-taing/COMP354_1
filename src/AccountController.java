/*
 * Account Controller
 * Controls all activities of the account
 */
public class AccountController {
	
	Database myDatabase;
	
	AccountModel account;
	AccountTransactionRepository transctions;
		
	public AccountController(AccountModel account, Database myDatabase){
		this.account = account;
		this.myDatabase = myDatabase;
		this.transctions = new AccountTransactionRepository(myDatabase);
		this.transctions.setAccount(account);
	}
	
	
	public void LoadTransactions() {
		transctions.loadAllItems();
	}
	
	
	public void loadTransaction(Integer transactionId) {
		transctions.loadItem(transactionId);
	}
	
	public void saveTransacton(TransactionModel transaction) {
		transctions.saveItem(transaction);
	}
}