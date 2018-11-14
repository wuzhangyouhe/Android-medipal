package sg.edu.nus.iss.se8.medipal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.iss.se8.medipal.R;
import sg.edu.nus.iss.se8.medipal.models.EmergencyContact;

public class EmergencyContactsAdapter extends RecyclerView.Adapter<EmergencyContactsAdapter.ViewHolder> {

    private List<EmergencyContact> emergencyContacts;
    public MyAdapterListener onClickListener;

    public EmergencyContactsAdapter(List<EmergencyContact> emergencyContacts, MyAdapterListener onClickListener) {
        this.emergencyContacts = emergencyContacts;
        this.onClickListener = onClickListener;
    }

    @Override
    public EmergencyContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_emergency_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        EmergencyContact emergencyContact = emergencyContacts.get(position);
        emergencyContact.setPriority(position);

        viewHolder.textViewContactName.setText(emergencyContact.getName());
        viewHolder.textViewContactPhoneNumber.setText(emergencyContact.getPhoneNumber());
        viewHolder.textViewContactType.setText(emergencyContact.getType().toString());
        viewHolder.textViewContactPriority.setText("Priority: " + String.valueOf(emergencyContact.getPriority() + 1));
    }

    @Override
    public int getItemCount() {
        return emergencyContacts.size();
    }

    public EmergencyContact getItem(int position) {
        return emergencyContacts.get(position);
    }

    public void setEmergencyContacts(List<EmergencyContact> emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout leftLayout;
        private TextView textViewContactType;
        private TextView textViewContactName;
        private TextView textViewContactPhoneNumber;
        private TextView textViewContactPriority;
        private ImageView imageViewCallButton;
        private ImageView imageViewMessageButton;

        private ViewHolder(View view) {
            super(view);
            leftLayout = (RelativeLayout) view.findViewById(R.id.leftLayout);
            textViewContactName = (TextView) view.findViewById(R.id.contactName);
            textViewContactPhoneNumber = (TextView) view.findViewById(R.id.contactPhoneNumber);
            textViewContactType = (TextView) view.findViewById(R.id.contactType);
            textViewContactPriority = (TextView) view.findViewById(R.id.contactPriority);
            imageViewCallButton = (ImageView) view.findViewById(R.id.callButton);
            imageViewMessageButton = (ImageView) view.findViewById(R.id.messageButton);

            leftLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.layoutOnClick(v, getAdapterPosition());
                }
            });

            leftLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onClickListener.layoutOnLongClick(v, getAdapterPosition());
                    return true;
                }
            });

            imageViewCallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.callButtonOnClick(v, getAdapterPosition());
                }
            });

            imageViewMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.messageButtonOnClick(v, getAdapterPosition());
                }
            });
        }
    }

    public interface MyAdapterListener {

        void layoutOnClick(View v, int position);

        boolean layoutOnLongClick(View v, int position);

        void callButtonOnClick(View v, int position);

        void messageButtonOnClick(View v, int position);
    }
}
