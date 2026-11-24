package model;

import exceptions.AccountException;
import java.math.BigDecimal;

public abstract class BankAccount {
    private String customerName;
    private final long accountNumber;
    private Long userId;
    private String userPassword;
    private Double accountBalance = 0.0;

    public BankAccount(String customerName, long accountNumber, Long userId, String userPassword) {
        this.accountNumber = accountNumber;
        setCustomerName(customerName);
        setUserId(userId);
        setUserPassword(userPassword);
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String name) {
        if (name == null || name.isBlank()) {
            throw new AccountException("Введіть ім’я власника.");
        }
        customerName = name.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        if (id == null) {
            throw new AccountException("Вкажіть ID користувача.");
        }
        userId = id;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String password) {
        if (password == null || password.length() < 8 || password.chars().noneMatch(Character::isDigit)) {
            throw new AccountException("Пароль має містити щонайменше 8 символів і хоча б одну цифру.");
        }
        userPassword = password;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double balance) {
        if (balance == null || !Double.isFinite(balance) || balance < 0) {
            throw new AccountException("Баланс має бути скінченним невід’ємним числом.");
        }
        accountBalance = balance;
    }

    protected static BigDecimal positiveAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new AccountException("Сума має бути більша за нуль.");
        }
        return BigDecimal.valueOf(amount);
    }

    protected void updateBalance(BigDecimal balance) {
        double value = balance.doubleValue();
        if (!Double.isFinite(value) || BigDecimal.valueOf(value).compareTo(balance) != 0) {
            throw new AccountException("Сума перевищує доступну точність балансу.");
        }
        setAccountBalance(value);
    }

    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);
}
