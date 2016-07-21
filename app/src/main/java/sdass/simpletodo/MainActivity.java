package sdass.simpletodo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditNameDialogFragment.EditNameDialogListener {
    private ArrayList<Item> items;
    private ArrayAdapter itemsAdapter;
    private ListView lvItems;
    private final int REQUEST_CODE = 20;

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
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItemsToDB();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String name = data.getExtras().getString("itemValue");
            int position = data.getExtras().getInt("position", 0);
            String date = data.getExtras().getString("dueDate");
            // Toast the name to display temporarily on screen

            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            items.remove(position);
            items.add(position, new Item(name, date));
            itemsAdapter.notifyDataSetChanged();
            writeItemsToDB();
        }
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

    private void writeItemsToDB() {
        for (int i = 0; i < items.size(); i++) {
            Item item = new Item();
            item.remoteId = i;
            item.name = items.get(i).name;
            item.dueDate = items.get(i).dueDate;
            item.save();
        }
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItemText = etNewItem.getText().toString();
        if (!newItemText.equals("")) {
            itemsAdapter.add(new Item(newItemText));
            etNewItem.setText("");
            writeItemsToDB();
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
    public void onFinishEditDialog(String editedValue, String date, int pos) {
        items.remove(pos);
        items.add(pos, new Item(editedValue, date));
        itemsAdapter.notifyDataSetChanged();
    }
}
