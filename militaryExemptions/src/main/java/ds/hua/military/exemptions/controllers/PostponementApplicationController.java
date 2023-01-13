package ds.hua.military.exemptions.controllers;

import ds.hua.military.exemptions.dtos.PostponementApplicationDto;
import ds.hua.military.exemptions.dtos.ResponseMessage;
import ds.hua.military.exemptions.models.PostponementApplication;
import ds.hua.military.exemptions.models.enums.Status;
import ds.hua.military.exemptions.services.FilesStorageService;
import ds.hua.military.exemptions.services.PostponementApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/applications")
public class PostponementApplicationController {


        @Autowired
        PostponementApplicationService applicationService;

        @Autowired
        FilesStorageService storageService;
        @PostMapping
        @ResponseBody
        @PreAuthorize("hasRole('ROLE_CITIZEN') or hasRole('ROLE_ADMIN')")
        public PostponementApplication createApplication(@RequestBody @Valid PostponementApplicationDto.Add applicationDto) {
            return applicationService.save(applicationDto);
        }

        @PreAuthorize("hasRole('ROLE_CITIZEN') or hasRole('ROLE_ADMIN')")
        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public String updateApplication(@RequestBody @Valid PostponementApplicationDto.Update applicationDto, @PathVariable("id") UUID id) {
            applicationService.update(applicationDto, id);
            return "application with id : " + id + " updated !";
        }

        @GetMapping("/{id}")
        @ResponseBody
        public PostponementApplication getApplication(@PathVariable("id") UUID id) {
            return applicationService.findById(id);
        }

        @PreAuthorize("hasRole('ROLE_CITIZEN') or hasRole('ROLE_ADMIN')")
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public String deleteApplication(@PathVariable("id") UUID id) {
                applicationService.delete(id);
                return "application with id : " + id + " deleted !";
        }

        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping
        @ResponseBody
        public Collection<PostponementApplication> getApplications() {
            return applicationService.findAll();
        }

        @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
        @GetMapping("/ByStatus")
        @ResponseBody
        public Collection<PostponementApplication> getApplicationsByStatus(@RequestParam(name="status") Status status) {
            return applicationService.findAllByStatus(status);
        }

        @PreAuthorize("hasRole('ROLE_CITIZEN') or hasRole('ROLE_ADMIN')")
        @GetMapping("/ByCitizen")
        @ResponseBody
        public Collection<PostponementApplication> getApplicationsByCitizen(@RequestParam(name="citizenUsername") String username) {
            return applicationService.findCitizenApplications(username);
        }

        @PreAuthorize("hasRole('ROLE_OFFICER') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
        @PatchMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public String changeApplicationStatus(@RequestBody @Valid PostponementApplicationDto.ChangeStatus applicationDto, @PathVariable("id") UUID id) {
                applicationService.changeStatus(applicationDto.getStatus(),id);
                return "application with id : " + id + " updated !";
        }

        @PostMapping("/{id}/file")
        public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file,@PathVariable("id") UUID id) {
                String message = "";
                try {
                        PostponementApplication pa = applicationService.findById(id);
                        if (pa.getFile()!=null){
                                storageService.delete(pa.getFile());
                        }
                        storageService.save(file);
                        applicationService.saveFile(file.getOriginalFilename(),id);
                        message = "Uploaded the file successfully: " + file.getOriginalFilename();
                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
                } catch (Exception e) {
                        message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
                        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
                }
        }

        @GetMapping("/{id}/file")
        @ResponseBody
        public ResponseEntity<Resource> getFile(@PathVariable UUID id)  {
                PostponementApplication pa = applicationService.findById(id);
                Resource file = storageService.load(pa.getFile());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }
}
