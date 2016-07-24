package sdass.simpletodo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditNameDialogFragment.EditNameDialogListener {
    private ArrayList<Item> items;
    private ArrayAdapter itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItemsFromDB();
        ToDoItemAdapter toDoItemAdapter = new ToDoItemAdapter(this, items);
        // Attach the adapter to a ListView
        itemsAdapter = toDoItemAdapter;
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> Adapter, View item, int pos, long id) {
                        Item itemToDelete = items.get(pos);
                        itemToDelete.delete();
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> Adapter, View item, int pos, long id) {
                        showEditDialog(pos);
                    }
                }
        );
    }

    private void readItemsFromDB() {
        try {
            List<Item> itemFromDB = Item.getAll();
            items = new ArrayList<Item>();
            if (itemFromDB.size() > 0)
                for (int i = 0; i < itemFromDB.size(); i++) {
                    items.add(itemFromDB.get(i));
                }
        } catch (Exception e) {
            items = new ArrayList<Item>();
        }
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItemText = etNewItem.getText().toString();
        if (!newItemText.equals("")) {
            Item item = new Item(newItemText);
            item.save();
            items.add(item);
            itemsAdapter.notifyDataSetChanged();
            etNewItem.setText("");
        } else {
            Toast.makeText(this, "Add a non-empty todo", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        Item itemFromList = (Item) lvItems.getItemAtPosition(pos);
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(itemFromList, pos);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onFinishEditDialog(String editedValue, String date, int pos, String priority) {
        Item itemToUpdate = items.get(pos);
        itemToUpdate.dueDate = date;
        itemToUpdate.name = editedValue;
        itemToUpdate.priority = priority;
        itemToUpdate.save();
        itemsAdapter.notifyDataSetChanged();

    }

}
