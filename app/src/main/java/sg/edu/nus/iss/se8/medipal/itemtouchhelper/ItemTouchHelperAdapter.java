package sg.edu.nus.iss.se8.medipal.itemtouchhelper;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
