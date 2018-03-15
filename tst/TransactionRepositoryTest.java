import org.junit.Test;
import java.io.File;
import static org.junit.Assert.*;
import java.util.HashMap;
import org.junit.BeforeClass;
import org.junit.AfterClass;

public class TransactionRepositoryTest {
	
	private static TransactionRepository transacRepoTest;
	
	@BeforeClass
	
	public static void setUpClass() {
		/* Test Account */
		AccountModel testAcc = new AccountModel();
		testAcc.setId(1);
		testAcc.setBalance(0);
		testAcc.setBankName("Fort Knox");
		testAcc.setNickname("Oddjob");
		
		/* Expected transaction tuple */
		Integer accountID = 1;
		String type = "deposit";
		String date = "09-09-1999";
		Float amount = 100.0f;
		TransactionModel expected = new TransactionModel();
		expected.setAccountId(accountID);
		expected.setType(type);
		expected.setDate(date);
		expected.setAmount(amount);
		
		/* Test database for the transaction */
		Database testDatabase = new Database("transactions");
		AccountTransactionRepository testAccTransacRepo = new AccountTransactionRepository(testDatabase, testAcc);
		
		ImportTransaction transaction = new ImportTransaction();
		transaction.setAccountTransactionRepository(testAccTransacRepo);
		transaction.addTransaction("tst/spread_sheet_test_case.csv");
		
		transacRepoTest = new TransactionRepository(testDatabase);
	}
	
	@Test
	public void loadItemTest() {
		Integer itemID = new Integer(1);
		transacRepoTest.loadItem(itemID);
		
		int expectedNumAccount = 1;
		assertEquals(transacRepoTest.itemMap.size(), expectedNumAccount);
	}

	@Test
	public void loadAllTest() {
		transacRepoTest.itemMap.clear(); //Clear ItemMap to test loadItem() and loadAll() successively.
		transacRepoTest.loadAllItems();
		int expectedNumAccount = 1;
		assertEquals(transacRepoTest.itemMap.size(), expectedNumAccount);
	}
	
	@AfterClass
	public static void tearDownclass() {
		transacRepoTest = null;
		File f = new File("transactions");
		f.delete();
	}
}