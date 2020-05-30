package com.bridgelabz.fundoonotes.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.NoteModel;

@Repository
@Transactional
public interface NoteRepository extends JpaRepository<NoteModel, Long>{

	@Query(value = "select * from note_model where note_id = :noteId", nativeQuery = true)
	Optional<NoteModel> findBynoteId(long noteId);
	
	@Query(value = "select * from note_model where note_id = :noteId", nativeQuery = true)
	NoteModel findById(long noteId);

	@Query(value = "select * from note_model where note_id=?", nativeQuery = true)
	NoteModel findByNoteId(long noteId);

	@Query(value = "select * from note_model where user_id = :userId", nativeQuery = true)
	NoteModel findByuserid(long userId);

	@Modifying
	@Query(value = "insert into note_model (description, created_date, title, note_color, updated_date, user_id) values ( :description, :createdDate, :title, :noteColor, :updatedDate, :userId)" , nativeQuery = true)
	public void insertData(String description, LocalDateTime createdDate, String title, LocalDateTime updatedDate, String noteColor, long userId);

	@Modifying
	@Query(value = "update note_model set description = :description , title = :title , updated_date = :updatedDate where user_id = :userId AND note_id = :noteId", nativeQuery = true)
	void updateData(String description, String title, LocalDateTime updatedDate, long userId, long noteId);

	@Modifying
	@Query(value = "update note_model set note_color = :color where user_id = :userId and note_id = :noteId", nativeQuery = true)
	void updateColor(long userId, long noteId, String color);
	
	@Modifying
	@Query(value = "delete from note_model  where user_id = :userId AND note_id = :noteId", nativeQuery = true)
	void deleteNote(long userId, long noteId);
	
	@Query(value = "select * from note_model where user_id = :userId", nativeQuery = true)
	List<NoteModel> getAll(long userId);
	
	@Query(value = "select * from note_model where title = :title", nativeQuery = true)
	List<NoteModel> searchBy(String title);
	
	@Query(value = "select * from note_model where description = :description", nativeQuery = true)
	List<NoteModel> searchByDescription(String description);

	@Query(value = "select * from note_model order by title asc", nativeQuery = true)
	List<NoteModel> findByOrderByTitleAsc();
	
	@Query(value = "select * from note_model order by description asc", nativeQuery = true)
	List<NoteModel> findByOrderByDescriptionAsc();
	
	@Query(value = "select * from note_model where user_id=:userId and note_id = :noteId", nativeQuery = true)
	List<NoteModel> searchAllNotesByNoteId(long userId, long noteId);
	
	@Modifying
	@Query(value = "update note_model set is_pinned = :b where user_id = :userId AND note_id = :noteId", nativeQuery = true)
	void setPinned(boolean b, long userId, long noteId);
	
	@Query(value = "select * from note_model where user_id = :userId and is_pinned = true and is_trashed = false and is_archived = false", nativeQuery = true)
	List<NoteModel> getallpinned(long userId);

	@Query(value = "select * from note_model where user_id = :userId and is_pinned = false and is_trashed = false and is_archived = false", nativeQuery = true)
	List<NoteModel> getallunpinned(long userId);
	
	@Modifying
	@Query(value = "update note_model set is_archived = :b where user_id = :userId AND note_id = :noteId", nativeQuery = true)
	void setArchive(boolean b, long userId, long noteId);

	@Query(value = "select * from note_model where user_id = :userId and is_archived = true and is_trashed = false ", nativeQuery = true)
	List<NoteModel> getallarchived(long userId);

	@Query(value = "select * from note_model where user_id = :userId and is_archived = false", nativeQuery = true)
	List<NoteModel> getallunarchived(long userId);
	
	@Modifying
	@Query(value = "update note_model set is_trashed = :b where user_id = :userId AND note_id = :noteId" ,  nativeQuery = true)
	void setTrashed(boolean b,long userId,long noteId);
	
	@Query(value = "select * from note_model where user_id = :userId and is_trashed = true", nativeQuery = true)
	List<NoteModel> getalltrashed(long userId);
}
