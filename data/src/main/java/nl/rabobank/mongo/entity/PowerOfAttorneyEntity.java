package nl.rabobank.mongo.entity;

import lombok.Data;
import nl.rabobank.authorizations.PowerOfAttorney;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class PowerOfAttorneyEntity {
    @Id
    private String id;
    private PowerOfAttorney powerOfAttorney;
}
