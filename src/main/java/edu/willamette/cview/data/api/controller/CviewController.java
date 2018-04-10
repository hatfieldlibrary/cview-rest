package edu.willamette.cview.data.api.controller;

import edu.willamette.cview.data.api.repository.ContentdmRepository;
import edu.willamette.cview.data.api.repository.ContentdmResponse;
import model.NormalizedResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class CviewController {

	@RequestMapping(value="/cview", method=GET)

	@ResponseBody
	public HttpEntity<ContentdmResponse> search(
			@RequestParam(value = "name") String searchTerm,
			@RequestParam(value = "offset") String offset) {


		ContentdmRepository contentdmRepository = new ContentdmRepository(searchTerm, offset);
		NormalizedResult result = contentdmRepository.execQuery();
		ContentdmResponse response = new ContentdmResponse(result);
		response.add(linkTo(methodOn(CviewController.class).search(searchTerm, offset)).withSelfRel());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}