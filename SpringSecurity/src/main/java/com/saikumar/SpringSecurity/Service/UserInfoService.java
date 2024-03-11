package com.saikumar.SpringSecurity.Service;

import com.saikumar.SpringSecurity.Models.UserInfo;
import com.saikumar.SpringSecurity.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {
    @Autowired
    UserInfoRepository userInfoRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = userInfoRepository.findByUserName(username);
        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public ResponseEntity<?> createNewUser(UserInfo userInfo) {
       userInfo.setUserPassword(new BCryptPasswordEncoder().encode(userInfo.getUserPassword()));
       userInfoRepository.save(userInfo);
       return  ResponseEntity.ok("User signed in Successfully");
    }

    public String getUserPassword(String userName) {
        UserDetails userDetails = loadUserByUsername(userName);
        return userDetails.getPassword();
    }
}
