Invalid-Test-login-1
	Open Browser	https://demoqa.com/login	Chrome
#	LINE1	ce1
	Input Text	xpath=//input[@id='firstName' and @placeholder='First Name']	INVALID_DATA
	Input Text	xpath=//input[@id='postalCode' and @placeholder='Postal Code']	INVALID_DATA
	Input Text	xpath=//input[@id='city' and @placeholder='City']	INVALID_DATA
	Input Text	xpath=//input[@id='email' and @placeholder='example@email.com']	INVALID_DATA
	Location Should Be	url1
	Close Browser
Invalid-Test-login-2
	Open Browser	https://demoqa.com/login	Chrome
#	LINE1	ce1
	Input Text	xpath=//input[@id='firstName' and @placeholder='First Name']	INVALID_DATA
	Input Text	xpath=//input[@id='postalCode' and @placeholder='Postal Code']	INVALID_DATA
	Input Text	xpath=//input[@id='city' and @placeholder='City']	INVALID_DATA
	Input Text	xpath=//input[@id='email' and @placeholder='example@email.com']	INVALID_DATA
	Location Should Be	url2
	Close Browser
Invalid-Test-login-3
	Open Browser	https://demoqa.com/login	Chrome
	Click Element	xpath=//input[@id='stateProvince' and @placeholder='State or Province']
	Input Text	xpath=//input[@id='firstName' and @placeholder='First Name']	a1
	Input Text	xpath=//input[@id='postalCode' and @placeholder='Postal Code']	e1
	Input Text	xpath=//input[@id='city' and @placeholder='City']	d1
	Location Should Be	urla1
#	LINE4	ce2
	Input Text	xpath=//input[@id='firstName' and @placeholder='First Name']	INVALID_DATA
	Location Should Be	urlb1
	Close Browser
Invalid-Test-login-4
	Open Browser	https://demoqa.com/login	Chrome
	Click Element	xpath=//input[@id='stateProvince' and @placeholder='State or Province']
	Input Text	xpath=//input[@id='firstName' and @placeholder='First Name']	a2
	Input Text	xpath=//input[@id='postalCode' and @placeholder='Postal Code']	e2
	Input Text	xpath=//input[@id='city' and @placeholder='City']	d2
	Location Should Be	urla2
#	LINE4	ce2
	Input Text	xpath=//input[@id='firstName' and @placeholder='First Name']	INVALID_DATA
	Location Should Be	urlb2
	Close Browser
Invalid-Test-login-5
	Open Browser	https://demoqa.com/login	Chrome
	Click Element	xpath=//input[@id='stateProvince' and @placeholder='State or Province']
	Input Text	xpath=//input[@id='firstName' and @placeholder='First Name']	a1
	Input Text	xpath=//input[@id='postalCode' and @placeholder='Postal Code']	e1
	Input Text	xpath=//input[@id='email' and @placeholder='example@email.com']	c1
	Location Should Be	urla1
#	LINE4	ce2
	Input Text	xpath=//input[@id='firstName' and @placeholder='First Name']	INVALID_DATA
	Location Should Be	urlb1
	Close Browser
