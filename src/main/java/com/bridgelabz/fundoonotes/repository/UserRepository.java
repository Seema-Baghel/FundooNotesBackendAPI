package com.bridgelabz.fundoonotes.repository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.UserModel;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserModel, Long> {
	
	@Query(value="Select * from user_model where email = :email",nativeQuery = true)
	UserModel findEmail(String email);
	
	@Query(value = "select * from user_model where email=?", nativeQuery = true)
	UserModel findByEmail(String user_mail);
	
	@Query(value = "select * from user_model where user_id = :userId", nativeQuery = true)
	UserModel findById(long userId);

	@Modifying
	@Query(value="Insert into user_model(first_name, last_name, email, mobile, password,is_verified, created_at,modified_time) values (:firstName,:lastName,:email, :mobile, :password,:isVerified,:createdAt,:modifiedTime)",nativeQuery = true)
	void insertdata(String firstName, String lastName, String email,long mobile, String password ,boolean isVerified, LocalDateTime createdAt, LocalDateTime modifiedTime);

	@Modifying
	@Query(value="update user_model set is_verified = true where user_id = :userId", nativeQuery = true)
	void verify(long userId);

	@Modifying
	@Query(value="update user_model set modified_time = now() where user_id = :userId", nativeQuery = true)
	void modifiedTime(long userId);
}