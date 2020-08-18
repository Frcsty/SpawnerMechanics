package com.github.frcsty.spawnermechanics.wrapper.economy;

public final class TaxHolder {

    private int amount = 0;
    private int remaining = 0;

    public void setAmount(final int value) {
        this.amount = value;
    }

    public void setRemaining(final int value) {
        this.remaining = value;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getRemaining() {
        return this.remaining;
    }
}
