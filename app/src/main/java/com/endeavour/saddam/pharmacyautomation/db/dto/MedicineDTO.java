package com.endeavour.saddam.pharmacyautomation.db.dto;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "medicine")
public class MedicineDTO implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String name;
    private String manufacturer;
    private String uses;
    private String type;
    private Integer price;
    private Integer quantity;
    private Integer available;
    private Long manufacturingDate;
    private Long expireDate;
    private String location;

    private boolean wish;

    public MedicineDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Long getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(Long manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWish(boolean wish) {
        this.wish = wish;
    }

    public boolean isWish() {
        return wish;
    }


    private MedicineDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        manufacturer = in.readString();
        uses = in.readString();
        type = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        if (in.readByte() == 0) {
            available = null;
        } else {
            available = in.readInt();
        }
        if (in.readByte() == 0) {
            manufacturingDate = null;
        } else {
            manufacturingDate = in.readLong();
        }
        if (in.readByte() == 0) {
            expireDate = null;
        } else {
            expireDate = in.readLong();
        }
        location = in.readString();
        wish = in.readByte() != 0;
    }

    public static final Creator<MedicineDTO> CREATOR = new Creator<MedicineDTO>() {
        @Override
        public MedicineDTO createFromParcel(Parcel in) {
            return new MedicineDTO(in);
        }

        @Override
        public MedicineDTO[] newArray(int size) {
            return new MedicineDTO[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(manufacturer);
        dest.writeString(uses);
        dest.writeString(type);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(price);
        }
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
        if (available == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(available);
        }
        if (manufacturingDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(manufacturingDate);
        }
        if (expireDate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(expireDate);
        }
        dest.writeString(location);
        dest.writeByte((byte) (wish ? 1 : 0));
    }

    @Override
    public String toString() {
        return "MedicineDTO{" +
                "id=" + id +
                ", suggestName='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", uses='" + uses + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", available=" + available +
                ", manufacturingDate=" + manufacturingDate +
                ", expireDate=" + expireDate +
                ", location='" + location + '\'' +
                '}';
    }
}
