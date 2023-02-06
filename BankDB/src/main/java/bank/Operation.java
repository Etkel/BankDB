package bank;

public enum Operation {
    TRANSFER("transfer"), REPLENISHMENT("replenishment"), EXCHANGE("exchange"), BALANCE("balance check");

    private String v;

    Operation(String v) {
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
