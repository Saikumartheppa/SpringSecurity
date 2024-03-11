package com.saikumar.SpringSecurity.Controller;

import com.saikumar.SpringSecurity.Models.AuthRequest;
import com.saikumar.SpringSecurity.Models.AuthResponse;
import com.saikumar.SpringSecurity.Models.UserInfo;
import com.saikumar.SpringSecurity.Service.JWTService;
import com.saikumar.SpringSecurity.Service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class userController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTService jwtService;
    @RequestMapping({"welcome"})
    public String homePage(){
        return "Welcome user. This api is Accessible by every User";
    }

    @RequestMapping(value = "/addNewUser", method = RequestMethod.POST)
    public ResponseEntity<?> createNewUser(@RequestBody UserInfo userInfo){
        return userInfoService.createNewUser(userInfo);
    }
    @RequestMapping(value = "/generateToken", method = RequestMethod.POST)
    public ResponseEntity<?> CreateToken(@RequestBody AuthRequest authRequest) {
        Authentication  authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getUserPassword()));
        if(authentication.isAuthenticated()){
            final String jwt = jwtService.generateToken(authRequest.getUserName());
            return ResponseEntity.ok(new AuthResponse(jwt));
        }else{
            throw new UsernameNotFoundException("Incorrect UserName or Password");
        }
    }
    @GetMapping("/user/getUserDetails")
    @PreAuthorize("hasAuthority('USER')")
    public String getUserInfo(){
       return "Welcome to user profile";
    }
    @GetMapping("/admin/getAdminDetails")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAdminInfo(){
        return "Welcome to admin profile";
    }
    @GetMapping("/user/{userName}")
    @PreAuthorize("hasAuthority('USER')")
    public String getUserPassword(@PathVariable String userName){
        return userInfoService.getUserPassword(userName);
    }

}
