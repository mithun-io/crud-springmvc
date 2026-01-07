package mvc.controller;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mvc.dao.Employeedao;
import mvc.entity.Employee;

@Controller
public class EmployeeController {
	@Autowired
	Employeedao employeedao;

	@GetMapping("/")
	public String loadmain() {
		return "main.jsp";
	}

	@GetMapping("/add")
	public String loadAdd() {
		return "add.jsp";
	}

	@PostMapping("/add")
	public String add(Employee employee, RedirectAttributes attributes) {
		employeedao.save(employee);
		attributes.addFlashAttribute("message", "data inserted successfully");
		return "redirect:/";
	}

	@GetMapping("/manage")
	public String viewManage(ModelMap map) {
		map.put("employees", employeedao.fetchEmployees());
		return "view.jsp";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") Long id, RedirectAttributes attributes) {
		employeedao.deleteById(id);
		attributes.addFlashAttribute("message", "record deleted sucessfully");
		return "redirect:/manage";
	}

	@GetMapping("/edit")
	public String loadedit(@RequestParam("id") Long id, ModelMap map) {
		Employee employee = employeedao.findById(id);
		map.put("employee", employee);
		return "edit.jsp";
	}

	@PostMapping("/update")
	public String update(Employee employee, RedirectAttributes attributes) {
		employeedao.update(employee);
		attributes.addFlashAttribute("message", "record updated sucessfully");
		return "redirect:/manage";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(LocalDate.parse(text)); // ISO yyyy-MM-dd
			}
		});
	}

}
