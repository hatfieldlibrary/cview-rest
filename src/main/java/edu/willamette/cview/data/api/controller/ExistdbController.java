package edu.willamette.cview.data.api.controller;

import edu.willamette.cview.data.api.repository.ExistDbResponse;
import edu.willamette.cview.data.api.repository.Pagination;
import edu.willamette.cview.data.api.repository.RepositoryInterface;
import edu.willamette.cview.data.api.model.NormalizedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ExistdbController {

    @Autowired
    RepositoryInterface existDbRepository;

    @Autowired
    Pagination pagination;

    @RequestMapping(value = "/exist/search", method = GET)
    @ResponseBody
    public HttpEntity<ExistDbResponse> search(@RequestParam(value = "term") String searchTerm,
                                              @RequestParam(value = "offset") String offset,
                                              @RequestParam(value = "mode", defaultValue = "all") String mode,
                                              @RequestParam(value = "collections", defaultValue = "all") String collections) {

        NormalizedResult result = existDbRepository.execQuery(searchTerm, offset, mode, collections);
        ExistDbResponse response = new ExistDbResponse(result);
        response.add(linkTo(methodOn(ExistdbController.class).search(searchTerm, offset, mode, collections)).withSelfRel());
        boolean hasNext = pagination.hasNext(result.getPager(), offset);
        boolean hasPrev = pagination.hasPrev(offset);
        if (hasNext) {
            String nextOffset = pagination.getOffset("next", offset, result.getPager());
            Link ordersLink = linkTo(methodOn(CdmController.class).search(searchTerm, nextOffset, mode, collections)).withRel("next");
            response.add(ordersLink);
        }
        if (hasPrev) {
            String prevOffset = pagination.getOffset("prev", offset, result.getPager());
            Link ordersLink = linkTo(methodOn(CdmController.class).search(searchTerm, prevOffset, mode, collections)).withRel("prev");
            response.add(ordersLink);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
