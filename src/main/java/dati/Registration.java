package dati;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class Registration {
	
	// TODO - marco
	/**
	 * qui dobbiamo inserire anche le regex
	 * in javax.validation ci sono cose fighe, appena riesco a far runnare il progetto le inserisco.
	 */
	
	private String userName;
	@NotEmpty
	@Size(min = 4,max = 20)
	
	private String password;
	
	@NotEmpty
	private String confirmPassword;
	
	@NotEmpty
	@Email
	private String email;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}
	
	
	

}
