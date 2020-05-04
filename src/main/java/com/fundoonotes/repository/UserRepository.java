package com.fundoonotes.repository;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import com.fundoonotes.model.UserModel;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserModel, Long> {
	
	@Query(value="Select * from user_model where email = :email",nativeQuery = true)
	UserModel findEmail(String email);
	
	@Query(value = "select * from user_model where email=?", nativeQuery = true)
	UserModel findByEmail(String user_mail);
	
	@Query(value = "select * from user_model where id = :id", nativeQuery = true)
	UserModel findById(long id);

	@Modifying
	@Query(value="Insert into user_model(fname,lname,email,password,is_verified, created_at,modified_time) values (:fname,:lname,:email,:password,:isVerified,:createdAt,:modifiedTime)",nativeQuery = true)
	void insertdata(String fname, String lname, String email, String password ,boolean isVerified, Date createdAt, Date modifiedTime);

	@Modifying
	@Query(value="update user_model set is_verified = true where id = :id", nativeQuery = true)
	void verify(long id);
}