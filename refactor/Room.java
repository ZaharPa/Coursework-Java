package refactor;


public class Room {

    private String type;
    private int number;

    public Room(String type, int number) {
        this.type = type;
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Room{" + "type='" + type + '\'' + ", number=" + number + '}';
    }
}

