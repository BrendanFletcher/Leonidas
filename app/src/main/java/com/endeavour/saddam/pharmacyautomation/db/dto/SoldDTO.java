package com.endeavour.saddam.pharmacyautomation.db.dto;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.endeavour.saddam.pharmacyautomation.utils.DateConverter;

import java.util.Calendar;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.SET_DEFAULT;

@Entity(tableName = "sold", indices = @Index("medicineId"), foreignKeys = @ForeignKey(entity = MedicineDTO.class, parentColumns = "id", childColumns = "medicineId", onDelete = SET_DEFAULT))
@TypeConverters(DateConverter.class)
public class SoldDTO {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long medicineId;

    private int quantity;
    private int price;
    private Date time;
    private String medicineName;
   // private boolean soldOut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    public void setCurrentTime() {
        setTime(Calendar.getInstance().getTime());
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String name) {
        this.medicineName = name;
    }

//    public boolean isSoldOut() {
//        return soldOut;
//    }
//
//    public void setSoldOut(boolean soldOut) {
//        this.soldOut = soldOut;
//    }

    @Override
    public String toString() {
        return "SoldDTO{" +
                "id=" + id +
                ", medicineId=" + medicineId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", time=" + time +
                ", name=" + medicineName +
                '}';
    }
}
