package com.endeavour.saddam.pharmacyautomation.adapter;

import android.app.Activity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.endeavour.saddam.pharmacyautomation.NewMedicineActivity;
import com.endeavour.saddam.pharmacyautomation.R;
import com.endeavour.saddam.pharmacyautomation.RevenueActivity;
import com.endeavour.saddam.pharmacyautomation.SingleItemDetailsActivity;
import com.endeavour.saddam.pharmacyautomation.WarningListActivity;
import com.endeavour.saddam.pharmacyautomation.adapter.holder.MedicineViewHolder;
import com.endeavour.saddam.pharmacyautomation.db.AppDatabase;
import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.MedicineDTO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldDTO;
import com.endeavour.saddam.pharmacyautomation.utils.Constants;
import com.endeavour.saddam.pharmacyautomation.utils.Utils;

import java.text.MessageFormat;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineViewHolder> {
    private Activity activity;
    private MedicineDAO medicineDAO;
    private Handler mHandler;
    private List<MedicineDTO> medicines;

    private final String TYPE_FORMAT;
    private final String MFR_FORMAT;
    private final String USES_FORMAT;
    private final String MFG_FORMAT;
    private final String PRICE_FORMAT;
    private final String AVAILABLE_FORMAT;
    private final String LOCATION_FORMAT;
    private  int type = 0;
    private int navigationType;
    private LayoutInflater badgeInflater;
    private ViewGroup badgeView;
    private TextView textCartItemCount = null;

    public MedicineAdapter(Activity activity, Handler handler, List<MedicineDTO> medicines, int type) {
        this.activity = activity;
        this.medicines = medicines;

        this.mHandler = handler;
        this.medicineDAO = AppDatabase.getInstance(activity).medicineDAO();

        this.TYPE_FORMAT = activity.getString(R.string.type_format);
        this.MFR_FORMAT = activity.getString(R.string.mfr_format);
        this.USES_FORMAT = activity.getString(R.string.uses_format);
        this.MFG_FORMAT = activity.getString(R.string.mfg_date_format);
        //this.EXP_FORMAT = activity.getString(R.string.exp_date_format);
        this.PRICE_FORMAT = activity.getString(R.string.price_format);
        this.AVAILABLE_FORMAT = activity.getString(R.string.available_format);
        this.LOCATION_FORMAT = activity.getString(R.string.location_format);
        this.type = type;
        if(type == Constants.WARNING_TYPE){
            navigationType = type;
        }
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.medicine_list_row, parent, false);
        //badgeInflater = inflater;
        //badgeView = parent;
        return new MedicineViewHolder(itemView, type);
    }

    @Override
    public void onBindViewHolder(@NonNull final MedicineViewHolder holder, int position) {
        final MedicineDTO medicineDTO = medicines.get(position);

        holder.name.setText(medicineDTO.getName());
        //holder.type.setText(MessageFormat.format(TYPE_FORMAT, medicineDTO.getType()));
        //holder.mfr.setText(MessageFormat.format(MFR_FORMAT, medicineDTO.getManufacturer()));
        holder.uses.setText(MessageFormat.format(USES_FORMAT, medicineDTO.getUses()));
        //holder.mfgDate.setText(MessageFormat.format(MFG_FORMAT, Utils.formatDate(medicineDTO.getManufacturingDate())));
        //holder.expDate.setText(MessageFormat.format(EXP_FORMAT, Utils.formatDate(medicineDTO.getExpireDate())));
        String q = String.valueOf(medicineDTO.getQuantity());
        Log.d("quantity" + " ", q);
        holder.quantity.setText(q);
        holder.type.setText(medicineDTO.getType());
        holder.price.setText(MessageFormat.format(PRICE_FORMAT, medicineDTO.getPrice()));
        if(type == 1) {
            holder.expDate.setText(Utils.formatDate(medicineDTO.getExpireDate()));
        }
        //holder.available.setText(MessageFormat.format(AVAILABLE_FORMAT, medicineDTO.getAvailable()));
        //holder.location.setText(MessageFormat.format(LOCATION_FORMAT, medicineDTO.getLocation()));
        holder.lout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SingleItemDetailsActivity.class);
                intent.putExtra(Constants.Key.MEDICINE, medicineDTO);
                activity.startActivity(intent);
            }
        });
        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(activity, holder.action);
                popup.inflate(R.menu.action);
                updatePopupMenuItems(popup);
