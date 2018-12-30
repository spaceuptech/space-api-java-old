package com.spaceuptech.space.api.utils;

/**
 * Class representing AND condition for where clauses.
 */
public class And extends Condition {
    public Condition conds[];

    public And(CondType condType, Condition conds[]) {
        this.condType = condType;
        this.conds = conds;
    }

    /**
     * Creates where clause by performing AND logic operation on multiple conditions.
     * @param conds Multiple conditions that needs to be logically AND.
     * @return
     */
    public static And create(Condition ...conds) {
        return new And(CondType.AND, conds);
    }
}
