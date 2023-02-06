package bank;

import javax.persistence.*;

@Entity
@Table(name = "Clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long clientKey;
    private String name;
    private Long phone;
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private Bills bills;

    public Client(String name, Long phone) {
        this.name = name;
        this.phone = phone;
    }

    public Client() {
    }

    public Long getClientKey() {
        return clientKey;
    }

    public void setClientKey(Long clientKey) {
        this.clientKey = clientKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Bills getCounts() {
        return bills;
    }

    public void setCounts(Bills bills) {
        this.bills = bills;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + clientKey +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                '}';
    }
}
