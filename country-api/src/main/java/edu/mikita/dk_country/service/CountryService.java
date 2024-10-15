package edu.mikita.dk_country.service;

import edu.mikita.dk_country.client.CountryApiClient;
import edu.mikita.dk_country.domain.CountryDto;
import edu.mikita.dk_country.entity.CountryDocument;
import edu.mikita.dk_country.entity.CountryEntity;
import edu.mikita.dk_country.repository.CountryJpaRepository;
import edu.mikita.dk_country.repository.mongo.CountryMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryApiClient apiClient;
    private final CountryMongoRepository countryMongoRepository;
    private final CountryJpaRepository countryJpaRepository;


    public void processCountries() {
        List<CountryDto> countryDtos = apiClient.getAllCountries();
        List<CountryDocument> countryDocuments = countryDtos.stream()
                .filter(dto -> !dto.getAlpha2().contains("P"))
                .map(CountryDto::toMongoDocument)
                .toList();
        List<CountryEntity> countryEntities = countryDtos.stream()
                .map(CountryDto::toJpaEntity)
                .toList();
        countryJpaRepository.deleteAll();
        countryMongoRepository.deleteAll();
        countryJpaRepository.saveAll(countryEntities);
        countryMongoRepository.saveAll(countryDocuments);
    }

    public List<CountryDto> getAllCountries() {
        List<CountryEntity> countryEntities = countryJpaRepository.findAll();
        if (CollectionUtils.isEmpty(countryEntities)) {
            return Collections.emptyList();
        }
        return countryEntities.stream()
                .map(CountryDto::fromJpaEntity)
                .toList();
    }


    public CountryDto getDetailsByAlpha2Code(String alpha2Code) {
        CountryDocument document = countryMongoRepository.findByAlpha2(alpha2Code);
        if (Objects.nonNull(document)) {
            return CountryDto.fromMongoDocument(document);
        }
        CountryEntity countryEntity = countryJpaRepository.findByAlpha2(alpha2Code);
        return CountryDto.fromJpaEntity(countryEntity);
    }

}
