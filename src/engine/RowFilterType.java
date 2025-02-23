package engine;


public enum RowFilterType {
    EQ, GT, LT, GTE, LTE;

    static public RowFilterType getRowFilterType(String op) {
        switch (op) {
            case "=":
                return EQ;
            case "<=":
                return LTE;
            case ">=":
                return GTE;
            case "<":
                return LT;
            case ">":
                return GT;
        }
        return EQ;
    }
}
