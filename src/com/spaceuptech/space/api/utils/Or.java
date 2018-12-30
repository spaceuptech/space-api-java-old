package com.spaceuptech.space.api.utils;

/**
 * Class representing OR condition for where clauses.
 */
public class Or extends Condition {
    public Condition conds[];

    public Or(CondType condType, Condition conds[]) {
        this.condType = condType;
        this.conds = conds;
    }

    /**
     * Creates where clause by performing OR logic operation on multiple conditions.
     * @param conds Multiple conditions that needs to be logically OR.
     * @return
     */
    public static Or create(Condition ...conds) {
        return new Or(CondType.OR, conds);
    }
}
