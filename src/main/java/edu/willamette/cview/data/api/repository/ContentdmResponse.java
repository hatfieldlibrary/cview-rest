package edu.willamette.cview.data.api.repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

public class ContentdmResponse extends ResourceSupport {


    private final String content;

    @JsonCreator
    public ContentdmResponse(@JsonProperty("content") String content)

    {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