//                popup.getMenu().removeItem(R.id.menu_wish_list);
//                popup.getMenu().removeItem(R.id.menu_sold);
//                popup.getMenu().removeItem(R.id.menu_edit);
                //popup.getMenu().getItem(R.id.menu_delete).setTitle("Remove from wishlist");

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        System.out.println(item.getTitle());

                        int id = item.getItemId();
                        switch (id) {
                            case R.id.menu_sold:
                                addToSold(medicineDTO);
                                break;
                            case R.id.menu_wish_list:
                                addToWishList(medicineDTO.getId());
                                break;
                            case R.id.menu_edit:
                                edit(medicineDTO);
                                break;
                            case R.id.menu_delete:
                                delete(medicineDTO);
                                break;
                            case R.id.menu_remove_from_wishlist:
                                removeFromWishlist(medicineDTO);
                                break;
//                            case R.id.menu_remove_from_Soldout:
//                                //removeFromSoldout(medicineDTO);
//                                break;
                            case R.id.menu_search:
                                //searchOnInternet();
                                String query = medicineDTO.getName() + " " + medicineDTO.getType() + " " + medicineDTO.getManufacturer();
                                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                                intent.putExtra(SearchManager.QUERY, query); // query contains search string
                                activity.startActivity(intent);

//                                Intent intent = new Intent(activity, SingleItemDetailsActivity.class);
//                                intent.putExtra(Constants.Key.MEDICINE, medicineDTO);
//                                activity.startActivity(intent);
                                //break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    private void removeFromSoldout(MedicineDTO medicineDTO) {
        if (medicineDTO == null || medicineDTO.getId() == null) {
            Toast.makeText(activity, "Invalid Medicine", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
               // medicineDAO.updateSoldout(medicineDTO.getId(), false);
                //medicineDTO.setWish(false);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Medicine removed from Sold Out", Toast.LENGTH_SHORT).show();
                        medicines.remove(medicineDTO);
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void removeFromWishlist(MedicineDTO medicineDTO) {
        if (medicineDTO == null || medicineDTO.getId() == null) {
            Toast.makeText(activity, "Invalid Medicine", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicineDAO.updateWish(medicineDTO.getId(), false);
                //medicineDTO.setWish(false);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Medicine removed from wishlist", Toast.LENGTH_SHORT).show();
                        medicines.remove(medicineDTO);
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void updatePopupMenuItems(PopupMenu popup) {
        switch (navigationType){
            case Constants.AVAILABLE_TYPE:
                popup.getMenu().removeItem(R.id.menu_sold);
                popup.getMenu().removeItem(R.id.menu_edit);
                popup.getMenu().removeItem(R.id.menu_delete);
                break;

            case Constants.WISHLIST_TYPE:
                popup.getMenu().removeItem(R.id.menu_wish_list);
                popup.getMenu().removeItem(R.id.menu_sold);
                popup.getMenu().removeItem(R.id.menu_edit);
                //popup.getMenu().findItem(R.id.menu_delete).setTitle(R.string.remove_from_wishlist);
                popup.getMenu().findItem(R.id.menu_delete).setVisible(false);
                popup.getMenu().findItem(R.id.menu_remove_from_wishlist).setVisible(true);
                break;

            case Constants.SOLD_OUT_TYPE:
                popup.getMenu().removeItem(R.id.menu_sold);
                popup.getMenu().removeItem(R.id.menu_edit);
                //popup.getMenu().findItem(R.id.menu_delete).setTitle(R.string.remove_from_wishlist);
                popup.getMenu().findItem(R.id.menu_delete).setVisible(false);
//                popup.getMenu().findItem(R.id.menu_remove_from_Soldout).setVisible(true);
                break;

            case Constants.EXPIRED_TYPE:
                popup.getMenu().removeItem(R.id.menu_sold);
                popup.getMenu().removeItem(R.id.menu_edit);
                popup.getMenu().findItem(R.id.menu_delete).setVisible(false);
//                popup.getMenu().findItem(R.id.menu_remove_from_expiredlist).setVisible(true);
                break;

            case Constants.WARNING_TYPE:
                //popup.getMenu().removeItem(R.id.menu_sold);
                popup.getMenu().removeItem(R.id.menu_edit);
                popup.getMenu().removeItem(R.id.menu_delete);
                break;

            default:
        }
    }

    private void delete(final MedicineDTO medicineDTO) {
        if (medicineDTO == null || medicineDTO.getId() == null) {
            Toast.makeText(activity, "Invalid Medicine", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delete Medicine");
        builder.setMessage("Do you really want to delete ?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        medicineDAO.delete(medicineDTO);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "Medicine Deleted", Toast.LENGTH_SHORT).show();
                                medicines.remove(medicineDTO);
                                updateWarningCount();
                                notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    private void updateWarningCount() {
//        if(textCartItemCount == null) {
//            View inflater = badgeInflater.inflate(R.layout.badge_layout, badgeView, false);
//            textCartItemCount = inflater.findViewById(R.id.cart_badge);
//        }
        new UpdateWarningTask().execute("");
    }

    public void setWarningCount(TextView warningCount) {
        textCartItemCount = warningCount;
    }

    public class UpdateWarningTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            int warningCount = 0;
            try {
                warningCount = medicineDAO.getWarningListCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return warningCount;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(Integer count) {
//            if (textCartItemCount != null) {
//                if(textCartItemCount.getVisibility() != View.VISIBLE){
//                    textCartItemCount.setVisibility(View.VISIBLE);
//                }
//                textCartItemCount.setText("9");
//                Log.d("DELETION", " OCCURED");
//            if(textCartItemCount == null){
//                textCartItemCount = WarningListActivity.getWarnCount();
//            }
            if (textCartItemCount != null) {
                if (count > 0) {
                    Log.d("WarningListVal", " = " + count);
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                    textCartItemCount.setText(String.valueOf(count));
                } else {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void edit(MedicineDTO medicineDTO) {
        Intent intent = new Intent(activity, NewMedicineActivity.class);
        intent.putExtra(Constants.Key.MEDICINE, medicineDTO);
        activity.startActivityForResult(intent, Constants.Request.NEW_MEDICINE);
    }

    private void addToSold(MedicineDTO medicineDTO) {
        addToSold(medicineDTO, null, null);
    }

    private void addToSold(final MedicineDTO medicineDTO, String quantityError, String priceError) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("Sold Amount");

        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_amount_sold, null);
        final EditText priceText = view.findViewById(R.id.input_price);
        priceText.setError(priceError);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beforeTextChanged ", " "  + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence input, int start, int before, int count) {
                String text = "";
                String qError = null;
                int quantity = 0;
                if(input.length() > 0){
                    if (!Utils.isNumber(input.toString())) {
                        qError = "Invalid quantity";
                        addToSold(medicineDTO, qError, "null");
                    }
                    quantity = Integer.parseInt(input.toString());
                    if(quantity > 0 && quantity <= medicineDTO.getAvailable()) {
                        int perItemPrice = medicineDTO.getPrice() / medicineDTO.getQuantity();
                        int price = perItemPrice * quantity;
                        text += String.valueOf(price);
                    }
                    else{
                        qError = "Invalid quantity";
                        addToSold(medicineDTO, qError, "null");
                    }
                }
                else{
                    text = "";
                }
                priceText.setText(String.valueOf(text));
            Log.d("onTextChanged ", " start = " + start + " count = " + count + " strVal = "+ input.toString() + " and length = " + input.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged ", "val ");
            }
        };
        final EditText quantityText = view.findViewById(R.id.input_quantity);
        quantityText.addTextChangedListener(textWatcher );

        quantityText.setError(quantityError);

//        final EditText priceText = view.findViewById(R.id.input_price);
//        priceText.setError(priceError);

        builder.setView(view);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int viewId) {
                dialog.dismiss();

                String qError = null, pError = null;

                if (!Utils.isNumber(quantityText.getText().toString()) || Utils.getNumber(quantityText) < 1) {
                    qError = "Invalid quantity";
                }

                if (!Utils.isNumber(priceText.getText().toString()) || Utils.getNumber(priceText) < 1) {
                    pError = "Invalid price";
                }

                if (qError != null || pError != null) {
                    addToSold(medicineDTO, qError, pError);
                } else {
                    final int quantity = Utils.getNumber(quantityText);
                    final int price = Utils.getNumber(priceText);

                    if (medicineDTO.getAvailable() < quantity) {
                        addToSold(medicineDTO, "Not Available", null);
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                medicineDTO.setAvailable(medicineDTO.getAvailable() - quantity);
                                medicineDAO.upsert(medicineDTO);

                                SoldDTO soldDTO = new SoldDTO();
                                soldDTO.setMedicineId(medicineDTO.getId());
                                soldDTO.setQuantity(quantity);
                                soldDTO.setPrice(price);
                                soldDTO.setCurrentTime();
                                soldDTO.setMedicineName(medicineDTO.getName());
                                Log.d("HELLP ", ""+ medicineDTO.getName());
                                if(medicineDTO.getAvailable() == 0){
                                    //soldDTO.setSoldOut(true);
                                }
                                medicineDAO.upsert(soldDTO);

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity, "Added to Sold", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        }).start();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addToWishList(final long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                medicineDAO.updateWish(id, true);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Added to Wish List", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void setData(List<MedicineDTO> data, int navigationType) {
        this.navigationType = navigationType;
        //setMedicineItemMenuVisibility();
        this.medicines = data;
    }

    public void add(MedicineDTO medicineDTO) {
        this.medicines.add(medicineDTO);
    }

    public boolean isEmpty() {
        return medicines == null || medicines.isEmpty();
    }
}
