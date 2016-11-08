import java.io.Serializable;

/**
 * Created by qx on 16/11/8.
 */
public class Record implements Serializable{

    private static final long serialVersionUID = 6382999283571014672L;
    private String title;
    private float rank;
    private int number;

    public Record(String title, float rank, int number) {
        this.title = title;
        this.rank = rank;
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return title.equals(record.title);

    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
