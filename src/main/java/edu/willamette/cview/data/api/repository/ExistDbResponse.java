package edu.willamette.cview.data.api.repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.NormalizedResult;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

public class ExistDbResponse extends ResourceSupport {

    private final NormalizedResult content;

    @JsonCreator
    public ExistDbResponse(@JsonProperty("content") NormalizedResult content)

    {
        this.content = content;
    }

    public NormalizedResult getContent() {
        return content;
    }
}
