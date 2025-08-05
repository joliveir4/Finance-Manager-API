package com.jotadev.br.FinanceAPP.service;

import com.jotadev.br.FinanceAPP.dto.ProfileDTO;
import com.jotadev.br.FinanceAPP.entity.ProfileEntity;
import com.jotadev.br.FinanceAPP.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;

    public ProfileDTO registerProfile(ProfileDTO profileDTO){
        ProfileEntity newProfile = toEntity(profileDTO);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepository.save(newProfile);

        // Send activation email
        String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your Finance Manager account";
        String body = "Hello " + newProfile.getFullName() + ",\n\n" +
                      "Thank you for registering with Finance Manager. Please click the link below to activate your account:\n" +
                      activationLink + "\n\n" +
                      "If you did not register, please ignore this email.\n\n" +
                      "Best regards,\n" +
                      "Finance Manager Team";
        emailService.sendMail(newProfile.getEmail(), subject, body);
        return toDTO(newProfile);
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO){
        return ProfileEntity.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(profileDTO.getPassword())
                .profilePictureUrl(profileDTO.getProfilePictureUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .build();
    }

    public ProfileDTO toDTO(ProfileEntity profileEntity){
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profilePictureUrl(profileEntity.getProfilePictureUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }
}
