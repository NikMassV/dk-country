package edu.mikita.dk_country.repository;

import edu.mikita.dk_country.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryJpaRepository extends JpaRepository<CountryEntity, String> {
    CountryEntity findByAlpha2(String alpha2);
}
