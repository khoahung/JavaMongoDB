package info.magnolia.poc.custom;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "date")
public class MigrateAssetDate {
	private String $date;


	public String get$date() {
		return $date;
	}

	public void set$date(String $date) {
		this.$date = $date;
	}
	
	

}
