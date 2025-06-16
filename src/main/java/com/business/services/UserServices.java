package com.business.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.business.entities.User;
import com.business.repositories.UserRepository;

@Component
public class UserServices 
{
	@Autowired
	private UserRepository userRepository;
		
	//Get All Users
	public List<User> getAllUser()
	{
		return (List<User>) this.userRepository.findAll();
	}
	
	//Get Single User
	public User getUser(int id)
	{
		return this.userRepository.findById(id).get();
	}
	
	//Get Single User By Email
	public User getUserByEmail(String email)
	{
		return this.userRepository.findUserByUemail(email);
	}
	
	//Update
	public void updateUser(User user,int id)
	{
		user.setU_id(id);
		 this.userRepository.save(user);
	}
	
	//delete single User
	public void deleteUser(int id)
	{
		this.userRepository.deleteById(id);
	}

	//Add User
	public void addUser(User user)
	{
		this.userRepository.save(user);
	}
	
	public boolean validateLoginCredentials(String email,String password)
	{
		List<User> users = (List<User>) this.userRepository.findAll();
		for(User u:users)
		{
		if(u!=null && u.getUpassword().equals(password) && u.getUemail().equals(email))
		{
			return true;
		}
		}
		return false;
	}
}