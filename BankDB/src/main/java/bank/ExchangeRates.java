package bank;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "Course")
public class ExchangeRates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exchangeId;
    private Double rateUSD;
    private Double rateEUR;
    @OneToMany(mappedBy = "exchangeRates")
    private List<Bills> bills = new ArrayList<>();


    public ExchangeRates(Double rateUSD, Double rateEUR) {
        this.rateUSD = rateUSD;
        this.rateEUR = rateEUR;
    }

    public ExchangeRates() {
    }

    public void addCount(Bills bills) {
        this.bills.add(bills);
        bills.setExchangeRates(this);
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Double getRateUSD() {
        return rateUSD;
    }

    public void setRateUSD(Double rateUSD) {
        this.rateUSD = rateUSD;
    }

    public Double getRateEUR() {
        return rateEUR;
    }

    public void setRateEUR(Double rateEUR) {
        this.rateEUR = rateEUR;
    }

    public List<Bills> getBills() {
        return bills;
    }

    public void setBills(List<Bills> bills) {
        this.bills = bills;
    }

    @Override
    public String toString() {
        return "ExchangeRates{" +
                "exchangeId=" + exchangeId +
                ", rateUSD=" + rateUSD +
                ", rateEUR=" + rateEUR +
                '}';
    }
}
