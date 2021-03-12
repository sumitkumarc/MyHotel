package ontime.app.sqllight;

public class CardDetails {
    int Id;
    String Name;
    String Card_number;
    String Expiry_Date;

    public CardDetails(int id, String name, String card_no, String card_date) {
        this.Id = id;
        this.Name = name;
        this.Card_number = card_no;
        this.Expiry_Date = card_date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCard_number() {
        return Card_number;
    }

    public void setCard_number(String card_number) {
        Card_number = card_number;
    }

    public String getExpiry_Date() {
        return Expiry_Date;
    }

    public void setExpiry_Date(String expiry_Date) {
        Expiry_Date = expiry_Date;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


}
