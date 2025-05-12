package com.example.location_voiture.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class ReservationRequest {

    private Long annonceId;
    private Date dateStart;
    private Date dateEnd;
}
