package com.drg.orderapi.dto;

import com.drg.orderapi.entities.Client;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ClientDTO {

	private Long id;
	private String name;
	private String email;
	private String phoneNumber;
	private String streetName;
	private String city;
	private String state;
	private String homeNumber;
	private String zipCode;

	public ClientDTO(@NonNull Client client) {
		this.id = client.getId();
		this.name = client.getName();
		this.email = client.getEmail();
		this.phoneNumber = client.getPhoneNumber();
		this.streetName = client.getStreetName();
		this.city = client.getCity();
		this.state = client.getState();
		this.homeNumber = client.getHomeNumber();
		this.zipCode = client.getZipCode();
	}
}
