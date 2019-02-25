package com.prs.business.purchaseRequestLineItem;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.prs.business.purchaseRequest.PurchaseRequest;

public interface PurchaseRequestLineItemRepository
		extends PagingAndSortingRepository<PurchaseRequestLineItem, Integer> {
	List<PurchaseRequestLineItem> findByPurchaseRequest(PurchaseRequest pr);
}
