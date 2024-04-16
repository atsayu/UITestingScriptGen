package objects;

public interface Expression extends Comparable<Expression>{
    public boolean isExprEquals(Object obj);
    public String exprToString();
    public String getType();

    @Override
    public int compareTo(Expression o);
}
