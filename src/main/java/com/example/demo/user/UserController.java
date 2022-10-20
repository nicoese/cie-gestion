package com.example.demo.user;

import com.example.demo.enums.UserRole;
import com.example.demo.excepciones.ExcepcionServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<AppUser> getAll() {
        return userService.getAppUsers();
    }

    @GetMapping(path = "{id}")
    public Optional<AppUser> getOne(@PathVariable("id") String id) {
        return userService.getOne(id);
    }

    @PostMapping
    public AppUser addNewUser(AppUser user) throws ExcepcionServicio {

        return userService.addNewUser(user);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }

    @PutMapping("{id}")
    public AppUser updateUser(@PathVariable("id") String id, @RequestBody AppUser user) throws ExcepcionServicio {
        return userService.updateUser(id, user);
    }

    @PostMapping("setRole")
    public AppUser setRole(String dni,
                           String role) throws ExcepcionServicio {
        System.out.println(dni);
        return userService.setRole(dni, role);
    }

    @GetMapping("createuser")
    public String createuser(Model model) {
        model.addAttribute("user", new AppUser());
        model.addAttribute("roles", UserRole.values());
        return "crearUser";
    }

    @GetMapping("showUsers")
    public String showUsers(Model model) {
        model.addAttribute("users", this.getAll());
        return "admin";
    }
}
