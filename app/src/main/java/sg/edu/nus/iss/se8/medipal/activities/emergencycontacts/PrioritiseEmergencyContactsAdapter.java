package sg.edu.nus.iss.se8.medipal.activities.emergencycontacts;

import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.dao.EmergencyContactDao;
import sg.edu.nus.iss.se8.medipal.itemtouchhelper.ItemTouchHelperAdapter;
import sg.edu.nus.iss.se8.medipal.itemtouchhelper.ItemTouchHelperViewHolder;
import sg.edu.nus.iss.se8.medipal.itemtouchhelper.OnStartDragListener;
import sg.edu.nus.iss.se8.medipal.models.EmergencyContact;

public class PrioritiseEmergencyContactsAdapter extends RecyclerView.Adapter<PrioritiseEmergencyContactsAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<EmergencyContact> emergencyContacts;
    private final OnStartDragListener mDragStartListener;

    public PrioritiseEmergencyContactsAdapter(ArrayList<EmergencyContact> emergencyContacts, OnStartDragListener dragStartListener) {
        this.emergencyContacts = emergencyContacts;
        this.mDragStartListener = dragStartListener;
    }

    @Override
    public PrioritiseEmergencyContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_prioritise_emergency_contact, parent, false);
        return new PrioritiseEmergencyContactsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PrioritiseEmergencyContactsAdapter.ViewHolder viewHolder, int position) {
        EmergencyContact emergencyContact = emergencyContacts.get(position);
        emergencyContact.setPriority(position);

        viewHolder.textViewContactName.setText(emergencyContact.getName());
        viewHolder.textViewContactPhoneNumber.setText(emergencyContact.getPhoneNumber());
        viewHolder.textViewContactType.setText(emergencyContact.getType().toString());

        viewHolder.textViewContactPriority.setVisibility(View.VISIBLE);
        viewHolder.textViewContactPriority.setText("Priority: " + String.valueOf(emergencyContact.getPriority() + 1));

        viewHolder.imageViewReorderButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return emergencyContacts.size();
    }

    public EmergencyContact getItem(int position) {
        return emergencyContacts.get(position);
    }

    @Override
    public void onItemDismiss(int position) {
        emergencyContacts.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        EmergencyContact fromEC = emergencyContacts.get(fromPosition);
        fromEC.setPriority(toPosition);
        EmergencyContactDao.update(fromEC);

        EmergencyContact toEC = emergencyContacts.get(toPosition);
        toEC.setPriority(fromPosition);
        EmergencyContactDao.update(toEC);

        Collections.swap(emergencyContacts, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyDataSetChanged();
        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        private TextView textViewContactType;
        private TextView textViewContactName;
        private TextView textViewContactPhoneNumber;
        private TextView textViewContactPriority;
        private ImageView imageViewReorderButton;

        private ViewHolder(View view) {
            super(view);
            textViewContactName = (TextView) view.findViewById(R.id.contactName);
            textViewContactPhoneNumber = (TextView) view.findViewById(R.id.contactPhoneNumber);
            textViewContactType = (TextView) view.findViewById(R.id.contactType);
            textViewContactPriority = (TextView) view.findViewById(R.id.contactPriority);
            imageViewReorderButton = (ImageView) view.findViewById(R.id.reorderButton);
        }

        @Override
        public void onItemSelected() {

            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}

