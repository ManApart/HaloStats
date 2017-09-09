package rak.halo.stats.haloStats.model.serviceRecord;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRecordResult {
	
	@JsonProperty("CustomStats")
	private CustomStats customStats;
	
    @Override
    public String toString(){
    	return customStats.toString();
    }

}