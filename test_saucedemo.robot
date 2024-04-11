*** Setting ***
Library	SeleniumLibrary

*** Test Cases ***
Test 1
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
	CLick Element	//input[@id='login-button']
	Location should be	https://www.saucedemo.com/inventory.html
