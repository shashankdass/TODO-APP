package sdass.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private EditText etEditItem ;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText)findViewById(R.id.etEditItem);
        String selectedItem = getIntent().getStringExtra("item");
        etEditItem.setText(selectedItem);
        etEditItem.setSelection(selectedItem.length());
        position = getIntent().getIntExtra("pos", 0);

    }

    public void onEditedItemSave(View view) {
        etEditItem = (EditText)findViewById(R.id.etEditItem);
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("itemValue", etEditItem.getText().toString());
        data.putExtra("position", position); // ints work too
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent

    }
}
