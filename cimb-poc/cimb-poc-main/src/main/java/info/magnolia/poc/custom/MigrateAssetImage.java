package info.magnolia.poc.custom;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "image")
public class MigrateAssetImage {
	
	private MigrateAssetImageBinary $binary;

    @XmlElementWrapper(name = "$binary")
    @XmlElement(name = "binary")
    @JsonProperty("$binary")
	public MigrateAssetImageBinary get$binary() {
		return $binary;
	}

	public void set$binary(MigrateAssetImageBinary $binary) {
		this.$binary = $binary;
	}
	
	

}
