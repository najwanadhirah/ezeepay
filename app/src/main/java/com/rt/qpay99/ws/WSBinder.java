package com.rt.qpay99.ws;

import com.rt.qpay99.Config;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.AgentProductRebate;
import com.rt.qpay99.object.AgentSales;
import com.rt.qpay99.object.AgentSalesByMSISDN;
import com.rt.qpay99.object.BankIn;
import com.rt.qpay99.object.CustomerInputList;
import com.rt.qpay99.object.CustomerTopupTx;
import com.rt.qpay99.object.CustomerTxStatusInfo;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.object.RequestInputResponse;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WSBinder {

	public static List<AgentSalesByMSISDN> bindAgentSalesByMSISDN(
			RTSoapProperty tag) {
		List<AgentSalesByMSISDN> p = new ArrayList<AgentSalesByMSISDN>();
		Collection<RTSoapProperty> props = tag.getProperties();
		for (RTSoapProperty prop : props) {
			if (prop != null) {
				DLog.e(Config.DEBUG_TAG, prop.getName());
				p.add(WSBinder.bindAgentSalesByMSISDNList(prop));
			}
		}
		DLog.e(Config.DEBUG_TAG, "" + p.size());
		return p;
	}

	public static AgentSalesByMSISDN bindAgentSalesByMSISDNList(RTSoapProperty tag) {
		AgentSalesByMSISDN info = null;

		Collection<RTSoapProperty> props = tag.getProperties();
		if (props != null)
			info = new AgentSalesByMSISDN();
		for (RTSoapProperty prop : props) {
			if ("Product".equalsIgnoreCase(prop.getName()))
				info.setProduct(prop.getValue());
			if ("Count".equalsIgnoreCase(prop.getName()))
				info.setCount(prop.getIntValue());
			if ("RetailPrice".equalsIgnoreCase(prop.getName()))
				info.setRetailPrice(prop.getValue());
			if ("Cost".equalsIgnoreCase(prop.getName()))
				info.setCost(prop.getValue());
			if ("ToTalCostValue".equalsIgnoreCase(prop.getName()))
				info.setToTalCostValue(prop.getValue());
			if ("TotalFaceValue".equalsIgnoreCase(prop.getName()))
				info.setTotalFaceValue(prop.getValue());

		}
		return info;
	}


	public static List<CustomerTopupTx> bindCheckUserTopupTxByType(
			RTSoapProperty tag) {
		List<CustomerTopupTx> p = new ArrayList<CustomerTopupTx>();
		Collection<RTSoapProperty> props = tag.getProperties();
		for (RTSoapProperty prop : props) {
			if (prop != null) {
				DLog.e(Config.DEBUG_TAG, prop.getName());
				p.add(WSBinder.bindCheckUserTopupTxByTypeList(prop));
			}
		}
		DLog.e(Config.DEBUG_TAG, "" + p.size());
		return p;
	}

	public static CustomerTopupTx bindCheckUserTopupTxByTypeList(RTSoapProperty tag) {
		CustomerTopupTx info = null;

		Collection<RTSoapProperty> props = tag.getProperties();
		if (props != null)
			info = new CustomerTopupTx();
		for (RTSoapProperty prop : props) {
			if ("Amount".equalsIgnoreCase(prop.getName()))
				info.setAmount(prop.getValue());
			if ("Remarks".equalsIgnoreCase(prop.getName()))
				info.setRemarks(prop.getValue());
			if ("Type".equalsIgnoreCase(prop.getName()))
				info.setType(prop.getValue());
			if ("NewBalance".equalsIgnoreCase(prop.getName()))
				info.setNewBalance(prop.getValue());
			if ("CreatedTS".equalsIgnoreCase(prop.getName()))
				info.setCreatedTS(prop.getValue());

		}
		return info;
	}


	public static List<BankIn> bindBankIns(
			RTSoapProperty tag) {
		List<BankIn> p = new ArrayList<BankIn>();
		Collection<RTSoapProperty> props = tag.getProperties();
		for (RTSoapProperty prop : props) {
			if (prop != null) {
				DLog.e(Config.DEBUG_TAG, prop.getName());
				p.add(WSBinder.bindBankInList(prop));
			}
		}
		DLog.e(Config.DEBUG_TAG, "" + p.size());
		return p;
	}

	public static BankIn bindBankInList(RTSoapProperty tag) {
		BankIn info = null;

		Collection<RTSoapProperty> props = tag.getProperties();
		if (props != null)
			info = new BankIn();
		for (RTSoapProperty prop : props) {
			if ("Bank".equalsIgnoreCase(prop.getName()))
				info.setBank(prop.getValue());
			if ("Amount".equalsIgnoreCase(prop.getName()))
				info.setAmount(prop.getValue());
			if ("DateBI".equalsIgnoreCase(prop.getName()))
				info.setDateBI(prop.getValue());
			if ("Time".equalsIgnoreCase(prop.getName()))
				info.setTime(prop.getValue());
			if ("Status".equalsIgnoreCase(prop.getName()))
				info.setStatus(prop.getValue());
			if ("RecipientReference".equalsIgnoreCase(prop.getName()))
				info.setRecipientReference(prop.getValue());
			if ("Created".equalsIgnoreCase(prop.getName()))
				info.setCreated(prop.getValue());

		}
		return info;
	}

	public static List<CustomerInputList> bindCustomerInputLists(
			RTSoapProperty tag) {
		List<CustomerInputList> p = new ArrayList<CustomerInputList>();
		Collection<RTSoapProperty> props = tag.getProperties();
		for (RTSoapProperty prop : props) {
			if (prop != null) {
				DLog.e(Config.DEBUG_TAG, prop.getName());
				p.add(WSBinder.bindCustomerInputList(prop));
			}
		}
		DLog.e(Config.DEBUG_TAG, "" + p.size());
		return p;
	}

	public static CustomerInputList bindCustomerInputList(RTSoapProperty tag) {
		CustomerInputList info = null;

		Collection<RTSoapProperty> props = tag.getProperties();
		if (props != null)
			info = new CustomerInputList();
		for (RTSoapProperty prop : props) {
			if ("TxID".equalsIgnoreCase(prop.getName()))
				info.setTxID(prop.getValue());
			if ("ProductID".equalsIgnoreCase(prop.getName()))
				info.setProductID(prop.getValue());
			if ("ProductName".equalsIgnoreCase(prop.getName()))
				info.setProductName(prop.getValue());
			if ("ProductPrice".equalsIgnoreCase(prop.getName()))
				info.setProductPrice(prop.getValue());
			if ("CustomerAccountNumber".equalsIgnoreCase(prop.getName()))
				info.setCustomerAccountNumber(prop.getValue());
			if ("CustomerMobileNumber".equalsIgnoreCase(prop.getName()))
				info.setCustomerMobileNumber(prop.getValue());
			if ("Status".equalsIgnoreCase(prop.getName()))
				info.setStatus(prop.getValue());

		}
		return info;
	}

	public static List<AgentSales> bindAgentSaleInfos(RTSoapProperty tag) {
		List<AgentSales> p = new ArrayList<AgentSales>();
		Collection<RTSoapProperty> props = tag.getProperties();
		for (RTSoapProperty prop : props) {
			if (prop != null) {
				DLog.e(Config.DEBUG_TAG, prop.getName());
				p.add(WSBinder.bindAgentSales(prop));
			}
		}
		DLog.e(Config.DEBUG_TAG, "" + p.size());
		return p;
	}

	public static AgentSales bindAgentSales(RTSoapProperty tag) {
		AgentSales info = null;

		Collection<RTSoapProperty> props = tag.getProperties();
		if (props != null)
			info = new AgentSales();
		for (RTSoapProperty prop : props) {
			if ("ProductName".equalsIgnoreCase(prop.getName()))
				info.setProductName(prop.getValue());
			if ("TotalSales".equalsIgnoreCase(prop.getName()))
				info.setTotalSales(prop.getValue());
			// DLog.e(Config.DEBUG_TAG, info.getProductName());
		}
		return info;
	}

	public static List<AgentProductRebate> bindAgentProductRebates(
			RTSoapProperty tag) {
		List<AgentProductRebate> p = new ArrayList<AgentProductRebate>();
		Collection<RTSoapProperty> props = tag.getProperties();
		if (props != null)
			for (RTSoapProperty prop : props) {
				if (prop != null) {
					DLog.e(Config.DEBUG_TAG, prop.getName());
					p.add(WSBinder.bindAgentProductRebate(prop));
				}
			}
		DLog.e(Config.DEBUG_TAG, "" + p.size());
		return p;
	}

	public static AgentProductRebate bindAgentProductRebate(RTSoapProperty tag) {
		AgentProductRebate info = null;

		Collection<RTSoapProperty> props = tag.getProperties();
		info = new AgentProductRebate();
		info.setProductID(tag.getStringProperty("ProductID"));
		info.setProductName(tag.getStringProperty("ProductName"));
		info.setDenomination(tag.getStringProperty("Denomination"));
		info.setRebateRate(tag.getStringProperty("RebateRate"));
		info.setRebateType(tag.getStringProperty("RebateType"));
		info.setLastUpdated(tag.getStringProperty("LastUpdated"));
		DLog.e(Config.DEBUG_TAG, info.getProductName());
		// if (props != null)
		// for (RTSoapProperty prop : props) {
		// info = new AgentProductRebate();
		// info.setProductID(prop.getStringProperty("ProductID"));
		// info.setProductName(prop.getStringProperty("ProductName"));
		// info.setDenomination(prop.getStringProperty("Denomination"));
		// info.setRebateRate(prop.getStringProperty("RebateRate"));
		// info.setRebateType(prop.getStringProperty("RebateType"));
		// info.setLastUpdated(prop.getStringProperty("LastUpdated"));
		// DLog.e(Config.DEBUG_TAG, info.getProductName());
		// }
		return info;
	}

	public static List<AgentProductDiscount> bindAgentProductDiscounts(
			RTSoapProperty tag) {
		List<AgentProductDiscount> p = new ArrayList<AgentProductDiscount>();
		Collection<RTSoapProperty> props = tag.getProperties();
		for (RTSoapProperty prop : props) {
			if (prop != null) {
				DLog.e(Config.DEBUG_TAG, prop.getName());
				p.add(WSBinder.bindAgentProductDiscount(prop));
			}
		}
		DLog.e(Config.DEBUG_TAG, "" + p.size());
		return p;
	}

	public static AgentProductDiscount bindAgentProductDiscount(
			RTSoapProperty tag) {
		AgentProductDiscount info = new AgentProductDiscount();
		info.setProductId(tag.getIntProperty("ProductID"));
		info.setProductName(tag.getStringProperty("ProductName"));
		info.setDiscountType(tag.getStringProperty("DiscountType"));
		info.setDiscountRate(tag.getStringProperty("DiscountRate"));
		info.setLastUpdated(tag.getStringProperty("LastUpdated"));

		DLog.e(Config.DEBUG_TAG, info.getProductName());
		return info;
	}

	public static List<CustomerTxStatusInfo> bindCustomerTxStatusInfos(
			RTSoapProperty tag) {
		List<CustomerTxStatusInfo> p = new ArrayList<CustomerTxStatusInfo>();
		Collection<RTSoapProperty> props = tag.getProperties();
		for (RTSoapProperty prop : props) {
			if (prop != null) {
				DLog.e(Config.DEBUG_TAG, prop.getName());
				p.add(WSBinder.bindCustomerTxStatusInfo(prop));
			}
		}
		DLog.e(Config.DEBUG_TAG, "" + p.size());
		return p;
	}

	public static CustomerTxStatusInfo bindCustomerTxStatusInfo(
			RTSoapProperty tag) {
		CustomerTxStatusInfo info = new CustomerTxStatusInfo();
		info.setProduct(tag.getStringProperty("Product"));
		info.setAmount(tag.getStringProperty("Amount"));
		info.setDateTime(tag.getStringProperty("DateTime"));
		info.setDN(tag.getStringProperty("DN"));
		info.setCode(tag.getStringProperty("Code"));
		info.setStatus(tag.getStringProperty("Status"));
		info.setLocalMOID(tag.getStringProperty("LocalMOID"));
		info.setsReloadMSISDN(tag.getStringProperty("sReloadMSISDN"));
		if(FunctionUtil.isSet(tag.getStringProperty("RetailPrice")))
			info.setRetailPrice(tag.getStringProperty("RetailPrice"));

		try{
			info.setRetry(tag.getIntProperty("Retry"));
		}catch (Exception e){
			info.setRetry(99);
		}

		DLog.e(Config.DEBUG_TAG, info.getProduct());
		return info;
	}

	public static List<ProductInfo> bindProductInfos(RTSoapProperty tag) {
		List<ProductInfo> p = new ArrayList<ProductInfo>();
		Collection<RTSoapProperty> props = tag.getProperties();
		for (RTSoapProperty prop : props) {
			DLog.e(Config.DEBUG_TAG, prop.getName());
			p.add(WSBinder.bindProductInfo(prop));
		}
		DLog.e(Config.DEBUG_TAG, "" + p.size());
		return p;
	}

	public static RequestInputResponse bindRequestInputResponse(
			RTSoapProperty tag) {
		RequestInputResponse result = new RequestInputResponse();
		Collection<RTSoapProperty> props = tag.getProperties();
		for (RTSoapProperty prop : props) {
			DLog.e(Config.DEBUG_TAG, prop.getName());

		}
		return result;

	}

	public static ProductInfo bindProductInfo(RTSoapProperty tag) {
		ProductInfo productInfo = new ProductInfo();
		productInfo.setDenomination(tag.getStringProperty("Denomination"));
		productInfo.setDescription(tag.getStringProperty("Description"));
		productInfo.setInstruction(tag.getStringProperty("Instruction"));
		productInfo.setKeyword(tag.getStringProperty("Keyword"));
		productInfo.setName(tag.getStringProperty("Name"));
		productInfo.setpId(tag.getIntProperty("ID"));
		productInfo.setStatus(tag.getStringProperty("Status"));
		String length = tag.getStringProperty("MobileLength");
		String[] len = length.split("-");
		productInfo.setMaxLen("10");
		productInfo.setMinLen("11");
        productInfo.setTax("0.00");
        if(Config.isForceGST){
            productInfo.setTax("0.00");
        }
        if(tag.getStringProperty("Tax")!=null)
            if(FunctionUtil.isSet(tag.getStringProperty("Tax"))){
                productInfo.setTax(tag.getStringProperty("Tax"));
            }

		if (len.length == 2) {
			DLog.e(Config.DEBUG_TAG, "len 0 " + len[0]);
			DLog.e(Config.DEBUG_TAG, "len 1 " + len[1]);
			productInfo.setMaxLen(len[1]);
			productInfo.setMinLen(len[0]);
		}

		try{
			productInfo.setDenominationDescription(tag.getStringProperty("DenominationDescription"));
		}catch(Exception ex){
			DLog.e(Config.DEBUG_TAG, ex.getLocalizedMessage());
			productInfo.setDenominationDescription("");
		}

		// DLog.e(Config.DEBUG_TAG, productInfo.getName());

		return productInfo;

	}
}
