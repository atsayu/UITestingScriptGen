*** Variables ***
${login_button}	xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']

*** Setting ***
Library	SeleniumLibrary

*** Test Cases ***
Test login 1
	Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${username}	 p1
	Input Text	${password}	u1
	Click Element	${login_button}
	Location should be	link1
Test login 2
	Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${username}	 p2
	Input Text	${password}	u2
	Click Element	${login_button}
	Location should be	link2
