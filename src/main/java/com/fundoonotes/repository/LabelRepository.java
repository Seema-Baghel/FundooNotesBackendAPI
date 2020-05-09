package com.fundoonotes.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fundoonotes.model.LabelModel;

@Repository
@Transactional
public interface LabelRepository extends JpaRepository<LabelModel, Long> {

	@Query(value = "select * from label where label_title = :labelname", nativeQuery = true)
	LabelModel findByName(String labelname);

	@Modifying
	@Query(value = "insert into label (label_title, user_id) values (:labelTitle, :userId)", nativeQuery = true)
	int insertLabelData(String labelTitle, long userId);

	@Query(value = "select * from label  where label_title = :labelTitle", nativeQuery = true)
	LabelModel findByTitle(String labelTitle);

	@Query(value = "select * from label where label_id = :labelId and user_id =:userId", nativeQuery = true)
	LabelModel findById(long labelId, long userId);

	@Modifying
	@Query(value = "update label set label_title = :labelTitle where label_id = :labelId", nativeQuery = true)
	void update(String labelTitle, long labelId);

	@Modifying
	@Query(value = "delete from label where user_id = :userId and label_id = :labelId", nativeQuery = true)
	void delete(long userId, long labelId);


}
