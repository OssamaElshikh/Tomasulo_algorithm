package Engine;

public class RSentry {
    String Name;
    int Busy;
    Type Op;
    float Vj;
    float Vk;
    String Qj;
    String Qk;
    int Latency;
    int issueTime;

    public RSentry(String Name, int Busy, Type Op, float Vj, float Vk, String Qj, String Qk, int Latency) {
        this.Name = Name;
        this.Busy = Busy;
        this.Op = Op;
        this.Vj = Vj;
        this.Vk = Vk;
        this.Qj = Qj;
        this.Qk = Qk;
        this.Latency = Latency;
        issueTime = 0;
    }
}
