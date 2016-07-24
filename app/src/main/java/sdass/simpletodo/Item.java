package sdass.simpletodo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Items", id = "_id")
public class Item extends Model {
    @Column(name = "Name")
    public String name;

    @Column(name = "DueDate")
    public String dueDate;

    @Column(name = "Priority")
    public String priority;

    public String defaultDueDate = "2011-01-01";
    public String defaulPriority = "Low";
    // Make sure to have a default constructor for every ActiveAndroid model
    public Item(){
        super();
    }

    public Item(String name){
        super();
        this.name = name;
        this.dueDate = defaultDueDate;
        this.priority = defaulPriority;
    }

    public Item(String name, String dueDate){
        super();
        this.name = name;
        this.dueDate = dueDate;
        this.priority = defaulPriority;
    }

    public Item(String name, String dueDate, String priority ){
        super();
        this.name = name;
        this.dueDate = dueDate;
        this.priority = priority;
    }
    public static List<Item> getAll() {
        // This is how you execute a query
        return new Select()
                .from(Item.class)
                .orderBy("Name ASC")
                .execute();
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
