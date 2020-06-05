package com.bridgelabz.fundoonotes.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.ProfilePicModel;

@Repository
@Transactional
public interface ProfilePicRepository extends JpaRepository<ProfilePicModel,Long> {
	
	@Query(value = "select * from profile_pic_model where user_id=?",nativeQuery = true)
	ProfilePicModel findByUserId(long user_id);
	
}

