package Engine;

public class StoreEntry {
    String Name;
    int Busy;
    int Address;
    float Value;
    String Qi;
    int Latency;
    int issueTime;
    
    public StoreEntry(String Name, int Busy, int Address, float Value, String Qi, int Latency) {
        this.Name = Name;
        this.Busy = Busy;
        this.Address = Address;
        this.Value = Value;
        this.Qi = Qi;
        this.Latency = Latency;
        issueTime = 0;
    }
}
