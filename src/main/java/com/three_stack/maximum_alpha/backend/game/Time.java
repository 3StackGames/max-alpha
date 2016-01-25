package com.three_stack.maximum_alpha.backend.game;

public class Time {
    protected final int value;
    protected static final Time setup;

    static {
        setup = new Time(0);
    }

    public static Time getSetup() {
        return setup;
    }

    /**
     * Constructor should ONLY be called in state's getTime() method
     * @param value
     */
    protected Time(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Time time = (Time) o;

        return value == time.value;

    }

    @Override
    public int hashCode() {
        return value;
    }
}