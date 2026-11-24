package model;

import exceptions.AccountException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CheckingAccount extends BankAccount {
    private double dailyWithdrawalLimit = 300;
    private LocalDate lastWithdrawalDate;
    private BigDecimal withdrawnToday = BigDecimal.ZERO;

    public CheckingAccount(String customerName, long accountNumber, Long userId, String userPassword) {
        super(customerName, accountNumber, userId, userPassword);
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double limit) {
        if (!Double.isFinite(limit) || limit < 0) {
            throw new AccountException("Денний ліміт має бути невід’ємним числом.");
        }
        dailyWithdrawalLimit = limit;
    }

    @Override
    public void deposit(double amount) {
        updateBalance(BigDecimal.valueOf(getAccountBalance()).add(positiveAmount(amount)));
    }

    @Override
    public void withdraw(double amount) {
        BigDecimal requested = positiveAmount(amount);
        LocalDate today = LocalDate.now();
        BigDecimal previous = today.equals(lastWithdrawalDate) ? withdrawnToday : BigDecimal.ZERO;
        BigDecimal newTotal = previous.add(requested);
        if (newTotal.compareTo(BigDecimal.valueOf(dailyWithdrawalLimit)) > 0) {
            throw new AccountException("Перевищено денний ліміт зняття.");
        }
        BigDecimal balance = BigDecimal.valueOf(getAccountBalance());
        if (requested.compareTo(balance) > 0) {
            throw new AccountException("Недостатньо коштів на рахунку.");
        }
        updateBalance(balance.subtract(requested));
        lastWithdrawalDate = today;
        withdrawnToday = newTotal;
    }
}
