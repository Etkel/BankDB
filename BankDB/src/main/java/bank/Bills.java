package bank;

import javax.persistence.*;
import java.util.*;


@Entity
public class Bills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "count_id")
    private Long countNumber;
    private Double balanceUSD;
    private Double balanceEUR;
    private Double balanceUAH;
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<Transactions> transaction = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "exchange_id")
    private ExchangeRates exchangeRates;

    public Bills(Double balanceUSD, Double balanceEUR, Double balanceUAH) {
        this.balanceUSD = balanceUSD;
        this.balanceEUR = balanceEUR;
        this.balanceUAH = balanceUAH;
    }

    public Bills() {
    }

    public void addTransaction(Transactions transactions) {
        getTransaction().add(transactions);
        transactions.setBill(this);
    }

    public void addExchangeRate(ExchangeRates exchangeRates) {
        setExchangeRates(exchangeRates);
        exchangeRates.getBills().add(this);
    }


    public void refillUSD(Double sum) {
        this.balanceUSD += sum;
        addTransaction(new Transactions(Status.DONE, Operation.REPLENISHMENT));
    }

    public void refillEUR(Double sum) {
        this.balanceEUR += sum;
        addTransaction(new Transactions(Status.DONE, Operation.REPLENISHMENT));
    }

    public void refillUAH(Double sum) {
        this.balanceUAH += sum;
        addTransaction(new Transactions(Status.DONE, Operation.REPLENISHMENT));
    }

    public void moneyTransferOperation(Bills to, Double sum, Currency currency) {
        switch (currency) {
            case EUR -> {
                if (getBalanceEUR() - sum > 0) {
                    setBalanceEUR(getBalanceEUR() - sum);
                    to.setBalanceEUR(getBalanceEUR() + sum);
                    addTransaction(new Transactions(Status.DONE, Operation.TRANSFER));
                    to.addTransaction(new Transactions(Status.DONE, Operation.REPLENISHMENT));
                } else {
                    addTransaction(new Transactions(Status.CANCELLED, Operation.TRANSFER));
                    System.out.println("No money ? No honey!");
                }

            }
            case USD -> {
                if (getBalanceUSD() - sum > 0) {
                    setBalanceUSD(getBalanceUSD() - sum);
                    to.setBalanceUSD(getBalanceUSD() + sum);
                    addTransaction(new Transactions(Status.DONE, Operation.TRANSFER));
                    to.addTransaction(new Transactions(Status.DONE, Operation.REPLENISHMENT));
                } else {
                    addTransaction(new Transactions(Status.CANCELLED, Operation.TRANSFER));
                    System.out.println("Balance can't be negative");
                }
            }
            case UAH -> {
                if (getBalanceUAH() - sum > 0) {
                    setBalanceUAH(getBalanceUAH() - sum);
                    to.setBalanceUAH(getBalanceUAH() + sum);
                    addTransaction(new Transactions(Status.DONE, Operation.TRANSFER));
                    to.addTransaction(new Transactions(Status.DONE, Operation.REPLENISHMENT));
                } else {
                    addTransaction(new Transactions(Status.CANCELLED, Operation.TRANSFER));
                    System.out.println("Balance can't be negative");
                }
            }
        }
    }

    public Double conversionToUAH(Currency currency, Double sum) {
        if (exchangeRates != null) {
            switch (currency) {
                case EUR -> {
                    if (getBalanceEUR() - sum > 0) {
                        setBalanceEUR(getBalanceEUR() - sum);
                        addTransaction(new Transactions(Status.DONE,Operation.EXCHANGE));
                        return exchangeRates.getBuyEUR() * getBalanceEUR();
                    } else {
                        System.out.println("Your balance is low!");
                        addTransaction(new Transactions(Status.CANCELLED,Operation.EXCHANGE));
                        return null;
                    }
                }
                case USD -> {
                    if (getBalanceUSD() - sum > 0) {
                        setBalanceUSD(getBalanceUSD() - sum);
                        addTransaction(new Transactions(Status.DONE,Operation.EXCHANGE));
                        return exchangeRates.getBuyUSD() * getBalanceUSD();
                    } else {
                        System.out.println("U got no cash! Lol");
                        addTransaction(new Transactions(Status.CANCELLED,Operation.EXCHANGE));
                        return null;
                    }
                }
                case UAH -> {
                    return getBalanceUAH();
                }
                default -> {
                    return null;
                }
            }
        } else {
            addTransaction(new Transactions(Status.CANCELLED,Operation.EXCHANGE));
            System.out.println("No exchange rates was found!");
            return null;
        }
    }


    public Double conversionFromUAH(Currency currency, Double sum) {
        if (exchangeRates != null) {
            switch (currency) {
                case EUR -> {
                    if (getBalanceUAH() - sum > 0) {
                        setBalanceUAH(getBalanceUAH() - sum);
                        addTransaction(new Transactions(Status.DONE,Operation.EXCHANGE));
                        return exchangeRates.getSaleEUR() * getBalanceEUR();
                    } else {
                        System.out.println("Your balance is low!");
                        addTransaction(new Transactions(Status.CANCELLED,Operation.EXCHANGE));
                        return null;
                    }
                }
                case USD -> {
                    if (getBalanceUAH() - sum > 0) {
                        setBalanceUAH(getBalanceUAH() - sum);
                        addTransaction(new Transactions(Status.DONE,Operation.EXCHANGE));
                        return exchangeRates.getSaleUSD() * getBalanceUSD();
                    } else {
                        System.out.println("U got no cash! Lol");
                        addTransaction(new Transactions(Status.CANCELLED,Operation.EXCHANGE));
                        return null;
                    }
                }
                case UAH -> {
                    return getBalanceUAH();
                }
                default -> {
                    return null;
                }
            }
        } else {
            addTransaction(new Transactions(Status.CANCELLED,Operation.EXCHANGE));
            System.out.println("No exchange rates was found!");
            return null;
        }
    }

    public Double totalCashToUAH() {
        if (exchangeRates != null) {
            double res = getBalanceUSD() * exchangeRates.getBuyUSD() +
                    getBalanceEUR() * exchangeRates.getBuyEUR() +
                    getBalanceUAH();
            addTransaction(new Transactions(Status.DONE, Operation.BALANCE));
            return res;
        } else {
            addTransaction(new Transactions(Status.CANCELLED,Operation.BALANCE));
            System.out.println("No exchange rates was found!");
            return null;
        }
    }


    public Long getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(Long countNumber) {
        this.countNumber = countNumber;
    }

    public Double getBalanceUSD() {
        return balanceUSD;
    }

    public void setBalanceUSD(Double balanceUSD) {
        this.balanceUSD = balanceUSD;
    }

    public Double getBalanceEUR() {
        return balanceEUR;
    }

    public void setBalanceEUR(Double balanceEUR) {
        this.balanceEUR = balanceEUR;
    }

    public Double getBalanceUAH() {
        return balanceUAH;
    }

    public void setBalanceUAH(Double balanceUAH) {
        this.balanceUAH = balanceUAH;
    }

    public Client getUser() {
        return client;
    }

    public void setUser(Client client) {
        this.client = client;
    }

    public List<Transactions> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<Transactions> transaction) {
        this.transaction = transaction;
    }

    public ExchangeRates getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(ExchangeRates exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    @Override
    public String toString() {
        return "Bills{" +
                "countNumber=" + countNumber +
                ", balanceUSD=" + balanceUSD +
                ", balanceEUR=" + balanceEUR +
                ", balanceUAH=" + balanceUAH +
                '}';
    }
}
