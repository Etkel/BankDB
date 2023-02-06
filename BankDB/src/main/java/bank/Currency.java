package bank;

public enum Currency {
    EUR("EUR"), USD("USD"), UAH("UAH");

    private String v;

    Currency(String v) {
        this.v = v;
    }

    public String getV() {
        return v;
    }

    @Override
    public String toString() {
        return v;
    }
}
