package com.bmd_app.bmd_app.Entity;


import javax.persistence.*;

@Entity
@Table(name = "T_CLIENT")
public class Client {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DAILYMESSAGEQUOTA")
	private Long dailyMessageQuota;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "EMAIL")
	private String email;


	public Client(String name, Long dailyMessageQuota) {
		this.name = name;
		this.dailyMessageQuota = dailyMessageQuota;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Client() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDailyMessageQuota() {
		return dailyMessageQuota;
	}

	public void setDailyMessageQuota(Long dailyMessageQuota) {
		this.dailyMessageQuota = dailyMessageQuota;
	}

}
