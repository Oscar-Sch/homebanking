package com.mindhub.homebanking;

import antlr.collections.List;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
		return (args) -> {
			// save a couple of customers
			Client cli1=new Client("Melba", "Morel","melba@mindhub.com");
			Client cli2=new Client("Pancracio", "Martinez","panma@mindhub.com");
			Account ac1= new Account("54824ABCD", LocalDateTime.now(),5000);
			Account ac2= new Account("87341ZHWO", LocalDateTime.now().plusDays(1),7500);
			Account ac3= new Account("16772YBHS", LocalDateTime.now().minusDays(50),100000);
			Transaction tr1=new Transaction(TransactionType.CREDIT,5000,LocalDateTime.now(),"Primera transaccion del dia");
			Transaction tr2=new Transaction(TransactionType.DEBIT,2000,LocalDateTime.now().minusDays(20),"Compra en los chinos");
			Transaction tr3=new Transaction(TransactionType.CREDIT,70000,LocalDateTime.now().minusDays(1),"Dia de paga");
			Loan loan1= new Loan("Hipotecario",500000, Arrays.asList(12,24,36,48,60));
			Loan loan2= new Loan("Personal",100000, Arrays.asList(6,12,24));
			Loan loan3= new Loan("Automotriz",300000, Arrays.asList(6,12,24,36));
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
			cli1.addAccount(ac1);
			cli1.addAccount(ac2);
			cli2.addAccount(ac3);
//			System.out.println(cli1.toString());
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
			clientRepository.save(cli1);
			accountRepository.save(ac1);
			accountRepository.save(ac2);
			clientRepository.save(cli2);
			accountRepository.save(ac3);
			transactionRepository.save(tr1);
			transactionRepository.save(tr2);
			transactionRepository.save(tr3);
			clientLoanRepository.save(cl1);
			clientLoanRepository.save(cl2);
			clientLoanRepository.save(cl3);
			clientLoanRepository.save(cl4);
		};
	}
}
