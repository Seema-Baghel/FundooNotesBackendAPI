package com.fundoonotes.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fundoonotes.model.CollaboratorModel;

@Repository
@Transactional
public interface CollaboratorRepository extends JpaRepository<CollaboratorModel, Object> {

	@Query(value = "select * from collaborator where email=? and noteid=?",nativeQuery=true)
	CollaboratorModel findOneByEmail(String email, long noteId);

	@Modifying
	@Query(value = "insert into collaborator(email,noteid)values(?,?)",nativeQuery=true)
	void addCollaborator(String email,long noteid);

	@Query(value = "select * from collaborator where id=?",nativeQuery=true)
	Optional<CollaboratorModel> findById(long id);

	@Modifying
	@Query(value = "delete from collaborator where id=? and noteid=?",nativeQuery=true)
	void deleteCollaborator(long collaboratorId,long noteId);

	@Query(value = "select * from collaborator where noteid=?", nativeQuery = true)
	List<CollaboratorModel> getAllNoteCollaborators(long noteId);
}
