package info.magnolia.poc.custom;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "binary")
public class MigrateAssetImageBinary {
	
	private String base64;
	private String subType;

	public String getBase64() {
		return base64;
	}
	public void setBase64(String base64) {
		this.base64 = base64;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	
	
	

}
