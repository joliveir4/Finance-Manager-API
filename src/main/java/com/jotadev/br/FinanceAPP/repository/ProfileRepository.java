package com.jotadev.br.FinanceAPP.repository;

import com.jotadev.br.FinanceAPP.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByEmail(String email);

    Optional<ProfileEntity> findByActivationToken(String activationToken);
}
