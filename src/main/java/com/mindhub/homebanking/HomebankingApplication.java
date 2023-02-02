package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			// save a couple of customers
			Client cli1=new Client("Melba", "Morel","melba@mindhub.com");
			Account ac1= new Account("54824ABCD", LocalDateTime.now(),5000);
			Account ac2= new Account("87341ZHWO", LocalDateTime.now().plusDays(1),7500);
			Account ac3= new Account("16772YBHS", LocalDateTime.now().minusDays(50),100000);
			Transaction tr1=new Transaction(TransactionType.CREDIT,5000,LocalDateTime.now(),"Primera transaccion del dia");
			Transaction tr2=new Transaction(TransactionType.DEBIT,2000,LocalDateTime.now().minusDays(20),"Compra en los chinos");
			Transaction tr3=new Transaction(TransactionType.CREDIT,70000,LocalDateTime.now().minusDays(1),"Dia de paga");
			ac1.addTransaction(tr1);
			ac1.addTransaction(tr2);
			ac3.addTransaction(tr3);
			cli1.addAccount(ac1);
			cli1.addAccount(ac2);
			Client cli2=new Client("Pancracio", "Martinez","panma@mindhub.com");
			cli2.addAccount(ac3);
//			System.out.println(cli1.toString());
			clientRepository.save(cli1);
			accountRepository.save(ac1);
			accountRepository.save(ac2);
			clientRepository.save(cli2);
			accountRepository.save(ac3);
			transactionRepository.save(tr1);
			transactionRepository.save(tr2);
			transactionRepository.save(tr3);
		};
	}
}
