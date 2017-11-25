package com.spring.redis.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.redis.api.exception.advice.ServiceException;
import com.spring.redis.api.model.Person;
import com.spring.redis.api.service.PersonService;

@Controller
@RequestMapping("/person")
public class PersonController {
	@Autowired
	private PersonService service;

	List<String> myList = null;

	@PostConstruct
	public void initList() {
		myList = new ArrayList<>();
		myList.add("");
	}

	@RequestMapping("/home")
	public String home(Model model, Person person) {
		model.addAttribute("id", new Random().nextInt(7657));
		model.addAttribute("matchesCount", "Total object available in redis server : " + service.totalCount());
		model.addAttribute("SubmitValue", "Register");
		return "register";
	}

	@RequestMapping("/register")
	public String addPerson(@Valid Person person, BindingResult bindingResult, Model model) throws ServiceException {
		boolean isExistUser = service.getPersons().stream()
				.filter(obj -> obj.getEmail().equalsIgnoreCase(person.getEmail())).collect(Collectors.toList())
				.size() >= 1;
		model.addAttribute("matchesCount", "Total object available in redis server : " + service.totalCount());
		if (bindingResult.hasErrors()) {
			model.addAttribute("id", person.getId());
			return "register";
		} else {
			if (service.getPersons().stream().filter(obj -> obj.getId() == person.getId()).collect(Collectors.toList())
					.size() >= 1) {
				model.addAttribute("myList", myList);
				model.addAttribute("Message", service.updatePerson(person));
				return "result";
			}
			if (isExistUser) {
				throw new ServiceException("User Already exist in our portal with email : " + person.getEmail()
						+ " please try with different user");
			}
			model.addAttribute("text", "person added successfully with id");
			model.addAttribute("addMessage", service.addPerson(person));
			model.addAttribute("SubmitValue", "Register");
			return "register";
		}

	}

	@RequestMapping("/get/{id}")
	public String getPerson(@PathVariable("id") int id, Model model) {
		Person person = service.getPersonById(id);
		List<String> dummylist = new ArrayList<>();
		dummylist.add("");
		model.addAttribute("dummylist", dummylist);
		model.addAttribute("person", person);
		return "result";
	}

	@GetMapping("/getAll")
	public String getPersons(Model model) {
		List<String> tablelist = new ArrayList<>();
		tablelist.add("");
		model.addAttribute("tablelist", tablelist);
		List<Person> persons = service.getPersons();
		model.addAttribute("persons", persons);
		model.addAttribute("count", persons.size());
		return "result";

	}

	@RequestMapping("/updatePage/{idNo}")
	public String updatePage(Model model, @PathVariable("idNo") int idNo) {
		Person person = service.getPersonById(idNo);
		int id = person.getId();
		model.addAttribute("id", id);
		model.addAttribute("person", person);
		model.addAttribute("SubmitValue", "Update");
		return "register";
	}

	@RequestMapping("/delete")
	public String deletePerson(@RequestParam("id") int id, Model model) {
		model.addAttribute("myList", myList);
		model.addAttribute("Message", service.deletePerson(id));
		return "result";
	}

}
