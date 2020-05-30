package com.bridgelabz.fundoonotes.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.CollaboratorModel;

@Repository
@Transactional
public interface CollaboratorRepository extends JpaRepository<CollaboratorModel, Object> {

	@Query(value = "select * from collaborator_model where collaborator_mail=? and note_id=?",nativeQuery=true)
	CollaboratorModel findOneByEmail(String collaborator_mail, long note_id);
	
	@Query(value = "select * from collaborator_model where collaborator_id=:collaborator_id and note_id=:note_id",nativeQuery=true)
	CollaboratorModel findById(long collaborator_id, long note_id);

	@Modifying
	@Query(value = "insert into collaborator_model(collaborator_mail, note_id)values(?,?)",nativeQuery=true)
	void addCollaborator(String collaborator_mail, long note_id);

	@Query(value = "select * from collaborator_model where collaborator_id=?",nativeQuery=true)
	Optional<CollaboratorModel> findById(long collaborator_id);

	@Modifying
	@Query(value = "delete from collaborator_model where collaborator_id=? and note_id=?",nativeQuery=true)
	void deleteCollaborator(long collaborator_id,long note_id);

	@Query(value = "select * from collaborator_model where note_id=?", nativeQuery = true)
	List<CollaboratorModel> getAllNoteCollaborators(long noteId);
	
	@Query(value = "select * from note_model_collaborator where listcollaborator_id= :note_id and collaborator_collaborator_id= :collaborator_id",nativeQuery=true)
	Object findOneByCollabIdAndNoteId(long collaborator_id, long note_id);
	
	@Modifying
	@Query(value = "insert into note_model_collaborator(listcollaborator_id,collaborator_collaborator_id) values(:note_id,:collaborator_id)",nativeQuery=true)
	void insertdatatomap(long note_id, long collaborator_id);
}
