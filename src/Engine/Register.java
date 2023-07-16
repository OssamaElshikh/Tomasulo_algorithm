package Engine;

public class Register {

    String name;
    float value;
    String Qi = null;

    public Register(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public Register(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public String getQi() {
        return Qi;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public void setQi(String qi) {
        Qi = qi;
    }

    // create toString method
    @Override
    public String toString() {
        return "{" + name +
                ", Vi= " + value +
                ", Qi= " + Qi +
                '}';
    }

    public Register getRegister(String name) {
        if(this.name.equals(name)) {
            return this;
        }
        return null;
    }

}
