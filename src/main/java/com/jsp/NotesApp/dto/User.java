package com.jsp.NotesApp.dto;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "\"user\"") // quoted for reserved keyword
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private long phone;

	@Column(nullable = false)
	private String password;

	@OneToMany
	@JoinColumn(name = "user_id")
	private List<Note> notes;
}
