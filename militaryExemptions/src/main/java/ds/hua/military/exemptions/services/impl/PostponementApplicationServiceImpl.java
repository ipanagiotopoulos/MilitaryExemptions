package ds.hua.military.exemptions.services.impl;

import ds.hua.military.exemptions.dtos.PostponementApplicationDto;
import ds.hua.military.exemptions.exceptions.EntityNotFoundException;
import ds.hua.military.exemptions.models.PostponementApplication;
import ds.hua.military.exemptions.models.User;
import ds.hua.military.exemptions.models.enums.Status;
import ds.hua.military.exemptions.repositories.PostponementApplicationRepository;
import ds.hua.military.exemptions.repositories.UserRepository;
import ds.hua.military.exemptions.services.FilesStorageService;
import ds.hua.military.exemptions.services.PostponementApplicationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class PostponementApplicationServiceImpl implements PostponementApplicationService {
    @Autowired
    PostponementApplicationRepository applicationRepository;
    @Autowired
    FilesStorageService storageService;
    @Autowired
    UserRepository userRepository;
    @Override
    public PostponementApplication save(PostponementApplicationDto.Add application) {
        PostponementApplication new_app = new PostponementApplication();
        new_app.setReason(application.getReason());
        new_app.setMilNumber(application.getMilNumber());
        User citizen = userRepository.findByUsername(application.getCitizenUsername()).orElseThrow(() -> new EntityNotFoundException("User not found with username : "+application.getCitizenUsername()));
        new_app.setCitizen(citizen);
        PostponementApplication saved = applicationRepository.save(new_app);
        return saved;
    }

    @Override
    public void delete(UUID id) {
        PostponementApplication pa = applicationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Postponement Application not found with id : "+id));
        if (pa.getFile()!=null){
            try {
                storageService.delete(pa.getFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        applicationRepository.deleteById(id);
    }

    @Override
    public void update(PostponementApplicationDto.Update application, UUID id) {
        if (!applicationRepository.existsById(id)){
            throw new EntityNotFoundException("Postponement Application not found with id : "+id);
        }
        applicationRepository.updateReasonAndMilNumberById(application.getReason(),application.getMilNumber(),id);
    }

    @Override
    public PostponementApplication findById(UUID id) {
        return applicationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Postponement Application not found with id : "+id));
    }

    @Override
    public List<PostponementApplication> findAll() {

        System.out.println(applicationRepository.findAllOrderByCreatedAtAsc());
        return applicationRepository.findAllOrderByCreatedAtAsc();
    }

    @Override
    public void changeStatus(Status status, UUID id) {
        if (!applicationRepository.existsById(id)){
            throw new EntityNotFoundException("Postponement Application not found with id : "+id);
        }
        applicationRepository.updateStatusById(status,id);
    }

    @Override
    public List<PostponementApplication> findAllByStatus(Status status) {
        return applicationRepository.findByStatusOrderByCreatedAtAsc(status);
    }

    @Override
    public List<PostponementApplication> findCitizenApplications(String username) {
        return applicationRepository.findByCitizen_UsernameOrderByCreatedAtAsc(username);
    }

    @Override
    public void saveFile(String fileName,UUID id) {
        if (!applicationRepository.existsById(id)){
            throw new EntityNotFoundException("Postponement Application not found with id : "+id);
        }
        applicationRepository.updateFileById(fileName,id);
    }
}
