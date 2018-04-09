package edu.willamette.cview.data.api.controller;

import edu.willamette.cview.data.api.repository.ContentdmResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class CviewController {

	private static final String TEMPLATE = "Hello from cview, %s!";

	@RequestMapping(value="/cview", method=GET)

	@ResponseBody
	public HttpEntity<ContentdmResponse> greeting(
			@RequestParam(value = "name", required = false, defaultValue = "Bob") String name) {

		ContentdmResponse greeting = new ContentdmResponse(String.format(TEMPLATE, name));
		greeting.add(linkTo(methodOn(CviewController.class).greeting(name)).withSelfRel());

		return new ResponseEntity<>(greeting, HttpStatus.OK);
	}

}