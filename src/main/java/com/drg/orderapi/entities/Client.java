package com.drg.orderapi.entities;

import com.drg.orderapi.dto.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_client")
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String streetName;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String state;

	@Column(nullable = false)
	private String homeNumber;

	@Column(nullable = false)
	private String zipCode;

	public Client(ClientDTO clientDTO) {
		this.id = clientDTO.getId();
		this.name = clientDTO.getName();
		this.email = clientDTO.getEmail();
		this.phoneNumber = clientDTO.getPhoneNumber();
		this.streetName = clientDTO.getStreetName();
		this.city = clientDTO.getCity();
		this.state = clientDTO.getState();
		this.homeNumber = clientDTO.getHomeNumber();
		this.zipCode = clientDTO.getZipCode();
	}
}
