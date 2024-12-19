package com.srishasti.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity

public class MetaData {

    @Id
    private String metakey;
    private String value;

    public String getMetakey() {
        return metakey;
    }

    public void setMetakey(String metakey) {
        this.metakey = metakey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
