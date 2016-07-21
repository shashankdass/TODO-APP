package sdass.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {
    private EditText etEditItem ;
    private DatePicker dpDatePicker;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText)findViewById(R.id.etEditItem);
        dpDatePicker = (DatePicker)findViewById(R.id.dpDatePicker);

        String selectedItem = getIntent().getStringExtra("item");
        String date = getIntent().getStringExtra("dueDate");

        int day = Integer.parseInt(date.split("-")[2]);
        int month = Integer.parseInt(date.split("-")[1]);
        int year =  Integer.parseInt(date.split("-")[0]);
        dpDatePicker.init(year, month-1, day, null);
        etEditItem.setText(selectedItem);
        etEditItem.setSelection(selectedItem.length());
        position = getIntent().getIntExtra("pos", 0);

    }

    public void onEditedItemSave(View view) {
        etEditItem = (EditText)findViewById(R.id.etEditItem);
        dpDatePicker = (DatePicker) findViewById(R.id.dpDatePicker);
        int day = dpDatePicker.getDayOfMonth();
        int month = dpDatePicker.getMonth();
        int year =  dpDatePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(calendar.getTime());

        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("itemValue", etEditItem.getText().toString());
        data.putExtra("position", position); // ints work too
        data.putExtra("dueDate", dateString);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent

    }
}
