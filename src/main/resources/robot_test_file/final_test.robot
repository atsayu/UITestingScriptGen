Invalid-Test-Login-1
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	 loc_a1	NOT_a
	Input Text	//input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']	NOT_valid_password
	Click Element	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
	Location Should Be	abc2
	Close Browser
Invalid-Test-Login-2
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	 loc_a1	NOT_a
	Input Text	//input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']	NOT_valid_password
	Click Element	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
	Location Should Be	abc3
	Close Browser
Invalid-Test-Login-3
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	 loc_a1	NOT_a
	Input Text	//input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']	NOT_valid_password
	Click Element	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
	Location Should Be	abc1
	Close Browser
Invalid-Test-Login-4
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	 loc_a1	NOT_a
	Input Text	//input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']	NOT_valid_username
	Click Element	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
	Location Should Be	abc2
	Close Browser
Invalid-Test-Login-5
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	 loc_a1	NOT_a
	Input Text	//input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']	NOT_valid_username
	Click Element	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
	Location Should Be	abc3
	Close Browser
Invalid-Test-Login-6
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	 loc_a1	NOT_a
	Input Text	//input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']	NOT_valid_username
	Click Element	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
	Location Should Be	abc1
	Close Browser
Invalid-Test-Login-7
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	 loc_a1	a
	Input Text	//input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']	valid_username
	Input Text	//input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']	valid_password
#	Click Element	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
	Location Should Be	abc2
	Close Browser
Invalid-Test-Login-8
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	 loc_a1	a
	Input Text	//input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']	valid_username
	Input Text	//input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']	valid_password
#	Click Element	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
	Location Should Be	abc3
	Close Browser
Invalid-Test-Login-9
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	 loc_a1	a
	Input Text	//input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']	valid_username
	Input Text	//input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']	valid_password
#	Click Element	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
	Location Should Be	abc1
	Close Browser
