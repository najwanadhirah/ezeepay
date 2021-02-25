package com.rt.qpay99.FirebaseObj;

import java.util.List;

/**
 * Created by User on 6/6/2017.
 */

public class FirebaseProduct {

    public int id;
    public String modal;
    public List<PriceList> prices;
    public String Description;


    public FirebaseProduct(){

    }

    public FirebaseProduct(int id, String modal, String Description, List<PriceList>  prices){
        this.id = id;
        this.modal= modal;
        this.Description = Description;
        this.prices = prices;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<PriceList> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceList> prices) {
        this.prices = prices;
    }
}


