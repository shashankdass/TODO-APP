package sdass.simpletodo;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private final int REQUEST_CODE = 20;
    private boolean storeInDb = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        if(!storeInDb)
            readItems();
        else
            readItemsFromDB();
        itemsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }
    public void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> Adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        if(!storeInDb)
                            writeItems();
                        else
                            writeItemsToDB();
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> Adapter, View item, int pos, long id) {
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        String selectedFromList = (String)lvItems.getItemAtPosition(pos);
                        i.putExtra("item", selectedFromList);
                        i.putExtra("pos", pos);
                        startActivityForResult(i,REQUEST_CODE);
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
            // Toast the name to display temporarily on screen

            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            items.remove(position);
            items.add(position,name);
            itemsAdapter.notifyDataSetChanged();
            if(!storeInDb)
                writeItems();
            else
                writeItemsToDB();
        }
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir,"todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch(IOException e) {
            items = new ArrayList<String>();
        }

    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readItemsFromDB() {
        try {
            List<Item> itemFromDB = Item.getAll();
            items = new ArrayList<String>();
            if( itemFromDB.size() > 0 )
                for (int i=0; i<itemFromDB.size(); i++) {
                    items.add(itemFromDB.get(i).name);
                }
        } catch(Exception e) {
            items = new ArrayList<String>();
        }

    }

    private void writeItemsToDB() {
       for(int i=0; i<items.size(); i++) {
           Item item = new Item();
           item.remoteId = i;
           item.name = items.get(i);
           item.save();
       }
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String newItemText = etNewItem.getText().toString();
        if(!newItemText.equals(""))
            itemsAdapter.add(newItemText);
            etNewItem.setText("");
            if(!storeInDb)
                writeItems();
            else
                writeItemsToDB();
    }
}
