package com.rt.qpay99.ws;

import java.util.Collection;

public class RTSoap {

	RTSoapProperty element = new RTSoapProperty();

	@Override
	public String toString() {
		return "GDSoap{" + "element=" + element + '}';
	}

	public RTSoapProperty addProperty(String name, int value) {
		return this.element.addProperty(name, value);
	}

	public RTSoapProperty addProperty(String name, String value) {
		return this.addProperty(name, value, null);
	}

	public RTSoapProperty addProperty(String name, String value,
			String nameSpace) {
		return this.element.addProperty(name, value, nameSpace);
	}

	public RTSoapProperty addProperty(RTSoapProperty element) {
		return this.element.addProperty(element);
	}

	public Collection<RTSoapProperty> getProperties() {
		return element.getProperties();
	}

	public Collection<RTSoapProperty> getProperties(String name) {
		return this.element.getProperties(name);
	}

	public RTSoapProperty addProperty(String name, boolean value) {
		return this.element.addProperty(name, value);
	}
}
