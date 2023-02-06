package bank;

import javax.persistence.*;
import java.util.Scanner;


public class Main {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            emf = Persistence.createEntityManagerFactory("Bank");
            em = emf.createEntityManager();
            try {
            //Count replenishment
                em.getTransaction().begin();
                Bills bill1 = new Bills(100d, 100d, 100d);
                em.persist(bill1);
                refillUAH(75D, bill1);
                //check
                TypedQuery<Bills> query1 = em.createQuery("SELECT o FROM Bills o", Bills.class);
                System.out.println(query1.getResultList());
                em.getTransaction().commit();
            //Transfer money from one count to another
                em.getTransaction().begin();
                Bills bill2 = new Bills(100d, 100d, 100d);
                em.persist(bill2);
                Bills bill3 = new Bills(100d, 100d, 100d);
                em.persist(bill3);
                transferMoney(Currency.USD, bill2, bill3, 74d);
                //check
                TypedQuery<Bills> query2 = em.createQuery("SELECT o FROM Bills o WHERE o.id > 1", Bills.class);
                System.out.println(query2.getResultList());
                em.getTransaction().commit();
            //Conversion for Bills id = 1;
                em.getTransaction().begin();
                ExchangeRates exchangeRates = new ExchangeRates(25.54d, 28.7d);
                em.persist(exchangeRates);
                conversionCurrency(bill1, exchangeRates, Currency.EUR);
                //check
                TypedQuery<Bills> query3 = em.createQuery("SELECT o FROM Bills o WHERE o.id = 1", Bills.class);
                System.out.println(query3.getResultList());
                em.getTransaction().commit();
            //Total balance in UAH
                em.getTransaction().begin();
                Client client1 = new Client("Ollan Jonsan", 42342342L);
                em.persist(client1);
                client1.setCounts(bill3);
                bill3.setUser(client1);
                System.out.println(totalBalanceUAH(client1, exchangeRates));
                em.getTransaction().commit();
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void refillUAH(Double sum, Bills bills) {
        Query query = em.createQuery("UPDATE Bills b SET b.balanceUAH = " +
                "b.balanceUAH + :s WHERE b.countNumber = :id");
        query.setParameter("s", sum);
        query.setParameter("id", bills.getCountNumber());
        query.executeUpdate();
        em.refresh(bills);
    }

    public static void refillEUR(Double sum, Bills bills) {
        Query query = em.createQuery("UPDATE Bills b SET b.balanceEUR = " +
                "b.balanceEUR + :s WHERE b.countNumber = :id");
        query.setParameter("s", sum);
        query.setParameter("id", bills.getCountNumber());
        query.executeUpdate();
        em.refresh(bills);
    }

    public static void refillUSD(Double sum, Bills bills) {
        Query query = em.createQuery("UPDATE Bills b SET b.balanceUSD = " +
                "b.balanceUSD + :s WHERE b.countNumber = :id");
        query.setParameter("s", sum);
        query.setParameter("id", bills.getCountNumber());
        query.executeUpdate();
        em.refresh(bills);
    }

    public static void unRefillUAH(Double sum, Bills bills) {
        Query query = em.createQuery("UPDATE Bills b SET b.balanceUAH = " +
                "b.balanceUAH - :s WHERE b.countNumber = :id");
        query.setParameter("s", sum);
        query.setParameter("id", bills.getCountNumber());
        query.executeUpdate();
        em.refresh(bills);
    }

    public static void unRefillEUR(Double sum, Bills bills) {
        Query query = em.createQuery("UPDATE Bills b SET b.balanceEUR = " +
                "b.balanceEUR - :s WHERE b.countNumber = :id");
        query.setParameter("s", sum);
        query.setParameter("id", bills.getCountNumber());
        query.executeUpdate();
        em.refresh(bills);
    }

    public static void unRefillUSD(Double sum, Bills bills) {
        Query query = em.createQuery("UPDATE Bills b SET b.balanceUSD = " +
                "b.balanceUSD - :s WHERE b.countNumber = :id");
        query.setParameter("s", sum);
        query.setParameter("id", bills.getCountNumber());
        query.executeUpdate();
        em.refresh(bills);
    }

    public static void transferMoney(Currency currency, Bills from, Bills to, Double sum) {
        switch (currency) {
            case UAH -> {
                unRefillUAH(sum, from);
                refillUAH(sum, to);
            }
            case USD -> {
                unRefillUSD(sum, from);
                refillUSD(sum, to);
            }
            case EUR -> {
                unRefillEUR(sum, from);
                refillEUR(sum, to);
            }
        }
    }

    public static void conversionCurrency(Bills bills, ExchangeRates exchangeRates, Currency currency) {
        if (Currency.USD == currency) {
            Query query = em.createQuery("UPDATE Bills b SET b.balanceUAH = " +
                    "b.balanceUSD * :s, b.balanceUSD = 0  WHERE b.countNumber = :id");
            query.setParameter("s", exchangeRates.getRateUSD());
            query.setParameter("id", bills.getCountNumber());
            query.executeUpdate();
            em.refresh(bills);
        }
        if (Currency.EUR == currency) {
            Query query = em.createQuery("UPDATE Bills b SET b.balanceUAH = " +
                    "b.balanceEUR * :s, b.balanceEUR = 0 WHERE b.countNumber = :id");
            query.setParameter("s", exchangeRates.getRateEUR());
            query.setParameter("id", bills.getCountNumber());
            query.executeUpdate();
            em.refresh(bills);
        }
        if (Currency.UAH == currency) {
            System.out.println("Nothing happen! lol");
            em.refresh(bills);
        }
    }

    public static Double totalBalanceUAH(Client client, ExchangeRates exchangeRates) {
        TypedQuery<Double> count = em.createQuery("SELECT o.balanceUAH + (o.balanceEUR * :e) + " +
                "(o.balanceUSD * :u) FROM Bills o WHERE o.client.clientKey = :id", Double.class);
        count.setParameter("e", exchangeRates.getRateEUR());
        count.setParameter("u", exchangeRates.getRateUSD());
        count.setParameter("id", client.getClientKey());
        return count.getSingleResult();
    }
}
