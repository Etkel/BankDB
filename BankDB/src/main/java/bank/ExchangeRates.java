package bank;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "Course")
public class ExchangeRates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exchangeId;
    private Double buyUSD;
    private Double buyEUR;
    private Double saleUSD;
    private Double saleEUR;
    @OneToMany(mappedBy = "exchangeRates")
    private List<Bills> bills = new ArrayList<>();


    public ExchangeRates(Double buyUSD, Double buyEUR, Double saleUSD, Double saleEUR) {
        this.buyUSD = buyUSD;
        this.buyEUR = buyEUR;
        this.saleUSD = saleUSD;
        this.saleEUR = saleEUR;
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

    public Double getBuyUSD() {
        return buyUSD;
    }

    public void setBuyUSD(Double buyUSD) {
        this.buyUSD = buyUSD;
    }

    public Double getBuyEUR() {
        return buyEUR;
    }

    public void setBuyEUR(Double buyEUR) {
        this.buyEUR = buyEUR;
    }

    public Double getSaleUSD() {
        return saleUSD;
    }

    public void setSaleUSD(Double saleUSD) {
        this.saleUSD = saleUSD;
    }

    public Double getSaleEUR() {
        return saleEUR;
    }

    public void setSaleEUR(Double saleEUR) {
        this.saleEUR = saleEUR;
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
                ", buyUSD=" + buyUSD +
                ", buyEUR=" + buyEUR +
                ", saleUSD=" + saleUSD +
                ", saleEUR=" + saleEUR +
                '}';
    }
}
