package com.bridgelabz.fundoonotes.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.LabelModel;

@Repository
@Transactional
public interface LabelRepository extends JpaRepository<LabelModel, Long> {

	@Query(value = "select * from label_model where label_title = :labelname", nativeQuery = true)
	LabelModel findByName(String labelname);

	@Modifying
	@Query(value = "insert into label_model (label_title, user_id) values (:labelTitle, :userId)", nativeQuery = true)
	int insertLabelData(String labelTitle, long userId);

	@Query(value = "select * from label_model  where label_title = :labelTitle", nativeQuery = true)
	LabelModel findByTitle(String labelTitle);

	@Query(value = "select * from label_model where label_id = :labelId and user_id =:userId", nativeQuery = true)
	LabelModel findById(long labelId, long userId);

	@Modifying
	@Query(value = "update label_model set label_title = :labelTitle where label_id = :labelId", nativeQuery = true)
	void update(String labelTitle, long labelId);

	@Modifying
	@Query(value = "delete from label_model where user_id = :userId and label_id = :labelId", nativeQuery = true)
	void delete(long userId, long labelId);
	
	@Query(value = "select * from label_model where user_id = :userId", nativeQuery = true)
	List<LabelModel> getall(long userId);
	
	@Modifying
	@Query(value = "insert into note_model_labels(listnote_note_id ,  labels_label_id)  values(:noteId, :labelId)", nativeQuery = true)
	void insertdatatomap(long noteId, long labelId);

	@Query(value = "select * from  note_model_labels where listnote_note_id = :noteId and labels_label_id =:labelId", nativeQuery = true)
	Object findoneByLabelIdAndNoteId(long labelId, long noteId);
}
