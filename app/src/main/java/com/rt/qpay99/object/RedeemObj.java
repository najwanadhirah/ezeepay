package com.rt.qpay99.object;

public class RedeemObj {
    private int  mProductId;
    private String mName;
    private String Description;
    private int RedeemPoints;
    private int PromoDiscount;
    private String Status;
    private int isPromo;
    private String Category;
    private String FirebaseDBId;
    private String ProductURL;

//    public RedeemObj (int mProductId,String mName,String Description,int RedeemPoints, int PromoDiscount,int isPromo, String Category, String FirebaseDBId){
//
//
//
//    }


    public String getProductURL() {
        return ProductURL;
    }

    public void setProductURL(String productURL) {
        ProductURL = productURL;
    }

    public int getmProductId() {
        return mProductId;
    }

    public void setmProductId(int mProductId) {
        this.mProductId = mProductId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getRedeemPoints() {
        return RedeemPoints;
    }

    public void setRedeemPoints(int redeemPoints) {
        RedeemPoints = redeemPoints;
    }

    public int getPromoDiscount() {
        return PromoDiscount;
    }

    public void setPromoDiscount(int promoDiscount) {
        PromoDiscount = promoDiscount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getIsPromo() {
        return isPromo;
    }

    public void setIsPromo(int isPromo) {
        this.isPromo = isPromo;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getFirebaseDBId() {
        return FirebaseDBId;
    }

    public void setFirebaseDBId(String firebaseDBId) {
        FirebaseDBId = firebaseDBId;
    }
}
