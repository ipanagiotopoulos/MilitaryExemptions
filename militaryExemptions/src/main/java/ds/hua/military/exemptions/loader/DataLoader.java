package ds.hua.military.exemptions.loader;

import ds.hua.military.exemptions.models.User;
import ds.hua.military.exemptions.models.enums.Role;
import ds.hua.military.exemptions.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public DataLoader(UserRepository userRepository , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void run(ApplicationArguments args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setRole(Role.ROLE_ADMIN);
            admin.setFirstName("Dimitrios");
            admin.setLastName("Papadimitriou");
            admin.setPassword(passwordEncoder.encode("admin_password"));
            admin.setUsername("admin");
            userRepository.save(admin);
        }else{

            System.out.println(userRepository.findAll());
        }
    }
}
