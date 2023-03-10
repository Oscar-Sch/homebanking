package com.mindhub.homebanking;

import antlr.collections.List;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.mindhub.homebanking.utils.Utilities.cardCvvGenerator;
import static com.mindhub.homebanking.utils.Utilities.handleCardNumberGeneration;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers
			Client cli1=new Client("Melba", "Morel","melba@mindhub.com",passwordEncoder.encode("asd95321"),"Melba85","2215483251");
			Client cli2=new Client("Pancracio", "Martinez","panma@mindhub.com",passwordEncoder.encode("54a4fa7gt"),"Pancanator","1154832512");
			Client admin= new Client("admin","admin","admin@mindhub.com",passwordEncoder.encode("123456"),"Godlike","0800453842");
			Account ac1= new Account("VIN-58732618", LocalDateTime.now(),5000);
			Account ac2= new Account("VIN-35746558", LocalDateTime.now().plusDays(1),7500);
			Account ac3= new Account("VIN-96821167", LocalDateTime.now().minusDays(50),100000);
			Transaction tr1=new Transaction(TransactionType.CREDIT,5000,LocalDateTime.now(),"Primera transaccion del dia");
			Transaction tr2=new Transaction(TransactionType.DEBIT,2000,LocalDateTime.now().minusDays(20),"Compra en los chinos");
			Transaction tr3=new Transaction(TransactionType.CREDIT,70000,LocalDateTime.now().minusDays(1),"Dia de paga");
			Transaction tr4=new Transaction(TransactionType.DEBIT,1000,LocalDateTime.now().minusDays(1),"Pedidos ya");
			Transaction tr5=new Transaction(TransactionType.CREDIT,50000,LocalDateTime.now().minusDays(1),"me saque la grande");
			Loan loan1= new Loan("Mortgage",500000, Arrays.asList(12,24,36,48,60), Arrays.asList(10.0,20.0,45.0,70.0,95.0));
			Loan loan2= new Loan("Personal",100000, Arrays.asList(6,12,24), Arrays.asList(7.0,13.0,25.0));
			Loan loan3= new Loan("Automotive",300000, Arrays.asList(6,12,24,36), Arrays.asList(10.0,15.0,30.0,45.0));
			ClientLoan cl1= new ClientLoan(40000,60);
			ClientLoan cl2= new ClientLoan(50000,12);
			ClientLoan cl3= new ClientLoan(100000,24);
			ClientLoan cl4= new ClientLoan(200000,36);
			cli1.addClientLoan(cl1);
			loan1.addClientLoan(cl1);
			cli1.addClientLoan(cl2);
			loan2.addClientLoan(cl2);
			cli2.addClientLoan(cl3);
			loan2.addClientLoan(cl3);
			cli2.addClientLoan(cl4);
			loan3.addClientLoan(cl4);
			ac1.addTransaction(tr1);
			ac1.addTransaction(tr2);
			ac3.addTransaction(tr3);
			ac1.addTransaction(tr4);
			ac1.addTransaction(tr5);
			cli1.addAccount(ac1);
			cli1.addAccount(ac2);
			cli2.addAccount(ac3);
//			System.out.println(cli1.toString());
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
			clientRepository.save(cli1);
			accountRepository.save(ac2);
			accountRepository.save(ac1);
			clientRepository.save(cli2);
			accountRepository.save(ac3);
			transactionRepository.save(tr1);
			transactionRepository.save(tr2);
			transactionRepository.save(tr3);
			transactionRepository.save(tr4);
			transactionRepository.save(tr5);
			clientLoanRepository.save(cl1);
			clientLoanRepository.save(cl2);
			clientLoanRepository.save(cl3);
			clientLoanRepository.save(cl4);

			Card card1=new Card(cli1.getFirstName()+" "+cli1.getLastName(), CardType.CREDIT,CardColor.GOLD);
			Card card2=new Card(cli1.getFirstName()+" "+cli1.getLastName(), CardType.DEBIT,CardColor.SILVER);
			card1.setNumber(handleCardNumberGeneration(cardRepository));
			card1.setCvv(cardCvvGenerator());
			card2.setNumber(handleCardNumberGeneration(cardRepository));
			card2.setCvv(cardCvvGenerator());
			cli1.addCard(card1);
			cli1.addCard(card2);
			clientRepository.save(cli1);
			cardRepository.save(card1);
			cardRepository.save(card2);
			clientRepository.save(admin);

		};
	}
}
