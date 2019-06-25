package es.upm.miw.fem.fembitacora;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import es.upm.miw.fem.fembitacora.models.Event;

public class EventAdapter extends ArrayAdapter<Event> {

    final static String LOG_TAG = "MiW";

    private Context context;
    private List<Event> events;
    private int resourceId;

    public EventAdapter(@NonNull Context context, int resource, @NonNull List<Event> events) {
        super(context, resource, events);
        this.context = context;
        this.resourceId = resource;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout v;
        if (null != convertView) {
            v = (LinearLayout) convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = (LinearLayout) inflater.inflate(resourceId, parent, false);
        }

        if (events != null) {
            Log.i(LOG_TAG, "Adapter getView " + events.size());
        }

        Event event = events.get(position);

        if (event != null) {

            TextView notesTextView = v.findViewById(R.id.notesTextView);
            notesTextView.setText(event.getNotes());
        }

        return v;
    }


    public void removeAllFromView() {
        this.events.clear();
        notifyDataSetChanged();
    }
}
