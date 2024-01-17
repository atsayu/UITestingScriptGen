*** Variables ***
${login_button}	xpath=//input[@class='submit-button btn_action' and @data-test='login-button' and @id='login-button' and @name='login-button' and @value='Login']

*** Setting ***
Library	SeleniumLibrary

*** Test Cases ***
Test login 1
	Open Browser	https://www.saucedemo.com/	Chrome
	Maximize Browser Window
	Input Text	${username}	123
	Input Text	${password}	421
	Click Element	${login_button}
	Location should be	validlink1
