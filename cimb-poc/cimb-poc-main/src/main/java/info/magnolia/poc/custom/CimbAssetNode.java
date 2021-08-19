package info.magnolia.poc.custom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import info.magnolia.rest.service.node.v1.RepositoryNode;
import info.magnolia.rest.service.node.v1.RepositoryProperty;

@XmlRootElement(name = "node")
public class CimbAssetNode {

    private String name;
    private String type;
    private String path;
    private String identifier;
    private List<RepositoryProperty> nodeProperties = new ArrayList<RepositoryProperty>();
    private List<RepositoryProperty> assetProperties = new ArrayList<RepositoryProperty>();
    private List<RepositoryNode> nodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean addNode(RepositoryNode name) {
        if (nodes == null) {
            nodes = new ArrayList<RepositoryNode>();
        }
        return nodes.add(name);
    }

    @XmlElementWrapper(name = "nodes")
    @XmlElement(name = "node")
    @JsonProperty("nodes")
    public List<RepositoryNode> getNodes() {
        return nodes;
    }

    @XmlElementWrapper(name = "nodeProperties")
    @XmlElement(name = "property")
    @JsonProperty("nodeProperties")
    public List<RepositoryProperty> getNodeProperties() {
        return nodeProperties;
    }

    public void setNodeProperties(List<RepositoryProperty> nodeProperties) {
        this.nodeProperties = nodeProperties;
    }
    
    @XmlElementWrapper(name = "assetProperties")
    @XmlElement(name = "property")
    @JsonProperty("assetProperties")
    public List<RepositoryProperty> getAssetProperties() {
        return assetProperties;
    }

    public void setAssetProperties(List<RepositoryProperty> assetProperties) {
        this.assetProperties = assetProperties;
    }


}
