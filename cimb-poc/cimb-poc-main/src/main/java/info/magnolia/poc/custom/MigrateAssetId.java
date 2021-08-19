package info.magnolia.poc.custom;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "id")
public class MigrateAssetId {
	private String $oid;

	public String get$oid() {
		return $oid;
	}

	public void set$oid(String $oid) {
		this.$oid = $oid;
	}
	
	

}
