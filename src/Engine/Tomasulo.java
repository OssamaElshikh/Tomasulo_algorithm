package Engine;

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class Tomasulo {

    static String fileString = "";

    static int clk = 0;
    static int AddLatency;
    static int MulLatency;
    static int DivLatency;
    static int LoadLatency;
    static int StoreLatency;
    static int addressFlag = -1;
    static RSentry[] AddRS = new RSentry[3];
    static RSentry[] MulRS = new RSentry[2];
    static LoadEntry[] LoadBuffer = new LoadEntry[3];
    static StoreEntry[] StoreBuffer = new StoreEntry[3];
    static boolean isStalledAdd = false;
    static boolean isStalledMul = false;
    static boolean isStalledLoad = false;
    static boolean isStalledStore = false;
    static boolean addressStall = false;
    static String output = "";
    static boolean finishFlag = false;
    
    static ArrayList<Register> registerFile = new ArrayList<>();
    static Queue<Instruction> instructions = new LinkedList<>();
    static ArrayList<String> instructionQueue = new ArrayList<>();
    static Memory memory = new Memory();
    static HashMap<Integer, Boolean> tracker = new HashMap<>();
    static void addRegister(Register reg) {
        registerFile.add(reg);
    }

    static void addRegister(String name, float value) {
        registerFile.add(new Register(name, value));
    }

    static void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    static Register getRegister(String Name) {
        for (Register reg : registerFile) {
            if (reg.getName().equals(Name)) {
                return reg;
            }
        }
        return null;
    }

    static void issueInstruction() {
        if (!instructions.isEmpty()) {
            if (instructions.peek().type == Type.ADD || instructions.peek().type == Type.SUB) {
                if (AddRS[0].Busy == 0) {
                    AddRS[0].Busy = 1;
                    AddRS[0].Op = instructions.peek().type;
                    AddRS[0].Latency = AddLatency;
                    if (getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi() == null) {
                        AddRS[0].Vj = ((ALUInstruction) instructions.peek()).Rs.getValue();
                    } else {
                        AddRS[0].Qj = getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi();
                    }
                    if (getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi() == null) {
                        AddRS[0].Vk = ((ALUInstruction) instructions.peek()).Rt.getValue();
                    } else {
                        AddRS[0].Qk = getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi();
                    }
                    getRegister(instructions.peek().Rd.getName()).setQi(AddRS[0].Name);
                    AddRS[0].issueTime = clk;
                    tracker.put(clk, false);
                    System.out
                            .println("Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk);
                    output += "Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk + "\n";
                    instructions.remove();
                } else if (AddRS[1].Busy == 0) {
                    AddRS[1].Busy = 1;
                    AddRS[1].Op = instructions.peek().type;
                    AddRS[1].Latency = AddLatency;
                    if (getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi() == null) {
                        AddRS[1].Vj = ((ALUInstruction) instructions.peek()).Rs.getValue();
                    } else {
                        AddRS[1].Qj = getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi();
                    }
                    if (getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi() == null) {
                        AddRS[1].Vk = ((ALUInstruction) instructions.peek()).Rt.getValue();
                    } else {
                        AddRS[1].Qk = getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi();
                    }
                    getRegister(instructions.peek().Rd.getName()).setQi(AddRS[1].Name);
                    AddRS[1].issueTime = clk;
                    tracker.put(clk, false);
                    System.out
                            .println("Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk);
                    output += "Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk + "\n";
                    instructions.remove();
                } else if (AddRS[2].Busy == 0) {
                    AddRS[2].Busy = 1;
                    AddRS[2].Op = instructions.peek().type;
                    AddRS[2].Latency = AddLatency;
                    if (getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi() == null) {
                        AddRS[2].Vj = ((ALUInstruction) instructions.peek()).Rs.getValue();
                    } else {
                        AddRS[2].Qj = getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi();
                    }
                    if (getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi() == null) {
                        AddRS[2].Vk = ((ALUInstruction) instructions.peek()).Rt.getValue();
                    } else {
                        AddRS[2].Qk = getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi();
                    }
                    getRegister(instructions.peek().Rd.getName()).setQi(AddRS[2].Name);
                    AddRS[2].issueTime = clk;
                    tracker.put(clk, false);
                    System.out
                            .println("Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk);
                    output += "Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk + "\n";
                    instructions.remove();
                } else {
                    isStalledAdd = true;
                }
            } else if (instructions.peek().type == Type.MUL || instructions.peek().type == Type.DIV) {
                if (MulRS[0].Busy == 0) {
                    MulRS[0].Busy = 1;
                    MulRS[0].Op = instructions.peek().type;
                    MulRS[0].Latency = (instructions.peek().type == Type.MUL) ? MulLatency : DivLatency;
                    if (getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi() == null) {
                        MulRS[0].Vj = ((ALUInstruction) instructions.peek()).Rs.getValue();
                    } else {
                        MulRS[0].Qj = getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi();
                    }
                    if (getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi() == null) {
                        MulRS[0].Vk = ((ALUInstruction) instructions.peek()).Rt.getValue();
                    } else {
                        MulRS[0].Qk = getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi();
                    }
                    getRegister(instructions.peek().Rd.getName()).setQi(MulRS[0].Name);
                    MulRS[0].issueTime = clk;
                    tracker.put(clk, false);
                    System.out
                            .println("Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk);
                    output += "Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk + "\n";
                    instructions.remove();
                } else if (MulRS[1].Busy == 0) {
                    MulRS[1].Busy = 1;
                    MulRS[1].Op = instructions.peek().type;
                    MulRS[1].Latency = (instructions.peek().type == Type.MUL) ? MulLatency : DivLatency;
                    if (getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi() == null) {
                        MulRS[1].Vj = ((ALUInstruction) instructions.peek()).Rs.getValue();
                    } else {
                        MulRS[1].Qj = getRegister(((ALUInstruction) instructions.peek()).Rs.getName()).getQi();
                    }
                    if (getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi() == null) {
                        MulRS[1].Vk = ((ALUInstruction) instructions.peek()).Rt.getValue();
                    } else {
                        MulRS[1].Qk = getRegister(((ALUInstruction) instructions.peek()).Rt.getName()).getQi();
                    }
                    getRegister(instructions.peek().Rd.getName()).setQi(MulRS[1].Name);
                    MulRS[1].issueTime = clk;
                    tracker.put(clk, false);
                    System.out
                            .println("Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk);
                    output += "Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk + "\n";
                    instructions.remove();
                } else {
                    isStalledMul = true;
                }
            } else if (instructions.peek().type == Type.LD) {
                int add = ((MemInstruction) instructions.peek()).address;
                for (int i = 0; i < StoreBuffer.length; i++) {
                    if (StoreBuffer[i].Busy == 1) {
                        if (StoreBuffer[i].Address == add) {
                            addressFlag = add;
                            addressStall = true;
                            break;
                        }
                    }
                }
                if (!isStalledLoad && !addressStall) {
                    int i;
                    for (i = 0; i < LoadBuffer.length; i++) {
                        if (LoadBuffer[i].Busy == 0) {
                            LoadBuffer[i].Busy = 1;
                            LoadBuffer[i].Latency = LoadLatency;
                            LoadBuffer[i].Address = ((MemInstruction) instructions.peek()).address;
                            getRegister(instructions.peek().Rd.getName()).setQi(LoadBuffer[i].Name);
                            LoadBuffer[i].issueTime = clk;
                            tracker.put(clk, false);
                            System.out.println(
                                    "Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk);
                            output += "Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk
                                    + "\n";
                            instructions.remove();
                            break;
                        }
                    }
                    if (i == LoadBuffer.length) {
                        isStalledLoad = true;
                    }
                }
            } else if (instructions.peek().type == Type.ST) {
                int add = ((MemInstruction) instructions.peek()).address;
                for (int i = 0; i < LoadBuffer.length; i++) {
                    if (LoadBuffer[i].Busy == 1) {
                        if (LoadBuffer[i].Address == add) {
                            addressFlag = add;
                            addressStall = true;
                            break;
                        }
                    }
                }
                for (int i = 0; i < StoreBuffer.length; i++) {
                    if (StoreBuffer[i].Busy == 1) {
                        if (StoreBuffer[i].Address == add) {
                            addressFlag = add;
                            addressStall = true;
                            break;
                        }
                    }
                }
                if (!isStalledStore && !addressStall) {
                    int i;
                    for (i = 0; i < StoreBuffer.length; i++) {
                        if (StoreBuffer[i].Busy == 0) {
                            StoreBuffer[i].Busy = 1;
                            StoreBuffer[i].Latency = StoreLatency;
                            StoreBuffer[i].Address = ((MemInstruction) instructions.peek()).address;
                            if (getRegister(((MemInstruction) instructions.peek()).Rd.getName()).getQi() == null) {
                                StoreBuffer[i].Value = ((MemInstruction) instructions.peek()).Rd.getValue();
                            } else {
                                StoreBuffer[i].Qi = getRegister(((MemInstruction) instructions.peek()).Rd.getName())
                                        .getQi();
                            }
                            StoreBuffer[i].issueTime = clk;
                            tracker.put(clk, false);
                            System.out.println(
                                    "Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk);
                            output += "Instruction " + instructions.peek().toString() + " issued at clock cycle " + clk
                                    + "\n";
                            instructions.remove();
                            break;
                        }
                    }
                    if (i == StoreBuffer.length) {
                        isStalledStore = true;
                    }
                }
            }
        }
    }

    static void execute() {
        for (int i = 0; i < AddRS.length; i++) {
            if (AddRS[i].Busy == 1) {
                if (AddRS[i].issueTime == clk)
                    continue;
                if (AddRS[i].Qj == null && AddRS[i].Qk == null) {
                    if (AddRS[i].Latency == AddLatency) {
                        System.out.println("Instruction " + AddRS[i].Name + " starts execution");
                        output += "Instruction " + AddRS[i].Name + " starts execution" + "\n";
                    }
                    AddRS[i].Latency = AddRS[i].Latency - 1;
                    if (AddRS[i].Latency == 0) {
                        System.out.println("Instruction " + AddRS[i].Name + " finishes execution");
                        output += "Instruction " + AddRS[i].Name + " finishes execution" + "\n";
                    }
                    if (AddRS[i].Latency == -1)
                        tracker.put(AddRS[i].issueTime, true);
                }
            }
        }

        for (int i = 0; i < MulRS.length; i++) {
            if (MulRS[i].Busy == 1) {
                if (MulRS[i].issueTime == clk)
                    continue;
                if (MulRS[i].Qj == null && MulRS[i].Qk == null) {
                    if (MulRS[i].Op == Type.MUL && MulRS[i].Latency == MulLatency) {
                        System.out.println("Instruction " + MulRS[i].Name + " starts execution");
                        output += "Instruction " + MulRS[i].Name + " starts execution" + "\n";
                    }
                    if (MulRS[i].Op == Type.DIV && MulRS[i].Latency == DivLatency) {
                        System.out.println("Instruction " + MulRS[i].Name + " starts execution");
                        output += "Instruction " + MulRS[i].Name + " starts execution" + "\n";
                    }
                    MulRS[i].Latency = MulRS[i].Latency - 1;
                    if (MulRS[i].Latency == 0) {
                        System.out.println("Instruction " + MulRS[i].Name + " finishes execution");
                        output += "Instruction " + MulRS[i].Name + " finishes execution" + "\n";
                    }
                    if (MulRS[i].Latency == -1)
                        tracker.put(MulRS[i].issueTime, true);
                }
            }
        }

        for (int i = 0; i < LoadBuffer.length; i++) {
            if (LoadBuffer[i].Busy == 1) {
                if (LoadBuffer[i].issueTime == clk)
                    continue;
                if (LoadBuffer[i].Latency == LoadLatency) {
                    System.out.println("Instruction " + LoadBuffer[i].Name + " starts execution");
                    output += "Instruction " + LoadBuffer[i].Name + " starts execution" + "\n";
                }
                LoadBuffer[i].Latency = LoadBuffer[i].Latency - 1;
                if (LoadBuffer[i].Latency == 0) {
                    System.out.println("Instruction " + LoadBuffer[i].Name + " finishes execution");
                    output += "Instruction " + LoadBuffer[i].Name + " finishes execution" + "\n";
                }
                if (LoadBuffer[i].Latency == -1)
                    tracker.put(LoadBuffer[i].issueTime, true);
            }
        }

        for (int i = 0; i < StoreBuffer.length; i++) {
            if (StoreBuffer[i].Busy == 1) {
                if (StoreBuffer[i].issueTime == clk)
                    continue;
                if (StoreBuffer[i].Qi == null) {
                    if (StoreBuffer[i].Latency == StoreLatency) {
                        System.out.println("Instruction " + StoreBuffer[i].Name + " starts execution");
                        output += "Instruction " + StoreBuffer[i].Name + " starts execution" + "\n";
                    }
                    StoreBuffer[i].Latency = StoreBuffer[i].Latency - 1;
                    if (StoreBuffer[i].Latency == 0) {
                        System.out.println("Instruction " + StoreBuffer[i].Name + " finishes execution");
                        output += "Instruction " + StoreBuffer[i].Name + " finishes execution" + "\n";
                    }
                    if (StoreBuffer[i].Latency == -1)
                        tracker.put(StoreBuffer[i].issueTime, true);
                }
            }
        }

    }

    static void writeBack() {
        int smallestKey = clk + 1;
        for (Integer key : tracker.keySet()) {
            if (tracker.get(key) && key < smallestKey) {
                smallestKey = key;
            }
        }
        for (int i = 0; i < AddRS.length; i++) {
            if (AddRS[i].Busy == 1) {
                if (AddRS[i].issueTime == smallestKey) {
                    float result = 0;
                    if (AddRS[i].Op == Type.ADD) {
                        result = AddRS[i].Vj + AddRS[i].Vk;
                    } else if (AddRS[i].Op == Type.SUB) {
                        result = AddRS[i].Vj - AddRS[i].Vk;
                    }
                    // for each register in the register file, if the Qi is the same as the
                    // reservation station, set the value to the result
                    for (Register reg : registerFile) {
                        if (reg.getQi() != null && reg.getQi().equals(AddRS[i].Name)) {
                            reg.setQi(null);
                            reg.setValue(result);
                        }
                    }
                    // for each entry in the AddRS, if the Qi is the same as the reservation
                    // station, set the Qi to null
                    for (RSentry rs : AddRS) {
                        if (rs.Qj != null && rs.Qj.equals(AddRS[i].Name)) {
                            rs.Qj = null;
                            rs.Vj = result;
                        }
                        if (rs.Qk != null && rs.Qk.equals(AddRS[i].Name)) {
                            rs.Qk = null;
                            rs.Vk = result;
                        }
                    }
                    // for each entry in the MulRS, if the Qi is the same as the reservation
                    // station, set the Qi to null
                    for (RSentry rs : MulRS) {
                        if (rs.Qj != null && rs.Qj.equals(AddRS[i].Name)) {
                            rs.Qj = null;
                            rs.Vj = result;
                        }
                        if (rs.Qk != null && rs.Qk.equals(AddRS[i].Name)) {
                            rs.Qk = null;
                            rs.Vk = result;
                        }
                    }
                    // for each entry in the store buffer, if the Qi is the same as the
                    // reservation station, set the Qi to null
                    for (StoreEntry sb : StoreBuffer) {
                        if (sb.Qi != null && sb.Qi.equals(AddRS[i].Name)) {
                            sb.Qi = null;
                            sb.Value = result;
                        }
                    }
                    AddRS[i].Busy = 0;
                    AddRS[i].Op = null;
                    AddRS[i].Vj = 0;
                    AddRS[i].Vk = 0;
                    AddRS[i].Qj = null;
                    AddRS[i].Qk = null;
                    System.out.println("Write back: " + AddRS[i].Name);
                    output += "Write back: " + AddRS[i].Name + "\n";
                    tracker.remove(smallestKey);
                    isStalledAdd = false;
                }
            }
        }
        for (int i = 0; i < MulRS.length; i++) {
            if (MulRS[i].Busy == 1) {
                if (MulRS[i].issueTime == smallestKey) {
                    float result = 0;
                    if (MulRS[i].Op == Type.MUL) {
                        result = MulRS[i].Vj * MulRS[i].Vk;
                    } else if (MulRS[i].Op == Type.DIV) {
                        result = MulRS[i].Vj / MulRS[i].Vk;
                    }
                    // for each register in the register file, if the Qi is the same as the
                    // MulRS[i].Name, set Qi to null
                    for (Register reg : registerFile) {
                        if (reg.getQi() != null && reg.getQi().equals(MulRS[i].Name)) {
                            reg.setQi(null);
                            reg.setValue(result);
                        }
                    }
                    // for each entry in the AddRS, if Qj or Qk is the same as the MulRS[i].Name,
                    // set Qj or Qk to null
                    for (int j = 0; j < AddRS.length; j++) {
                        if (AddRS[j].Qj != null && AddRS[j].Qj.equals(MulRS[i].Name)) {
                            AddRS[j].Qj = null;
                            AddRS[j].Vj = result;
                        }
                        if (AddRS[j].Qk != null && AddRS[j].Qk.equals(MulRS[i].Name)) {
                            AddRS[j].Qk = null;
                            AddRS[j].Vk = result;
                        }
                    }
                    // for each entry in the MulRS, if Qj or Qk is the same as the MulRS[i].Name,
                    // set Qj or Qk to null
                    for (int j = 0; j < MulRS.length; j++) {
                        if (MulRS[j].Qj != null && MulRS[j].Qj.equals(MulRS[i].Name)) {
                            MulRS[j].Qj = null;
                            MulRS[j].Vj = result;
                        }
                        if (MulRS[j].Qk != null && MulRS[j].Qk.equals(MulRS[i].Name)) {
                            MulRS[j].Qk = null;
                            MulRS[j].Vk = result;
                        }
                    }
                    // for each entry in the StoreBuffer, if Qi is the same as the MulRS[i].Name,
                    // set Qi to null
                    for (int j = 0; j < StoreBuffer.length; j++) {
                        if (StoreBuffer[j].Qi != null && StoreBuffer[j].Qi.equals(MulRS[i].Name)) {
                            StoreBuffer[j].Qi = null;
                            StoreBuffer[j].Value = result;
                        }
                    }
                    MulRS[i].Busy = 0;
                    MulRS[i].Op = null;
                    MulRS[i].Vj = 0;
                    MulRS[i].Vk = 0;
                    MulRS[i].Qj = null;
                    MulRS[i].Qk = null;
                    System.out.println("Write back: " + MulRS[i].Name);
                    output += "Write back: " + MulRS[i].Name + "\n";
                    tracker.remove(smallestKey);
                    isStalledMul = false;
                }
            }
        }
        for (int i = 0; i < LoadBuffer.length; i++) {
            if (LoadBuffer[i].Busy == 1) {
                if (LoadBuffer[i].issueTime == smallestKey) {
                    // for each register in the register file, if the Qi is the same as the
                    // LoadBuffer[i].Name, set Qi to null
                    for (Register reg : registerFile) {
                        if (reg.getQi() != null && reg.getQi().equals(LoadBuffer[i].Name)) {
                            reg.setValue(memory.read(LoadBuffer[i].Address));
                            reg.setQi(null);
                        }
                    }
                    // for each entry in the AddRS, if the Qi is the same as the LoadBuffer[i].Name,
                    // set Qi to null
                    for (int j = 0; j < AddRS.length; j++) {
                        if (AddRS[j].Qj != null && AddRS[j].Qj.equals(LoadBuffer[i].Name)) {
                            AddRS[j].Qj = null;
                            AddRS[j].Vj = memory.read(LoadBuffer[i].Address);
                        }
                        if (AddRS[j].Qk != null && AddRS[j].Qk.equals(LoadBuffer[i].Name)) {
                            AddRS[j].Qk = null;
                            AddRS[j].Vk = memory.read(LoadBuffer[i].Address);
                        }
                    }
                    // for each entry in the MulRS, if the Qi is the same as the LoadBuffer[i].Name,
                    // set Qi to null
                    for (int j = 0; j < MulRS.length; j++) {
                        if (MulRS[j].Qj != null && MulRS[j].Qj.equals(LoadBuffer[i].Name)) {
                            MulRS[j].Qj = null;
                            MulRS[j].Vj = memory.read(LoadBuffer[i].Address);
                        }
                        if (MulRS[j].Qk != null && MulRS[j].Qk.equals(LoadBuffer[i].Name)) {
                            MulRS[j].Qk = null;
                            MulRS[j].Vk = memory.read(LoadBuffer[i].Address);
                        }
                    }
                    // for each entry in the StoreBuffer, if the Qi is the same as the
                    // LoadBuffer[i].Name, set Qi to null
                    for (int j = 0; j < StoreBuffer.length; j++) {
                        if (StoreBuffer[j].Qi != null && StoreBuffer[j].Qi.equals(LoadBuffer[i].Name)) {
                            StoreBuffer[j].Qi = null;
                            StoreBuffer[j].Value = memory.read(LoadBuffer[i].Address);
                        }
                    }
                    if (addressFlag == -1) {
                        LoadBuffer[i].Busy = 0;
                        LoadBuffer[i].Address = -1;
                        System.out.println("Write back: " + LoadBuffer[i].Name);
                        output += "Write back: " + LoadBuffer[i].Name + "\n";
                        tracker.remove(smallestKey);
                        isStalledLoad = false;
                    } else {
                        if (addressFlag == LoadBuffer[i].Address) {
                            LoadBuffer[i].Busy = 0;
                            LoadBuffer[i].Address = -1;
                            System.out.println("Write back: " + LoadBuffer[i].Name);
                            output += "Write back: " + LoadBuffer[i].Name + "\n";
                            tracker.remove(smallestKey);
                            isStalledLoad = false;
                            addressStall = false;
                            addressFlag = -1;
                        } else {
                            LoadBuffer[i].Busy = 0;
                            LoadBuffer[i].Address = -1;
                            System.out.println("Write back: " + LoadBuffer[i].Name);
                            output += "Write back: " + LoadBuffer[i].Name + "\n";
                            tracker.remove(smallestKey);
                            isStalledLoad = false;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < StoreBuffer.length; i++) {
            if (StoreBuffer[i].Busy == 1) {
                if (StoreBuffer[i].issueTime == smallestKey) {
                    if (addressFlag == -1) {
                        memory.write(StoreBuffer[i].Address, StoreBuffer[i].Value);
                        StoreBuffer[i].Busy = 0;
                        StoreBuffer[i].Address = -1;
                        StoreBuffer[i].Value = 0;
                        StoreBuffer[i].Qi = null;
                        System.out.println("Write back: " + StoreBuffer[i].Name);
                        output += "Write back: " + StoreBuffer[i].Name + "\n";
                        tracker.remove(smallestKey);
                        isStalledStore = false;
                    } else {
                        if (addressFlag == StoreBuffer[i].Address) {
                            memory.write(StoreBuffer[i].Address, StoreBuffer[i].Value);
                            StoreBuffer[i].Busy = 0;
                            StoreBuffer[i].Address = -1;
                            StoreBuffer[i].Value = 0;
                            StoreBuffer[i].Qi = null;
                            System.out.println("Write back: " + StoreBuffer[i].Name);
                            output += "Write back: " + StoreBuffer[i].Name + "\n";
                            tracker.remove(smallestKey);
                            isStalledStore = false;
                            addressStall = false;
                            addressFlag = -1;
                        } else {
                            memory.write(StoreBuffer[i].Address, StoreBuffer[i].Value);
                            StoreBuffer[i].Busy = 0;
                            StoreBuffer[i].Address = -1;
                            StoreBuffer[i].Value = 0;
                            StoreBuffer[i].Qi = null;
                            isStalledStore = false;
                            System.out.println("Write back: " + StoreBuffer[i].Name);
                            output += "Write back: " + StoreBuffer[i].Name + "\n";
                            tracker.remove(smallestKey);
                        }
                    }
                }
            }
        }
    }

    static void run() {
        while (true) {
            output = "";
            clk++;
            System.out.println("CLOCK: " + clk);
            output += "CLOCK: " + clk + "\n \n";
            output += "\n";
            System.out.println("Instructions Queue: ");
            output += "Instructions Queue: \n";
            for (Instruction i : instructions) {
                System.out.println(i);
                output += i + "\n";
            }
            System.out.println();
            output += "\n";
            if (!isStalledAdd && !isStalledMul && !isStalledLoad && !isStalledStore && !addressStall) {
                issueInstruction();
                System.out.println();
                output += "\n";
            }
            execute();
            System.out.println();
            output += "\n";
            writeBack();
            System.out.println();
            output += "\n";
            System.out.println("Name Busy   Op   Vj    Vk     Qj      Qk");
            output += "Name Busy   Op   Vj    Vk     Qj      Qk" + "\n";
            for (RSentry rSentry : AddRS) {
                System.out.println(" " + rSentry.Name + "   " + rSentry.Busy + "    " + rSentry.Op + "  " + rSentry.Vj
                        + "   " + rSentry.Vk + "   " + rSentry.Qj + "   " + rSentry.Qk + " ");
                output += " " + rSentry.Name + "   " + rSentry.Busy + "    " + rSentry.Op + "  " + rSentry.Vj + "   "
                        + rSentry.Vk + "   " + rSentry.Qj + "   " + rSentry.Qk + " " + "\n";
            }
            System.out.println();
            output += "\n";
            System.out.println("Name Busy   Op   Vj    Vk     Qj      Qk");
            output += "Name Busy   Op   Vj    Vk     Qj      Qk" + "\n";
            for (RSentry rSentry : MulRS) {
                System.out.println(" " + rSentry.Name + "   " + rSentry.Busy + "    " + rSentry.Op + "  " + rSentry.Vj
                        + "   " + rSentry.Vk + "   " + rSentry.Qj + "   " + rSentry.Qk + " ");
                output += " " + rSentry.Name + "   " + rSentry.Busy + "    " + rSentry.Op + "  " + rSentry.Vj + "   "
                        + rSentry.Vk + "   " + rSentry.Qj + "   " + rSentry.Qk + " " + "\n";
            }
            System.out.println();
            output += "\n";
            System.out.println("Name Busy   Address");
            output += "Name Busy   Address" + "\n";
            for (LoadEntry lSentry : LoadBuffer) {
                System.out.println(" " + lSentry.Name + "   " + lSentry.Busy + "      " + lSentry.Address);
                output += " " + lSentry.Name + "   " + lSentry.Busy + "      " + lSentry.Address + "\n";
            }
            System.out.println();
            output += "\n";
            System.out.println("Name Busy   Address   Value    Qi");
            output += "Name Busy   Address   Value   Qi" + "\n";
            for (StoreEntry sSentry : StoreBuffer) {
                System.out.println(" " + sSentry.Name + "   " + sSentry.Busy + "      " + sSentry.Address + "        "
                        + sSentry.Value + "     " + sSentry.Qi);
                output += " " + sSentry.Name + "   " + sSentry.Busy + "      " + sSentry.Address + "        "
                        + sSentry.Value + "     " + sSentry.Qi + "\n";
            }
            System.out.println();
            output += "\n";
            for (Register register : registerFile) {
                System.out.println(register.getName() + "  " + register.getValue() + "  " + register.getQi());
                output += register.getName() + " " + register.getValue() + " " + register.getQi() + "\n";
            }
            System.out.println();
            output += "\n";
            System.out.println(memory);
            output += memory + "\n";
            System.out.println();
            output += "\n";
            if (tracker.isEmpty() && instructions.isEmpty()) {
                break;
            }

        }
    }
        
    
    
    static void readfile() {
        try {
            File file = new File("text.txt");
            FileReader fr = new FileReader(file); // reads the file
			BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream
			StringBuffer sb = new StringBuffer(); // constructs a string buffer with no characters
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
   
        Register F0 = new Register("F0", -1);
        Register F1 = new Register("F1", 2);
        Register F2 = new Register("F2", 2);
        Register F3 = new Register("F3", -1);
        Register F4 = new Register("F4", -1);
        Register F5 = new Register("F5", -1);
        Register F6 = new Register("F6", -1);
        Register F7 = new Register("F7", -1);
        Register F8 = new Register("F8", 6);
        Register F9 = new Register("F9", 3);
        Register F10 = new Register("F10", -1);
        Register F11 = new Register("F11", -1);

        // add registers to the register file
        registerFile.add(F0);
        registerFile.add(F1);
        registerFile.add(F2);
        registerFile.add(F3);
        registerFile.add(F4);
        registerFile.add(F5);
        registerFile.add(F6);
        registerFile.add(F7);
        registerFile.add(F8);
        registerFile.add(F9);
        registerFile.add(F10);
        registerFile.add(F11);

        // Name  reservation stations entry
        for (int i = 0; i < AddRS.length; i++) {
            AddRS[i] = new RSentry("A" + (i + 1), 0, null, 0, 0, null, null, 0);
        }
        for (int i = 0; i < MulRS.length; i++) {
            MulRS[i] = new RSentry("M" + (i + 1), 0, null, 0, 0, null, null, 0);
        }
        for (int i = 0; i < LoadBuffer.length; i++) {
            LoadBuffer[i] = new LoadEntry("L" + (i + 1), 0, -1, 0);
        }
        for (int i = 0; i < StoreBuffer.length; i++) {
            StoreBuffer[i] = new StoreEntry("S" + (i + 1), 0, -1, 0, null, 0);
        }
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in); 
        // Read  instructions from  user
        System.out.println("Please type your instructions here then type start to begin your program");
        System.out.println("You can use the text file as a reference ;) ");
        String instruction = scanner.nextLine();
        while (true) {
            if (instruction.contains("ADD.D") || instruction.contains("SUB.D") ||
                    instruction.contains("MUL.D") || instruction.contains("L.D") ||
                    instruction.contains("S.D") || instruction.contains("DIV.D"))
                instructionQueue.add(instruction);
            else if (instruction.contains("START") || instruction.contains("start"))
                break;
            else
                System.out.println("Invalid Instruction! Please try again.");
            instruction = scanner.nextLine();
        }
        // create the instructions
        for (int i = 0; i < instructionQueue.size(); i++) {
            String[] instruction1 = instructionQueue.get(i).split(" ");
            if (instruction1[0].equals("ADD.D")) {
                addInstruction(new ALUInstruction(Type.ADD, getRegister(instruction1[1]),
                        getRegister(instruction1[2]), getRegister(instruction1[3])));
            } else if (instruction1[0].equals("SUB.D")) {
                addInstruction(new ALUInstruction(Type.SUB, getRegister(instruction1[1]),
                        getRegister(instruction1[2]), getRegister(instruction1[3])));
            } else if (instruction1[0].equals("MUL.D")) {
                addInstruction(new ALUInstruction(Type.MUL, getRegister(instruction1[1]),
                        getRegister(instruction1[2]), getRegister(instruction1[3])));
            } else if (instruction1[0].equals("DIV.D")) {
                addInstruction(new ALUInstruction(Type.DIV, getRegister(instruction1[1]),
                        getRegister(instruction1[2]), getRegister(instruction1[3])));
            } else if (instruction1[0].equals("L.D")) {
                addInstruction(
                        new MemInstruction(Type.LD, getRegister(instruction1[1]),
                                Integer.parseInt(instruction1[2])));
            } else if (instruction1[0].equals("S.D")) {
                addInstruction(
                        new MemInstruction(Type.ST, getRegister(instruction1[1]),
                                Integer.parseInt(instruction1[2])));
            }    }
        System.out.println("\nEnter the latency of the Add instruction"); 
        AddLatency = scanner.nextInt();
        System.out.println("Enter the latency of the Mul instruction");   
        MulLatency = scanner.nextInt();
        System.out.println("Enter the latency of the Div instruction");      
        DivLatency = scanner.nextInt();
        System.out.println("Enter the latency of the Load instruction");
        LoadLatency = scanner.nextInt();
        System.out.println("Enter the latency of the Store instruction");
        StoreLatency = scanner.nextInt();    
        run();
        
        System.out.println("That's the end!!");
    }   }
