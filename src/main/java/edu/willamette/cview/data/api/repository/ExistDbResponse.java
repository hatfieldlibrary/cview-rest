package edu.willamette.cview.data.api.repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.willamette.cview.data.api.model.NormalizedResult;
import org.springframework.hateoas.ResourceSupport;

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
