package com.saikumar.SpringSecurity.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class AuthResponse{
   private final String jwt;
}
