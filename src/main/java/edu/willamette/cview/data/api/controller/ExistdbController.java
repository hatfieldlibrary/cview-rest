package edu.willamette.cview.data.api.controller;

import edu.willamette.cview.data.api.repository.ExistDbResponse;
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

    private static final String TEMPLATE = "Hello from exist-db, %s!";

    @RequestMapping(value="/exist", method=GET)

    @ResponseBody
    public HttpEntity<ExistDbResponse> search(
            @RequestParam(value = "name", required = false, defaultValue = "Bob") String name) {

        ExistDbResponse response = new ExistDbResponse(String.format(TEMPLATE, name));
        response.add(linkTo(methodOn(ExistdbController.class).search(name)).withSelfRel());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
