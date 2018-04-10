package edu.willamette.cview.data.api.controller;

import edu.willamette.cview.data.api.repository.ContentdmRepository;
import edu.willamette.cview.data.api.repository.ContentdmResponse;
import model.NormalizedResult;
import org.springframework.hateoas.Link;
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
		response.add(
				linkTo(methodOn(CviewController.class).search(searchTerm, offset))
				.withSelfRel());
		boolean hasNext = contentdmRepository.hasNext(result.getPager(), offset);
		boolean hasPrev = contentdmRepository.hasPrev(offset);
		if (hasNext) {
			String nextOffset = contentdmRepository.getOffset("next", offset, result.getPager());
			Link ordersLink = linkTo(methodOn(CviewController.class).search(searchTerm, nextOffset)).withRel("next");
			response.add(ordersLink);
		}
		if (hasPrev) {
			String prevOffset = contentdmRepository.getOffset("prev", offset, result.getPager());
			Link ordersLink = linkTo(methodOn(CviewController.class).search(searchTerm, prevOffset)).withRel("prev");
			response.add(ordersLink);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}