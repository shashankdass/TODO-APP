package sdass.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sdass on 7/8/16.
 */
public class ToDoItemAdapter extends ArrayAdapter<Item> {
    public ToDoItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        // Populate the data into the template view using the data object
        tvName.setText(item.name);
        tvPriority.setText(item.priority);
        // Return the completed view to render on screen
        return convertView;
    }

}
