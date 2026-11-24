import exceptions.AccountException;
import model.CheckingAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientInterface {
    private final Map<Long, CheckingAccount> accounts = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private long nextAccountNumber = 1;

    public void start() {
        while (true) {
            System.out.println("\n1 — Створити рахунок\n2 — Видалити рахунок\n3 — Поповнити рахунок"
                    + "\n4 — Зняти кошти\n5 — Переглянути рахунок\n6 — Вийти");
            try {
                switch (read("Оберіть дію").trim()) {
                    case "1" -> addNewAccount();
                    case "2" -> deleteAccount();
                    case "3" -> deposit();
                    case "4" -> withdraw();
                    case "5" -> printInfo();
                    case "6" -> { return; }
                    default -> throw new AccountException("Введіть номер від 1 до 6.");
                }
            } catch (AccountException error) {
                System.err.println("Помилка: " + error.getMessage());
            } catch (NoSuchElementException endOfInput) {
                return;
            }
        }
    }

    private String read(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    private long readId() {
        try {
            return Long.parseLong(read("ID користувача").trim());
        } catch (NumberFormatException error) {
            throw new AccountException("ID має бути цілим числом.");
        }
    }

    private double readAmount() {
        try {
            return Double.parseDouble(read("Сума").trim().replace(',', '.'));
        } catch (NumberFormatException error) {
            throw new AccountException("Введіть числову суму.");
        }
    }

    private CheckingAccount authenticate() {
        long id = readId();
        String password = read("Пароль");
        CheckingAccount account = accounts.get(id);
        if (account == null || !account.getUserPassword().equals(password)) {
            throw new AccountException("Неправильний ID або пароль.");
        }
        return account;
    }

    private void addNewAccount() {
        String name = read("Ім’я та прізвище");
        long id = readId();
        if (accounts.containsKey(id)) {
            throw new AccountException("Такий ID уже використано.");
        }
        String password = read("Пароль");
        if (!password.equals(read("Повторіть пароль"))) {
            throw new AccountException("Паролі не збігаються.");
        }
        CheckingAccount account = new CheckingAccount(name, nextAccountNumber, id, password);
        accounts.put(id, account);
        nextAccountNumber++;
        System.out.println("Рахунок створено.");
    }

    private void deleteAccount() {
        CheckingAccount account = authenticate();
        if (account.getAccountBalance() != 0.0) {
            throw new AccountException("Для видалення баланс має дорівнювати нулю.");
        }
        accounts.remove(account.getUserId());
        System.out.println("Рахунок видалено.");
    }

    private void deposit() {
        CheckingAccount account = authenticate();
        account.deposit(readAmount());
        System.out.println("Рахунок поповнено.");
    }

    private void withdraw() {
        CheckingAccount account = authenticate();
        account.withdraw(readAmount());
        System.out.println("Кошти знято.");
    }

    private void printInfo() {
        CheckingAccount account = authenticate();
        System.out.printf("Власник: %s%nНомер рахунку: %d%nID: %d%nТип: Checking%nБаланс: %s%nДенний ліміт: %s%n",
                account.getCustomerName(), account.getAccountNumber(), account.getUserId(),
                account.getAccountBalance(), account.getDailyWithdrawalLimit());
    }
}
