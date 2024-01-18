*** Variables ***
${username}	//input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']
${password}	//input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']
${login}	//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']

*** Setting ***
Library	SeleniumLibrary

*** Test Cases ***
Test login 1
	Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${username}	14
	Input Text	${password}	124
	Click Element	${login}
	Location should be	1214124
Test login 2
	Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${username}	142412
	Input Text	${password}	12421
	Click Element	${login}
	Location should be	gasdfads
Test-login-1
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-2
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-3
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   12421
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-4
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-5
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   142412
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-6
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-7
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   124
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-8
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   12421
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-9
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-10
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   142412
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-11
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-12
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-13
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   12421
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-14
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-15
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   142412
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-16
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-17
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   124
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-18
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   12421
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-19
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-20
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   142412
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   NOT124
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-21
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-22
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   142412
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   12421
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-23
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   124
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-24
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   142412
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   12421
   Click Element   //input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-25
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   14
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   124
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
Test-login-26
   Open Browser   https://www.saucedemo.com/   Edge
   Input Text   //input[@class='input_error form_input' and @placeholder='Username' and @type='text' and @data-test='username' and @id='user-name' and @name='user-name' and @autocorrect='off' and @autocapitalize='none' and @value='']   142412
   Input Text   //input[@class='input_error form_input' and @placeholder='Password' and @type='password' and @data-test='password' and @id='password' and @name='password' and @autocorrect='off' and @autocapitalize='none' and @value='']   12421
   Click Element   NOT//input[@type='submit' and @class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']
