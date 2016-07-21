package sdass.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditNameDialogFragment extends DialogFragment  implements AdapterView.OnItemSelectedListener {

    private static int item_pos;
    private EditText etEditItem ;
    private DatePicker dpDatePicker;
    public String defaultDueDate = "2011-01-01";
    private Button b;
    private String priority="Low";

    public EditNameDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        priority = (String) parent.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(String editedValue, String date, int pos, String priority);
    }
    private EditNameDialogListener caller;

    public static EditNameDialogFragment newInstance(Item todo, int pos) {
        item_pos = pos;
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("value",todo.name);
        args.putString("date",todo.dueDate);
        args.putString("pos",todo.dueDate);
        args.putString("priority", todo.priority);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = (Spinner) view.findViewById(R.id.spPriority);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.priority_value, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // Get field from view
        etEditItem = (EditText) view.findViewById(R.id.etEditItem);
        dpDatePicker = (DatePicker) view.findViewById(R.id.dpDatePicker);
        // Fetch arguments from bundle and set title
        String value = getArguments().getString("value", "");
        String date = getArguments().getString("date","");
        String priority = getArguments().getString("priority","");
        int spinnerPosition = adapter.getPosition(priority);
        spinner.setSelection(spinnerPosition);

        etEditItem.setText(value);        // Show soft keyboard automatically and request focus to field
        int day;
        int month;
        int year;
        if(date != null) {
            day = Integer.parseInt(date.split("-")[2]);
            month = Integer.parseInt(date.split("-")[1]);
            year = Integer.parseInt(date.split("-")[0]);
        } else {
            day = Integer.parseInt(defaultDueDate.split("-")[2]);
            month = Integer.parseInt(defaultDueDate.split("-")[1]);
            year = Integer.parseInt(defaultDueDate.split("-")[0]);
        }
        dpDatePicker.init(year, month - 1, day, null);

        etEditItem.requestFocus();
        b = (Button) view.findViewById(R.id.btnSave);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditedItemSaveFragment(view);
                getDialog().dismiss();
            }
        });
    }

    public void onEditedItemSaveFragment(View view) {
        etEditItem = (EditText)view.findViewById(R.id.etEditItem);
        dpDatePicker = (DatePicker) view.findViewById(R.id.dpDatePicker);


        int day = dpDatePicker.getDayOfMonth();
        int month = dpDatePicker.getMonth();
        int year =  dpDatePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(calendar.getTime());

        EditNameDialogListener listener = (EditNameDialogListener) getActivity();
        listener.onFinishEditDialog(etEditItem.getText().toString(), dateString, item_pos, priority);

    }
}