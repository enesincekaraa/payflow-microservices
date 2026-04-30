package com.payflow.accountservice.application.service;

import com.payflow.accountservice.domain.model.AccountStatus;
import com.payflow.accountservice.domain.model.Transaction;
import com.payflow.accountservice.domain.model.TransactionType;
import com.payflow.accountservice.infrastructure.repository.AccountRepository;
import com.payflow.accountservice.infrastructure.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DailyReportService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DailyReportService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void generateDailyReport() {
        LocalDateTime startOfDay = LocalDate.now()
                .minusDays(1).atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atStartOfDay();

        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("📊 GÜNLÜK RAPOR — {}",
                LocalDate.now().minusDays(1));
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");


        List<Transaction> allTx= transactionRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(
                startOfDay,endOfDay
        );

        long paymentCount = allTx.stream()
                .filter(tx->tx.getType() == TransactionType.PAYMENT)
                .count();


        BigDecimal paymentVolume = allTx.stream()
                .filter(tx->tx.getType() == TransactionType.PAYMENT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        long depositCount = allTx.stream()
                .filter(tx -> tx.getType() == TransactionType.DEPOSIT)
                .count();

        BigDecimal depositVolume = allTx.stream()
                .filter(tx -> tx.getType() == TransactionType.DEPOSIT)
                .map(com.payflow.accountservice.domain.model.Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long suspendedAccounts = accountRepository
                .findAllByStatus(AccountStatus.SUSPENDED).size();

        log.info("💳 Ödemeler    : {} işlem | {} TRY",
                paymentCount, paymentVolume);
        log.info("💰 Yatırmalar  : {} işlem | {} TRY",
                depositCount, depositVolume);
        log.info("📦 Toplam      : {} işlem",
                allTx.size());
        log.info("🚨 Askıdaki    : {} hesap",
                suspendedAccounts);
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

    }


    @Scheduled(initialDelay = 10000,fixedDelay = Long.MAX_VALUE)
    @Transactional(readOnly = true)
    public void generateReportOnStartup() {
        log.info("🔄 Başlangıç raporu oluşturuluyor...");

        generateDailyReport();
    }
}
