package com.prs.web;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.prs.business.purchaseRequest.PurchaseRequest;
import com.prs.business.purchaseRequest.PurchaseRequestRepository;
import com.prs.business.purchaseRequestLineItem.PurchaseRequestLineItemRepository;
@CrossOrigin
@RestController
@RequestMapping(path = "/purchase-requests")

public class PurchaseRequestController {
	private static final String NEW = "New";
	private static final String REVIEW = "Review";
	private static final String APPROVED = "Approved";
	private static final String REJECTED = "Rejected";

	@Autowired
	PurchaseRequestRepository purchaseRequestRepo;
	@Autowired
	PurchaseRequestLineItemRepository purchaseRequestLineItemRepo;
	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

//get purchase request paginated
	@GetMapping(path = "")
	public @ResponseBody JsonResponse getUser(@RequestParam int start, @RequestParam int limit) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(purchaseRequestRepo.findAll(PageRequest.of(start, limit)));

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;

		try {
			Optional<PurchaseRequest> purchaseRequest = purchaseRequestRepo.findById(id);
			if (purchaseRequest.isPresent()) {
				jr = JsonResponse.getInstance(purchaseRequest);
			} else {
				jr = JsonResponse.getInstance(new Exception("No purchaseRequest found for id= " + id));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}

		return jr;

	}

	@PostMapping("/")
	public JsonResponse addPurchaseRequest(@RequestBody PurchaseRequest pr) {
		JsonResponse jr= savePurchaseRequest(pr);
		return jr;
	}

	@PutMapping("/")
	public JsonResponse updatePurchaseRequest(@RequestBody PurchaseRequest pr) {
		JsonResponse jr= savePurchaseRequest(pr);
		return jr;
	}

	private JsonResponse savePurchaseRequest(PurchaseRequest pr) {
		JsonResponse jr = null;
		try {
			purchaseRequestRepo.save(pr);
			jr = JsonResponse.getInstance(pr);

		} catch (DataIntegrityViolationException dve) {

			jr = JsonResponse.getInstance(dve);

		}
		return jr;
	}

	@DeleteMapping("/{id}")
	public JsonResponse deletePurchaseRequest(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<PurchaseRequest> pr = purchaseRequestRepo.findById(id);
			if (pr.isPresent()) {
				purchaseRequestRepo.deleteById(id);
				jr = JsonResponse.getInstance(pr);

			} else {
				jr = JsonResponse.getInstance("Delete failed. No purchaseRequest for id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@PostMapping("/submit-new")
	public JsonResponse submitNewPurchaseRequest(@RequestBody PurchaseRequest pr) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		pr.setStatus(NEW);
		pr.setSubmittedDate(currentDateTime);
		return savePurchaseRequest(pr);
	}

	@PostMapping("/submit-review")
	public JsonResponse submitReviewPurchaseRequest(@RequestBody PurchaseRequest pr) {
		double TotalPr = pr.getTotal();
		LocalDateTime currentDateTime = LocalDateTime.now();
		if (TotalPr <= 50) {
			pr.setStatus(APPROVED);
			pr.setSubmittedDate(currentDateTime);
		} else {
			pr.setStatus(REVIEW);
			pr.setSubmittedDate(currentDateTime);
		}
		return savePurchaseRequest(pr);
	}

	@PostMapping("/approve")
	public JsonResponse submitApprovePurchaseRequest(@RequestBody PurchaseRequest pr) {
		pr.setStatus(APPROVED);
		return savePurchaseRequest(pr);

	}

	@PostMapping("/reject")
	public JsonResponse submitRejectPurchaseRequest(@RequestBody PurchaseRequest pr) {
		pr.setStatus(REJECTED);
		return savePurchaseRequest(pr);

	}
	
}
