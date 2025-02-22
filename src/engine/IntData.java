package engine;

class IntData implements Data {
    int maxVal;
    int minVal;
    public IntData(int maxVal, int minVal) {
        this.maxVal = maxVal;
        this.minVal = minVal;
    }
    public boolean validate(Object value) {
        if(!(value instanceof Integer)) {
            return false;
        }
        int intVal = (Integer)value;
        return intVal >= minVal && intVal <= maxVal;
    }


}