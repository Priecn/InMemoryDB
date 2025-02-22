package engine;

public class StringData implements Data {
    int maxLen;
    int minLen;
    public StringData(int maxLen, int minLen) {
        this.maxLen = maxLen;
        this.minLen = minLen;
    }
    public boolean validate(Object value) {
        if(!(value instanceof java.lang.String stringVal)) {
            return false;
        }
        return stringVal.length() >= minLen && stringVal.length() <= maxLen;
    }
}
