package sdass.simpletodo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Items")
public class Item extends Model {
    // This is the unique id given by the server
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;
    // This is a regular field
    @Column(name = "Name")
    public String name;

    // Make sure to have a default constructor for every ActiveAndroid model
    public Item(){
        super();
    }

    public Item(int remoteId, String name){
        super();
        this.remoteId = remoteId;
        this.name = name;
    }

    public Item( String name){
        super();
        this.name = name;
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
        return name;
    }
}
