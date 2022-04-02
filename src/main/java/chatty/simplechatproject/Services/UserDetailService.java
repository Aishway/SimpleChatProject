package chatty.simplechatproject.Services;

import chatty.simplechatproject.Models.User;
import chatty.simplechatproject.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepo;

    @Autowired
    public UserDetailService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUserName(username);
        user.setOnline(true);
        userRepo.save(user);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user"));
        return new org.springframework.security.core.userdetails.User
                (user.getUserName(), user.getPassword(), authorities) {
        };
    }
}