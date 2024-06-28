*** Setting ***
Library	SeleniumLibrary

*** Test Cases ***
Test 1
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
	CLick Element	//input[@id='login-button']
	Location should be	https://www.saucedemo.com/inventory.html
	CLick Element	//a[@id='item_4_title_link']
	Location should be	https://www.saucedemo.com/inventory-item.html?id=4
Test 2
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	visual_user
	Input Text	//input[@id='password']	secret_sauce
	CLick Element	//input[@id='login-button']
	Location should be	https://www.saucedemo.com/inventory.html
	CLick Element	//a[@id='item_4_title_link']
	Location should be	https://www.saucedemo.com/inventory-item.html?id=4
Invalid-Test-login-1
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	https://www.saucedemo.com/inventory.html
	Close Browser
Invalid-Test-login-2
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	https://www.saucedemo.com/inventory.html
	Close Browser
Invalid-Test-login-3
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	https://www.saucedemo.com/inventory.html
	Close Browser
Invalid-Test-login-4
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	https://www.saucedemo.com/inventory.html
	Close Browser
Invalid-Test-login-5
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	https://www.saucedemo.com/inventory.html
	Close Browser
Invalid-Test-login-6
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	https://www.saucedemo.com/inventory.html
	Close Browser
Invalid-Test-login-7
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	https://www.saucedemo.com/inventory.html
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	https://www.saucedemo.com/inventory-item.html?id=4
	Close Browser
Invalid-Test-login-8
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	visual_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	https://www.saucedemo.com/inventory.html
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	https://www.saucedemo.com/inventory-item.html?id=4
	Close Browser
Invalid-Test-login-1
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-2
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-3
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-4
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-5
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-6
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-7
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	backpack_1
	Close Browser
Invalid-Test-login-8
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	visual_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	backpack_2
	Close Browser
Invalid-Test-login-1
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-2
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-3
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-4
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-5
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-6
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-7
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	backpack_1
	Close Browser
Invalid-Test-login-8
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	visual_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	backpack_2
	Close Browser
Invalid-Test-login-1
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-2
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-3
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-4
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-5
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-6
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-7
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	backpack_1
	Close Browser
Invalid-Test-login-8
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	visual_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	backpack_2
	Close Browser
Invalid-Test-login-1
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-2
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	NOT_standard_user
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-3
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-4
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='password']	NOT_secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-5
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
	Close Browser
Invalid-Test-login-6
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
#	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
	Close Browser
Invalid-Test-login-7
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	standard_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_1
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	backpack_1
	Close Browser
Invalid-Test-login-8
	Open Browser	https://www.saucedemo.com/	Chrome
	Input Text	//input[@id='user-name']	visual_user
	Input Text	//input[@id='password']	secret_sauce
	Click Element	//input[@id='login-button']
	Location Should Be	homepage_ 2
#	Click Element	//a[@id='item_4_title_link']
	Location Should Be	backpack_2
	Close Browser
