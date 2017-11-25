package com.spring.redis.api.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.redis.api.model.Person;

@Repository
@Transactional
public class PersonRepository {
	private static final String KEY = "persons";
	@Autowired
	private RedisTemplate<String, Person> template;

	@Resource(name = "redisTemplate")
	private HashOperations<String, Integer, Person> hashOps;

	public void addPerson(Person person) {
		hashOps.putIfAbsent(KEY, person.getId(), person);
	}

	public void updatePerson(Person person) {
		hashOps.put(KEY, person.getId(), person);
	}

	public Person getPerson(Integer id) {
		return hashOps.get(KEY, id);
	}

	public long getNumberOfPersons() {
		return hashOps.size(KEY);
	}

	public Map<Integer, Person> getAllPersons() {
		return hashOps.entries(KEY);
	}

	public void deletePerson(int id) {
		hashOps.delete(KEY, id);
	}
}
