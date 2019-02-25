package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
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
import com.prs.business.purchaseRequestLineItem.PurchaseRequestLineItem;
import com.prs.business.purchaseRequestLineItem.PurchaseRequestLineItemRepository;

@RestController
@RequestMapping(path = "/purchase-request-line-items")
public class PurchaseRequestLineItemController {
	@Autowired
	PurchaseRequestLineItemRepository prliRepo;
	@Autowired
	PurchaseRequestRepository purchaseRequestRepo;

	@GetMapping("/")
	public JsonResponse getAll() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(prliRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

//get prli paginated
	@GetMapping(path = "")
	public @ResponseBody JsonResponse getPurchaseRequestLineItem(@RequestParam int start, @RequestParam int limit) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(prliRepo.findAll(PageRequest.of(start, limit)));

		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;

		try {
			Optional<PurchaseRequestLineItem> purchaseRequestLineItem = prliRepo.findById(id);
			if (prliRepo.existsById(id)) {
				jr = JsonResponse.getInstance(purchaseRequestLineItem);
			} else {
				jr = JsonResponse.getInstance(new Exception("No Purchase Request line item found for id= " + id));
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}

		return jr;

	}

	@PostMapping("/")
	public JsonResponse addPurchaseRequestLineItem(@RequestBody PurchaseRequestLineItem prli) {
		JsonResponse jr=savePurchaseRequestLineItem(prli);
		recalculateTotal(prli);
		return jr;
	}

	@PutMapping("/")
	public JsonResponse updatePurchaseRequest(@RequestBody PurchaseRequestLineItem prli) {
		JsonResponse jr= savePurchaseRequestLineItem(prli);
		recalculateTotal(prli);
		return jr;
		
	}

	public JsonResponse savePurchaseRequestLineItem(PurchaseRequestLineItem prli) {
		JsonResponse jr = null;
		try {
			prliRepo.save(prli);
			recalculateTotal(prli);
			jr = JsonResponse.getInstance(prli);

		} catch (DataIntegrityViolationException dve) {

			jr = JsonResponse.getInstance(dve);

		}
		return jr;
	}

	@DeleteMapping("/{id}")
	public JsonResponse deletePurchaseRequestLineItem(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			// get the PRLI for the id given
			Optional<PurchaseRequestLineItem> prli = prliRepo.findById(id);
			if (prli.isPresent()) {
				// get the PR from the PRLI
				PurchaseRequest pr = prli.get().getPurchaseRequest();
				prliRepo.deleteById(id);
				recalculateTotal(prli.get());
				jr = JsonResponse.getInstance(prli);

			} else {
				jr = JsonResponse.getInstance("Delete failed. No purchaseRequest Line Item for id: " + id);
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	// pass the PR into the recalculate method
	private void recalculateTotal(PurchaseRequestLineItem prli) {
		PurchaseRequest pr = prli.getPurchaseRequest();
		List<PurchaseRequestLineItem> prlisorted = prliRepo.findByPurchaseRequest(pr);
		double subTotal = 0;
		for (PurchaseRequestLineItem rtl : prlisorted)
			subTotal += prli.getProduct().getPrice() * prli.getQuantity();
		pr.setTotal(subTotal);
		purchaseRequestRepo.save(pr);
	}

}
