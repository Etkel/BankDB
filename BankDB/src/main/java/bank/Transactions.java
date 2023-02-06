package bank;

import javax.persistence.*;
@Entity
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionNumber;
    private String status;
    private String operation;
    @ManyToOne
    @JoinColumn(name = "count_id")
    private Bills bill;

    public Transactions(Status status, Operation operation) {
        this.status = status.getV();
        this.operation = operation.getV();
    }

    public Transactions() {
    }


    public void addCount(Bills c){
        setBill(c);
        c.getTransaction().add(this);
    }

    public long getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(long transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Bills getBill() {
        return bill;
    }

    public void setBill(Bills bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "transactionNumber=" + transactionNumber +
                ", status='" + status + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}
