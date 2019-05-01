package com.endeavour.saddam.pharmacyautomation.db.dto;

import android.arch.persistence.room.Embedded;

public class SoldMedicineDTO {

    @Embedded(prefix = "sold_")
    private SoldDTO soldDTO;

    @Embedded
    private MedicineDTO medicineDTO;

    public void setMedicineDTO(MedicineDTO medicineDTO) {
        this.medicineDTO = medicineDTO;
    }

    public MedicineDTO getMedicineDTO() {
        return medicineDTO;
    }

    public void setSoldDTO(SoldDTO soldDTO) {
        this.soldDTO = soldDTO;
    }

    public SoldDTO getSoldDTO() {
        return soldDTO;
    }
}
