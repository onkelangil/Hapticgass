package model;

/**
 * Created by Anders on 21-05-2017.
 */

public class Fart {
    public String receiver, sender, id;
    public boolean seen;

    public Fart(){

    }
    public Fart(String receiver, String seen, String sender, String id){
        this.receiver = receiver;
        this.seen = Boolean.parseBoolean(seen);
        this.sender = sender;
        this.id = id;
    }
    public String getId(){
        return id;
    }
}
