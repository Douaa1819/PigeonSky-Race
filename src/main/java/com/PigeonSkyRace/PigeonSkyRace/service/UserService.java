package com.PigeonSkyRace.PigeonSkyRace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.PigeonSkyRace.PigeonSkyRace.dto.UserRegistrationDto;
import com.PigeonSkyRace.PigeonSkyRace.model.Breeder;
import com.PigeonSkyRace.PigeonSkyRace.model.Pigeon;
import com.PigeonSkyRace.PigeonSkyRace.repository.BreederRepository;
import com.PigeonSkyRace.PigeonSkyRace.repository.PigeonsRepository;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BreederRepository breederRepository;

    @Autowired
    private PigeonsRepository pigeonsRepository;

    public Breeder registerBreederWithPigeons(UserRegistrationDto registrationDTO) {
        Breeder breeder = new Breeder();
        breeder.setName(registrationDTO.getName());
        breeder.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        breeder.setEmail(registrationDTO.getEmail());
        breeder.setDoveCote(registrationDTO.getDoveCote());
        breeder.setGpsCoordinates(registrationDTO.getGpsCoordinates());
        if (registrationDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be  daali");
        }
        // Extract and set pigeons' ring numbers
        List<String> pigeonIds = registrationDTO.getPigeons().stream()
                .map(Pigeon::getRingNumber)
                .collect(Collectors.toList());
        breeder.setPigeonIds(pigeonIds);

        List<Pigeon> pigeons = registrationDTO.getPigeons();
        pigeonsRepository.saveAll(pigeons);

        return breederRepository.save(breeder);
    }

    public boolean emailExists(String email) {
        return breederRepository.existsByEmail(email);
    }

}