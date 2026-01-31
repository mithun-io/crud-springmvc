package com.controller;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dao.UserDao;
import com.entity.User;

@Controller
public class UserController {

	@Autowired
	UserDao userDao;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(LocalDate.parse(text)); // ISO yyyy-MM-dd
			}
		});
	}
	
	@GetMapping("/")
	public String loadMain() {
		return "main.jsp";
	}
	
	@GetMapping("/add")
	public String loadAdd() {
		return "add.jsp";
	}
	
	@PostMapping("/add")
	public String add(User user, RedirectAttributes redirectAttributes) {
		userDao.insert(user);
		redirectAttributes.addFlashAttribute("message", "inserted successfully");
		return "redirect:/";
	}
	
	@PostMapping("/update")
	public String update(User user, RedirectAttributes redirectAttributes) {
		userDao.update(user);
		redirectAttributes.addFlashAttribute("message", "updated successfully");
		return "redirect:/manage";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
		userDao.deleteById(id);
		redirectAttributes.addFlashAttribute("message", "deleted successfully");
		return "redirect:/manage";
	}
	
	@GetMapping("/edit")
	public String loadEdit(@RequestParam("id") Long id, ModelMap modelMap) {
		User user = userDao.fetchById(id);
		modelMap.put("user", user);
		return "edit.jsp";
	}
	
	@GetMapping("/manage")
	public String viewManage(ModelMap modelMap) {
		List<User> users = userDao.fetchAll();
		modelMap.put("users", users);
		return "view.jsp";
	}
}
