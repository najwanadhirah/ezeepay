package com.rt.qpay99.object;

public class ProductInfo {
	private int pId;
	private String name;
	private String keyword;
	private String Denomination;
	private String DenominationDescription;
	private String Description;
	private String Instruction;
	private String Status;
	private String MaxLen;
	private String minLen;
	private String Tax;
	private String model;
	private String FirebaseDBId;


	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFirebaseDBId() {
		return FirebaseDBId;
	}

	public void setFirebaseDBId(String firebaseDBId) {
		FirebaseDBId = firebaseDBId;
	}

	public String getDenominationDescription() {
		return DenominationDescription;
	}

	public void setDenominationDescription(String denominationDescription) {
		DenominationDescription = denominationDescription;
	}

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getMaxLen() {
		return MaxLen;
	}

	public void setMaxLen(String maxLen) {
		MaxLen = maxLen;
	}

	public String getMinLen() {
		return minLen;
	}

	public void setMinLen(String minLen) {
		this.minLen = minLen;
	}

	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDenomination() {
		return Denomination;
	}

	public void setDenomination(String denomination) {
		Denomination = denomination;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getInstruction() {
		return Instruction;
	}

	public void setInstruction(String instruction) {
		Instruction = instruction;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

}
