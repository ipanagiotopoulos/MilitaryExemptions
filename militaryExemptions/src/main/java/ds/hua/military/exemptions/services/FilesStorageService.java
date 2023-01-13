package ds.hua.military.exemptions.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FilesStorageService {
        public void init();

        public void save(MultipartFile file);



        public void delete(String fileName) throws IOException;

        public Resource load(String file);
}
