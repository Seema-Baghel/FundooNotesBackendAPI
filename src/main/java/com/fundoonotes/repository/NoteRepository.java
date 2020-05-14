package com.fundoonotes.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fundoonotes.model.LabelModel;
import com.fundoonotes.model.NoteModel;
import com.fundoonotes.model.UserModel;

@Repository
@Transactional
public interface NoteRepository extends JpaRepository<NoteModel, Long>{

	@Query(value = "select * from note_model where id = :id", nativeQuery = true)
	Optional<NoteModel> findBynoteId(long id);
	
	@Query(value = "select * from note_model where id = :id", nativeQuery = true)
	NoteModel findById(long id);

	@Query(value = "select * from note_model where id=?", nativeQuery = true)
	NoteModel findByNoteId(long note_id);

	@Query(value = "select * from note_model where user_id = :userid", nativeQuery = true)
	NoteModel findByuserid(long userid);

	@Modifying
	@Query(value = "insert into note_model (description, created_date, title, note_color, updated_date, user_id) values ( :description, :createdDate, :title, :noteColor, :updatedDate, :id)" , nativeQuery = true)
	public void insertData(String description, String createdDate, String title, String updatedDate, String noteColor, long id);

	@Modifying
	@Query(value = "update note_model set description = :description , title = :title , updated_date = :updatedDate where user_id = :id AND id = :id2", nativeQuery = true)
	void updateData(String description, String title, String updatedDate, long id, long id2);

	@Modifying
	@Query(value = "update note_model set note_color = :color where user_id = :userid and id = :id", nativeQuery = true)
	void updateColor(long userid, long id, String color);
	
	@Modifying
	@Query(value = "delete from note_model  where user_id = :userid AND id = :id", nativeQuery = true)
	void deleteNote(long userid, long id);
	
	@Query(value = "select * from note_model where user_id = :userId", nativeQuery = true)
	List<NoteModel> getAll(long userId);
	
	@Query(value = "select * from note_model where title = :title", nativeQuery = true)
	List<NoteModel> searchBy(String title);
	
	@Query(value = "select * from note_model where description = :description", nativeQuery = true)
	List<NoteModel> searchByDescription(String description);

	@Query(value = "select * from note_model where user_id=:userId and id = :id", nativeQuery = true)
	List<NoteModel> searchAllNotesByNoteId(long userId, long id);
	
	@Modifying
	@Query(value = "update note_model set is_pinned = :b where user_id = :userid AND id = :id", nativeQuery = true)
	void setPinned(boolean b, long userid, long id);
	
	@Query(value = "select * from note_model where user_id = :userId and is_pinned = true and is_trashed = false and is_archived = false", nativeQuery = true)
	List<NoteModel> getallpinned(long userId);

	@Query(value = "select * from note_model where user_id = :userId and is_pinned = false and is_trashed = false and is_archived = false", nativeQuery = true)
	List<NoteModel> getallunpinned(long userId);
	
	@Modifying
	@Query(value = "update note_model set is_archived = :b where user_id = :userid AND id = :id", nativeQuery = true)
	void setArchive(boolean b, long userid, long id);

	@Query(value = "select * from note_model where user_id = :userId and is_archived = true and is_trashed = false ", nativeQuery = true)
	List<NoteModel> getallarchived(long userId);

	@Query(value = "select * from note_model where user_id = :userId and is_archived = false", nativeQuery = true)
	List<NoteModel> getallunarchived(long userId);
	
	@Modifying
	@Query(value = "update note_model set is_trashed = :b where user_id = :userid AND id = :id" ,  nativeQuery = true)
	void setTrashed(boolean b,long userid,long id);
	
	@Query(value = "select * from note_model where user_id = :userId and is_trashed = true", nativeQuery = true)
	List<NoteModel> getalltrashed(long userId);
}
