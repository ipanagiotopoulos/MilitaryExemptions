package ds.hua.military.exemptions.controllers.backend;


import ds.hua.military.exemptions.models.User;
import ds.hua.military.exemptions.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;


@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncode;

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    @GetMapping("/new")
    public String showUserCreateForm(User user, Model model) {
        return "user/add";
    }

    @PostMapping("/new")
    public String createUser(Model model, @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (userRepository.existsByUsername(user.getUsername())) {
            bindingResult.addError(new FieldError("user", "username", user.getUsername(), false, null, null, "User with this username already exists !"));
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.addError(new FieldError("user", "confirmPassword", user.getConfirmPassword(), false, null, null, "Passwords not match !"));
        }

        if (bindingResult.hasErrors()) {
            return "user/add";
        }
        user.setPassword(passwordEncode.encode(user.getPassword()));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "User saved !");
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String showEditUserForm(Model model, @PathVariable("id") UUID id) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") UUID id, @Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if ((!userRepository.findById(id).get().getUsername().equals(user.getUsername())) && (userRepository.existsByUsername(user.getUsername()))) {
            bindingResult.addError(new FieldError("user", "username", user.getUsername(), false, null, null, "User with this username already exists !"));
        }
        if (bindingResult.hasErrors()) {
            user.setId(id);
            return "user/edit";
        }
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "User updated !");
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(RedirectAttributes redirectAttributes, @PathVariable("id") UUID id) {
        userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "User deleted !");
        return "redirect:/admin/users";
    }
}
