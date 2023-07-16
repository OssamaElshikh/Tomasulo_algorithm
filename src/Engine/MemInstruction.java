package Engine;

public class MemInstruction extends Instruction {

    public int address;

    public MemInstruction(Type type, Register rd, int address) {
        super.type = type;
        super.Rd = rd;
        this.address = address;
    }

    // create toString method
    @Override
    public String toString() {
        return  "" + type + "  " + Rd.name + "  " + address;
    }

}
