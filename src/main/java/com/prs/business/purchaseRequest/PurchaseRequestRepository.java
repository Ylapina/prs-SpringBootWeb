package com.prs.business.purchaseRequest;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.prs.business.purchaseRequestLineItem.PurchaseRequestLineItem;

public interface PurchaseRequestRepository extends PagingAndSortingRepository<PurchaseRequest, Integer> {
//	List<PurchaseRequestLineItem> findAllByPurchaseRequestId(int purchaseRequestId);

}
