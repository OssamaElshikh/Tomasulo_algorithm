package Engine;

public class LoadEntry {
    String Name;
    int Busy;
    int Address;
    int Latency;
    int issueTime;

    public LoadEntry(String Name, int Busy, int Address, int Latency) {
        this.Name = Name;
        this.Busy = Busy;
        this.Address = Address;
        this.Latency = Latency;
        issueTime = 0;
    }
    
}
