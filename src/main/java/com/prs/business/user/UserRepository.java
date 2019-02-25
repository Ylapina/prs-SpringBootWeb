package com.prs.business.user;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	Optional<User> findByuserName(String userName);

	Optional<User> findByUserNameAndPassword(String userName, String password);
}
