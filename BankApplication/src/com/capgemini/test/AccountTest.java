package com.capgemini.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capgemini.exceptions.InsufficientBalanceException;
import com.capgemini.exceptions.InsufficientInitialBalanceException;
import com.capgemini.exceptions.InvalidAccountNumberException;
import com.capgemini.model.Account;
import com.capgemini.repository.AccountRepository;
import com.capgemini.service.AccountService;
import com.capgemini.service.AccountServiceImpl;

public class AccountTest {
	
	@Mock
	AccountRepository accountRepository;
	AccountService accountService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		accountService = new AccountServiceImpl(accountRepository);
	}

	// For Account Creation Functionality
	
	@Test(expected = com.capgemini.exceptions.InsufficientInitialBalanceException.class)
	public void whenTheAmountIsLessThan500SystemShouldThrowException() throws InsufficientInitialBalanceException {
		accountService.createAccount(101, 300);
	}
	
	@Test
	public void whenTheValidInfoIsPassedAccountShouldBeCreatedSuccessfully() throws InsufficientInitialBalanceException{
		
		Account account = new Account();
		account.setAccountNumber(101);
		account.setAmount(5000);
		
		when(accountRepository.save(account)).thenReturn(true);
		
		assertEquals(account,accountService.createAccount(101, 5000));
	}
	
	// For Displaying Balance (Show Balance)
	
		@Test
		public void whenTheValidAccountNumberIsPassedBalanceShouldReturn() throws InsufficientInitialBalanceException{
			
			Account account = new Account();
			account.setAccountNumber(102);
			account.setAmount(3000);
		
			when(accountRepository.searchAccount(account.getAccountNumber())).thenReturn(account);
			
			assertEquals(account.getAmount(),accountService.showBalance(102));
		}
	
	// For Deposit Functionality
	@Test
	public void whenTheDepositedAmountIsPassedShouldReturnNewBalance() throws InvalidAccountNumberException, InsufficientBalanceException {
		
		Account account = new Account();
		account.setAccountNumber(104);
		account.setAmount(6000);
	
		when(accountRepository.searchAccount(account.getAccountNumber())).thenReturn(account);
		
		assertEquals(8000,accountService.depositAmount(104, 2000));
	}
	
	@Test(expected = com.capgemini.exceptions.InvalidAccountNumberException.class)
	public void whenTheAccountIsInvalidForDepositShouldThrowException() throws InvalidAccountNumberException, InsufficientBalanceException {
		Account account = new Account();
		account.setAccountNumber(100);
		account.setAmount(2000);
		
		when(accountRepository.searchAccount(account.getAccountNumber())).thenReturn(account);
		accountService.depositAmount(102,3000);
	}
	
	@Test(expected = com.capgemini.exceptions.InsufficientBalanceException.class)
	public void whenTheDepositAmountIsNegativeForDepositShouldThrowException() throws InvalidAccountNumberException, InsufficientBalanceException {
		Account account = new Account();
		account.setAccountNumber(200);
		account.setAmount(4000);
		
		when(accountRepository.searchAccount(account.getAccountNumber())).thenReturn(account);
		accountService.depositAmount(200,-400);
	}

	
	
	
	// For Withdraw Functionality
	@Test
	public void whenTheWithdrawAmountIsValidShouldReturnRemainingBalance() throws InsufficientBalanceException{
		
		Account account = new Account();
		account.setAccountNumber(103);
		account.setAmount(5000);
	
		when(accountRepository.searchAccount(account.getAccountNumber())).thenReturn(account);
		
		assertEquals(4000,accountService.withDrawAmount(103, 1000));
	}
	
	
	
	@Test
	public void whenTheFundTransferAmountIsPassedShouldReturnNewBalanceOfDestination() {
		
		Account sourceAccount = new Account();
		sourceAccount.setAccountNumber(105);
		sourceAccount.setAmount(7000);
		
		when(accountRepository.save(sourceAccount)).thenReturn(true);
		
		Account destinationAccount = new Account();
		destinationAccount.setAccountNumber(106);
		destinationAccount.setAmount(8000);
	
		when(accountRepository.save(destinationAccount)).thenReturn(true);
		
		when(accountRepository.searchAccount(destinationAccount.getAccountNumber())).thenReturn(destinationAccount);
		when(accountRepository.searchAccount(sourceAccount.getAccountNumber())).thenReturn(sourceAccount);
		
		assertEquals("6002000800",accountService.fundTransfer(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), 2000));
	}
}
