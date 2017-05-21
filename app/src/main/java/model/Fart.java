package model;

/**
 * Created by Anders on 21-05-2017.
 */

public class Fart {
    public String receiver, sender;
    public boolean seen;

    public Fart(){

    }
    public Fart(String receiver, String seen, String sender){
        this.receiver = receiver;
        this.seen = Boolean.parseBoolean(seen);
        this.sender =sender;
    }
}
