package com.endeavour.saddam.pharmacyautomation.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.endeavour.saddam.pharmacyautomation.db.dto.MedicineDTO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldDTO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldMedicineDTO;
import com.endeavour.saddam.pharmacyautomation.db.dto.StatsDTO;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MedicineDAO {

    @Query("SELECT * FROM medicine WHERE id = :id")
    MedicineDTO getByID(long id);

    @Query("SELECT * FROM medicine WHERE name LIKE :keyword")
    List<MedicineDTO> selectAll(String keyword);

    @Query("SELECT count(*) FROM medicine")
    int  getTotalRows();

    @Query("SELECT * FROM medicine WHERE available > 0 AND name LIKE :keyword")
    List<MedicineDTO> selectAvailable(String keyword);

    @Query("SELECT * FROM medicine WHERE expireDate < strftime('%s','now') * 1000 AND name LIKE :keyword")
    List<MedicineDTO> selectExpired(String keyword);

    @Query("SELECT * FROM medicine WHERE available = 0 AND name LIKE :keyword")
    List<MedicineDTO> selectSoldOut(String keyword);

    @Query("SELECT * FROM medicine WHERE wish = 1 AND name LIKE :keyword")
    List<MedicineDTO> selectWishList(String keyword);

    @Query("SELECT * FROM sold ORDER BY time DESC")
    List<SoldDTO> selectSolds();

    @Query("SELECT sold.id as sold_id, sold.medicineId as sold_medicineId, sold.quantity as sold_quantity, sold.price as sold_price, sold.time as sold_time, medicine.* FROM sold LEFT JOIN medicine ON sold.medicineId = medicine.id ORDER BY time DESC")
    List<SoldMedicineDTO> selectSoldMedicine();

    @Query("SELECT DISTINCT type FROM medicine")
    List<String> getTypes();

    @Query("SELECT DISTINCT manufacturer FROM medicine")
    List<String> getManufacturers();

    @Query("SELECT DISTINCT uses FROM medicine")
    List<String> getUses();

    @Query("SELECT DISTINCT location FROM medicine")
    List<String> getLocations();

    @Query("SELECT strftime('%Y', date(time/1000, 'unixepoch')) AS year, strftime('%m', date(time/1000, 'unixepoch')) AS month, strftime('%d', date(time/1000, 'unixepoch')) AS day, sum(price) as value FROM sold GROUP BY year, month, day ORDER BY year DESC, month DESC, day ASC")
    List<StatsDTO> selectSoldStats();

    @Query("UPDATE medicine SET wish = :wish WHERE id = :id")
    void updateWish(long id, boolean wish);

//    @Query("UPDATE sold SET soldOut = :sold WHERE id = :id")
//    void updateSoldout(long id, boolean sold);

//    @Insert(onConflict = IGNORE)
//    long[] upsert(MedicineDTO... medicines);

    @Insert(onConflict = REPLACE)
    long[] upsert(MedicineDTO... medicines);

    @Delete
    void delete(MedicineDTO medicineDTO);

    @Insert(onConflict = REPLACE)
    long[] upsert(SoldDTO... solds);

    @Query("SELECT count(*) FROM medicine WHERE (expireDate - strftime('%s','now') * 1000) < (3*24*60*60*1000)+1 AND available > 0")
    int getWarningListCount();

    @Query("SELECT * FROM medicine WHERE (expireDate - strftime('%s','now') * 1000) < (3*24*60*60*1000)+1")
    List<MedicineDTO> selectWarningList();

    @Query("SELECT * FROM sold ORDER BY time DESC")
    List<SoldDTO> selectSoldMedicineLog();

//    @Query("UPDATE medicine SET wish = :wish WHERE id = :id")
//    void updateSold(long id, boolean sold);

    @Query("SELECT * FROM sold ORDER BY medicineName")
    List<SoldDTO> selectSoldByName();

    @Query("SELECT * FROM medicine WHERE (expireDate > strftime('%s','now','+4 days') * 1000)  AND name LIKE :keyword")
    List<MedicineDTO> selectToBeExpired(String keyword);

    @Query("SELECT distinct count(*) FROM medicine WHERE (expireDate - strftime('%s','now','+4 days') * 1000)")
    int getToBeExpiredCount();

//    @Query("SELECT * FROM medicine WHERE (expireDate - strftime('%s','now') * 1000) < (3*24*60*60*1000)+1 AND (expireDate - strftime('%s','now') * 1000) > (1*24*60*60*1000)+1 AND available > 0")
//    List<MedicineDTO> selectWarningList();
}
