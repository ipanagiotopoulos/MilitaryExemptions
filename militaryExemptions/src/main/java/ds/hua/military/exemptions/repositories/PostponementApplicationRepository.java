package ds.hua.military.exemptions.repositories;

import ds.hua.military.exemptions.dtos.PostponementApplicationDto;
import ds.hua.military.exemptions.models.PostponementApplication;
import ds.hua.military.exemptions.models.enums.Reason;
import ds.hua.military.exemptions.models.enums.Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostponementApplicationRepository extends Repository<PostponementApplication, UUID> {
    @Transactional
    @Modifying
    @Query("update PostponementApplication p set p.file = ?1 where p.id = ?2")
    int updateFileById(String file, UUID id);
    List<PostponementApplication> findByCitizen_UsernameOrderByCreatedAtAsc(String username);
    List<PostponementApplication> findByStatusOrderByCreatedAtAsc(Status status);
    @Transactional
    @Modifying
    @Query("update PostponementApplication p set p.status = ?1 where p.id = ?2")
    void  updateStatusById(Status status, UUID id);
    @Query("select p from PostponementApplication p order by p.createdAt")
    List<PostponementApplication> findAllOrderByCreatedAtAsc();
    @Transactional
    @Modifying
    @Query("update PostponementApplication p set p.reason = ?1, p.milNumber = ?2 where p.id = ?3")
    void updateReasonAndMilNumberById(Reason reason, Long milNumber, UUID id);

    boolean existsById(UUID id);
    Optional<PostponementApplication> findById(UUID id);
    void deleteById(UUID id);
    PostponementApplication save(PostponementApplication applicationDto);


}