package bank;

public enum Status {
    DONE("done"), CANCELLED("cancelled");

    private String v;

    Status(String v) {
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
