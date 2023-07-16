package Engine;

public class ALUInstruction extends Instruction {

    public Register Rs;
    public Register Rt;

    public ALUInstruction(Type type, Register rd, Register rs, Register rt) {

        super.type = type;
        super.Rd = rd;
        Rs = rs;
        Rt = rt;
    }

    // create toString method
    @Override
    public String toString() {
        return "" + type + "  " + Rd.name + "  " + Rs.name + "  " + Rt.name;
    }

}
