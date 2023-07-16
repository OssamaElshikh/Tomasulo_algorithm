package Engine;

public class Memory {

    float[] memory = new float[32];

    public Memory() {
        for (int i = 0; i < 32; i++) {
            memory[i] = (float) i;
        }
    }

    public float read(int address) {
        return memory[address];
    }

    public void write(int address, float value) {
        memory[address] = value;
    }

    // create toString method
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < 32; i++) {
            s += "M[" + i + "] = " + memory[i] + "\n";
        }
        return s;
    }

}
