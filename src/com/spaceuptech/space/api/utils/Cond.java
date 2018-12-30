package com.spaceuptech.space.api.utils;

/**
 * Class used to represent a single condition.
 */
public class Cond extends Condition {
    public String f1;
    public Object f2;
    public String eval;

    /**
     * Creates a single condition. Use Cond with AND.create or OR.create to combine multiple conditions.
     * @param f1 First operand.
     * @param eval Type of comparison (>, <, >= , <=, ==, !=)
     * @param f2 Second operand.
     */
    public Cond(String f1, String eval, Object f2) {
        this.condType = CondType.COND;
        this.f1 = f1;
        this.f2 = f2;
        this.eval = eval;
    }

    public static Cond create(String f1, String eval, Object f2) {
        return new Cond(f1, eval, f2);
    }
}
