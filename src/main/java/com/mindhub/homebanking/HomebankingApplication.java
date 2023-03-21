package com.mindhub.homebanking;

import antlr.collections.List;
import com.mindhub.homebanking.Services.*;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.mindhub.homebanking.utils.Utilities.cardCvvGenerator;
import static com.mindhub.homebanking.utils.Utilities.handleCardNumberGeneration;

@SpringBootApplication
public class HomebankingApplication {
//	@Autowired
//	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner initData(ClientService clientService, AccountService accountService, TransactionService transactionService, LoanService loanService, ClientLoanService clientLoanService, CardService cardService) {
//		return (args) -> {
//			// save a couple of customers
//			Client cli1=new Client("Melba", "Morel","melba@mindhub.com",passwordEncoder.encode("asd95321"),"Melba85","2215483251");
//			Client cli2=new Client("Pancracio", "Martinez","panma@mindhub.com",passwordEncoder.encode("54a4fa7gt"),"Pancanator","1154832512");
//			Client admin= new Client("admin","admin","admin@mindhub.com",passwordEncoder.encode("123456"),"Godlike","0800453842");
//			Account ac1= new Account(AccountType.CHECKING,"VIN-58732618", LocalDateTime.now(),0);
//			Account ac2= new Account(AccountType.SAVINGS,"VIN-35746558", LocalDateTime.now().minusDays(47),0);
//			Account ac3= new Account(AccountType.CHECKING,"VIN-96821167", LocalDateTime.now().minusDays(73),0);
//			Transaction tr1=new Transaction(TransactionType.CREDIT,5000,LocalDateTime.now().minusMonths(130),"Payment for services",ac1.getNumber(),"Mindhub Brothers");
//			ac1.setBalance(ac1.getBalance()+5000);
//			tr1.setNewBalance(ac1.getBalance());
//			Transaction tr2=new Transaction(TransactionType.DEBIT,2000,LocalDateTime.now().minusDays(100),"Amazon purchase",ac1.getNumber(),"Mindhub Brothers");
//			ac1.setBalance(ac1.getBalance()-2000);
//			tr2.setNewBalance(ac1.getBalance());
//			Transaction tr6=new Transaction(TransactionType.CREDIT,100,LocalDateTime.now().minusDays(93),"Taxes refund",ac1.getNumber(),"Mindhub Brothers");
//			ac1.setBalance(ac1.getBalance()+100);
//			tr6.setNewBalance(ac1.getBalance());
//			Transaction tr7=new Transaction(TransactionType.CREDIT,1500,LocalDateTime.now().minusDays(80),"Payment for services",ac1.getNumber(),"Mindhub Brothers");
//			ac1.setBalance(ac1.getBalance()+1500);
//			tr7.setNewBalance(ac1.getBalance());
//			Transaction tr8=new Transaction(TransactionType.DEBIT,357,LocalDateTime.now().minusDays(70),"Hotel reservations",ac1.getNumber(),"Mindhub Brothers");
//			ac1.setBalance(ac1.getBalance()-357);
//			tr8.setNewBalance(ac1.getBalance());
//			Transaction tr9=new Transaction(TransactionType.DEBIT,100,LocalDateTime.now().minusDays(50),"Restaurant expenses",ac1.getNumber(),"Mindhub Brothers");
//			ac1.setBalance(ac1.getBalance()-100);
//			tr9.setNewBalance(ac1.getBalance());
//			Transaction tr10=new Transaction(TransactionType.CREDIT,3000,LocalDateTime.now().minusDays(25),"Lottery price",ac1.getNumber(),"Mindhub Brothers");
//			ac1.setBalance(ac1.getBalance()+3000);
//			tr10.setNewBalance(ac1.getBalance());
//			Transaction tr3=new Transaction(TransactionType.CREDIT,70000,LocalDateTime.now().minusDays(3),"Dia de paga",ac3.getNumber(),"Mindhub Brothers");
//			ac3.setBalance(ac3.getBalance()+70000);
//			tr3.setNewBalance(ac3.getBalance());
//			Transaction tr4=new Transaction(TransactionType.DEBIT,1000,LocalDateTime.now().minusDays(1),"Pedidos ya",ac3.getNumber(),"Mindhub Brothers");
//			ac3.setBalance(ac3.getBalance()-1000);
//			tr4.setNewBalance(ac3.getBalance());
//			Transaction tr5=new Transaction(TransactionType.CREDIT,50000,LocalDateTime.now().minusDays(1),"me saque la grande",ac3.getNumber(),"Mindhub Brothers");
//			ac3.setBalance(ac3.getBalance()+50000);
//			tr5.setNewBalance(ac3.getBalance());
//			Loan loan1= new Loan("Mortgage",500000, Arrays.asList(12,24,36,48,60), Arrays.asList(10.0,20.0,45.0,70.0,95.0));
//			Loan loan2= new Loan("Personal",100000, Arrays.asList(6,12,24), Arrays.asList(7.0,13.0,25.0));
//			Loan loan3= new Loan("Automotive",300000, Arrays.asList(6,12,24,36), Arrays.asList(10.0,15.0,30.0,45.0));
//			ClientLoan cl1= new ClientLoan(40000,60);
//			ClientLoan cl2= new ClientLoan(50000,12);
//			ClientLoan cl3= new ClientLoan(100000,24);
//			ClientLoan cl4= new ClientLoan(200000,36);
//			cli1.addClientLoan(cl1);
//			loan1.addClientLoan(cl1);
//			cli1.addClientLoan(cl2);
//			loan2.addClientLoan(cl2);
//			cli2.addClientLoan(cl3);
//			loan2.addClientLoan(cl3);
//			cli2.addClientLoan(cl4);
//			loan3.addClientLoan(cl4);
//			ac1.addTransaction(tr1);
//			ac1.addTransaction(tr2);
//			ac1.addTransaction(tr6);
//			ac1.addTransaction(tr7);
//			ac1.addTransaction(tr8);
//			ac1.addTransaction(tr9);
//			ac1.addTransaction(tr10);
//			ac3.addTransaction(tr3);
//			ac3.addTransaction(tr4);
//			ac3.addTransaction(tr5);
//			cli1.addAccount(ac1);
//			cli1.addAccount(ac2);
//			cli2.addAccount(ac3);
////			System.out.println(cli1.toString());
//			loanService.save(loan1);
//			loanService.save(loan2);
//			loanService.save(loan3);
//			clientService.save(cli1);
//			accountService.save(ac2);
//			accountService.save(ac1);
//			clientService.save(cli2);
//			accountService.save(ac3);
//			transactionService.save(tr1);
//			transactionService.save(tr2);
//			transactionService.save(tr3);
//			transactionService.save(tr4);
//			transactionService.save(tr5);
//			transactionService.save(tr6);
//			transactionService.save(tr7);
//			transactionService.save(tr8);
//			transactionService.save(tr9);
//			transactionService.save(tr10);
//			clientLoanService.save(cl1);
//			clientLoanService.save(cl2);
//			clientLoanService.save(cl3);
//			clientLoanService.save(cl4);
//
//			Card card1=new Card(cli1.getFirstName()+" "+cli1.getLastName(), CardType.CREDIT,CardColor.GOLD);
//			Card card2=new Card(cli1.getFirstName()+" "+cli1.getLastName(), CardType.DEBIT,CardColor.SILVER);
//			card1.setNumber(handleCardNumberGeneration(cardService));
//			card1.setCvv(cardCvvGenerator());
//			card1.setThruDate(LocalDate.now().minusMonths(3));
//			card2.setNumber(handleCardNumberGeneration(cardService));
//			card2.setCvv(cardCvvGenerator());
//			cli1.addCard(card1);
//			cli1.addCard(card2);
//			clientService.save(cli1);
//			cardService.save(card1);
//			cardService.save(card2);
//			clientService.save(admin);
//
//		};
//	}
}
