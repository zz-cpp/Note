package intexpr;

class Number implements IntegerExpression {
    private final int n;
    
    // Abstraction function
    //    AF(n) = the integer n
    // Rep invariant
    //    true
    // Safety from rep exposure
    //    all fields are immutable and final
    
    /** Make a Number. */
    public Number(int n) {
        this.n = n;
    }
    
    @Override 
    public int value() {
        return n;
    }
    
    @Override public String toString() {
        return "Number(" + n + ")";
    }

    // TODO: implement equals() and hashCode()?
}
