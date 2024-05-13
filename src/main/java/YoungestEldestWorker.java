import java.util.Date;

public class YoungestEldestWorker {
    private Age type;
    private String name;
    private Date birthday;

    public Age getType() {
        return type;
    }

    public void setType(Age type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "YoungestEldestWorker{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
