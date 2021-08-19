package info.magnolia.poc.custom;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import info.magnolia.rest.service.node.v1.RepositoryProperty;


/**
 * Represents a node in a workspace.
 */
@XmlRootElement(name = "asset")
public class MigrateAsset {

	private MigrateAssetId _id;
	private String fn;
	private String date_loc;
	private String fsrvr_loc;
	private String etl_loc;
	private String doc_no;
	private String type;
	private String img_ext;
	private MigrateAssetImage img_str;
	private String fsize;
	private String str_size;
	private String big_tag;
	private String corrupted_tag;
	private MigrateAssetDate gcash_rcv_time;
	private MigrateAssetDate etl_land_time;
	private MigrateAssetDate mongo_datetime;

    @XmlElementWrapper(name = "_id")
    @XmlElement(name = "id")
    @JsonProperty("_id")
	public MigrateAssetId get_id() {
		return _id;
	}
	public void set_id(MigrateAssetId _id) {
		this._id = _id;
	}
	public String getFn() {
		return fn;
	}
	public void setFn(String fn) {
		this.fn = fn;
	}
	public String getDate_loc() {
		return date_loc;
	}
	public void setDate_loc(String date_loc) {
		this.date_loc = date_loc;
	}
	public String getFsrvr_loc() {
		return fsrvr_loc;
	}
	public void setFsrvr_loc(String fsrvr_loc) {
		this.fsrvr_loc = fsrvr_loc;
	}
	public String getEtl_loc() {
		return etl_loc;
	}
	public void setEtl_loc(String etl_loc) {
		this.etl_loc = etl_loc;
	}
	public String getDoc_no() {
		return doc_no;
	}
	public void setDoc_no(String doc_no) {
		this.doc_no = doc_no;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImg_ext() {
		return img_ext;
	}
	public void setImg_ext(String img_ext) {
		this.img_ext = img_ext;
	}
    @XmlElementWrapper(name = "img_str")
    @XmlElement(name = "image")
    @JsonProperty("img_str")
	public MigrateAssetImage getImg_str() {
		return img_str;
	}
	public void setImg_str(MigrateAssetImage img_str) {
		this.img_str = img_str;
	}
	public String getFsize() {
		return fsize;
	}
	public void setFsize(String fsize) {
		this.fsize = fsize;
	}
	public String getStr_size() {
		return str_size;
	}
	public void setStr_size(String str_size) {
		this.str_size = str_size;
	}
	public String getBig_tag() {
		return big_tag;
	}
	public void setBig_tag(String big_tag) {
		this.big_tag = big_tag;
	}
	public String getCorrupted_tag() {
		return corrupted_tag;
	}
	public void setCorrupted_tag(String corrupted_tag) {
		this.corrupted_tag = corrupted_tag;
	}
    @XmlElementWrapper(name = "date")
    @XmlElement(name = "date")
    @JsonProperty("gcash_rcv_time")
	public MigrateAssetDate getGcash_rcv_time() {
		return gcash_rcv_time;
	}
	public void setGcash_rcv_time(MigrateAssetDate gcash_rcv_time) {
		this.gcash_rcv_time = gcash_rcv_time;
	}
    @XmlElementWrapper(name = "etl_land_time")
    @XmlElement(name = "date")
    @JsonProperty("etl_land_time")
	public MigrateAssetDate getEtl_land_time() {
		return etl_land_time;
	}
	
	public void setEtl_land_time(MigrateAssetDate etl_land_time) {
		this.etl_land_time = etl_land_time;
	}
    @XmlElementWrapper(name = "mongo_datetime")
    @XmlElement(name = "date")
    @JsonProperty("mongo_datetime")
	public MigrateAssetDate getMongo_datetime() {
		return mongo_datetime;
	}

	public void setMongo_datetime(MigrateAssetDate mongo_datetime) {
		this.mongo_datetime = mongo_datetime;
	}
    
    

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public String getIdentifier() {
//        return identifier;
//    }
//
//    public void setIdentifier(String identifier) {
//        this.identifier = identifier;
//    }
//
//    public boolean addNode(MigrateAsset name) {
//        if (nodes == null) {
//            nodes = new ArrayList<MigrateAsset>();
//        }
//        return nodes.add(name);
//    }
//
//    @XmlElementWrapper(name = "nodes")
//    @XmlElement(name = "node")
//    @JsonProperty("nodes")
//    public List<MigrateAsset> getNodes() {
//        return nodes;
//    }
//
//    @XmlElementWrapper(name = "properties")
//    @XmlElement(name = "property")
//    @JsonProperty("properties")
//    public List<RepositoryProperty> getProperties() {
//        return properties;
//    }
//
//    public void setProperties(List<RepositoryProperty> properties) {
//        this.properties = properties;
//    }
}
