package ds.hua.military.exemptions.services;

import ds.hua.military.exemptions.dtos.PostponementApplicationDto;
import ds.hua.military.exemptions.models.PostponementApplication;
import ds.hua.military.exemptions.models.enums.Status;


import java.util.List;
import java.util.UUID;

public interface PostponementApplicationService {
    PostponementApplication save(PostponementApplicationDto.Add application);

    void delete(UUID id);

    void update(PostponementApplicationDto.Update application, UUID id);

    PostponementApplication findById(UUID id);

    List<PostponementApplication> findAll();


    void changeStatus(Status status, UUID id);

    List<PostponementApplication> findAllByStatus(Status status);

    List<PostponementApplication> findCitizenApplications(String username);

    void saveFile(String fileName,UUID id);
}
