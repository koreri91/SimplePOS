package com.app.estockcard.controller;

import com.app.estockcard.model.Discount;
import com.app.estockcard.model.Employee;
import com.app.estockcard.model.Product;
import com.app.estockcard.model.Profile;
import com.app.estockcard.model.Shift;
import com.app.estockcard.model.Transaction;
import com.app.estockcard.view.admin.management.employee.EmployeeAdapterRV;
import com.app.estockcard.view.admin.shift.ShiftAdapterRV;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DBManager {

    public DBManager() {
    }

    public List<Employee> getEmployees(int type) {
        RealmResults results = Realm.getDefaultInstance().where(Employee.class).equalTo("type", type).findAll();
        List<Employee> employees = Realm.getDefaultInstance().copyFromRealm(results);
        for (int idx = 0; idx < employees.size(); idx++) {
            employees.get(idx).setViewType(EmployeeAdapterRV.TYPE_HOLDER_DISPLAY);
        }
        return employees;
    }

    public Employee getEmployee(int employeeID) {
        return Realm.getDefaultInstance().where(Employee.class).equalTo("id", employeeID).findFirst();
    }

    public void insertEmployee(Employee employee, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            Number currentIdNum = realm.where(Employee.class).max("id");
            int nextId = (currentIdNum == null) ? 1 : currentIdNum.intValue() + 1;
            employee.setId(nextId);
            realm.insert(employee);

        }, () -> listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public void updateEmployee(Employee employee, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.insertOrUpdate(employee), () -> listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public void deleteEmployee(Employee employee, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.where(Employee.class).equalTo("id", employee.getId()).findAll().deleteAllFromRealm(), () -> listener.onSuccess("Success"), error -> listener.onError("Error"));
    }

    //get product by type
    public List<Product> getProduct(int type) {
        RealmResults results = Realm.getDefaultInstance().where(Product.class).equalTo("type", type).findAll();
        return Realm.getDefaultInstance().copyFromRealm(results);
    }

    public List<Product> getProductsOrdered(int type) {
        RealmResults results = Realm.getDefaultInstance().where(Product.class).equalTo("type", type)
                .greaterThan("countOrdered", 0).findAll();
        return Realm.getDefaultInstance().copyFromRealm(results);
    }

    public int getCountOfProductOrdered() {
        return Realm.getDefaultInstance().where(Product.class).sum("countOrdered").intValue();
    }

    //get product by name
    public Product getProductByName(String name, int type) {
        return Realm.getDefaultInstance().where(Product.class).equalTo("type", type).equalTo("name", name).findFirst();
    }


    //get product by categoryId
    public List<Product> getProductsByCategoryId(int categoryId) {
        RealmResults results = Realm.getDefaultInstance().where(Product.class).equalTo("categoryId", categoryId).findAll();
        return Realm.getDefaultInstance().copyFromRealm(results);
    }

    public Product getCategoryByCategoryId(int categoryId, int type) {
        return Realm.getDefaultInstance().where(Product.class)
                .equalTo("id", categoryId)
                .equalTo("type", type).findFirst();
    }

    public Product getProduct(int id, int type) {
        return Realm.getDefaultInstance().where(Product.class).equalTo("type", type).equalTo("id", id).findFirst();
    }


    public void insertProduct(Product product, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            Number currentIdNum = realm.where(Product.class).max("id");
            int nextId = (currentIdNum == null) ? 1 : currentIdNum.intValue() + 1;
            product.setId(nextId);
            realm.insert(product);
        }, () -> listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public synchronized void updateProduct(Product product, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.insertOrUpdate(product), () -> {
            if (listener != null)
                listener.onSuccess("Success");
        }, error -> {
            if (listener != null)
                listener.onError("Error : " + error.getMessage());
        });
    }

    public void deleteProduct(Product product, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.where(Product.class).equalTo("id", product.getId()).findAll().deleteAllFromRealm(), () -> listener.onSuccess("Success"), error -> listener.onError("Error"));
    }

    public List<Shift> getShifter() {
        RealmResults results = Realm.getDefaultInstance().where(Shift.class).findAll();
        return Realm.getDefaultInstance().copyFromRealm(results);
    }

    public List<Shift> getShifter(String groupID) {
        RealmResults results = Realm.getDefaultInstance().where(Shift.class).equalTo("groupID", groupID).findAll();
        return Realm.getDefaultInstance().copyFromRealm(results);
    }

    private String TAG = DBManager.class.getSimpleName();

    public List<Shift> getShifterWithGroup() {
        List<Shift> shiftResult = new ArrayList<>();

        List<Shift> groupIDs = Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Shift.class).distinct("groupID").findAll());


        for (Shift group : groupIDs) {
            Shift item = new Shift();
            item.setGroupID(group.getGroupID());
            item.setDateWork(item.getDateWork());
            item.setShifter(getShifter(group.getGroupID()));
            item.setHolderType(ShiftAdapterRV.TYPE_HOLDER_DISPLAY);
            shiftResult.add(item);
        }

        return shiftResult;
    }

    public void insertShift(Shift shift, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.insert(shift), () -> listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public void updateShift(Shift shift, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.insertOrUpdate(shift), () -> listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public void deleteShift(Shift shift, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.where(Shift.class).equalTo("groupID", shift.getGroupID()).findAll().deleteAllFromRealm(), () -> listener.onSuccess("Success"), error -> listener.onError("Error"));
    }


    public List<Discount> getAllDiscount() {
        return Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Discount.class).findAll());
    }

    public Discount getDiscount(int id) {
        return Realm.getDefaultInstance().where(Discount.class).equalTo("id", id).findFirst();
    }

    public void insertDiscount(Discount discount, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            Number currentIdNum = realm.where(Discount.class).max("id");
            int nextId = (currentIdNum == null) ? 1 : currentIdNum.intValue() + 1;
            discount.setId(nextId);
            realm.insert(discount);

        }, () -> listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public void updateDiscount(Discount discount, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.insertOrUpdate(discount), () -> listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public void deleteDiscount(Discount discount, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.where(Discount.class).equalTo("id", discount.getId()).findAll().deleteAllFromRealm(), () -> listener.onSuccess("Success"), error -> listener.onError("Error"));
    }

    public double getTotalPaymentByDay(String date) {
        return Realm.getDefaultInstance().where(Transaction.class).contains("dateTime", date, Case.INSENSITIVE).sum("total").doubleValue();
    }

    public double getTotalPaymentByDay(String date, int status) {
        return Realm.getDefaultInstance().where(Transaction.class).equalTo("status", status).contains("dateTime", date, Case.INSENSITIVE).sum("total").doubleValue();
    }

    public List<Transaction> getAllTransaction(int status) {
        List<Transaction> data = Realm.getDefaultInstance().copyFromRealm(
                (status == Transaction.TRANSACTION_ALL) ? Realm.getDefaultInstance().where(Transaction.class).sort("dateTime", Sort.DESCENDING).findAll() :
                        Realm.getDefaultInstance().where(Transaction.class).equalTo("status", status).sort("dateTime", Sort.DESCENDING).findAll());
        List<Transaction> transactions = new ArrayList<>();
        String dateBefore = "";

        for (Transaction item : data) {
            String currDate = item.getDateTime().split(" ")[0];
            SysLog.getInstance().sendLog(TAG, currDate + " = " + dateBefore + " , " + currDate.equals(dateBefore));
            if (!currDate.equals(dateBefore)) {
                dateBefore = item.getDateTime().split(" ")[0];
                Transaction title = new Transaction();
                title.setDateTime(item.getDateTime());
                title.setTotal((status == Transaction.TRANSACTION_ALL) ?
                        getTotalPaymentByDay(dateBefore.length() == 0 ? currDate : dateBefore) :
                        getTotalPaymentByDay(dateBefore.length() == 0 ? currDate : dateBefore, status));
                title.setViewHolder(Transaction.viewHolderTitle);
                transactions.add(title);
                SysLog.getInstance().sendLog(TAG, "item date is different, copy title to list (nested if block)");
            }
            if (currDate.equals(dateBefore)) {
                Transaction trx = item;
                trx.setViewHolder(Transaction.viewHolderData);
                transactions.add(trx);
                SysLog.getInstance().sendLog(TAG, "item date is same, copy data to list (nested else block)");
            }
        }
        return transactions;
    }

    public List<Transaction> getAllCustomerTransaction(int status) {
        Realm realm=  Realm.getDefaultInstance();
        List<Transaction> transactions;
        try {
            realm.beginTransaction();
             transactions = realm.copyFromRealm(Realm.getDefaultInstance()
                    .where(Transaction.class).equalTo("status", status).and()
                    .greaterThan("buyerId", -1).sort("dateTime").findAll());
            realm.commitTransaction();
        }finally {
            realm.close();
        }
        return transactions;
    }

    public Transaction getTransaction(String transactionID) {

        return Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Transaction.class).equalTo("id", transactionID).findFirst());
    }

    public void insertTransaction(Transaction transaction, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {

            realm.insert(transaction);

        }, () ->
                listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public void updateTransaction(Transaction transaction, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.insertOrUpdate(transaction), () -> listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public void deleteTransaction(Transaction transaction, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.where(Transaction.class).equalTo("id", transaction.getId()).findAll().deleteAllFromRealm(), () -> listener.onSuccess("Success"), error -> listener.onError("Error"));
    }

    public void insertUser(Profile profile, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.insert(profile), () -> listener.onSuccess("Success"), error -> listener.onError("Error : " + error.getMessage()));
    }

    public Profile getUser() {
        Profile profile = Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Profile.class).findFirst());
        return profile;
    }

    public void deleteUser(Profile profile, OnDBResultListener listener) {
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.where(Profile.class).equalTo("id", profile.getId()).findAll().deleteAllFromRealm(), () -> listener.onSuccess("Success"), error -> listener.onError("Error"));
    }

    public boolean isProfileExists() {
        return Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Profile.class).findAll()).size() > 0;

    }


}
