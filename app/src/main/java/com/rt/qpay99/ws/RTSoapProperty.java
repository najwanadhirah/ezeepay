package com.rt.qpay99.ws;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.kobjects.isodate.IsoDate;

public class RTSoapProperty {

    private String name;
    private String value;
    private String nameSpace;
    private ArrayList<RTSoapProperty> subElements;
    private ArrayList<RTSoapAttribute> attributes;

    @Override
    public String toString() {
        return "GDSoapElement{" + "name=" + name + ", value=" + value
                + ", nameSpace=" + nameSpace + ", subElements=" + subElements
                + ", attributes=" + attributes + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIntValue() {
        try {
            return Integer.parseInt(this.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Collection<RTSoapAttribute> getAttributes() {
        return attributes;
    }

    public RTSoapAttribute addAttribute(String name, String value) {
        RTSoapAttribute result = new RTSoapAttribute();
        result.setName(name);
        result.setValue(value);
        if (this.attributes == null) {
            this.attributes = new ArrayList<RTSoapAttribute>();
        }
        this.attributes.add(result);
        return result;
    }

    public Collection<RTSoapAttribute> getAttribute(String name) {
        Collection<RTSoapAttribute> result = new ArrayList<RTSoapAttribute>();
        for (RTSoapAttribute element : this.attributes) {
            if (element.getName().equals(name)) {
                result.add(element);
            }
        }
        return result;
    }

    public Collection<RTSoapProperty> getProperties() {
        return this.subElements;
    }

    public RTSoapProperty addProperty(String name, int value) {
        return this.addProperty(name, String.valueOf(value));
    }

    public RTSoapProperty addProperty(String name, Date value) {
        return this.addProperty(name,
                IsoDate.dateToString(value, IsoDate.DATE_TIME));
    }

    public RTSoapProperty addProperty(String name, String value) {
        return this.addProperty(name, value, null);
    }

    public RTSoapProperty addProperty(String name, String value,
            String nameSpace) {
        RTSoapProperty soap = new RTSoapProperty();
        soap.setName(name);
        soap.setValue(value);
        soap.setNameSpace(nameSpace);
        return this.addProperty(soap);
    }

    public RTSoapProperty addProperty(RTSoapProperty element) {
        if (this.subElements == null) {
            this.subElements = new ArrayList<RTSoapProperty>();
        }
        this.subElements.add(element);
        return element;
    }

    public int getIntProperty(String name) {
        return this.getIntProperty(name, 0);
    }

    public int getIntProperty(String name, int defaultValue) {
        if (name == null) {
            return defaultValue;
        }
        Collection<RTSoapProperty> properties = this.getProperties(name);
        if (properties != null && !properties.isEmpty()) {
            try {
                return Integer
                        .parseInt(properties.iterator().next().getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        } else {
            return defaultValue;
        }
    }

    public String getStringProperty(String name) {
        if (name == null) {
            return null;
        }
        Collection<RTSoapProperty> properties = this.getProperties(name);
        if (properties != null && !properties.isEmpty()) {
            return properties.iterator().next().getValue();
        } else {
            return "";
        }
    }

    public BigInteger getBigIntegerProperty(String name) {
        if (name == null) {
            return null;
        }
        Collection<RTSoapProperty> properties = this.getProperties(name);
        if (properties != null && !properties.isEmpty()) {
            return new BigInteger(properties.iterator().next().getValue());
        } else {
            return null;
        }
    }

    public Date getDateProperty(String name) {
        if (name == null) {
            return null;
        }
        Collection<RTSoapProperty> properties = this.getProperties(name);
        if (properties != null && !properties.isEmpty()) {
            return IsoDate.stringToDate(this.getStringProperty(name),
                    IsoDate.DATE_TIME);
        } else {
            return null;
        }
    }

    public RTSoapProperty getProperty(String name) {
        if (name == null) {
            return null;
        }
        Collection<RTSoapProperty> properties = this.getProperties(name);
        if (properties != null && !properties.isEmpty()) {
            return properties.iterator().next();
        } else {
            return null;
        }
    }

    public Collection<RTSoapProperty> getProperties(String name) {
        Collection<RTSoapProperty> result = new ArrayList<RTSoapProperty>();
        for (RTSoapProperty element : this.subElements) {
            if (element.getName().equals(name)) {
                result.add(element);
            }
        }
        return result;
    }

    public boolean getBooleanValue() {
        return Boolean.parseBoolean(this.getValue());
    }

    public RTSoapProperty addProperty(String name2, boolean value2) {
        return this.addProperty(name, String.valueOf(value));
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getNameSpace() {
        return nameSpace;
    }
}
