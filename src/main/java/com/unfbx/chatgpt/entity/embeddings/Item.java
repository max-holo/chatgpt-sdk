package com.unfbx.chatgpt.entity.embeddings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item implements Serializable {
    private String object;
    private List<Float> embedding;
    private Integer index;

    public Item() {
    }

    public String getObject() {
        return this.object;
    }

    public List<Float> getEmbedding() {
        return this.embedding;
    }

    public Integer getIndex() {
        return this.index;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
