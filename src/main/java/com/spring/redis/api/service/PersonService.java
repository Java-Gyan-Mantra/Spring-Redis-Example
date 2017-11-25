package com.spring.redis.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.redis.api.dao.PersonRepository;
import com.spring.redis.api.model.Person;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;

	public String addPerson(Person person) {
		repository.addPerson(person);
		return "" + person.getId();
	}

	public Person getPersonById(int id) {
		return repository.getPerson(id);
	}

	public List<Person> getPersons() {
		return repository.getAllPersons().values().stream().collect(Collectors.toList());
	}

	public String updatePerson(Person person) {
		repository.updatePerson(person);
		return "user with id : " + person.getId() + " updated successfully";
	}

	public long totalCount() {
		return repository.getNumberOfPersons();
	}

	public String deletePerson(int id) {
		repository.deletePerson(id);
		return "person deleted with id : " + id;
	}
}
