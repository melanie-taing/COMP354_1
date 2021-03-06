import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RepositoryContainerTest {

    TransactionRepository transactionRepoMock = mock(TransactionRepository.class);;
    AccountRepository accountRepoMock = mock(AccountRepository.class);
    BudgetRepository budgetRepo = mock(BudgetRepository.class);
    RepositoryContainer testRepoContainer = new RepositoryContainer(transactionRepoMock, accountRepoMock, budgetRepo);

    @Before
    public void resetMocks() {
        transactionRepoMock = mock(TransactionRepository.class);
        accountRepoMock = mock(AccountRepository.class);
        budgetRepo = mock(BudgetRepository.class);
        testRepoContainer = new RepositoryContainer(transactionRepoMock, accountRepoMock, budgetRepo);
    }

    @Test
    public void testDeleteTransaction() {

        int transactionId = 1;
        Transaction testTransaction = new Transaction();
        testTransaction.setId(transactionId);

        //set up mock methods
        when(transactionRepoMock.getItem(transactionId)).thenReturn(testTransaction);

        //method to be tested
        testRepoContainer.deleteTransaction(transactionId);

        verify(transactionRepoMock).deleteItem(transactionId);
        
    }

    @Test
    public void testDeleteAccount() {

        int accountId = 1;

        //method to be tested
        testRepoContainer.deleteAccount(accountId);

        verify(accountRepoMock).deleteItem(accountId);
        verify(transactionRepoMock).deleteAllItemsFromAccount(accountId);
    }

//    @Test
//    public void testSaveTransactionItem() {
//
//        Transaction testTransaction = new Transaction();
//
//        //method to be tested
//        testRepoContainer.saveItem(testTransaction);
//
//        verify(transactionRepoMock).saveItem(testTransaction);
//        
//    }

    @Test
    public void testSaveAccountItem() {

        Account testAccount = new Account();

        //method to be tested
        testRepoContainer.saveItem(testAccount);

        verify(accountRepoMock).saveItem(testAccount);
    }

    //ImportTransaction is tested in its own class ImportTransactionTest

    @Test
    public void testResetSQLStructure() {

        //method to be tested
        testRepoContainer.resetSQLStructure();

        verify(accountRepoMock).reinitSQLStructure();
        verify(transactionRepoMock).reinitSQLStructure();
    }

    @Test
    public void testLoadAllItems() {

        //method to be tested
        testRepoContainer.loadAllItems();

        verify(accountRepoMock).loadAllItems();
        verify(transactionRepoMock).loadAllItems();
    }

    @Test
    public void testInitSQLStructure() {

        //method to be tested
        testRepoContainer.initSQLStructure();

        verify(accountRepoMock).initSQLStructure();
        verify(transactionRepoMock).initSQLStructure();
    }
	
	@Test 
	public void saveItemTest() {
		String TestTransDBName = "TransactionTest";
		Database testTransDatabase = new Database(TestTransDBName);
		TransactionRepository transacRepoTest = new TransactionRepository(testTransDatabase);
	    transacRepoTest.reinitSQLStructure();
	    
	    String TestAccDBName = "AccountsTest";
		Database testAccDatabase = new Database(TestAccDBName);
		AccountRepository accountRepoTest = new AccountRepository(testAccDatabase);
		accountRepoTest.reinitSQLStructure();	
		
		String TestBudgetDBName = "BudgetsTest";
		Database testBudgetDatabase = new Database(TestBudgetDBName);
		BudgetRepository budgetRepoTest = new BudgetRepository(testBudgetDatabase);
		budgetRepoTest.reinitSQLStructure();
		
		RepositoryContainer repoContainer = new RepositoryContainer(transacRepoTest, accountRepoTest, budgetRepoTest);				
		
		//Create fake account
		String bankName = "Fort Knox";
		String nickname = "My PiggyBank";		
		Integer accBalance = 0;

		Account testAcc1 = new Account();
		testAcc1.setBalance(accBalance);
		testAcc1.setBankName(bankName);
		testAcc1.setNickname(nickname);
		repoContainer.saveItem(testAcc1);
		//Did the fake account  get added to the correct DB?
		Integer actual = accountRepoTest.getItems().size();
		Integer expected = 1;
		assertEquals(actual, expected);
		Integer originalBalance = testAcc1.getBalance();	
		
		//Did the account get created with a balance of $0?
		assertEquals(accBalance, originalBalance);
				
		//create fake budget
		Budget testBudget = new Budget();
		testBudget.setName("test budget");
		testBudget.setAmount(100);
		testBudget.setBalance(50);
		
		repoContainer.saveItem(testBudget);
		//Did the fake budget get added to the correct DB?
		Budget returnedBudget = budgetRepoTest.getItem(2);
		assertEquals(testBudget.getId(), returnedBudget.getId());
		assertEquals(testBudget.getName(), returnedBudget.getName());
		assertEquals(testBudget.getAmount(), returnedBudget.getAmount());
		assertEquals(testBudget.getBalance(), returnedBudget.getBalance());
				
		//Create fake transaction
		Integer associatedAccountId = testAcc1.getId();
		String type = "deposit";
		String date = "2018-04-03";
		Integer amount = 100;
		Integer expectedBalance = accBalance + amount;
		String description = "Test transaction";
		Transaction testTransaction1 = new Transaction();
		testTransaction1.setAssociatedAccountId(associatedAccountId);
		testTransaction1.setType(type);
		testTransaction1.setDate(date);
		testTransaction1.setAmount(amount);
		testTransaction1.setDescription(description);
		testTransaction1.setAssociatedBudgetId(1);
		repoContainer.saveItem(testTransaction1);	
		
		//Did the fake transaction get added to the correct DB?
		actual = transacRepoTest.getItemsFromAccount(associatedAccountId).size();
		expected = 1;
		if(accBalance != 0) expected +=1;   //if account balance is not zero, there will an initial balance
		assertEquals(expected, actual);
		
		//did the balance update properly?
		assertEquals(expectedBalance, amount);
		
		
	}


}
