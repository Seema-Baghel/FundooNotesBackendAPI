package com.fundoonotes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.bridgelabz.fundoonotes.dto.LoginDto;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FundooNotesControllerTest {

	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private UserRepository userRepository;
	
	@Test
	public void userRegistrationTest(){
			UserDto userdto = new UserDto();
			UserModel user = new UserModel();
	
			when(modelMapper.map(userdto, UserModel.class)).thenReturn(user);
			user.setFirstName("Seema");
			user.setLastName("Baghel");
			user.setEmail("seemabaghel696@gmail.com");
			user.setPassword("Seema@567");
			user.setMobile("8596968033");
			when(bCryptPasswordEncoder.encode(userdto.getPassword())).thenReturn(user.getPassword());
			when(userRepository.save(user)).thenReturn(user);
			System.out.println(user);
			assertEquals("Seema", user.getFirstName());
	}
	
	@Test
	public void userLoginValidationTest(){	
		LoginDto logindto = new LoginDto();
		UserModel user = new UserModel();
		
		when(modelMapper.map(logindto,UserModel.class)).thenReturn(user);
		
		logindto.setEmail("seemabaghel696@gmail.com");
		logindto.setPassword("Seema@567");
		System.out.println(logindto);
		when(bCryptPasswordEncoder.matches("Seema@567", logindto.getPassword())).thenReturn(true);
		assertEquals("seemabaghel696@gmail.com", logindto.getEmail());	
	}

}
